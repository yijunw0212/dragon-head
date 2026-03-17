package org.dragon.config.store;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 内存配置存储实现
 * 使用 ConcurrentHashMap 存储，支持命名空间隔离和 workspace 维度配置
 */
@Slf4j
public class MemoryConfigStore implements ConfigStore {

    /**
     * 存储结构: namespace -> (key -> value)
     */
    private final ConcurrentMap<String, ConcurrentMap<String, Object>> store = new ConcurrentHashMap<>();

    /**
     * Workspace 维度存储: workspace -> entityType -> entityId -> key -> value
     */
    private final ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<String, Object>>>> workspaceStore = new ConcurrentHashMap<>();

    @Override
    public void set(String namespace, String key, Object value) {
        if (namespace == null || key == null) {
            throw new IllegalArgumentException("namespace and key cannot be null");
        }
        store.computeIfAbsent(namespace, k -> new ConcurrentHashMap<>()).put(key, value);
        log.debug("Config set: {}.{} = {}", namespace, key, value);
    }

    @Override
    public Optional<Object> get(String namespace, String key) {
        if (namespace == null || key == null) {
            return Optional.empty();
        }
        ConcurrentMap<String, Object> namespaceMap = store.get(namespace);
        if (namespaceMap == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(namespaceMap.get(key));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String namespace, String key, T defaultValue) {
        return (T) get(namespace, key).orElse(defaultValue);
    }

    @Override
    public void delete(String namespace, String key) {
        if (namespace == null || key == null) {
            return;
        }
        ConcurrentMap<String, Object> namespaceMap = store.get(namespace);
        if (namespaceMap != null) {
            namespaceMap.remove(key);
            // 如果命名空间为空，删除该命名空间
            if (namespaceMap.isEmpty()) {
                store.remove(namespace);
            }
        }
        log.debug("Config deleted: {}.{}", namespace, key);
    }

    @Override
    public Map<String, Object> getNamespace(String namespace) {
        if (namespace == null) {
            return Collections.emptyMap();
        }
        ConcurrentMap<String, Object> namespaceMap = store.get(namespace);
        if (namespaceMap == null) {
            return Collections.emptyMap();
        }
        return new HashMap<>(namespaceMap);
    }

    @Override
    public void deleteNamespace(String namespace) {
        if (namespace == null) {
            return;
        }
        store.remove(namespace);
        log.debug("Namespace deleted: {}", namespace);
    }

    @Override
    public boolean exists(String namespace, String key) {
        if (namespace == null || key == null) {
            return false;
        }
        ConcurrentMap<String, Object> namespaceMap = store.get(namespace);
        return namespaceMap != null && namespaceMap.containsKey(key);
    }

    @Override
    public Set<String> getAllNamespaces() {
        return new HashSet<>(store.keySet());
    }

    @Override
    public long count(String namespace) {
        if (namespace == null) {
            return 0;
        }
        ConcurrentMap<String, Object> namespaceMap = store.get(namespace);
        return namespaceMap == null ? 0 : namespaceMap.size();
    }

    @Override
    public void clear() {
        store.clear();
        workspaceStore.clear();
        log.info("All config cleared");
    }

    // ==================== Workspace 维度配置实现 ====================

    @Override
    public void set(String workspace, String entityType, String entityId, String key, Object value) {
        if (workspace == null || entityType == null || entityId == null || key == null) {
            throw new IllegalArgumentException("workspace, entityType, entityId and key cannot be null");
        }
        workspaceStore.computeIfAbsent(workspace, w ->
                new ConcurrentHashMap<>()).computeIfAbsent(entityType, t ->
                new ConcurrentHashMap<>()).computeIfAbsent(entityId, eid ->
                new ConcurrentHashMap<>()).put(key, value);
        log.debug("Workspace config set: {}.{}.{}.{} = {}", workspace, entityType, entityId, key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String workspace, String entityType, String entityId, String key, T defaultValue) {
        if (workspace == null || entityType == null || entityId == null || key == null) {
            return defaultValue;
        }
        ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<String, Object>>> entityTypeMap = workspaceStore.get(workspace);
        if (entityTypeMap == null) {
            return defaultValue;
        }
        ConcurrentMap<String, ConcurrentMap<String, Object>> entityIdMap = entityTypeMap.get(entityType);
        if (entityIdMap == null) {
            return defaultValue;
        }
        ConcurrentMap<String, Object> keyMap = entityIdMap.get(entityId);
        if (keyMap == null) {
            return defaultValue;
        }
        return (T) keyMap.getOrDefault(key, defaultValue);
    }

    @Override
    public Map<String, Object> get(String workspace, String entityType, String entityId) {
        if (workspace == null || entityType == null || entityId == null) {
            return Collections.emptyMap();
        }
        ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<String, Object>>> entityTypeMap = workspaceStore.get(workspace);
        if (entityTypeMap == null) {
            return Collections.emptyMap();
        }
        ConcurrentMap<String, ConcurrentMap<String, Object>> entityIdMap = entityTypeMap.get(entityType);
        if (entityIdMap == null) {
            return Collections.emptyMap();
        }
        ConcurrentMap<String, Object> keyMap = entityIdMap.get(entityId);
        if (keyMap == null) {
            return Collections.emptyMap();
        }
        return new HashMap<>(keyMap);
    }

    @Override
    public Map<String, Map<String, Object>> getByEntityType(String workspace, String entityType) {
        if (workspace == null || entityType == null) {
            return Collections.emptyMap();
        }
        ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<String, Object>>> entityTypeMap = workspaceStore.get(workspace);
        if (entityTypeMap == null) {
            return Collections.emptyMap();
        }
        ConcurrentMap<String, ConcurrentMap<String, Object>> entityIdMap = entityTypeMap.get(entityType);
        if (entityIdMap == null) {
            return Collections.emptyMap();
        }
        Map<String, Map<String, Object>> result = new HashMap<>();
        entityIdMap.forEach((entityId, keyMap) -> result.put(entityId, new HashMap<>(keyMap)));
        return result;
    }

    @Override
    public Map<String, Map<String, Map<String, Object>>> getWorkspace(String workspace) {
        if (workspace == null) {
            return Collections.emptyMap();
        }
        ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<String, Object>>> entityTypeMap = workspaceStore.get(workspace);
        if (entityTypeMap == null) {
            return Collections.emptyMap();
        }
        Map<String, Map<String, Map<String, Object>>> result = new HashMap<>();
        entityTypeMap.forEach((entityType, entityIdMap) -> {
            Map<String, Map<String, Object>> entityIdResult = new HashMap<>();
            entityIdMap.forEach((entityId, keyMap) -> entityIdResult.put(entityId, new HashMap<>(keyMap)));
            result.put(entityType, entityIdResult);
        });
        return result;
    }

    @Override
    public void delete(String workspace, String entityType, String entityId) {
        if (workspace == null || entityType == null || entityId == null) {
            return;
        }
        ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<String, Object>>> entityTypeMap = workspaceStore.get(workspace);
        if (entityTypeMap != null) {
            ConcurrentMap<String, ConcurrentMap<String, Object>> entityIdMap = entityTypeMap.get(entityType);
            if (entityIdMap != null) {
                entityIdMap.remove(entityId);
                if (entityIdMap.isEmpty()) {
                    entityTypeMap.remove(entityType);
                }
            }
            if (entityTypeMap.isEmpty()) {
                workspaceStore.remove(workspace);
            }
        }
        log.debug("Workspace config deleted: {}.{}.{}", workspace, entityType, entityId);
    }

    @Override
    public void deleteWorkspace(String workspace) {
        if (workspace == null) {
            return;
        }
        workspaceStore.remove(workspace);
        log.debug("Workspace deleted: {}", workspace);
    }

    @Override
    public Set<String> getAllWorkspaces() {
        return new HashSet<>(workspaceStore.keySet());
    }
}

package org.dragon.config.store;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 配置存储抽象接口
 * 提供通用的 KV 配置存储能力，支持命名空间隔离
 */
public interface ConfigStore {

    /**
     * 存储配置值
     *
     * @param namespace 命名空间（如 "character", "agent"）
     * @param key      键
     * @param value    值（支持任意类型）
     */
    void set(String namespace, String key, Object value);

    /**
     * 获取配置值
     *
     * @param namespace 命名空间
     * @param key      键
     * @return Optional 配置值
     */
    Optional<Object> get(String namespace, String key);

    /**
     * 获取配置值（带默认值）
     *
     * @param namespace    命名空间
     * @param key         键
     * @param defaultValue 默认值
     * @param <T>         值类型
     * @return 配置值或默认值
     */
    <T> T get(String namespace, String key, T defaultValue);

    /**
     * 删除配置
     *
     * @param namespace 命名空间
     * @param key      键
     */
    void delete(String namespace, String key);

    /**
     * 获取命名空间下所有配置
     *
     * @param namespace 命名空间
     * @return 配置键值对
     */
    Map<String, Object> getNamespace(String namespace);

    /**
     * 删除命名空间
     *
     * @param namespace 命名空间
     */
    void deleteNamespace(String namespace);

    /**
     * 检查是否存在
     *
     * @param namespace 命名空间
     * @param key      键
     * @return 是否存在
     */
    boolean exists(String namespace, String key);

    /**
     * 列出所有命名空间
     *
     * @return 命名空间集合
     */
    Set<String> getAllNamespaces();

    /**
     * 获取命名空间下配置数量
     *
     * @param namespace 命名空间
     * @return 配置数量
     */
    long count(String namespace);

    /**
     * 清空所有配置
     */
    void clear();

    // ==================== Workspace 维度配置 ====================
    // 支持按 workspace -> entityType -> key 的三级维度配置
    // 用于运行时动态配置，如不同 character 或 workspace 下的不同配置

    /**
     * 按 workspace + entityType + key 设置配置
     *
     * @param workspace  工作空间 ID (如 "default", "workspace-1")
     * @param entityType 实体类型 (如 "character", "model", "channel")
     * @param entityId   实体 ID (如 characterId, modelId)
     * @param key       配置键
     * @param value     配置值
     */
    void set(String workspace, String entityType, String entityId, String key, Object value);

    /**
     * 按 workspace + entityType + entityId + key 获取配置
     *
     * @param workspace    工作空间 ID
     * @param entityType  实体类型
     * @param entityId    实体 ID
     * @param key        配置键
     * @param defaultValue 默认值
     * @param <T>        值类型
     * @return 配置值或默认值
     */
    <T> T get(String workspace, String entityType, String entityId, String key, T defaultValue);

    /**
     * 获取 workspace 下某 entityType + entityId 的所有配置
     *
     * @param workspace  工作空间 ID
     * @param entityType 实体类型
     * @param entityId   实体 ID
     * @return 配置键值对
     */
    Map<String, Object> get(String workspace, String entityType, String entityId);

    /**
     * 获取 workspace 下某 entityType 的所有实体配置
     *
     * @param workspace  工作空间 ID
     * @param entityType 实体类型
     * @return entityId -> 配置键值对
     */
    Map<String, Map<String, Object>> getByEntityType(String workspace, String entityType);

    /**
     * 获取 workspace 下所有配置
     *
     * @param workspace 工作空间 ID
     * @return entityType -> entityId -> 配置键值对
     */
    Map<String, Map<String, Map<String, Object>>> getWorkspace(String workspace);

    /**
     * 删除 workspace 下的某个实体配置
     *
     * @param workspace  工作空间 ID
     * @param entityType 实体类型
     * @param entityId   实体 ID
     */
    void delete(String workspace, String entityType, String entityId);

    /**
     * 删除 workspace
     *
     * @param workspace 工作空间 ID
     */
    void deleteWorkspace(String workspace);

    /**
     * 列出所有 workspace
     *
     * @return workspace 集合
     */
    Set<String> getAllWorkspaces();
}

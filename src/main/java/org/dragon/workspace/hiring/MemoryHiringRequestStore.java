package org.dragon.workspace.hiring;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * MemoryHiringRequestStore 雇佣请求内存存储实现
 *
 * @author wyj
 * @version 1.0
 */
@Slf4j
@Component
public class MemoryHiringRequestStore implements HiringRequestStore {

    private final Map<String, HiringRequest> store = new ConcurrentHashMap<>();

    @Override
    public void save(HiringRequest request) {
        store.put(request.getId(), request);
        log.debug("[MemoryHiringRequestStore] Saved hiring request: {}", request.getId());
    }

    @Override
    public void update(HiringRequest request) {
        if (store.containsKey(request.getId())) {
            store.put(request.getId(), request);
            log.debug("[MemoryHiringRequestStore] Updated hiring request: {}", request.getId());
        }
    }

    @Override
    public void delete(String id) {
        store.remove(id);
        log.debug("[MemoryHiringRequestStore] Deleted hiring request: {}", id);
    }

    @Override
    public Optional<HiringRequest> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<HiringRequest> findByWorkspaceId(String workspaceId) {
        return store.values().stream()
                .filter(req -> workspaceId.equals(req.getWorkspaceId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<HiringRequest> findByStatus(HiringRequestStatus status) {
        return store.values().stream()
                .filter(req -> req.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public boolean exists(String id) {
        return store.containsKey(id);
    }
}

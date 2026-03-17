package org.dragon.workspace.task;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * MemoryWorkspaceTaskStore 工作空间任务内存存储实现
 *
 * @author wyj
 * @version 1.0
 */
@Slf4j
@Component
public class MemoryWorkspaceTaskStore implements WorkspaceTaskStore {

    private final Map<String, WorkspaceTask> store = new ConcurrentHashMap<>();

    @Override
    public void save(WorkspaceTask task) {
        store.put(task.getId(), task);
        log.debug("[MemoryWorkspaceTaskStore] Saved task: {}", task.getId());
    }

    @Override
    public void update(WorkspaceTask task) {
        if (store.containsKey(task.getId())) {
            store.put(task.getId(), task);
            log.debug("[MemoryWorkspaceTaskStore] Updated task: {}", task.getId());
        }
    }

    @Override
    public void delete(String id) {
        store.remove(id);
        log.debug("[MemoryWorkspaceTaskStore] Deleted task: {}", id);
    }

    @Override
    public Optional<WorkspaceTask> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<WorkspaceTask> findByWorkspaceId(String workspaceId) {
        return store.values().stream()
                .filter(task -> workspaceId.equals(task.getWorkspaceId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkspaceTask> findByHiringRequestId(String hiringRequestId) {
        return store.values().stream()
                .filter(task -> hiringRequestId.equals(task.getHiringRequestId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkspaceTask> findByExecutorId(String executorId) {
        return store.values().stream()
                .filter(task -> executorId.equals(task.getExecutorId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkspaceTask> findByStatus(WorkspaceTaskStatus status) {
        return store.values().stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<WorkspaceTask> findByInternalTaskId(String internalTaskId) {
        return store.values().stream()
                .filter(task -> internalTaskId.equals(task.getInternalTaskId()))
                .findFirst();
    }

    @Override
    public boolean exists(String id) {
        return store.containsKey(id);
    }
}

package org.dragon.workspace.task;

import java.util.List;
import java.util.Optional;

/**
 * WorkspaceTaskStore 工作空间任务存储接口
 *
 * @author wyj
 * @version 1.0
 */
public interface WorkspaceTaskStore {

    /**
     * 保存任务
     *
     * @param task 任务
     */
    void save(WorkspaceTask task);

    /**
     * 更新任务
     *
     * @param task 任务
     */
    void update(WorkspaceTask task);

    /**
     * 删除任务
     *
     * @param id 任务 ID
     */
    void delete(String id);

    /**
     * 根据 ID 获取任务
     *
     * @param id 任务 ID
     * @return Optional 任务
     */
    Optional<WorkspaceTask> findById(String id);

    /**
     * 根据工作空间 ID 获取任务列表
     *
     * @param workspaceId 工作空间 ID
     * @return 任务列表
     */
    List<WorkspaceTask> findByWorkspaceId(String workspaceId);

    /**
     * 根据雇佣请求 ID 获取任务列表
     *
     * @param hiringRequestId 雇佣请求 ID
     * @return 任务列表
     */
    List<WorkspaceTask> findByHiringRequestId(String hiringRequestId);

    /**
     * 根据执行者 ID 获取任务列表
     *
     * @param executorId 执行者 ID
     * @return 任务列表
     */
    List<WorkspaceTask> findByExecutorId(String executorId);

    /**
     * 根据状态获取任务列表
     *
     * @param status 任务状态
     * @return 任务列表
     */
    List<WorkspaceTask> findByStatus(WorkspaceTaskStatus status);

    /**
     * 根据内部任务 ID 获取任务
     *
     * @param internalTaskId 内部任务 ID
     * @return Optional 任务
     */
    Optional<WorkspaceTask> findByInternalTaskId(String internalTaskId);

    /**
     * 检查任务是否存在
     *
     * @param id 任务 ID
     * @return 是否存在
     */
    boolean exists(String id);
}

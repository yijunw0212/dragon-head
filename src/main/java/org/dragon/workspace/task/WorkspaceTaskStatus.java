package org.dragon.workspace.task;

/**
 * WorkspaceTaskStatus 工作空间任务状态
 *
 * @author wyj
 * @version 1.0
 */
public enum WorkspaceTaskStatus {
    PENDING,    // 待执行
    RUNNING,    // 执行中
    COMPLETED,  // 已完成
    FAILED,     // 失败
    CANCELLED   // 已取消
}

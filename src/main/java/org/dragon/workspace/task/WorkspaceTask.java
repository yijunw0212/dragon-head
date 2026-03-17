package org.dragon.workspace.task;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WorkspaceTask 工作空间任务实体
 * 雇佣成功后生成的可执行单元
 *
 * @author wyj
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceTask {

    /**
     * 执行者类型
     */
    public enum ExecutorType {
        CHARACTER,
        ORGANIZATION
    }

    /**
     * 任务唯一标识
     */
    private String id;

    /**
     * 工作空间 ID
     */
    private String workspaceId;

    /**
     * 雇佣请求 ID
     */
    private String hiringRequestId;

    /**
     * 雇佣记录 ID
     */
    private String hiringRecordId;

    /**
     * 执行者类型
     */
    private ExecutorType executorType;

    /**
     * 执行者 ID（Character ID 或 Organization ID）
     */
    private String executorId;

    /**
     * 内部任务 ID（如 CharacterTask ID）
     */
    private String internalTaskId;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务状态
     */
    @Builder.Default
    private WorkspaceTaskStatus status = WorkspaceTaskStatus.PENDING;

    /**
     * 任务输入
     */
    private String input;

    /**
     * 任务输出/结果
     */
    private String result;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 开始执行时间
     */
    private LocalDateTime startedAt;

    /**
     * 完成时间
     */
    private LocalDateTime completedAt;
}

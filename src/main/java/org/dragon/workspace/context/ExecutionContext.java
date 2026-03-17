package org.dragon.workspace.context;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ExecutionContext 执行上下文
 * 任务执行所需的动态信息，包括会话历史、环境变量、临时数据等
 *
 * @author wyj
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionContext {

    /**
     * 唯一会话 ID
     */
    private String sessionId;

    /**
     * 工作空间 ID
     */
    private String workspaceId;

    /**
     * 任务 ID
     */
    private String taskId;

    /**
     * 雇佣请求 ID
     */
    private String hiringRequestId;

    /**
     * 执行者 ID
     */
    private String executorId;

    /**
     * 执行者类型
     */
    private String executorType;

    /**
     * 任务目标
     */
    private String taskGoal;

    /**
     * 任务描述
     */
    private String taskDescription;

    /**
     * 任务参数
     */
    private Map<String, Object> taskParameters;

    /**
     * 物料引用列表
     */
    private List<String> materialIds;

    /**
     * 环境信息
     */
    private Map<String, Object> environment;

    /**
     * 历史会话摘要
     */
    private String sessionSummary;

    /**
     * 协作成员信息
     */
    private List<MemberInfo> collaborationMembers;

    /**
     * 执行提示词（由 LLM 生成）
     */
    private String executionPrompt;

    /**
     * 成员信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfo {
        private String id;
        private String name;
        private String role;
        private Map<String, Object> metadata;
    }
}

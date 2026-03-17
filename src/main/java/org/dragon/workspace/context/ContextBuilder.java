package org.dragon.workspace.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dragon.workspace.hiring.Candidate;
import org.dragon.workspace.hiring.HiringRequest;
import org.dragon.workspace.material.Material;
import org.dragon.workspace.task.WorkspaceTask;

/**
 * ContextBuilder 上下文构建器
 * 将任务参数、物料、环境信息组装成执行器可理解的上下文
 *
 * @author wyj
 * @version 1.0
 */
public class ContextBuilder {

    /**
     * 构建基础执行上下文
     *
     * @param request 雇佣请求
     * @param candidate 被录用的候选人
     * @return 执行上下文
     */
    public static ExecutionContext build(HiringRequest request, Candidate candidate) {
        return ExecutionContext.builder()
                .sessionId(UUID.randomUUID().toString())
                .workspaceId(request.getWorkspaceId())
                .hiringRequestId(request.getId())
                .executorId(candidate.getId())
                .executorType(candidate.getType().name())
                .taskGoal(request.getWorkDescription())
                .taskDescription(request.getWorkDescription())
                .taskParameters(request.getTaskParameters() != null ? request.getTaskParameters() : new HashMap<>())
                .materialIds(request.getMaterialIds() != null ? request.getMaterialIds() : new ArrayList<>())
                .environment(new HashMap<>())
                .build();
    }

    /**
     * 添加物料信息到上下文
     *
     * @param context 上下文
     * @param materials 物料列表
     * @return 更新后的上下文
     */
    public static ExecutionContext withMaterials(ExecutionContext context, List<Material> materials) {
        List<String> materialDescriptions = new ArrayList<>();
        for (Material material : materials) {
            materialDescriptions.add(String.format("- %s (type: %s, size: %d bytes)",
                    material.getName(), material.getType(), material.getSize()));
        }
        Map<String, Object> env = new HashMap<>(context.getEnvironment());
        env.put("materials", materialDescriptions);
        context.setEnvironment(env);
        return context;
    }

    /**
     * 添加环境信息到上下文
     *
     * @param context 上下文
     * @param key 键
     * @param value 值
     * @return 更新后的上下文
     */
    public static ExecutionContext withEnvironment(ExecutionContext context, String key, Object value) {
        Map<String, Object> env = new HashMap<>(context.getEnvironment());
        env.put(key, value);
        context.setEnvironment(env);
        return context;
    }

    /**
     * 添加协作成员信息
     *
     * @param context 上下文
     * @param members 成员列表
     * @return 更新后的上下文
     */
    public static ExecutionContext withCollaborationMembers(ExecutionContext context, List<Candidate> members) {
        List<ExecutionContext.MemberInfo> memberInfos = new ArrayList<>();
        for (Candidate member : members) {
            memberInfos.add(ExecutionContext.MemberInfo.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .role("collaborator")
                    .build());
        }
        context.setCollaborationMembers(memberInfos);
        return context;
    }

    /**
     * 设置执行提示词
     *
     * @param context 上下文
     * @param prompt 提示词
     * @return 更新后的上下文
     */
    public static ExecutionContext withExecutionPrompt(ExecutionContext context, String prompt) {
        context.setExecutionPrompt(prompt);
        return context;
    }

    /**
     * 从任务构建上下文
     *
     * @param task 工作空间任务
     * @return 执行上下文
     */
    public static ExecutionContext fromTask(WorkspaceTask task) {
        return ExecutionContext.builder()
                .sessionId(UUID.randomUUID().toString())
                .workspaceId(task.getWorkspaceId())
                .taskId(task.getId())
                .hiringRequestId(task.getHiringRequestId())
                .executorId(task.getExecutorId())
                .executorType(task.getExecutorType().name())
                .taskGoal(task.getName())
                .taskDescription(task.getDescription())
                .environment(new HashMap<>())
                .build();
    }
}

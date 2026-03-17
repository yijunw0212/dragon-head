package org.dragon.workspace.hiring;

import java.util.List;

import org.dragon.workspace.context.ExecutionContext;

/**
 * LLMHiringEngine LLM 雇佣引擎接口
 * 使用 LLM 驱动雇佣流程的解析、匹配和决策
 *
 * @author wyj
 * @version 1.0
 */
public interface LLMHiringEngine {

    /**
     * 解析雇佣需求
     * 使用 LLM 解析 workDescription，提取结构化信息
     *
     * @param request 雇佣请求
     * @return 解析结果
     */
    ParsedRequirements parseRequirements(HiringRequest request);

    /**
     * 匹配候选人
     * 使用 LLM 根据能力要求、历史表现、当前负载等因素评估并排序候选人
     *
     * @param request 雇佣请求
     * @param candidates 可用候选人列表
     * @return 排序后的候选人列表（含匹配分数）
     */
    List<Candidate> matchCandidates(HiringRequest request, List<Candidate> candidates);

    /**
     * 做出录用决策
     * 使用 LLM 根据筛选结果做出录用决策并生成理由
     *
     * @param request 雇佣请求
     * @param candidates 已评分的候选人列表
     * @return 录用决策结果
     */
    HiringDecision makeDecision(HiringRequest request, List<Candidate> candidates);

    /**
     * 构建执行上下文
     * 使用 LLM 生成任务执行的上下文提示词
     *
     * @param request 雇佣请求
     * @param candidate 被录用的候选人
     * @return 执行上下文
     */
    ExecutionContext buildExecutionContext(HiringRequest request, Candidate candidate);

    /**
     * 解析结果
     */
    class ParsedRequirements {
        private String taskGoal;
        private List<String> requiredSkills;
        private List<String> preferredTraits;
        private String suggestedRole;
        private java.util.Map<String, Object> additionalContext;

        // Getters and setters
        public String getTaskGoal() { return taskGoal; }
        public void setTaskGoal(String taskGoal) { this.taskGoal = taskGoal; }
        public List<String> getRequiredSkills() { return requiredSkills; }
        public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
        public List<String> getPreferredTraits() { return preferredTraits; }
        public void setPreferredTraits(List<String> preferredTraits) { this.preferredTraits = preferredTraits; }
        public String getSuggestedRole() { return suggestedRole; }
        public void setSuggestedRole(String suggestedRole) { this.suggestedRole = suggestedRole; }
        public java.util.Map<String, Object> getAdditionalContext() { return additionalContext; }
        public void setAdditionalContext(java.util.Map<String, Object> additionalContext) { this.additionalContext = additionalContext; }
    }

    /**
     * 录用决策结果
     */
    class HiringDecision {
        private List<Candidate> acceptedCandidates;
        private String decisionReason;
        private boolean requireManualConfirmation;

        // Getters and setters
        public List<Candidate> getAcceptedCandidates() { return acceptedCandidates; }
        public void setAcceptedCandidates(List<Candidate> acceptedCandidates) { this.acceptedCandidates = acceptedCandidates; }
        public String getDecisionReason() { return decisionReason; }
        public void setDecisionReason(String decisionReason) { this.decisionReason = decisionReason; }
        public boolean isRequireManualConfirmation() { return requireManualConfirmation; }
        public void setRequireManualConfirmation(boolean requireManualConfirmation) { this.requireManualConfirmation = requireManualConfirmation; }
    }
}

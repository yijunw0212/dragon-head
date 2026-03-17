package org.dragon.workspace.hiring;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.dragon.workspace.context.ExecutionContext;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * DefaultLLMHiringEngine LLM 雇佣引擎默认实现
 * 这是一个占位实现，实际使用时需要接入真实的 LLM 调用
 *
 * @author wyj
 * @version 1.0
 */
@Slf4j
@Component
public class DefaultLLMHiringEngine implements LLMHiringEngine {

    @Override
    public ParsedRequirements parseRequirements(HiringRequest request) {
        log.info("[DefaultLLMHiringEngine] Parsing requirements for hiring request: {}", request.getId());

        // 简化实现：直接从请求中提取信息
        // 实际实现应该调用 LLM 来解析自然语言描述
        ParsedRequirements result = new ParsedRequirements();
        result.setTaskGoal(request.getWorkDescription());
        result.setRequiredSkills(request.getRequiredCapabilities());
        result.setSuggestedRole("general");

        return result;
    }

    @Override
    public List<Candidate> matchCandidates(HiringRequest request, List<Candidate> candidates) {
        log.info("[DefaultLLMHiringEngine] Matching candidates for hiring request: {}", request.getId());

        // 简化实现：基于能力匹配评分
        // 实际实现应该调用 LLM 来评估每个候选人
        List<String> requiredCapabilities = request.getRequiredCapabilities();

        for (Candidate candidate : candidates) {
            int score = 50; // 基础分数

            // 如果有候选人描述，增加分数
            if (candidate.getDescription() != null && !candidate.getDescription().isEmpty()) {
                score += 20;
            }

            // 检查能力匹配
            if (requiredCapabilities != null && !requiredCapabilities.isEmpty()) {
                if (candidate.getCapabilities() != null) {
                    long matchedCount = candidate.getCapabilities().stream()
                            .filter(requiredCapabilities::contains)
                            .count();
                    score += (int) (matchedCount * 10);
                }
            }

            // 如果指定了特定能力要求，给额外分数
            if (candidate.getHistoricalScore() != null) {
                score += candidate.getHistoricalScore() / 5;
            }

            candidate.setMatchScore(Math.min(score, 100));
        }

        // 按匹配分数排序
        return candidates.stream()
                .sorted(Comparator.comparingInt(Candidate::getMatchScore).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public HiringDecision makeDecision(HiringRequest request, List<Candidate> candidates) {
        log.info("[DefaultLLMHiringEngine] Making hiring decision for request: {}", request.getId());

        // 简化实现：选择前 N 个候选人
        int quantity = request.getQuantity();
        List<Candidate> accepted = new ArrayList<>();

        for (int i = 0; i < Math.min(quantity, candidates.size()); i++) {
            if (candidates.get(i).getMatchScore() >= 30) {
                accepted.add(candidates.get(i));
            }
        }

        HiringDecision decision = new HiringDecision();
        decision.setAcceptedCandidates(accepted);
        decision.setDecisionReason("Auto-hired based on matching score");
        decision.setRequireManualConfirmation(false);

        return decision;
    }

    @Override
    public ExecutionContext buildExecutionContext(HiringRequest request, Candidate candidate) {
        log.info("[DefaultLLMHiringEngine] Building execution context for candidate: {}", candidate.getId());

        // 构建基础上下文
        ExecutionContext context = ExecutionContext.builder()
                .taskGoal(request.getWorkDescription())
                .taskDescription(request.getWorkDescription())
                .taskParameters(request.getTaskParameters())
                .materialIds(request.getMaterialIds())
                .executorId(candidate.getId())
                .executorType(candidate.getType().name())
                .build();

        // 生成执行提示词
        String prompt = String.format(
                "You are tasked with completing the following work:\n\n%s\n\n" +
                        "Your ID: %s\n" +
                        "Your Type: %s\n" +
                        "Required capabilities: %s",
                request.getWorkDescription(),
                candidate.getId(),
                candidate.getType().name(),
                request.getRequiredCapabilities() != null ?
                        String.join(", ", request.getRequiredCapabilities()) : "none"
        );

        context.setExecutionPrompt(prompt);

        return context;
    }
}

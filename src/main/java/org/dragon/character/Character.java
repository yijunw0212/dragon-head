package org.dragon.character;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Character 实体
 * AI 数字员工实体，由 Mind 和 Agent Engine 两部分组成
 *
 * @author zhz
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Character {

    /**
     * Character 全局唯一标识
     */
    private String id;

    /**
     * Character 名称
     */
    private String name;

    /**
     * 版本号，用于版本管理
     */
    private int version;

    /**
     * 描述
     */
    private String description;

    /**
     * 心智模块配置
     */
    private MindConfig mindConfig;

    /**
     * 执行引擎配置
     */
    private AgentEngineConfig agentEngineConfig;

    /**
     * 扩展属性
     */
    private Map<String, Object> extensions;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 状态
     */
    private Status status;

    /**
     * Character 状态枚举
     */
    public enum Status {
        /** 未加载 */
        UNLOADED,
        /** 已加载 */
        LOADED,
        /** 运行中 */
        RUNNING,
        /** 暂停 */
        PAUSED,
        /** 已销毁 */
        DESTROYED
    }

    /**
     * Mind 配置
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MindConfig {
        /** 性格描述文件路径 */
        private String personalityDescriptorPath;
        /** 标签存储类型 */
        private String tagRepositoryType;
        /** 记忆存储类型 */
        private String memoryAccessType;
        /** 技能存储类型 */
        private String skillAccessType;
    }

    /**
     * Agent Engine 配置
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentEngineConfig {
        /** 默认模型 ID */
        private String defaultModelId;
        /** 工作流配置 */
        private WorkflowConfig workflowConfig;
        /** ReAct 配置 */
        private ReActConfig reActConfig;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkflowConfig {
        private String defaultWorkflowId;
        private int maxSteps;
        private String timeout;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReActConfig {
        private int maxIterations;
        private boolean enableMemorySearch;
        private boolean enableToolUse;
    }
}

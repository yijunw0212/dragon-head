package org.dragon.workspace;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Workspace 实体
 * 工作空间是对外提供服务的统一入口，可包含多个 Organization 和 Character
 *
 * @author wyj
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {

    /**
     * 工作空间状态
     */
    public enum Status {
        ACTIVE,    // 活跃
        INACTIVE,  // 未激活
        ARCHIVED   // 已归档
    }

    /**
     * 工作空间唯一标识
     */
    private String id;

    /**
     * 工作空间名称
     */
    private String name;

    /**
     * 工作空间描述
     */
    private String description;

    /**
     * 所有者 ID
     */
    private String owner;

    /**
     * 工作空间状态
     */
    @Builder.Default
    private Status status = Status.INACTIVE;

    /**
     * 扩展属性
     */
    private Map<String, Object> properties;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}

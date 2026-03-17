package org.dragon.workspace.hiring;

/**
 * HiringRequestStatus 雇佣请求状态
 *
 * @author wyj
 * @version 1.0
 */
public enum HiringRequestStatus {
    OPEN,       // 开放
    PROCESSING, // 处理中
    FILLED,     // 已满足
    CANCELLED,  // 已取消
    EXPIRED     // 已过期
}

package org.dragon.workspace.hiring;

import java.util.List;
import java.util.Optional;

/**
 * HiringRequestStore 雇佣请求存储接口
 *
 * @author wyj
 * @version 1.0
 */
public interface HiringRequestStore {

    /**
     * 保存雇佣请求
     *
     * @param request 雇佣请求
     */
    void save(HiringRequest request);

    /**
     * 更新雇佣请求
     *
     * @param request 雇佣请求
     */
    void update(HiringRequest request);

    /**
     * 删除雇佣请求
     *
     * @param id 雇佣请求 ID
     */
    void delete(String id);

    /**
     * 根据 ID 获取雇佣请求
     *
     * @param id 雇佣请求 ID
     * @return Optional 雇佣请求
     */
    Optional<HiringRequest> findById(String id);

    /**
     * 根据工作空间 ID 获取雇佣请求列表
     *
     * @param workspaceId 工作空间 ID
     * @return 雇佣请求列表
     */
    List<HiringRequest> findByWorkspaceId(String workspaceId);

    /**
     * 根据状态获取雇佣请求列表
     *
     * @param status 雇佣请求状态
     * @return 雇佣请求列表
     */
    List<HiringRequest> findByStatus(HiringRequestStatus status);

    /**
     * 检查雇佣请求是否存在
     *
     * @param id 雇佣请求 ID
     * @return 是否存在
     */
    boolean exists(String id);
}

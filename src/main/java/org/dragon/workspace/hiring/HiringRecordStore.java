package org.dragon.workspace.hiring;

import java.util.List;
import java.util.Optional;

/**
 * HiringRecordStore 雇佣记录存储接口
 *
 * @author wyj
 * @version 1.0
 */
public interface HiringRecordStore {

    /**
     * 保存雇佣记录
     *
     * @param record 雇佣记录
     */
    void save(HiringRecord record);

    /**
     * 更新雇佣记录
     *
     * @param record 雇佣记录
     */
    void update(HiringRecord record);

    /**
     * 删除雇佣记录
     *
     * @param id 雇佣记录 ID
     */
    void delete(String id);

    /**
     * 根据 ID 获取雇佣记录
     *
     * @param id 雇佣记录 ID
     * @return Optional 雇佣记录
     */
    Optional<HiringRecord> findById(String id);

    /**
     * 根据雇佣请求 ID 获取雇佣记录列表
     *
     * @param hiringRequestId 雇佣请求 ID
     * @return 雇佣记录列表
     */
    List<HiringRecord> findByHiringRequestId(String hiringRequestId);

    /**
     * 根据候选人 ID 获取雇佣记录列表
     *
     * @param candidateId 候选人 ID
     * @return 雇佣记录列表
     */
    List<HiringRecord> findByCandidateId(String candidateId);

    /**
     * 根据决策获取雇佣记录列表
     *
     * @param decision 录用决策
     * @return 雇佣记录列表
     */
    List<HiringRecord> findByDecision(HiringRecord.Decision decision);

    /**
     * 检查雇佣记录是否存在
     *
     * @param id 雇佣记录 ID
     * @return 是否存在
     */
    boolean exists(String id);
}

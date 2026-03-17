package org.dragon.workspace.hiring;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * MemoryHiringRecordStore 雇佣记录内存存储实现
 *
 * @author wyj
 * @version 1.0
 */
@Slf4j
@Component
public class MemoryHiringRecordStore implements HiringRecordStore {

    private final Map<String, HiringRecord> store = new ConcurrentHashMap<>();

    @Override
    public void save(HiringRecord record) {
        store.put(record.getId(), record);
        log.debug("[MemoryHiringRecordStore] Saved hiring record: {}", record.getId());
    }

    @Override
    public void update(HiringRecord record) {
        if (store.containsKey(record.getId())) {
            store.put(record.getId(), record);
            log.debug("[MemoryHiringRecordStore] Updated hiring record: {}", record.getId());
        }
    }

    @Override
    public void delete(String id) {
        store.remove(id);
        log.debug("[MemoryHiringRecordStore] Deleted hiring record: {}", id);
    }

    @Override
    public Optional<HiringRecord> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<HiringRecord> findByHiringRequestId(String hiringRequestId) {
        return store.values().stream()
                .filter(record -> hiringRequestId.equals(record.getHiringRequestId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<HiringRecord> findByCandidateId(String candidateId) {
        return store.values().stream()
                .filter(record -> candidateId.equals(record.getCandidateId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<HiringRecord> findByDecision(HiringRecord.Decision decision) {
        return store.values().stream()
                .filter(record -> record.getDecision() == decision)
                .collect(Collectors.toList());
    }

    @Override
    public boolean exists(String id) {
        return store.containsKey(id);
    }
}

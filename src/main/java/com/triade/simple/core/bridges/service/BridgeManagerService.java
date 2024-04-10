package com.triade.simple.core.bridges.service;

import com.triade.simple.core.bridges.domain.Bridge;
import com.triade.simple.core.bridges.repository.BridgeRepository;
import com.triade.simple.exception.BridgeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BridgeManagerService {

    @Autowired
    private BridgeRepository repository;

    public Bridge create(Bridge bridge) {
        return repository.save(bridge);
    }

    public List<Bridge> getAll() {
        return repository.findAll();
    }

    public Bridge getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new BridgeNotFoundException(id));
    }

    public void delete(Long id) {
        repository.findById(id)
            .ifPresentOrElse(repository::delete, () -> {
                    throw new BridgeNotFoundException(id);
            });
    }

    public Bridge update(Long id, Bridge updatedBridge) {
        return repository.findById(id)
            .map(bridge -> {
                bridge.setModelName(updatedBridge.getModelName());
                bridge.setMovementDurationSeconds(updatedBridge.getMovementDurationSeconds());
                return repository.save(bridge);
            })
            .orElseThrow(() -> new BridgeNotFoundException(id));
    }
}

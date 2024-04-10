package com.triade.simple.core.bridges.service;

import com.triade.simple.core.bridges.domain.Bridge;
import com.triade.simple.core.bridges.domain.BridgePositionEnum;
import com.triade.simple.core.bridges.domain.BridgeStatusEnum;
import com.triade.simple.core.bridges.repository.BridgeRepository;
import com.triade.simple.exception.BridgeNotFoundException;
import com.triade.simple.exception.BridgeOperationNotAllowedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.triade.simple.core.Constants.OPERATION_BLOCKED_MESSAGE;

@Slf4j
@Service
public class BridgeOperationService {

    @Autowired
    private BridgeRepository repository;

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public Bridge canOperate(Long id) {
        var bridge = this.getById(id)
            .orElseThrow(() -> new BridgeNotFoundException(id));

        if (bridge.getPosition() == BridgePositionEnum.HIGHEST
            && isLessThanMinute(bridge.getLastOperationTime())) {
            throw new BridgeOperationNotAllowedException(OPERATION_BLOCKED_MESSAGE);
        }
        return bridge;
    }

    @Async
    public void moveBridge(Bridge bridge, byte[] commandBytes) {
        var originalStatus = bridge.getStatus();
        bridge = bridge.getPosition().doPositionTransition(bridge);

        executeBridgeMovement(commandBytes);

        if (originalStatus == BridgeStatusEnum.STOPPED) {
            Bridge destinationBridge = bridge;
            this.save(bridge.toBuilder()
                .position(BridgePositionEnum.MIDDLE)
                .status(BridgeStatusEnum.MOVING)
                .direction(bridge.getPosition().getDirection())
                .build());

            this.scheduleBridgeUpdate(destinationBridge);
        } else {
            this.save(bridge);
        }
    }

    protected void scheduleBridgeUpdate(Bridge destinationBridge) {
        executor.schedule(() -> {
            var currentBridge = getById(destinationBridge.getId())
                .orElseThrow(() -> new BridgeNotFoundException(destinationBridge.getId()));

            if (currentBridge.getStatus() == BridgeStatusEnum.MOVING) {
                save(destinationBridge);
            }
        }, destinationBridge.getMovementDurationSeconds(), TimeUnit.SECONDS);
    }

    private Optional<Bridge> getById(Long id) {
        return repository.findById(id);
    }

    private boolean isLessThanMinute(LocalDateTime lastOperationTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(lastOperationTime, currentTime);
        return duration.getSeconds() < 60;
    }

    private void executeBridgeMovement(byte[] commandBytes) {
        log.info("** Establishing connection with the bridge **");
        log.info("Sending command: {}", commandBytes);
    }

    private void save(Bridge bridge) {
        bridge.setLastOperationTime(LocalDateTime.now());
        repository.save(bridge);
    }
}

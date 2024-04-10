package com.triade.simple.core.bridges.facade;

import com.triade.simple.core.bridges.domain.Bridge;
import com.triade.simple.core.bridges.dto.BridgeDTO;
import com.triade.simple.core.bridges.service.BridgeManagerService;
import com.triade.simple.core.bridges.service.BridgeMapper;
import com.triade.simple.core.bridges.service.BridgeOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BridgeFacade {

    @Autowired
    private BridgeManagerService managerService;

    @Autowired
    private BridgeOperationService operationService;

    public void moveBridge(Long id, byte[] commandBytes) {
        var bridge = operationService.canOperate(id);
        operationService.moveBridge(bridge, commandBytes);
    }

    public BridgeDTO createBridge(BridgeDTO bridgeDTO) {
        Bridge bridge = BridgeMapper.INSTANCE.toEntity(bridgeDTO);
        Bridge createdBridge = managerService.create(bridge);
        return BridgeMapper.INSTANCE.toDto(createdBridge);
    }

    public List<BridgeDTO> getAllBridges() {
        List<Bridge> bridges = managerService.getAll();
        return bridges.stream()
            .map(BridgeMapper.INSTANCE::toDto)
            .collect(Collectors.toList());
    }

    public BridgeDTO getBridgeById(Long id) {
        Bridge bridge = managerService.getById(id);
        return BridgeMapper.INSTANCE.toDto(bridge);
    }

    public void deleteBridgeById(Long id) {
        managerService.delete(id);
    }

    public BridgeDTO updateBridgeById(Long id, BridgeDTO updatedBridgeDTO) {
        Bridge updatedBridge = BridgeMapper.INSTANCE.toEntity(updatedBridgeDTO);
        Bridge updated = managerService.update(id, updatedBridge);
        return BridgeMapper.INSTANCE.toDto(updated);
    }
}

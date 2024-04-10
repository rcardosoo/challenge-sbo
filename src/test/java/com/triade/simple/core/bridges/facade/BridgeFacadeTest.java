package com.triade.simple.core.bridges.facade;

import com.triade.simple.core.bridges.domain.Bridge;
import com.triade.simple.core.bridges.dto.BridgeDTO;
import com.triade.simple.core.bridges.service.BridgeManagerService;
import com.triade.simple.core.bridges.service.BridgeMapper;
import com.triade.simple.core.bridges.service.BridgeOperationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BridgeFacadeTest {

    @Mock
    private BridgeManagerService managerService;

    @Mock
    private BridgeOperationService operationService;

    @InjectMocks
    private BridgeFacade facade;

    @Test
    void moveBridge_CanOperateAndMoveBridge() {
        // Arrange
        Long id = 1L;
        byte[] commandBytes = {1, 2, 3};
        Bridge bridge = new Bridge();

        when(operationService.canOperate(id)).thenReturn(bridge);

        // Act
        facade.moveBridge(id, commandBytes);

        // Assert
        verify(operationService, times(1)).canOperate(id);
        verify(operationService, times(1)).moveBridge(bridge, commandBytes);
    }

    @Test
    void createBridge_ReturnsBridgeDTO() {
        // Arrange
        BridgeDTO bridgeDTO = new BridgeDTO();
        Bridge bridge = new Bridge();

        when(managerService.create(any())).thenReturn(bridge);

        // Act
        BridgeDTO result = facade.createBridge(bridgeDTO);

        // Assert
        assertNotNull(result);
    }

    @Test
    void getAllBridges_ReturnsListOfBridgeDTO() {
        // Arrange
        List<Bridge> bridges = new ArrayList<>();
        bridges.add(new Bridge());
        bridges.add(new Bridge());

        when(managerService.getAll()).thenReturn(bridges);

        // Act
        List<BridgeDTO> result = facade.getAllBridges();

        // Assert
        assertNotNull(result);
        assertEquals(bridges.size(), result.size());
    }

    @Test
    void getBridgeById_ReturnsBridgeDTO() {
        // Arrange
        Long id = 1L;
        Bridge bridge = new Bridge();
        BridgeDTO bridgeDTO = new BridgeDTO();

        when(managerService.getById(id)).thenReturn(bridge);

        // Act
        BridgeDTO result = facade.getBridgeById(id);

        // Assert
        assertNotNull(result);
    }

    @Test
    void deleteBridgeById_CallsManagerServiceDelete() {
        // Arrange
        Long id = 1L;

        // Act
        facade.deleteBridgeById(id);

        // Assert
        verify(managerService, times(1)).delete(id);
    }

    @Test
    void updateBridgeById_ReturnsUpdatedBridgeDTO() {
        // Arrange
        Long id = 1L;
        BridgeDTO updatedBridgeDTO = new BridgeDTO();
        Bridge updatedBridge = new Bridge();

        when(managerService.update(eq(id), any())).thenReturn(updatedBridge);

        // Act
        BridgeDTO result = facade.updateBridgeById(id, updatedBridgeDTO);

        // Assert
        assertNotNull(result);
    }
}

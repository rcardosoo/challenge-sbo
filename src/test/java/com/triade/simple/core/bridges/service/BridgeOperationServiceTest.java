package com.triade.simple.core.bridges.service;

import com.triade.simple.core.bridges.domain.Bridge;
import com.triade.simple.core.bridges.domain.BridgePositionEnum;
import com.triade.simple.core.bridges.domain.BridgeStatusEnum;
import com.triade.simple.core.bridges.repository.BridgeRepository;
import com.triade.simple.exception.BridgeNotFoundException;
import com.triade.simple.exception.BridgeOperationNotAllowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BridgeOperationServiceTest {

    @Mock
    private BridgeRepository repository;

    @InjectMocks
    private BridgeOperationService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void moveBridge_OriginalStatusStopped_SavesBridge() {
        // Arrange
        byte[] commandBytes = new byte[10];
        Bridge bridge = new Bridge();
        bridge.setId(1L);
        bridge.setPosition(BridgePositionEnum.BASE);
        bridge.setStatus(BridgeStatusEnum.STOPPED);

        when(repository.save(any())).thenReturn(bridge.toBuilder().build());

        // Act
        service.moveBridge(bridge, commandBytes);

        // Assert
        verify(repository, times(1)).save(any());
    }

    @Test
    void moveBridge_OriginalStatusNotStopped_SavesBridge() {
        // Arrange
        byte[] commandBytes = new byte[10];
        Bridge bridge = new Bridge();
        bridge.setId(1L);
        bridge.setPosition(BridgePositionEnum.BASE);
        bridge.setStatus(BridgeStatusEnum.MOVING);

        when(repository.save(any())).thenReturn(bridge.toBuilder().build());

        // Act
        service.moveBridge(bridge, commandBytes);

        // Assert
        verify(repository, times(1)).save(any());
    }

    @Test
    void canOperate_BridgeFoundAndPositionHighestAndLessThanMinute_ThrowsBridgeOperationNotAllowedException() {
        // Arrange
        Bridge bridge = new Bridge();
        bridge.setId(1L);
        bridge.setPosition(BridgePositionEnum.HIGHEST);
        bridge.setLastOperationTime(LocalDateTime.now().minusSeconds(30)); // Less than a minute

        when(repository.findById(1L)).thenReturn(Optional.of(bridge));

        // Act & Assert
        assertThrows(BridgeOperationNotAllowedException.class, () -> service.canOperate(1L));
    }

    @Test
    void canOperate_BridgeFoundAndPositionNotHighest_ReturnsBridge() {
        // Arrange
        Bridge bridge = new Bridge();
        bridge.setId(1L);
        bridge.setPosition(BridgePositionEnum.BASE);
        bridge.setLastOperationTime(LocalDateTime.now().minusSeconds(30)); // Less than a minute

        when(repository.findById(1L)).thenReturn(Optional.of(bridge));

        // Act
        Bridge result = service.canOperate(1L);

        // Assert
        assertNotNull(result);
        assertEquals(bridge, result);
    }

    @Test
    void canOperate_BridgeNotFound_ThrowsBridgeNotFoundException() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BridgeNotFoundException.class, () -> service.canOperate(1L));
    }
}

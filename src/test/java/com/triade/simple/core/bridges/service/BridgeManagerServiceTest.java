package com.triade.simple.core.bridges.service;

import com.triade.simple.core.bridges.domain.Bridge;
import com.triade.simple.core.bridges.repository.BridgeRepository;
import com.triade.simple.exception.BridgeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BridgeManagerServiceTest {

    @Mock
    private BridgeRepository repository;

    @InjectMocks
    private BridgeManagerService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ReturnsBridge() {
        // Arrange
        Bridge bridge = new Bridge();
        bridge.setId(1L);

        when(repository.save(any())).thenReturn(bridge);

        // Act
        Bridge result = service.create(bridge);

        // Assert
        assertNotNull(result);
        assertEquals(bridge, result);
    }

    @Test
    void getAll_ReturnsListOfBridges() {
        // Arrange
        List<Bridge> bridges = new ArrayList<>();
        Bridge bridge1 = new Bridge();
        bridge1.setId(1L);
        bridges.add(bridge1);
        Bridge bridge2 = new Bridge();
        bridge2.setId(2L);
        bridges.add(bridge2);

        when(repository.findAll()).thenReturn(bridges);

        // Act
        List<Bridge> result = service.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(bridge1));
        assertTrue(result.contains(bridge2));
    }

    @Test
    void getById_BridgeFound_ReturnsBridge() {
        // Arrange
        Bridge bridge = new Bridge();
        bridge.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(bridge));

        // Act
        Bridge result = service.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(bridge, result);
    }

    @Test
    void getById_BridgeNotFound_ThrowsBridgeNotFoundException() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BridgeNotFoundException.class, () -> service.getById(1L));
    }

    @Test
    void delete_BridgeFound_DeletesBridge() {
        // Arrange
        Bridge bridge = new Bridge();
        bridge.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(bridge));

        // Act
        service.delete(1L);

        // Assert
        verify(repository, times(1)).delete(bridge);
    }

    @Test
    void delete_BridgeNotFound_ThrowsBridgeNotFoundException() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BridgeNotFoundException.class, () -> service.delete(1L));
    }

    @Test
    void update_BridgeFound_ReturnsUpdatedBridge() {
        // Arrange
        Bridge bridge = new Bridge();
        bridge.setId(1L);
        bridge.setModelName("Old Model");
        bridge.setMovementDurationSeconds(30);

        Bridge updatedBridge = new Bridge();
        updatedBridge.setId(1L);
        updatedBridge.setModelName("New Model");
        updatedBridge.setMovementDurationSeconds(60);

        when(repository.findById(1L)).thenReturn(Optional.of(bridge));
        when(repository.save(any())).thenReturn(updatedBridge);

        // Act
        Bridge result = service.update(1L, updatedBridge);

        // Assert
        assertNotNull(result);
        assertEquals(updatedBridge, result);
    }

    @Test
    void update_BridgeNotFound_ThrowsBridgeNotFoundException() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BridgeNotFoundException.class, () -> service.update(1L, new Bridge()));
    }
}

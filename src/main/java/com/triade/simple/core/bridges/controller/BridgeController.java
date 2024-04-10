package com.triade.simple.core.bridges.controller;

import com.triade.simple.core.bridges.dto.BaseResponseDTO;
import com.triade.simple.core.bridges.dto.BridgeDTO;
import com.triade.simple.core.bridges.facade.BridgeFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.triade.simple.core.Constants.OPERATION_REQUESTED;

@RestController
@RequestMapping("/api/bridges")
public class BridgeController {

    @Autowired
    private BridgeFacade bridgeFacade;

    @PostMapping("/{id}/move")
    public ResponseEntity<?> moveBridge(@PathVariable Long id, @RequestBody byte[] commandBytes) {
        bridgeFacade.moveBridge(id, commandBytes);
        return ResponseEntity.ok(new BaseResponseDTO(OPERATION_REQUESTED));
    }

    @PostMapping
    public ResponseEntity<BridgeDTO> createBridge(@Valid @RequestBody BridgeDTO bridgeDTO) {
        BridgeDTO createdBridge = bridgeFacade.createBridge(bridgeDTO);
        return new ResponseEntity<>(createdBridge, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BridgeDTO>> getAllBridges() {
        List<BridgeDTO> bridges = bridgeFacade.getAllBridges();
        return new ResponseEntity<>(bridges, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BridgeDTO> getBridgeById(@PathVariable Long id) {
        BridgeDTO bridge = bridgeFacade.getBridgeById(id);
        return new ResponseEntity<>(bridge, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBridgeById(@PathVariable Long id) {
        bridgeFacade.deleteBridgeById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BridgeDTO> updateBridgeById(@PathVariable Long id, @Valid @RequestBody BridgeDTO updatedBridgeDTO) {
        BridgeDTO updatedBridge = bridgeFacade.updateBridgeById(id, updatedBridgeDTO);
        return new ResponseEntity<>(updatedBridge, HttpStatus.OK);
    }
}

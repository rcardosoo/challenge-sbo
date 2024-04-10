package com.triade.simple.core.bridges.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "bridge")
@AllArgsConstructor
@NoArgsConstructor
public class Bridge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private int movementDurationSeconds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BridgePositionEnum position = BridgePositionEnum.BASE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BridgeStatusEnum status = BridgeStatusEnum.STOPPED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BridgeDirectionEnum direction = BridgeDirectionEnum.UP;

    @Column(name = "last_operation_time")
    private LocalDateTime lastOperationTime = LocalDateTime.now();
}

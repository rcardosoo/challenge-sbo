package com.triade.simple.core.bridges.dto;

import com.triade.simple.core.bridges.domain.BridgeDirectionEnum;
import com.triade.simple.core.bridges.domain.BridgePositionEnum;
import com.triade.simple.core.bridges.domain.BridgeStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BridgeDTO {

    private Long id;

    @NotBlank
    private String modelName;

    @NotNull
    private Integer movementDurationSeconds;

    private BridgePositionEnum position = BridgePositionEnum.BASE;

    private BridgeStatusEnum status = BridgeStatusEnum.STOPPED;

    private BridgeDirectionEnum direction = BridgeDirectionEnum.UP;
}

package com.triade.simple.core.bridges.service;

import com.triade.simple.core.bridges.domain.Bridge;
import com.triade.simple.core.bridges.dto.BridgeDTO;
import com.triade.simple.core.user.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BridgeMapper extends EntityMapper<BridgeDTO, Bridge> {

    BridgeMapper INSTANCE = Mappers.getMapper(BridgeMapper.class);

    @Override
    Bridge toEntity(BridgeDTO dto);

    @Override
    BridgeDTO toDto(Bridge entity);

    @Override
    List<Bridge> toEntity(List<BridgeDTO> dtoList);

    @Override
    List<BridgeDTO> toDto(List<Bridge> entityList);
}

package com.oceanus.doris.service.mapper;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.service.dto.PositionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Position and its DTO PositionDTO.
 */
@Mapper(componentModel = "spring", uses = {AssetMapper.class, PortfolioMapper.class})
public interface PositionMapper extends EntityMapper<PositionDTO, Position> {

    @Mapping(source = "asset.id", target = "assetId")
    @Mapping(source = "asset.code", target = "assetCode")
    @Mapping(source = "portfolio.id", target = "portfolioId")
    @Mapping(source = "portfolio.description", target = "portfolioDescription")
    PositionDTO toDto(Position position); 

    @Mapping(source = "assetId", target = "asset")
    @Mapping(source = "portfolioId", target = "portfolio")
    Position toEntity(PositionDTO positionDTO);

    default Position fromId(Long id) {
        if (id == null) {
            return null;
        }
        Position position = new Position();
        position.setId(id);
        return position;
    }
}

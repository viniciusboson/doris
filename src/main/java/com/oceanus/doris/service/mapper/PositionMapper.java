package com.oceanus.doris.service.mapper;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.service.dto.PositionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Position and its DTO PositionDTO.
 */
@Mapper(componentModel = "spring", uses = {AssetMapper.class, WalletMapper.class})
public interface PositionMapper extends EntityMapper<PositionDTO, Position> {

    @Mapping(source = "asset.id", target = "assetId")
    @Mapping(source = "asset.code", target = "assetCode")
    @Mapping(source = "wallet.id", target = "walletId")
    @Mapping(source = "wallet.description", target = "walletDescription")
    PositionDTO toDto(Position position); 

    @Mapping(source = "assetId", target = "asset")
    @Mapping(source = "walletId", target = "wallet")
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

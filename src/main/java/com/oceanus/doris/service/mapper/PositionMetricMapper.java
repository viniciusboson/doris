package com.oceanus.doris.service.mapper;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.service.dto.PositionMetricDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PositionMetric and its DTO PositionMetricDTO.
 */
@Mapper(componentModel = "spring", uses = {PositionMapper.class, AssetMapper.class})
public interface PositionMetricMapper extends EntityMapper<PositionMetricDTO, PositionMetric> {

    @Mapping(source = "position.id", target = "positionId")
    @Mapping(source = "position.description", target = "positionDescription")
    @Mapping(source = "assetComparison.id", target = "assetComparisonId")
    @Mapping(source = "assetComparison.code", target = "assetComparisonCode")
    PositionMetricDTO toDto(PositionMetric positionMetric); 

    @Mapping(source = "positionId", target = "position")
    @Mapping(source = "assetComparisonId", target = "assetComparison")
    PositionMetric toEntity(PositionMetricDTO positionMetricDTO);

    default PositionMetric fromId(Long id) {
        if (id == null) {
            return null;
        }
        PositionMetric positionMetric = new PositionMetric();
        positionMetric.setId(id);
        return positionMetric;
    }
}

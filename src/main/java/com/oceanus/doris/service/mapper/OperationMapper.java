package com.oceanus.doris.service.mapper;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.service.dto.OperationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Operation and its DTO OperationDTO.
 */
@Mapper(componentModel = "spring", uses = {PositionMapper.class, InstitutionMapper.class})
public interface OperationMapper extends EntityMapper<OperationDTO, Operation> {

    @Mapping(source = "positionFrom.id", target = "positionFromId")
    @Mapping(source = "positionFrom.description", target = "positionFromDescription")
    @Mapping(source = "institutionFrom.id", target = "institutionFromId")
    @Mapping(source = "institutionFrom.description", target = "institutionFromDescription")
    @Mapping(source = "positionTo.id", target = "positionToId")
    @Mapping(source = "positionTo.description", target = "positionToDescription")
    @Mapping(source = "institutionTo.id", target = "institutionToId")
    @Mapping(source = "institutionTo.description", target = "institutionToDescription")
    OperationDTO toDto(Operation operation); 

    @Mapping(source = "positionFromId", target = "positionFrom")
    @Mapping(source = "institutionFromId", target = "institutionFrom")
    @Mapping(source = "positionToId", target = "positionTo")
    @Mapping(source = "institutionToId", target = "institutionTo")
    Operation toEntity(OperationDTO operationDTO);

    default Operation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Operation operation = new Operation();
        operation.setId(id);
        return operation;
    }
}

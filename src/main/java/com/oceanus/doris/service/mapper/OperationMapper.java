package com.oceanus.doris.service.mapper;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.service.dto.OperationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Operation and its DTO OperationDTO.
 */
@Mapper(componentModel = "spring", uses = {PositionMapper.class, InstitutionMapper.class})
public interface OperationMapper extends EntityMapper<OperationDTO, Operation> {

    @Mapping(source = "fromPosition.id", target = "fromPositionId")
    @Mapping(source = "fromPosition.description", target = "fromPositionDescription")
    @Mapping(source = "fromInstitution.id", target = "fromInstitutionId")
    @Mapping(source = "fromInstitution.description", target = "fromInstitutionDescription")
    @Mapping(source = "toPosition.id", target = "toPositionId")
    @Mapping(source = "toPosition.description", target = "toPositionDescription")
    @Mapping(source = "toInstitution.id", target = "toInstitutionId")
    @Mapping(source = "toInstitution.description", target = "toInstitutionDescription")
    OperationDTO toDto(Operation operation); 

    @Mapping(source = "fromPositionId", target = "fromPosition")
    @Mapping(source = "fromInstitutionId", target = "fromInstitution")
    @Mapping(source = "toPositionId", target = "toPosition")
    @Mapping(source = "toInstitutionId", target = "toInstitution")
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

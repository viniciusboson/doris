package com.oceanus.doris.service.mapper;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.service.dto.OperationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Operation and its DTO OperationDTO.
 */
@Mapper(componentModel = "spring", uses = {InstitutionMapper.class, AssetMapper.class})
public interface OperationMapper extends EntityMapper<OperationDTO, Operation> {

    @Mapping(source = "fromInstitution.id", target = "fromInstitutionId")
    @Mapping(source = "fromInstitution.description", target = "fromInstitutionDescription")
    @Mapping(source = "fromAsset.id", target = "fromAssetId")
    @Mapping(source = "fromAsset.code", target = "fromAssetCode")
    @Mapping(source = "toAsset.id", target = "toAssetId")
    @Mapping(source = "toAsset.code", target = "toAssetCode")
    @Mapping(source = "toInstitution.id", target = "toInstitutionId")
    @Mapping(source = "toInstitution.description", target = "toInstitutionDescription")
    OperationDTO toDto(Operation operation); 

    @Mapping(source = "fromInstitutionId", target = "fromInstitution")
    @Mapping(source = "fromAssetId", target = "fromAsset")
    @Mapping(source = "toAssetId", target = "toAsset")
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

package com.oceanus.doris.service.mapper;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.service.dto.ChargeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Charge and its DTO ChargeDTO.
 */
@Mapper(componentModel = "spring", uses = {InstitutionMapper.class, AssetMapper.class})
public interface ChargeMapper extends EntityMapper<ChargeDTO, Charge> {

    @Mapping(source = "institution.id", target = "institutionId")
    @Mapping(source = "institution.description", target = "institutionDescription")
    ChargeDTO toDto(Charge charge); 

    @Mapping(source = "institutionId", target = "institution")
    Charge toEntity(ChargeDTO chargeDTO);

    default Charge fromId(Long id) {
        if (id == null) {
            return null;
        }
        Charge charge = new Charge();
        charge.setId(id);
        return charge;
    }
}

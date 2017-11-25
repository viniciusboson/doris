package com.oceanus.doris.service.mapper;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.web.rest.dto.InstitutionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Institution and its DTO InstitutionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InstitutionMapper extends EntityMapper<InstitutionDTO, Institution> {





    default Institution fromId(Long id) {
        if (id == null) {
            return null;
        }
        Institution institution = new Institution();
        institution.setId(id);
        return institution;
    }
}

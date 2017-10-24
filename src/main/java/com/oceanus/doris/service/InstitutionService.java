package com.oceanus.doris.service;

import com.oceanus.doris.service.dto.InstitutionDTO;
import java.util.List;

/**
 * Service Interface for managing Institution.
 */
public interface InstitutionService {

    /**
     * Save a institution.
     *
     * @param institutionDTO the entity to save
     * @return the persisted entity
     */
    InstitutionDTO save(InstitutionDTO institutionDTO);

    /**
     *  Get all the institutions.
     *
     *  @return the list of entities
     */
    List<InstitutionDTO> findAll();

    /**
     *  Get the "id" institution.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    InstitutionDTO findOne(Long id);

    /**
     *  Delete the "id" institution.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}

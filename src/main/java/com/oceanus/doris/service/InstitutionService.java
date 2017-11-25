package com.oceanus.doris.service;

import com.oceanus.doris.domain.Institution;

import java.util.List;

/**
 * Service Interface for managing Institution.
 */
public interface InstitutionService {

    /**
     * Save a institution.
     *
     * @param institution the entity to save
     * @return the persisted entity
     */
    Institution save(Institution institution);

    /**
     *  Get all the institutions.
     *
     *  @return the list of entities
     */
    List<Institution> findAll();

    /**
     *  Get the "id" institution.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Institution findOne(Long id);

    /**
     *  Delete the "id" institution.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}

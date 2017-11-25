package com.oceanus.doris.service;

import com.oceanus.doris.domain.Charge;

import java.util.List;

/**
 * Service Interface for managing Charge.
 */
public interface ChargeService {

    /**
     * Save a charge.
     *
     * @param charge the entity to save
     * @return the persisted entity
     */
    Charge save(Charge charge);

    /**
     *  Get all the charges.
     *
     *  @return the list of entities
     */
    List<Charge> findAll();

    /**
     *  Get the "id" charge.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Charge findOne(Long id);

    /**
     *  Delete the "id" charge.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}

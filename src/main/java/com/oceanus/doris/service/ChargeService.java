package com.oceanus.doris.service;

import com.oceanus.doris.service.dto.ChargeDTO;
import java.util.List;

/**
 * Service Interface for managing Charge.
 */
public interface ChargeService {

    /**
     * Save a charge.
     *
     * @param chargeDTO the entity to save
     * @return the persisted entity
     */
    ChargeDTO save(ChargeDTO chargeDTO);

    /**
     *  Get all the charges.
     *
     *  @return the list of entities
     */
    List<ChargeDTO> findAll();

    /**
     *  Get the "id" charge.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ChargeDTO findOne(Long id);

    /**
     *  Delete the "id" charge.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}

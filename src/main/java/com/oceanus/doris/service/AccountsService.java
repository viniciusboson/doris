package com.oceanus.doris.service;

import com.oceanus.doris.domain.Accounts;
import java.util.List;

/**
 * Service Interface for managing Accounts.
 */
public interface AccountsService {

    /**
     * Save a accounts.
     *
     * @param accounts the entity to save
     * @return the persisted entity
     */
    Accounts save(Accounts accounts);

    /**
     *  Get all the accounts.
     *
     *  @return the list of entities
     */
    List<Accounts> findAll();

    /**
     *  Get the "id" accounts.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Accounts findOne(Long id);

    /**
     *  Delete the "id" accounts.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}

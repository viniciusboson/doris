package com.oceanus.doris.service;

import com.oceanus.doris.service.dto.AccountsDTO;
import java.util.List;

/**
 * Service Interface for managing Accounts.
 */
public interface AccountsService {

    /**
     * Save a accounts.
     *
     * @param accountsDTO the entity to save
     * @return the persisted entity
     */
    AccountsDTO save(AccountsDTO accountsDTO);

    /**
     *  Get all the accounts.
     *
     *  @return the list of entities
     */
    List<AccountsDTO> findAll();

    /**
     *  Get the "id" accounts.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AccountsDTO findOne(Long id);

    /**
     *  Delete the "id" accounts.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}

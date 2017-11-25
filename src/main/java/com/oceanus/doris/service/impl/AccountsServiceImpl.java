package com.oceanus.doris.service.impl;

import com.oceanus.doris.service.AccountsService;
import com.oceanus.doris.domain.Accounts;
import com.oceanus.doris.repository.AccountsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Accounts.
 */
@Service
@Transactional
public class AccountsServiceImpl implements AccountsService{

    private final Logger log = LoggerFactory.getLogger(AccountsServiceImpl.class);

    private final AccountsRepository accountsRepository;

    public AccountsServiceImpl(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    /**
     * Save a accounts.
     *
     * @param accounts the entity to save
     * @return the persisted entity
     */
    @Override
    public Accounts save(Accounts accounts) {
        log.debug("Request to save Accounts : {}", accounts);
        accounts = accountsRepository.save(accounts);
        return accounts;
    }

    /**
     *  Get all the accounts.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Accounts> findAll() {
        log.debug("Request to get all Accounts");
        return accountsRepository.findAllWithEagerRelationships().stream()
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one accounts by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Accounts findOne(Long id) {
        log.debug("Request to get Accounts : {}", id);
        Accounts accounts = accountsRepository.findOneWithEagerRelationships(id);
        return accounts;
    }

    /**
     *  Delete the  accounts by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Accounts : {}", id);
        accountsRepository.delete(id);
    }
}

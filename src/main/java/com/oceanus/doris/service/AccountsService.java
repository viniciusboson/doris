package com.oceanus.doris.service;

import com.oceanus.doris.domain.Accounts;
import com.oceanus.doris.repository.AccountsRepository;
import com.oceanus.doris.service.dto.AccountsDTO;
import com.oceanus.doris.service.mapper.AccountsMapper;
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
public class AccountsService {

    private final Logger log = LoggerFactory.getLogger(AccountsService.class);

    private final AccountsRepository accountsRepository;

    private final AccountsMapper accountsMapper;

    public AccountsService(AccountsRepository accountsRepository, AccountsMapper accountsMapper) {
        this.accountsRepository = accountsRepository;
        this.accountsMapper = accountsMapper;
    }

    /**
     * Save a accounts.
     *
     * @param accountsDTO the entity to save
     * @return the persisted entity
     */
    public AccountsDTO save(AccountsDTO accountsDTO) {
        log.debug("Request to save Accounts : {}", accountsDTO);
        Accounts accounts = accountsMapper.toEntity(accountsDTO);
        accounts = accountsRepository.save(accounts);
        return accountsMapper.toDto(accounts);
    }

    /**
     *  Get all the accounts.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AccountsDTO> findAll() {
        log.debug("Request to get all Accounts");
        return accountsRepository.findAll().stream()
            .map(accountsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one accounts by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AccountsDTO findOne(Long id) {
        log.debug("Request to get Accounts : {}", id);
        Accounts accounts = accountsRepository.findOne(id);
        return accountsMapper.toDto(accounts);
    }

    /**
     *  Delete the  accounts by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Accounts : {}", id);
        accountsRepository.delete(id);
    }
}

package com.oceanus.doris.service;

import com.oceanus.doris.domain.Transaction;
import com.oceanus.doris.repository.TransactionRepository;
import com.oceanus.doris.service.dto.TransactionDTO;
import com.oceanus.doris.service.mapper.TransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Transaction.
 */
@Service
@Transactional
public class TransactionService {

    private final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Save a transaction.
     *
     * @param transactionDTO the entity to save
     * @return the persisted entity
     */
    public TransactionDTO save(TransactionDTO transactionDTO) {
        log.debug("Request to save Transaction : {}", transactionDTO);
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    /**
     *  Get all the transactions.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO> findAll() {
        log.debug("Request to get all Transactions");
        return transactionRepository.findAll().stream()
            .map(transactionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one transaction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TransactionDTO findOne(Long id) {
        log.debug("Request to get Transaction : {}", id);
        Transaction transaction = transactionRepository.findOne(id);
        return transactionMapper.toDto(transaction);
    }

    /**
     *  Delete the  transaction by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Transaction : {}", id);
        transactionRepository.delete(id);
    }
}

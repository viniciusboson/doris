package com.oceanus.doris.service;

import com.oceanus.doris.domain.Transaction;

import java.util.List;

/**
 * Service Interface for managing Transaction.
 */
public interface TransactionService {

    /**
     * Save a transaction.
     *
     * @param transaction the entity to save
     * @return the persisted entity
     */
    Transaction save(Transaction transaction);

    /**
     *  Get all the transactions.
     *
     *  @return the list of entities
     */
    List<Transaction> findAll();

    /**
     *  Get the "id" transaction.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Transaction findOne(Long id);

    /**
     *  Delete the "id" transaction.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}

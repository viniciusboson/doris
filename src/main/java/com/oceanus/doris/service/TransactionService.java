package com.oceanus.doris.service;

import com.oceanus.doris.service.dto.OperationDTO;
import com.oceanus.doris.service.dto.TransactionDTO;
import java.util.List;

/**
 * Service Interface for managing Transaction.
 */
public interface TransactionService {

    /**
     * Save a transaction.
     *
     * @param transactionDTO the entity to save
     * @return the persisted entity
     */
    TransactionDTO save(TransactionDTO transactionDTO);

    /**
     *  Get all the transactions.
     *
     *  @return the list of entities
     */
    List<TransactionDTO> findAll();

    /**
     *  Get the "id" transaction.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TransactionDTO findOne(Long id);

    /**
     *  Delete the "id" transaction.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Create transactions based on a given operation.
     *
     * @param operationDTO containing the details which will generate the transactions
     */
    List<TransactionDTO> createTransactions(OperationDTO operationDTO);
}

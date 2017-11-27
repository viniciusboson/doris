package com.oceanus.doris.service.impl;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.repository.*;
import com.oceanus.doris.service.OperationService;
import com.oceanus.doris.service.PositionMetricService;
import com.oceanus.doris.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Operation.
 */
@Service
@Transactional
public class OperationServiceImpl implements OperationService{

    private final Logger log = LoggerFactory.getLogger(OperationServiceImpl.class);

    private final OperationRepository operationRepository;

    private final TransactionService transactionService;

    private final PositionMetricService positionMetricService;

    public OperationServiceImpl(OperationRepository operationRepository, TransactionService transactionService,
                                PositionMetricService positionMetricService) {
        this.operationRepository = operationRepository;
        this.transactionService = transactionService;
        this.positionMetricService = positionMetricService;
    }

    /**
     * Save a operation.
     *
     * @param operation the entity to save
     * @return the persisted entity
     */
    @Override
    public Operation save(Operation operation) {
        log.debug("Request to save Operation : {}", operation);
        operation = operationRepository.save(operation);
        return operation;
    }

    /**
     * Create an operation and all its dependencies. See {@link com.oceanus.doris.domain.Position},
     * {@link com.oceanus.doris.domain.Transaction}, {@link com.oceanus.doris.domain.PositionMetric}
     *
     * @param operation the entity to save
     * @return the persisted entity
     */
    public Operation create(Operation operation) {
        log.debug("Request to create Operation : {}", operation);

        operation = operationRepository.save(operation);

        //TODO: decouple PositionMetric with Operation using pub/sub topic
        transactionService.createTransactions(operation);
        positionMetricService.createMetric(operation);

        return operation;
    }

    /**
     *  Get all the operations.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Operation> findAll() {
        log.debug("Request to get all Operations");
        return operationRepository.findAll().stream()
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one operation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Operation findOne(Long id) {
        log.debug("Request to get Operation : {}", id);
        Operation operation = operationRepository.findOne(id);
        return operation;
    }

    /**
     *  Delete the  operation by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Operation : {}", id);
        operationRepository.delete(id);
    }
}

package com.oceanus.doris.service.impl;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.repository.*;
import com.oceanus.doris.service.*;
import com.oceanus.doris.service.dto.*;
import com.oceanus.doris.service.mapper.OperationMapper;
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

    private final OperationMapper operationMapper;

    private final TransactionService transactionService;

    private final PositionMetricService positionMetricService;

    public OperationServiceImpl(OperationRepository operationRepository, OperationMapper operationMapper,
                                TransactionService transactionService, PositionMetricService positionMetricService) {
        this.operationRepository = operationRepository;
        this.operationMapper = operationMapper;
        this.transactionService = transactionService;
        this.positionMetricService = positionMetricService;
    }

    /**
     * Save a operation.
     *
     * @param operationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OperationDTO save(OperationDTO operationDTO) {
        log.debug("Request to save Operation : {}", operationDTO);
        Operation operation = operationMapper.toEntity(operationDTO);
        operation = operationRepository.save(operation);
        return operationMapper.toDto(operation);
    }

    /**
     * Create an operation and all its dependencies. See {@link com.oceanus.doris.domain.Position},
     * {@link com.oceanus.doris.domain.Transaction}, {@link com.oceanus.doris.domain.PositionMetric}
     *
     * @param operationDTO the entity to save
     * @return the persisted entity
     */
    public OperationDTO create(final OperationDTO operationDTO) {
        log.debug("Request to create Operation : {}", operationDTO);

        Operation operation = operationMapper.toEntity(operationDTO);
        operation = operationRepository.save(operation);
        final OperationDTO result = operationMapper.toDto(operation);

        //TODO: decouple following services with Operation using pub/sub topic
        transactionService.createTransactions(result);
        positionMetricService.createMetric(result);

        return result;
    }

    /**
     *  Get all the operations.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<OperationDTO> findAll() {
        log.debug("Request to get all Operations");
        return operationRepository.findAll().stream()
            .map(operationMapper::toDto)
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
    public OperationDTO findOne(Long id) {
        log.debug("Request to get Operation : {}", id);
        Operation operation = operationRepository.findOne(id);
        return operationMapper.toDto(operation);
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

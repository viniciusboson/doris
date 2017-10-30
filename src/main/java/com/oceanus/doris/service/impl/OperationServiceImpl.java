package com.oceanus.doris.service.impl;

import com.oceanus.doris.domain.Operation;
import com.oceanus.doris.domain.Position;
import com.oceanus.doris.domain.Transaction;
import com.oceanus.doris.domain.enumeration.TransactionType;
import com.oceanus.doris.repository.OperationRepository;
import com.oceanus.doris.repository.PositionRepository;
import com.oceanus.doris.repository.TransactionRepository;
import com.oceanus.doris.service.OperationService;
import com.oceanus.doris.service.dto.OperationDTO;
import com.oceanus.doris.service.mapper.OperationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.oceanus.doris.domain.enumeration.TransactionType.DEBIT;
import static java.lang.String.join;

/**
 * Service Implementation for managing Operation.
 */
@Service
@Transactional
public class OperationServiceImpl implements OperationService{

    private final Logger log = LoggerFactory.getLogger(OperationServiceImpl.class);

    private final OperationRepository operationRepository;

    private final PositionRepository positionRepository;

    private final TransactionRepository transactionRepository;

    private final OperationMapper operationMapper;

    public OperationServiceImpl(OperationRepository operationRepository, OperationMapper operationMapper,
                                PositionRepository positionRepository, TransactionRepository transactionRepository) {
        this.operationRepository = operationRepository;
        this.operationMapper = operationMapper;
        this.positionRepository = positionRepository;
        this.transactionRepository = transactionRepository;
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
    public OperationDTO create(OperationDTO operationDTO) {
        log.debug("Request to create Operation : {}", operationDTO);

        Operation operation = operationMapper.toEntity(operationDTO);
        operation = operationRepository.save(operation);

        processOriginTransactions(operation);

        return operationMapper.toDto(operation);
    }

    Operation processOriginTransactions(final Operation operation) {
        Position origin = positionRepository.findOne(operation.getPositionFrom().getId());
        log.debug("Process transactions of operation {} over position {}", operation,  origin);

        final Double amount = operation.getAmountFrom();
        final Double newBalance = origin.getBalance() - amount;
        origin = positionRepository.save(origin.balance(newBalance));

        final Transaction transaction = new Transaction().executedAt(operation.getExecutedAt())
            .operation(operation)
            .position(origin)
            .description(join(" - ", operation.getOperationType().toString(),
                operation.getInstitutionFrom().getDescription()))
            .amount(amount)
            .balance(newBalance)
            .type(DEBIT);
        transactionRepository.save(transaction);

        return operation;
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

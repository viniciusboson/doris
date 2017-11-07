package com.oceanus.doris.service.impl;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.domain.enumeration.TransactionType;
import com.oceanus.doris.repository.PositionMetricRepository;
import com.oceanus.doris.repository.PositionRepository;
import com.oceanus.doris.repository.TransactionRepository;
import com.oceanus.doris.service.PositionMetricService;
import com.oceanus.doris.service.dto.OperationDTO;
import com.oceanus.doris.service.dto.PositionMetricDTO;
import com.oceanus.doris.service.dto.TransactionDTO;
import com.oceanus.doris.service.mapper.OperationMapper;
import com.oceanus.doris.service.mapper.PositionMetricMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.oceanus.doris.domain.enumeration.TransactionType.CHARGE;
import static com.oceanus.doris.domain.enumeration.TransactionType.CREDIT;
import static com.oceanus.doris.domain.enumeration.TransactionType.DEBIT;

/**
 * Service Implementation for managing PositionMetric.
 */
@Service
@Transactional
public class PositionMetricServiceImpl implements PositionMetricService{

    private final Logger log = LoggerFactory.getLogger(PositionMetricServiceImpl.class);

    private final PositionMetricRepository positionMetricRepository;

    private final PositionRepository positionRepository;

    private final TransactionRepository transactionRepository;

    private final PositionMetricMapper positionMetricMapper;

    private final OperationMapper operationMapper;

    public PositionMetricServiceImpl(PositionMetricRepository positionMetricRepository,
                                     PositionRepository positionRepository, TransactionRepository transactionRepository,
                                     PositionMetricMapper positionMetricMapper, OperationMapper operationMapper) {
        this.positionMetricRepository = positionMetricRepository;
        this.positionMetricMapper = positionMetricMapper;
        this.positionRepository = positionRepository;
        this.transactionRepository = transactionRepository;
        this.operationMapper = operationMapper;
    }

    /**
     * Save a positionMetric.
     *
     * @param positionMetricDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PositionMetricDTO save(PositionMetricDTO positionMetricDTO) {
        log.debug("Request to save PositionMetric : {}", positionMetricDTO);
        PositionMetric positionMetric = positionMetricMapper.toEntity(positionMetricDTO);
        positionMetric = positionMetricRepository.save(positionMetric);
        return positionMetricMapper.toDto(positionMetric);
    }

    /**
     *  Get all the positionMetrics.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PositionMetricDTO> findAll() {
        log.debug("Request to get all PositionMetrics");
        return positionMetricRepository.findAll().stream()
            .map(positionMetricMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one positionMetric by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PositionMetricDTO findOne(Long id) {
        log.debug("Request to get PositionMetric : {}", id);
        PositionMetric positionMetric = positionMetricRepository.findOne(id);
        return positionMetricMapper.toDto(positionMetric);
    }

    /**
     *  Delete the  positionMetric by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PositionMetric : {}", id);
        positionMetricRepository.delete(id);
    }

    /**
     * Create a new positionMetric or update an existed one based on a given operation.
     *
     * @param operationDTO containing the details to be metric
     */
    @Override
    public void createMetric(OperationDTO operationDTO) {
        log.debug("Request to create a PositionMetric based on the operation : {}", operationDTO);
        final Operation operation = operationMapper.toEntity(operationDTO);
        final Position origin = positionRepository.findOne(operation.getPositionFrom().getId());
        final List<Transaction> originTransactions = transactionRepository.findAllByOperationAndPosition(operation,
            origin);
        final Position destination = positionRepository.findOne(operation.getPositionTo().getId());
        final List<Transaction> destinationTransactions = transactionRepository.findAllByOperationAndPosition(operation,
            destination);

        calculateMetric(origin, destination.getAsset(), operation.getAmountFrom(), operation.getAmountTo(),
            originTransactions, DEBIT);
        calculateMetric(destination, origin.getAsset(), operation.getAmountFrom(), operation.getAmountTo(),
            destinationTransactions, CREDIT);
    }

    private PositionMetric calculateMetric(Position position, Asset assetComparison, Double price, Double amount,
                                           List<Transaction> transactions, TransactionType transactionType) {
        PositionMetric destinationPositionMetric = positionMetricRepository.findOneByPositionAndAssetComparison(
            position, assetComparison);
        if(destinationPositionMetric == null) {
            destinationPositionMetric = new PositionMetric().position(position)
                .assetComparison(assetComparison);
        }

        Double txCosts = transactions.stream()
            .filter(t -> CHARGE.equals(t.getType()))
            .mapToDouble(Transaction::getAmount)
            .sum();


        if(DEBIT.equals(transactionType)) {
            destinationPositionMetric.decreasePosition(price, amount, txCosts);
        } else {
            destinationPositionMetric.increasePosition(price, amount, txCosts);
        }

        return positionMetricRepository.save(destinationPositionMetric);
    }
}

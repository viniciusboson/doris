package com.oceanus.doris.service.impl;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.domain.enumeration.ChargeTarget;
import com.oceanus.doris.domain.enumeration.OperationType;
import com.oceanus.doris.domain.enumeration.TransactionType;
import com.oceanus.doris.repository.PositionMetricRepository;
import com.oceanus.doris.service.ChargeService;
import com.oceanus.doris.service.PositionMetricService;
import com.oceanus.doris.service.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.oceanus.doris.domain.enumeration.ChargeTarget.DESTINATION;
import static com.oceanus.doris.domain.enumeration.ChargeTarget.ORIGIN;
import static com.oceanus.doris.domain.enumeration.OperationType.SELL;
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

    private final PositionService positionService;

    private final ChargeService chargeService;

    public PositionMetricServiceImpl(PositionMetricRepository positionMetricRepository,
                                     PositionService positionService,
                                     ChargeService chargeService) {
        this.positionMetricRepository = positionMetricRepository;
        this.positionService = positionService;
        this.chargeService = chargeService;
    }

    /**
     * Save a positionMetric.
     *
     * @param positionMetric the entity to save
     * @return the persisted entity
     */
    @Override
    public PositionMetric save(PositionMetric positionMetric) {
        log.debug("Request to save PositionMetric : {}", positionMetric);
        positionMetric = positionMetricRepository.save(positionMetric);
        return positionMetric;
    }

    /**
     *  Get all the positionMetrics.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PositionMetric> findAll() {
        log.debug("Request to get all PositionMetrics");
        return positionMetricRepository.findAll().stream()
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
    public PositionMetric findOne(Long id) {
        log.debug("Request to get PositionMetric : {}", id);
        PositionMetric positionMetric = positionMetricRepository.findOne(id);
        return positionMetric;
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
     * @param operation containing the details to be metric
     */
    @Override
    public void createMetric(Operation operation) {
        log.debug("Request to create a PositionMetric based on the operation : {}", operation);

        final Position origin = positionService.findOne(operation.getPositionFrom().getId());
        final Position destination = positionService.findOne(operation.getPositionTo().getId());

        Double chargeCostsOrigin = getTotalChargeCostForOperation(operation, origin, destination, ORIGIN,
            operation.getAmountFrom());
        Double chargeCostsDestination = getTotalChargeCostForOperation(operation, origin, destination, DESTINATION,
            operation.getAmountTo());

        calculateMetric(origin,
            destination.getAsset(),
            operation.getAmountFrom(), // + chargeCostsOrigin,
            operation.getAmountTo(), // - chargeCostsDestination,
            DEBIT,
            chargeCostsOrigin,
            operation.getOperationTypeFrom());
        calculateMetric(destination,
            origin.getAsset(),
            operation.getAmountFrom(), //  + chargeCostsOrigin,
            operation.getAmountTo(), //  - chargeCostsDestination,
            CREDIT,
            chargeCostsDestination,
            operation.getOperationTypeTo());
    }

    private PositionMetric calculateMetric(Position position, Asset assetComparison, Double amountFrom,
                                           Double amountTo, TransactionType transactionType, Double chargeCosts,
                                           OperationType operationType) {
        PositionMetric positionMetric = positionMetricRepository.findOneByPositionAndAssetComparison(
            position, assetComparison);
        if(positionMetric == null) {
            positionMetric = new PositionMetric().position(position).assetComparison(assetComparison);
        }

        final Double price = SELL.equals(operationType) ? amountTo : amountFrom;
        final Double amount = SELL.equals(operationType) ? amountFrom : amountTo;

        if(DEBIT.equals(transactionType)) {
            positionMetric.decreasePosition(price, amount, chargeCosts);
        } else {
            positionMetric.increasePosition(price, amount, chargeCosts);
        }

        return positionMetricRepository.save(positionMetric);
    }

    private Double getTotalChargeCostForOperation(Operation operation, Position origin, Position destination,
                                                  ChargeTarget target, Double amount) {
        return chargeService.getTotalChargeCost(
            operation.getInstitutionFrom(),
            origin.getAsset(),
            operation.getOperationTypeFrom(),
            operation.getInstitutionTo(),
            destination.getAsset(),
            operation.getOperationTypeTo(),
            target,
            amount);
    }
}

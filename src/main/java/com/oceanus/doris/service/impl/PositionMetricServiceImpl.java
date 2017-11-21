package com.oceanus.doris.service.impl;

import com.oceanus.doris.domain.*;
import com.oceanus.doris.domain.enumeration.ChargeTarget;
import com.oceanus.doris.domain.enumeration.OperationType;
import com.oceanus.doris.domain.enumeration.TransactionType;
import com.oceanus.doris.repository.PositionMetricRepository;
import com.oceanus.doris.service.ChargeService;
import com.oceanus.doris.service.PositionMetricService;
import com.oceanus.doris.service.PositionService;
import com.oceanus.doris.service.dto.*;
import com.oceanus.doris.service.mapper.AssetMapper;
import com.oceanus.doris.service.mapper.OperationMapper;
import com.oceanus.doris.service.mapper.PositionMapper;
import com.oceanus.doris.service.mapper.PositionMetricMapper;
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

    private final PositionMetricMapper positionMetricMapper;

    private final OperationMapper operationMapper;

    private final PositionMapper positionMapper;

    private final AssetMapper assetMapper;

    public PositionMetricServiceImpl(PositionMetricRepository positionMetricRepository,
                                     PositionService positionService, ChargeService chargeService,
                                     PositionMetricMapper positionMetricMapper, OperationMapper operationMapper,
                                     PositionMapper positionMapper, AssetMapper assetMapper) {
        this.positionMetricRepository = positionMetricRepository;
        this.chargeService = chargeService;
        this.positionMetricMapper = positionMetricMapper;
        this.positionService = positionService;
        this.positionMapper = positionMapper;
        this.assetMapper = assetMapper;
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
        final PositionDTO originDTO = positionService.findOne(operation.getPositionFrom().getId());
        final PositionDTO destinationDTO = positionService.findOne(operation.getPositionTo().getId());

        Double chargeCostsOrigin = getTotalChargeCostForOperation(operationDTO, originDTO, destinationDTO, ORIGIN,
            operationDTO.getAmountFrom());
        Double chargeCostsDestination = getTotalChargeCostForOperation(operationDTO, originDTO, destinationDTO, DESTINATION,
            operationDTO.getAmountTo());

        calculateMetric(originDTO,
            new AssetDTO().id(destinationDTO.getAssetId()),
            operation.getAmountFrom() + chargeCostsOrigin,
            operation.getAmountTo() - chargeCostsDestination,
            DEBIT,
            chargeCostsOrigin,
            operation.getOperationTypeFrom());
        calculateMetric(destinationDTO,
            new AssetDTO().id(originDTO.getAssetId()),
            operation.getAmountFrom()  + chargeCostsOrigin,
            operation.getAmountTo()  - chargeCostsDestination,
            CREDIT,
            chargeCostsDestination,
            operation.getOperationTypeTo());
    }

    private PositionMetric calculateMetric(PositionDTO positionDTO, AssetDTO assetComparison, Double amountFrom, Double amountTo,
                                           TransactionType transactionType, Double chargeCosts, OperationType operationType) {
        PositionMetric positionMetric = positionMetricRepository.findOneByPositionAndAssetComparison(
            positionMapper.toEntity(positionDTO), assetMapper.toEntity(assetComparison));
        if(positionMetric == null) {
            positionMetric = new PositionMetric().position(positionMapper.toEntity(positionDTO))
                .assetComparison(assetMapper.toEntity(assetComparison));
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

    private Double getTotalChargeCostForOperation(OperationDTO operationDTO, PositionDTO origin,
                                                  PositionDTO destination, ChargeTarget target, Double amount) {
        return chargeService.getTotalChargeCost(
            new InstitutionDTO().id(operationDTO.getInstitutionFromId()),
            new AssetDTO().id(origin.getAssetId()),
            operationDTO.getOperationTypeFrom(),
            new InstitutionDTO().id(operationDTO.getInstitutionToId()),
            new AssetDTO().id(destination.getAssetId()),
            operationDTO.getOperationTypeTo(),
            target,
            amount);
    }
}

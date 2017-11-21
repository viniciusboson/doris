package com.oceanus.doris.service.impl;

import com.oceanus.doris.domain.enumeration.ChargeTarget;
import com.oceanus.doris.domain.enumeration.OperationType;
import com.oceanus.doris.service.ChargeService;
import com.oceanus.doris.domain.Charge;
import com.oceanus.doris.repository.ChargeRepository;
import com.oceanus.doris.service.dto.AssetDTO;
import com.oceanus.doris.service.dto.ChargeDTO;
import com.oceanus.doris.service.dto.InstitutionDTO;
import com.oceanus.doris.service.mapper.AssetMapper;
import com.oceanus.doris.service.mapper.ChargeMapper;
import com.oceanus.doris.service.mapper.InstitutionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.oceanus.doris.domain.enumeration.ChargeTarget.BOTH;

/**
 * Service Implementation for managing Charge.
 */
@Service
@Transactional
public class ChargeServiceImpl implements ChargeService{

    private final Logger log = LoggerFactory.getLogger(ChargeServiceImpl.class);

    private final ChargeRepository chargeRepository;

    private final ChargeMapper chargeMapper;
    private final InstitutionMapper institutionMapper;
    private final AssetMapper assetMapper;

    public ChargeServiceImpl(ChargeRepository chargeRepository, ChargeMapper chargeMapper,
                             InstitutionMapper institutionMapper, AssetMapper assetMapper) {
        this.chargeRepository = chargeRepository;
        this.chargeMapper = chargeMapper;
        this.institutionMapper = institutionMapper;
        this.assetMapper = assetMapper;
    }

    /**
     * Save a charge.
     *
     * @param chargeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ChargeDTO save(ChargeDTO chargeDTO) {
        log.debug("Request to save Charge : {}", chargeDTO);
        Charge charge = chargeMapper.toEntity(chargeDTO);
        charge = chargeRepository.save(charge);
        return chargeMapper.toDto(charge);
    }

    /**
     *  Get all the charges.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ChargeDTO> findAll() {
        log.debug("Request to get all Charges");
        return chargeRepository.findAllWithEagerRelationships().stream()
            .map(chargeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one charge by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ChargeDTO findOne(Long id) {
        log.debug("Request to get Charge : {}", id);
        Charge charge = chargeRepository.findOneWithEagerRelationships(id);
        return chargeMapper.toDto(charge);
    }

    /**
     *  Delete the  charge by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Charge : {}", id);
        chargeRepository.delete(id);
    }

    /**
     *  Get the charges applied for a given Institution, Asset and OperationType
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ChargeDTO> findByInstitutionAndAssetAndOperationType(InstitutionDTO institution, AssetDTO asset,
                                                                     OperationType operationType) {
        log.debug("Request to get Charges by institution, asset and operationType: {}", institution, asset,
            operationType);
        List<Charge> charges = chargeRepository.findAllByInstitutionAndAssetsContainsAndOperationType(
            institutionMapper.toEntity(institution), assetMapper.toEntity(asset), operationType);
        return chargeMapper.toDto(charges);
    }

    /**
     *  Get the charges applied for an operation between two Institutions with its assets and operation types.
     *
     *  @return the list of entities
     */
    @Override
    public List<ChargeDTO> getChargesForOperation(InstitutionDTO institutionFrom, AssetDTO assetFrom,
                                                  OperationType operationTypeFrom, InstitutionDTO institutionTo,
                                                  AssetDTO assetTo, OperationType operationTypeTo, ChargeTarget target) {
        log.debug("Request to get charges for operation from/to (institution, asset, operationType) with target: {}",
            institutionFrom, assetFrom, operationTypeFrom, institutionTo, assetTo, operationTypeTo, target);

        final List<ChargeDTO> charges = Stream
            .concat(
                findByInstitutionAndAssetAndOperationType(institutionFrom, assetFrom, operationTypeFrom).stream(),
                findByInstitutionAndAssetAndOperationType(institutionTo, assetTo, operationTypeTo).stream())
            .filter(charge -> charge.getTarget().equals(target) || charge.getTarget().equals(BOTH))
            .collect(Collectors.toList());

        return charges;
    }

    @Override
    public Double getTotalChargeCost(InstitutionDTO institutionFrom, AssetDTO assetFrom,
                                     OperationType operationTypeFrom, InstitutionDTO institutionTo,
                                     AssetDTO assetTo, OperationType operationTypeTo, ChargeTarget target,
                                     Double amount) {
        log.debug("Request to get the total charge cost for operation from/to (institution, asset, operationType) " +
                "with target: {}",
            institutionFrom, assetFrom, operationTypeFrom, institutionTo, assetTo, operationTypeTo, target);

        return getChargesForOperation(institutionFrom, assetFrom, operationTypeFrom, institutionTo, assetTo,
            operationTypeTo, target)
            .stream()
            .mapToDouble(charge -> charge.calculateFee(amount))
            .sum();
    }
}

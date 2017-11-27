package com.oceanus.doris.service.impl;

import com.oceanus.doris.domain.Asset;
import com.oceanus.doris.domain.Institution;
import com.oceanus.doris.domain.enumeration.ChargeTarget;
import com.oceanus.doris.domain.enumeration.OperationType;
import com.oceanus.doris.service.ChargeService;
import com.oceanus.doris.domain.Charge;
import com.oceanus.doris.repository.ChargeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ChargeServiceImpl(ChargeRepository chargeRepository) {
        this.chargeRepository = chargeRepository;
    }

    /**
     * Save a charge.
     *
     * @param charge the entity to save
     * @return the persisted entity
     */
    @Override
    public Charge save(Charge charge) {
        log.debug("Request to save Charge : {}", charge);
        charge = chargeRepository.save(charge);
        return charge;
    }

    /**
     *  Get all the charges.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Charge> findAll() {
        log.debug("Request to get all Charges");
        return chargeRepository.findAllWithEagerRelationships().stream()
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
    public Charge findOne(Long id) {
        log.debug("Request to get Charge : {}", id);
        Charge charge = chargeRepository.findOneWithEagerRelationships(id);
        return charge;
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
    public List<Charge> findByInstitutionAndAssetAndOperationType(Institution institution, Asset asset,
                                                                     OperationType operationType) {
        log.debug("Request to get Charges by institution:{}, \nasset: {} and \noperationType: {}", institution, asset,
            operationType);
        return  chargeRepository.findAllByInstitutionAndAssetsContainsAndOperationType(institution, asset,
            operationType);
    }

    /**
     *  Get the charges applied for an operation between two Institutions with its assets and operation types.
     *
     *  @return the list of entities
     */
    @Override
    public List<Charge> getChargesForOperation(Institution institutionFrom, Asset assetFrom,
                                               OperationType operationTypeFrom, Institution institutionTo,
                                               Asset assetTo, OperationType operationTypeTo, ChargeTarget target) {
        log.debug("Request to get charges for operation \nfrom (institution {}, asset {}, operationType {})" +
                "\nto (institution {}, asset {}, operationType {}) with \ntarget: {}",
            institutionFrom, assetFrom, operationTypeFrom, institutionTo, assetTo, operationTypeTo, target);

        final List<Charge> charges = Stream
            .concat(
                findByInstitutionAndAssetAndOperationType(institutionFrom, assetFrom, operationTypeFrom).stream(),
                findByInstitutionAndAssetAndOperationType(institutionTo, assetTo, operationTypeTo).stream())
            .filter(charge -> charge.getTarget().equals(target) || charge.getTarget().equals(BOTH))
            .collect(Collectors.toList());

        return charges;

    }

    @Override
    public Double getTotalChargeCost(Institution institutionFrom, Asset assetFrom,
                                     OperationType operationTypeFrom, Institution institutionTo,
                                     Asset assetTo, OperationType operationTypeTo, ChargeTarget target,
                                     Double amount) {
        log.debug("Request to get charges for operation \nfrom (institution {}, asset {}, operationType {})" +
            "\nto (institution {}, asset {}, operationType {}) with \ntarget: {} and amount: {}",
            institutionFrom, assetFrom, operationTypeFrom, institutionTo, assetTo, operationTypeTo, target, amount);

        return getChargesForOperation(institutionFrom, assetFrom, operationTypeFrom, institutionTo, assetTo,
            operationTypeTo, target)
            .stream()
            .mapToDouble(charge -> charge.calculateFee(amount))
            .sum();
    }
}

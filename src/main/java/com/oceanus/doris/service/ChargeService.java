package com.oceanus.doris.service;

import com.oceanus.doris.domain.Asset;
import com.oceanus.doris.domain.Charge;
import com.oceanus.doris.domain.Institution;
import com.oceanus.doris.domain.enumeration.ChargeTarget;
import com.oceanus.doris.domain.enumeration.OperationType;

import java.util.List;

/**
 * Service Interface for managing Charge.
 */
public interface ChargeService {

    /**
     * Save a charge.
     *
     * @param charge the entity to save
     * @return the persisted entity
     */
    Charge save(Charge charge);

    /**
     *  Get all the charges.
     *
     *  @return the list of entities
     */
    List<Charge> findAll();

    /**
     *  Get the "id" charge.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Charge findOne(Long id);

    /**
     *  Delete the "id" charge.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *  Get the charges applied for an operation between two Institutions with its assets and operation types.
     *
     *  @return the list of entities
     */
    List<Charge> getChargesForOperation(Institution institutionFrom, Asset assetFrom,
                                        OperationType operationTypeFrom, Institution institutionTo,
                                        Asset assetTo, OperationType operationTypeTo, ChargeTarget target);

    /**
     *  Get the charges applied for a given Institution, Asset and OperationType
     *
     *  @return the list of entities
     */
    List<Charge> findByInstitutionAndAssetAndOperationType(Institution institution, Asset asset,
                                                              OperationType operationType);

    /**
     *  Get the total amount of charge costs applied for an operation between two Institutions with its assets and
     *  operation types.
     *
     *  @return the list of entities
     */
    Double getTotalChargeCost(Institution institutionFrom, Asset assetFrom,
                              OperationType operationTypeFrom, Institution institutionTo,
                              Asset assetTo, OperationType operationTypeTo, ChargeTarget target,
                              Double amount);
}

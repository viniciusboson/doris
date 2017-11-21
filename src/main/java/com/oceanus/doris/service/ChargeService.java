package com.oceanus.doris.service;

import com.oceanus.doris.domain.enumeration.ChargeTarget;
import com.oceanus.doris.domain.enumeration.OperationType;
import com.oceanus.doris.service.dto.AssetDTO;
import com.oceanus.doris.service.dto.ChargeDTO;
import com.oceanus.doris.service.dto.InstitutionDTO;

import java.util.List;

/**
 * Service Interface for managing Charge.
 */
public interface ChargeService {

    /**
     * Save a charge.
     *
     * @param chargeDTO the entity to save
     * @return the persisted entity
     */
    ChargeDTO save(ChargeDTO chargeDTO);

    /**
     *  Get all the charges.
     *
     *  @return the list of entities
     */
    List<ChargeDTO> findAll();

    /**
     *  Get the "id" charge.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ChargeDTO findOne(Long id);

    /**
     *  Delete the "id" charge.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *  Get the charges applied for a given Institution, Asset and OperationType
     *
     *  @return the list of entities
     */
    List<ChargeDTO> findByInstitutionAndAssetAndOperationType(InstitutionDTO institution, AssetDTO asset,
                                                              OperationType operationType);
    /**
     *  Get the charges applied for an operation between two Institutions with its assets and operation types.
     *
     *  @return the list of entities
     */
    List<ChargeDTO> getChargesForOperation(InstitutionDTO institutionFrom, AssetDTO assetFrom,
                                           OperationType operationTypeFrom, InstitutionDTO institutionTo,
                                           AssetDTO assetTo, OperationType operationTypeTo, ChargeTarget target);

    /**
     *  Get the total amout of charge costs applied for an operation between two Institutions with its assets and
     *  operation types.
     *
     *  @return the list of entities
     */
    Double getTotalChargeCost(InstitutionDTO institutionFrom, AssetDTO assetFrom,
                              OperationType operationTypeFrom, InstitutionDTO institutionTo,
                              AssetDTO assetTo, OperationType operationTypeTo, ChargeTarget target,
                              Double amount);
}

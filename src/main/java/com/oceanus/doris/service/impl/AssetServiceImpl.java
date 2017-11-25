package com.oceanus.doris.service.impl;

import com.oceanus.doris.service.AssetService;
import com.oceanus.doris.domain.Asset;
import com.oceanus.doris.repository.AssetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Asset.
 */
@Service
@Transactional
public class AssetServiceImpl implements AssetService{

    private final Logger log = LoggerFactory.getLogger(AssetServiceImpl.class);

    private final AssetRepository assetRepository;

    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    /**
     * Save a asset.
     *
     * @param asset the entity to save
     * @return the persisted entity
     */
    @Override
    public Asset save(Asset asset) {
        log.debug("Request to save Asset : {}", asset);
        asset = assetRepository.save(asset);
        return asset;
    }

    /**
     *  Get all the assets.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Asset> findAll() {
        log.debug("Request to get all Assets");
        return assetRepository.findAll().stream()
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one asset by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Asset findOne(Long id) {
        log.debug("Request to get Asset : {}", id);
        Asset asset = assetRepository.findOne(id);
        return asset;

    }

    /**
     *  Delete the  asset by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Asset : {}", id);
        assetRepository.delete(id);
    }
}

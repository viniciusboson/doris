package com.oceanus.doris.service.impl;

import com.oceanus.doris.service.AssetService;
import com.oceanus.doris.domain.Asset;
import com.oceanus.doris.repository.AssetRepository;
import com.oceanus.doris.service.dto.AssetDTO;
import com.oceanus.doris.service.mapper.AssetMapper;
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

    private final AssetMapper assetMapper;

    public AssetServiceImpl(AssetRepository assetRepository, AssetMapper assetMapper) {
        this.assetRepository = assetRepository;
        this.assetMapper = assetMapper;
    }

    /**
     * Save a asset.
     *
     * @param assetDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AssetDTO save(AssetDTO assetDTO) {
        log.debug("Request to save Asset : {}", assetDTO);
        Asset asset = assetMapper.toEntity(assetDTO);
        asset = assetRepository.save(asset);
        return assetMapper.toDto(asset);
    }

    /**
     *  Get all the assets.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AssetDTO> findAll() {
        log.debug("Request to get all Assets");
        return assetRepository.findAll().stream()
            .map(assetMapper::toDto)
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
    public AssetDTO findOne(Long id) {
        log.debug("Request to get Asset : {}", id);
        Asset asset = assetRepository.findOne(id);
        return assetMapper.toDto(asset);
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

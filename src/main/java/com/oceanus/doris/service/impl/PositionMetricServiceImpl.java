package com.oceanus.doris.service.impl;

import com.oceanus.doris.service.PositionMetricService;
import com.oceanus.doris.domain.PositionMetric;
import com.oceanus.doris.repository.PositionMetricRepository;
import com.oceanus.doris.service.dto.PositionMetricDTO;
import com.oceanus.doris.service.mapper.PositionMetricMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PositionMetric.
 */
@Service
@Transactional
public class PositionMetricServiceImpl implements PositionMetricService{

    private final Logger log = LoggerFactory.getLogger(PositionMetricServiceImpl.class);

    private final PositionMetricRepository positionMetricRepository;

    private final PositionMetricMapper positionMetricMapper;

    public PositionMetricServiceImpl(PositionMetricRepository positionMetricRepository, PositionMetricMapper positionMetricMapper) {
        this.positionMetricRepository = positionMetricRepository;
        this.positionMetricMapper = positionMetricMapper;
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
}

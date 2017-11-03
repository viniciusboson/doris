package com.oceanus.doris.service;

import com.oceanus.doris.service.dto.PositionMetricDTO;
import java.util.List;

/**
 * Service Interface for managing PositionMetric.
 */
public interface PositionMetricService {

    /**
     * Save a positionMetric.
     *
     * @param positionMetricDTO the entity to save
     * @return the persisted entity
     */
    PositionMetricDTO save(PositionMetricDTO positionMetricDTO);

    /**
     *  Get all the positionMetrics.
     *
     *  @return the list of entities
     */
    List<PositionMetricDTO> findAll();

    /**
     *  Get the "id" positionMetric.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PositionMetricDTO findOne(Long id);

    /**
     *  Delete the "id" positionMetric.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}

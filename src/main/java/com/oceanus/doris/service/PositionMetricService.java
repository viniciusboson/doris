package com.oceanus.doris.service;

import com.oceanus.doris.domain.Operation;
import com.oceanus.doris.domain.PositionMetric;

import java.util.List;

/**
 * Service Interface for managing PositionMetric.
 */
public interface PositionMetricService {

    /**
     * Save a positionMetric.
     *
     * @param positionMetric the entity to save
     * @return the persisted entity
     */
    PositionMetric save(PositionMetric positionMetric);

    /**
     *  Get all the positionMetrics.
     *
     *  @return the list of entities
     */
    List<PositionMetric> findAll();

    /**
     *  Get the "id" positionMetric.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PositionMetric findOne(Long id);

    /**
     *  Delete the "id" positionMetric.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Create a new positionMetric or update an existed one based on a given operation.
     *
     * @param operation containing the details to be metric
     */
    void createMetric(Operation operation);
}

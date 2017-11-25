package com.oceanus.doris.service;

import com.oceanus.doris.domain.Operation;

import java.util.List;

/**
 * Service Interface for managing Operation.
 */
public interface OperationService {

    /**
     * Create an operation and all its dependencies. See {@link com.oceanus.doris.domain.Position},
     * {@link com.oceanus.doris.domain.Transaction}, {@link com.oceanus.doris.domain.PositionMetric}
     *
     * @param operation the entity to save
     * @return the persisted entity
     */
    Operation create(Operation operation);

    /**
     * Save a operation.
     *
     * @param operation the entity to save
     * @return the persisted entity
     */
    Operation save(Operation operation);

    /**
     *  Get all the operations.
     *
     *  @return the list of entities
     */
    List<Operation> findAll();

    /**
     *  Get the "id" operation.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Operation findOne(Long id);

    /**
     *  Delete the "id" operation.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}

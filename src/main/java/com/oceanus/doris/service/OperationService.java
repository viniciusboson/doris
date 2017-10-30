package com.oceanus.doris.service;

import com.oceanus.doris.service.dto.OperationDTO;
import java.util.List;

/**
 * Service Interface for managing Operation.
 */
public interface OperationService {

    /**
     * Create an operation and all its dependencies. See {@link com.oceanus.doris.domain.Position},
     * {@link com.oceanus.doris.domain.Transaction}, {@link com.oceanus.doris.domain.PositionMetric}
     *
     * @param operationDTO the entity to save
     * @return the persisted entity
     */
    OperationDTO create(OperationDTO operationDTO);

    /**
     * Save a operation.
     *
     * @param operationDTO the entity to save
     * @return the persisted entity
     */
    OperationDTO save(OperationDTO operationDTO);

    /**
     *  Get all the operations.
     *
     *  @return the list of entities
     */
    List<OperationDTO> findAll();

    /**
     *  Get the "id" operation.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    OperationDTO findOne(Long id);

    /**
     *  Delete the "id" operation.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}

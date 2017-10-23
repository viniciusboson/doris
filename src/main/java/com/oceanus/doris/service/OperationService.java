package com.oceanus.doris.service;

import com.oceanus.doris.domain.Operation;
import com.oceanus.doris.repository.OperationRepository;
import com.oceanus.doris.service.dto.OperationDTO;
import com.oceanus.doris.service.mapper.OperationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Operation.
 */
@Service
@Transactional
public class OperationService {

    private final Logger log = LoggerFactory.getLogger(OperationService.class);

    private final OperationRepository operationRepository;

    private final OperationMapper operationMapper;

    public OperationService(OperationRepository operationRepository, OperationMapper operationMapper) {
        this.operationRepository = operationRepository;
        this.operationMapper = operationMapper;
    }

    /**
     * Save a operation.
     *
     * @param operationDTO the entity to save
     * @return the persisted entity
     */
    public OperationDTO save(OperationDTO operationDTO) {
        log.debug("Request to save Operation : {}", operationDTO);
        Operation operation = operationMapper.toEntity(operationDTO);
        operation = operationRepository.save(operation);
        return operationMapper.toDto(operation);
    }

    /**
     *  Get all the operations.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OperationDTO> findAll() {
        log.debug("Request to get all Operations");
        return operationRepository.findAll().stream()
            .map(operationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one operation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public OperationDTO findOne(Long id) {
        log.debug("Request to get Operation : {}", id);
        Operation operation = operationRepository.findOne(id);
        return operationMapper.toDto(operation);
    }

    /**
     *  Delete the  operation by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Operation : {}", id);
        operationRepository.delete(id);
    }
}

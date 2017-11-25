package com.oceanus.doris.service.impl;

import com.oceanus.doris.service.PositionService;
import com.oceanus.doris.domain.Position;
import com.oceanus.doris.repository.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Position.
 */
@Service
@Transactional
public class PositionServiceImpl implements PositionService{

    private final Logger log = LoggerFactory.getLogger(PositionServiceImpl.class);

    private final PositionRepository positionRepository;

    public PositionServiceImpl(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    /**
     * Save a position.
     *
     * @param position the entity to save
     * @return the persisted entity
     */
    @Override
    public Position save(Position position) {
        log.debug("Request to save Position : {}", position);
        position = positionRepository.save(position);
        return position;
    }

    /**
     *  Get all the positions.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Position> findAll() {
        log.debug("Request to get all Positions");
        return positionRepository.findAll().stream()
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one position by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Position findOne(Long id) {
        log.debug("Request to get Position : {}", id);
        Position position = positionRepository.findOne(id);
        return position;
    }

    /**
     *  Delete the  position by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Position : {}", id);
        positionRepository.delete(id);
    }
}

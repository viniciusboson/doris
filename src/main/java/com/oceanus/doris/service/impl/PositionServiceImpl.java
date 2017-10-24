package com.oceanus.doris.service.impl;

import com.oceanus.doris.service.PositionService;
import com.oceanus.doris.domain.Position;
import com.oceanus.doris.repository.PositionRepository;
import com.oceanus.doris.service.dto.PositionDTO;
import com.oceanus.doris.service.mapper.PositionMapper;
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

    private final PositionMapper positionMapper;

    public PositionServiceImpl(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    /**
     * Save a position.
     *
     * @param positionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PositionDTO save(PositionDTO positionDTO) {
        log.debug("Request to save Position : {}", positionDTO);
        Position position = positionMapper.toEntity(positionDTO);
        position = positionRepository.save(position);
        return positionMapper.toDto(position);
    }

    /**
     *  Get all the positions.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PositionDTO> findAll() {
        log.debug("Request to get all Positions");
        return positionRepository.findAll().stream()
            .map(positionMapper::toDto)
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
    public PositionDTO findOne(Long id) {
        log.debug("Request to get Position : {}", id);
        Position position = positionRepository.findOne(id);
        return positionMapper.toDto(position);
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

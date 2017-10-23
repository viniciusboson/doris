package com.oceanus.doris.service;

import com.oceanus.doris.domain.Institution;
import com.oceanus.doris.repository.InstitutionRepository;
import com.oceanus.doris.service.dto.InstitutionDTO;
import com.oceanus.doris.service.mapper.InstitutionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Institution.
 */
@Service
@Transactional
public class InstitutionService {

    private final Logger log = LoggerFactory.getLogger(InstitutionService.class);

    private final InstitutionRepository institutionRepository;

    private final InstitutionMapper institutionMapper;

    public InstitutionService(InstitutionRepository institutionRepository, InstitutionMapper institutionMapper) {
        this.institutionRepository = institutionRepository;
        this.institutionMapper = institutionMapper;
    }

    /**
     * Save a institution.
     *
     * @param institutionDTO the entity to save
     * @return the persisted entity
     */
    public InstitutionDTO save(InstitutionDTO institutionDTO) {
        log.debug("Request to save Institution : {}", institutionDTO);
        Institution institution = institutionMapper.toEntity(institutionDTO);
        institution = institutionRepository.save(institution);
        return institutionMapper.toDto(institution);
    }

    /**
     *  Get all the institutions.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<InstitutionDTO> findAll() {
        log.debug("Request to get all Institutions");
        return institutionRepository.findAll().stream()
            .map(institutionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one institution by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public InstitutionDTO findOne(Long id) {
        log.debug("Request to get Institution : {}", id);
        Institution institution = institutionRepository.findOne(id);
        return institutionMapper.toDto(institution);
    }

    /**
     *  Delete the  institution by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Institution : {}", id);
        institutionRepository.delete(id);
    }
}

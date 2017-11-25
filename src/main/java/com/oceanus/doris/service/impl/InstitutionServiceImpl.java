package com.oceanus.doris.service.impl;

import com.oceanus.doris.service.InstitutionService;
import com.oceanus.doris.domain.Institution;
import com.oceanus.doris.repository.InstitutionRepository;
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
public class InstitutionServiceImpl implements InstitutionService{

    private final Logger log = LoggerFactory.getLogger(InstitutionServiceImpl.class);

    private final InstitutionRepository institutionRepository;

    public InstitutionServiceImpl(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    /**
     * Save a institution.
     *
     * @param institution the entity to save
     * @return the persisted entity
     */
    @Override
    public Institution save(Institution institution) {
        log.debug("Request to save Institution : {}", institution);
        institution = institutionRepository.save(institution);
        return institution;
    }

    /**
     *  Get all the institutions.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Institution> findAll() {
        log.debug("Request to get all Institutions");
        return institutionRepository.findAll().stream()
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one institution by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Institution findOne(Long id) {
        log.debug("Request to get Institution : {}", id);
        Institution institution = institutionRepository.findOne(id);
        return institution;
    }

    /**
     *  Delete the  institution by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Institution : {}", id);
        institutionRepository.delete(id);
    }
}

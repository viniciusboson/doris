package com.oceanus.doris.service.impl;

import com.oceanus.doris.service.ChargeService;
import com.oceanus.doris.domain.Charge;
import com.oceanus.doris.repository.ChargeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Charge.
 */
@Service
@Transactional
public class ChargeServiceImpl implements ChargeService{

    private final Logger log = LoggerFactory.getLogger(ChargeServiceImpl.class);

    private final ChargeRepository chargeRepository;

    public ChargeServiceImpl(ChargeRepository chargeRepository) {
        this.chargeRepository = chargeRepository;
    }

    /**
     * Save a charge.
     *
     * @param charge the entity to save
     * @return the persisted entity
     */
    @Override
    public Charge save(Charge charge) {
        log.debug("Request to save Charge : {}", charge);
        charge = chargeRepository.save(charge);
        return charge;
    }

    /**
     *  Get all the charges.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Charge> findAll() {
        log.debug("Request to get all Charges");
        return chargeRepository.findAllWithEagerRelationships().stream()
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one charge by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Charge findOne(Long id) {
        log.debug("Request to get Charge : {}", id);
        Charge charge = chargeRepository.findOneWithEagerRelationships(id);
        return charge;
    }

    /**
     *  Delete the  charge by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Charge : {}", id);
        chargeRepository.delete(id);
    }
}

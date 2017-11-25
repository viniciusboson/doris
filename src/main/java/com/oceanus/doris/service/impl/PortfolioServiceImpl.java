package com.oceanus.doris.service.impl;

import com.oceanus.doris.service.PortfolioService;
import com.oceanus.doris.domain.Portfolio;
import com.oceanus.doris.repository.PortfolioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Portfolio.
 */
@Service
@Transactional
public class PortfolioServiceImpl implements PortfolioService{

    private final Logger log = LoggerFactory.getLogger(PortfolioServiceImpl.class);

    private final PortfolioRepository portfolioRepository;

    public PortfolioServiceImpl(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    /**
     * Save a portfolio.
     *
     * @param portfolio the entity to save
     * @return the persisted entity
     */
    @Override
    public Portfolio save(Portfolio portfolio) {
        log.debug("Request to save Portfolio : {}", portfolio);
        portfolio = portfolioRepository.save(portfolio);
        return portfolio;
    }

    /**
     *  Get all the portfolios.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Portfolio> findAll() {
        log.debug("Request to get all Portfolios");
        return portfolioRepository.findAll().stream()
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one portfolio by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Portfolio findOne(Long id) {
        log.debug("Request to get Portfolio : {}", id);
        Portfolio portfolio = portfolioRepository.findOne(id);
        return portfolio;
    }

    /**
     *  Delete the  portfolio by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Portfolio : {}", id);
        portfolioRepository.delete(id);
    }
}

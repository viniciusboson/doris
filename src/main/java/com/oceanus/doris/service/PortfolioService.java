package com.oceanus.doris.service;

import com.oceanus.doris.domain.Portfolio;

import java.util.List;

/**
 * Service Interface for managing Portfolio.
 */
public interface PortfolioService {

    /**
     * Save a portfolio.
     *
     * @param portfolio the entity to save
     * @return the persisted entity
     */
    Portfolio save(Portfolio portfolio);

    /**
     *  Get all the portfolios.
     *
     *  @return the list of entities
     */
    List<Portfolio> findAll();

    /**
     *  Get the "id" portfolio.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Portfolio findOne(Long id);

    /**
     *  Delete the "id" portfolio.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}

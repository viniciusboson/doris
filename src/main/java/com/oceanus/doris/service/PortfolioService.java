package com.oceanus.doris.service;

import com.oceanus.doris.service.dto.PortfolioDTO;
import java.util.List;

/**
 * Service Interface for managing Portfolio.
 */
public interface PortfolioService {

    /**
     * Save a portfolio.
     *
     * @param portfolioDTO the entity to save
     * @return the persisted entity
     */
    PortfolioDTO save(PortfolioDTO portfolioDTO);

    /**
     *  Get all the portfolios.
     *
     *  @return the list of entities
     */
    List<PortfolioDTO> findAll();

    /**
     *  Get the "id" portfolio.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PortfolioDTO findOne(Long id);

    /**
     *  Delete the "id" portfolio.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}

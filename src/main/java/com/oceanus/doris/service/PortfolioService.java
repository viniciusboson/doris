package com.oceanus.doris.service;

import com.oceanus.doris.domain.Portfolio;
import com.oceanus.doris.repository.PortfolioRepository;
import com.oceanus.doris.service.dto.PortfolioDTO;
import com.oceanus.doris.service.mapper.PortfolioMapper;
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
public class PortfolioService {

    private final Logger log = LoggerFactory.getLogger(PortfolioService.class);

    private final PortfolioRepository portfolioRepository;

    private final PortfolioMapper portfolioMapper;

    public PortfolioService(PortfolioRepository portfolioRepository, PortfolioMapper portfolioMapper) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioMapper = portfolioMapper;
    }

    /**
     * Save a portfolio.
     *
     * @param portfolioDTO the entity to save
     * @return the persisted entity
     */
    public PortfolioDTO save(PortfolioDTO portfolioDTO) {
        log.debug("Request to save Portfolio : {}", portfolioDTO);
        Portfolio portfolio = portfolioMapper.toEntity(portfolioDTO);
        portfolio = portfolioRepository.save(portfolio);
        return portfolioMapper.toDto(portfolio);
    }

    /**
     *  Get all the portfolios.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<PortfolioDTO> findAll() {
        log.debug("Request to get all Portfolios");
        return portfolioRepository.findAllWithEagerRelationships().stream()
            .map(portfolioMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one portfolio by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PortfolioDTO findOne(Long id) {
        log.debug("Request to get Portfolio : {}", id);
        Portfolio portfolio = portfolioRepository.findOneWithEagerRelationships(id);
        return portfolioMapper.toDto(portfolio);
    }

    /**
     *  Delete the  portfolio by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Portfolio : {}", id);
        portfolioRepository.delete(id);
    }
}

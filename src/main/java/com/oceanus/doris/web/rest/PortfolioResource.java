package com.oceanus.doris.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.oceanus.doris.service.PortfolioService;
import com.oceanus.doris.web.rest.errors.BadRequestAlertException;
import com.oceanus.doris.web.rest.util.HeaderUtil;
import com.oceanus.doris.service.dto.PortfolioDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Portfolio.
 */
@RestController
@RequestMapping("/api")
public class PortfolioResource {

    private final Logger log = LoggerFactory.getLogger(PortfolioResource.class);

    private static final String ENTITY_NAME = "portfolio";

    private final PortfolioService portfolioService;

    public PortfolioResource(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    /**
     * POST  /portfolios : Create a new portfolio.
     *
     * @param portfolioDTO the portfolioDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new portfolioDTO, or with status 400 (Bad Request) if the portfolio has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/portfolios")
    @Timed
    public ResponseEntity<PortfolioDTO> createPortfolio(@Valid @RequestBody PortfolioDTO portfolioDTO) throws URISyntaxException {
        log.debug("REST request to save Portfolio : {}", portfolioDTO);
        if (portfolioDTO.getId() != null) {
            throw new BadRequestAlertException("A new portfolio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PortfolioDTO result = portfolioService.save(portfolioDTO);
        return ResponseEntity.created(new URI("/api/portfolios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /portfolios : Updates an existing portfolio.
     *
     * @param portfolioDTO the portfolioDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated portfolioDTO,
     * or with status 400 (Bad Request) if the portfolioDTO is not valid,
     * or with status 500 (Internal Server Error) if the portfolioDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/portfolios")
    @Timed
    public ResponseEntity<PortfolioDTO> updatePortfolio(@Valid @RequestBody PortfolioDTO portfolioDTO) throws URISyntaxException {
        log.debug("REST request to update Portfolio : {}", portfolioDTO);
        if (portfolioDTO.getId() == null) {
            return createPortfolio(portfolioDTO);
        }
        PortfolioDTO result = portfolioService.save(portfolioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, portfolioDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /portfolios : get all the portfolios.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of portfolios in body
     */
    @GetMapping("/portfolios")
    @Timed
    public List<PortfolioDTO> getAllPortfolios() {
        log.debug("REST request to get all Portfolios");
        return portfolioService.findAll();
        }

    /**
     * GET  /portfolios/:id : get the "id" portfolio.
     *
     * @param id the id of the portfolioDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the portfolioDTO, or with status 404 (Not Found)
     */
    @GetMapping("/portfolios/{id}")
    @Timed
    public ResponseEntity<PortfolioDTO> getPortfolio(@PathVariable Long id) {
        log.debug("REST request to get Portfolio : {}", id);
        PortfolioDTO portfolioDTO = portfolioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(portfolioDTO));
    }

    /**
     * DELETE  /portfolios/:id : delete the "id" portfolio.
     *
     * @param id the id of the portfolioDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/portfolios/{id}")
    @Timed
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long id) {
        log.debug("REST request to delete Portfolio : {}", id);
        portfolioService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

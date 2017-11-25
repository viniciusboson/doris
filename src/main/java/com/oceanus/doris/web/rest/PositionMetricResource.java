package com.oceanus.doris.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.oceanus.doris.domain.PositionMetric;
import com.oceanus.doris.service.PositionMetricService;
import com.oceanus.doris.service.mapper.PositionMetricMapper;
import com.oceanus.doris.web.rest.errors.BadRequestAlertException;
import com.oceanus.doris.web.rest.util.HeaderUtil;
import com.oceanus.doris.web.rest.dto.PositionMetricDTO;
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
 * REST controller for managing PositionMetric.
 */
@RestController
@RequestMapping("/api")
public class PositionMetricResource {

    private final Logger log = LoggerFactory.getLogger(PositionMetricResource.class);

    private static final String ENTITY_NAME = "positionMetric";

    private final PositionMetricService positionMetricService;

    private final PositionMetricMapper positionMetricMapper;

    public PositionMetricResource(PositionMetricService positionMetricService, PositionMetricMapper positionMetricMapper) {
        this.positionMetricService = positionMetricService;
        this.positionMetricMapper = positionMetricMapper;
    }

    /**
     * POST  /position-metrics : Create a new positionMetric.
     *
     * @param positionMetric the positionMetricDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new positionMetricDTO, or with status 400 (Bad Request) if the positionMetric has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/position-metrics")
    @Timed
    public ResponseEntity<PositionMetricDTO> createPositionMetric(@Valid @RequestBody PositionMetricDTO positionMetric) throws URISyntaxException {
        log.debug("REST request to save PositionMetric : {}", positionMetric);
        if (positionMetric.getId() != null) {
            throw new BadRequestAlertException("A new positionMetric cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PositionMetric result = positionMetricService.save(positionMetricMapper.toEntity(positionMetric));
        return ResponseEntity.created(new URI("/api/position-metrics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(positionMetricMapper.toDto(result));
    }

    /**
     * PUT  /position-metrics : Updates an existing positionMetric.
     *
     * @param positionMetricDTO the positionMetricDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated positionMetricDTO,
     * or with status 400 (Bad Request) if the positionMetricDTO is not valid,
     * or with status 500 (Internal Server Error) if the positionMetricDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/position-metrics")
    @Timed
    public ResponseEntity<PositionMetricDTO> updatePositionMetric(@Valid @RequestBody PositionMetricDTO positionMetricDTO) throws URISyntaxException {
        log.debug("REST request to update PositionMetric : {}", positionMetricDTO);
        if (positionMetricDTO.getId() == null) {
            return createPositionMetric(positionMetricDTO);
        }
        PositionMetric result = positionMetricService.save(positionMetricMapper.toEntity(positionMetricDTO));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, positionMetricDTO.getId().toString()))
            .body(positionMetricMapper.toDto(result));
    }

    /**
     * GET  /position-metrics : get all the positionMetrics.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of positionMetrics in body
     */
    @GetMapping("/position-metrics")
    @Timed
    public List<PositionMetricDTO> getAllPositionMetrics() {
        log.debug("REST request to get all PositionMetrics");
        return positionMetricMapper.toDto(positionMetricService.findAll());
        }

    /**
     * GET  /position-metrics/:id : get the "id" positionMetric.
     *
     * @param id the id of the positionMetricDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the positionMetricDTO, or with status 404 (Not Found)
     */
    @GetMapping("/position-metrics/{id}")
    @Timed
    public ResponseEntity<PositionMetricDTO> getPositionMetric(@PathVariable Long id) {
        log.debug("REST request to get PositionMetric : {}", id);
        PositionMetric positionMetric = positionMetricService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(positionMetricMapper.toDto(positionMetric)));
    }

    /**
     * DELETE  /position-metrics/:id : delete the "id" positionMetric.
     *
     * @param id the id of the positionMetricDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/position-metrics/{id}")
    @Timed
    public ResponseEntity<Void> deletePositionMetric(@PathVariable Long id) {
        log.debug("REST request to delete PositionMetric : {}", id);
        positionMetricService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

package com.oceanus.doris.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.oceanus.doris.service.ChargeService;
import com.oceanus.doris.web.rest.errors.BadRequestAlertException;
import com.oceanus.doris.web.rest.util.HeaderUtil;
import com.oceanus.doris.service.dto.ChargeDTO;
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
 * REST controller for managing Charge.
 */
@RestController
@RequestMapping("/api")
public class ChargeResource {

    private final Logger log = LoggerFactory.getLogger(ChargeResource.class);

    private static final String ENTITY_NAME = "charge";

    private final ChargeService chargeService;

    public ChargeResource(ChargeService chargeService) {
        this.chargeService = chargeService;
    }

    /**
     * POST  /charges : Create a new charge.
     *
     * @param chargeDTO the chargeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chargeDTO, or with status 400 (Bad Request) if the charge has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/charges")
    @Timed
    public ResponseEntity<ChargeDTO> createCharge(@Valid @RequestBody ChargeDTO chargeDTO) throws URISyntaxException {
        log.debug("REST request to save Charge : {}", chargeDTO);
        if (chargeDTO.getId() != null) {
            throw new BadRequestAlertException("A new charge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChargeDTO result = chargeService.save(chargeDTO);
        return ResponseEntity.created(new URI("/api/charges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /charges : Updates an existing charge.
     *
     * @param chargeDTO the chargeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chargeDTO,
     * or with status 400 (Bad Request) if the chargeDTO is not valid,
     * or with status 500 (Internal Server Error) if the chargeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/charges")
    @Timed
    public ResponseEntity<ChargeDTO> updateCharge(@Valid @RequestBody ChargeDTO chargeDTO) throws URISyntaxException {
        log.debug("REST request to update Charge : {}", chargeDTO);
        if (chargeDTO.getId() == null) {
            return createCharge(chargeDTO);
        }
        ChargeDTO result = chargeService.save(chargeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, chargeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /charges : get all the charges.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of charges in body
     */
    @GetMapping("/charges")
    @Timed
    public List<ChargeDTO> getAllCharges() {
        log.debug("REST request to get all Charges");
        return chargeService.findAll();
        }

    /**
     * GET  /charges/:id : get the "id" charge.
     *
     * @param id the id of the chargeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chargeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/charges/{id}")
    @Timed
    public ResponseEntity<ChargeDTO> getCharge(@PathVariable Long id) {
        log.debug("REST request to get Charge : {}", id);
        ChargeDTO chargeDTO = chargeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(chargeDTO));
    }

    /**
     * DELETE  /charges/:id : delete the "id" charge.
     *
     * @param id the id of the chargeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/charges/{id}")
    @Timed
    public ResponseEntity<Void> deleteCharge(@PathVariable Long id) {
        log.debug("REST request to delete Charge : {}", id);
        chargeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

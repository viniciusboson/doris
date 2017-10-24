package com.oceanus.doris.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.oceanus.doris.service.TransactionService;
import com.oceanus.doris.web.rest.errors.BadRequestAlertException;
import com.oceanus.doris.web.rest.util.HeaderUtil;
import com.oceanus.doris.service.dto.TransactionDTO;
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
 * REST controller for managing Transaction.
 */
@RestController
@RequestMapping("/api")
public class TransactionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    private static final String ENTITY_NAME = "transaction";

    private final TransactionService transactionService;

    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * POST  /transactions : Create a new transaction.
     *
     * @param transactionDTO the transactionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transactionDTO, or with status 400 (Bad Request) if the transaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transactions")
    @Timed
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) throws URISyntaxException {
        log.debug("REST request to save Transaction : {}", transactionDTO);
        if (transactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new transaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionDTO result = transactionService.save(transactionDTO);
        return ResponseEntity.created(new URI("/api/transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transactions : Updates an existing transaction.
     *
     * @param transactionDTO the transactionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transactionDTO,
     * or with status 400 (Bad Request) if the transactionDTO is not valid,
     * or with status 500 (Internal Server Error) if the transactionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transactions")
    @Timed
    public ResponseEntity<TransactionDTO> updateTransaction(@Valid @RequestBody TransactionDTO transactionDTO) throws URISyntaxException {
        log.debug("REST request to update Transaction : {}", transactionDTO);
        if (transactionDTO.getId() == null) {
            return createTransaction(transactionDTO);
        }
        TransactionDTO result = transactionService.save(transactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transactions : get all the transactions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of transactions in body
     */
    @GetMapping("/transactions")
    @Timed
    public List<TransactionDTO> getAllTransactions() {
        log.debug("REST request to get all Transactions");
        return transactionService.findAll();
        }

    /**
     * GET  /transactions/:id : get the "id" transaction.
     *
     * @param id the id of the transactionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transactionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/transactions/{id}")
    @Timed
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Long id) {
        log.debug("REST request to get Transaction : {}", id);
        TransactionDTO transactionDTO = transactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transactionDTO));
    }

    /**
     * DELETE  /transactions/:id : delete the "id" transaction.
     *
     * @param id the id of the transactionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        log.debug("REST request to delete Transaction : {}", id);
        transactionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

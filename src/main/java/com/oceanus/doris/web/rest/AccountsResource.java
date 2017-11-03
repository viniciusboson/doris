package com.oceanus.doris.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.oceanus.doris.service.AccountsService;
import com.oceanus.doris.web.rest.errors.BadRequestAlertException;
import com.oceanus.doris.web.rest.util.HeaderUtil;
import com.oceanus.doris.service.dto.AccountsDTO;
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
 * REST controller for managing Accounts.
 */
@RestController
@RequestMapping("/api")
public class AccountsResource {

    private final Logger log = LoggerFactory.getLogger(AccountsResource.class);

    private static final String ENTITY_NAME = "accounts";

    private final AccountsService accountsService;

    public AccountsResource(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    /**
     * POST  /accounts : Create a new accounts.
     *
     * @param accountsDTO the accountsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountsDTO, or with status 400 (Bad Request) if the accounts has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/accounts")
    @Timed
    public ResponseEntity<AccountsDTO> createAccounts(@Valid @RequestBody AccountsDTO accountsDTO) throws URISyntaxException {
        log.debug("REST request to save Accounts : {}", accountsDTO);
        if (accountsDTO.getId() != null) {
            throw new BadRequestAlertException("A new accounts cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountsDTO result = accountsService.save(accountsDTO);
        return ResponseEntity.created(new URI("/api/accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /accounts : Updates an existing accounts.
     *
     * @param accountsDTO the accountsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountsDTO,
     * or with status 400 (Bad Request) if the accountsDTO is not valid,
     * or with status 500 (Internal Server Error) if the accountsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/accounts")
    @Timed
    public ResponseEntity<AccountsDTO> updateAccounts(@Valid @RequestBody AccountsDTO accountsDTO) throws URISyntaxException {
        log.debug("REST request to update Accounts : {}", accountsDTO);
        if (accountsDTO.getId() == null) {
            return createAccounts(accountsDTO);
        }
        AccountsDTO result = accountsService.save(accountsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /accounts : get all the accounts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accounts in body
     */
    @GetMapping("/accounts")
    @Timed
    public List<AccountsDTO> getAllAccounts() {
        log.debug("REST request to get all Accounts");
        return accountsService.findAll();
        }

    /**
     * GET  /accounts/:id : get the "id" accounts.
     *
     * @param id the id of the accountsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/accounts/{id}")
    @Timed
    public ResponseEntity<AccountsDTO> getAccounts(@PathVariable Long id) {
        log.debug("REST request to get Accounts : {}", id);
        AccountsDTO accountsDTO = accountsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accountsDTO));
    }

    /**
     * DELETE  /accounts/:id : delete the "id" accounts.
     *
     * @param id the id of the accountsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccounts(@PathVariable Long id) {
        log.debug("REST request to delete Accounts : {}", id);
        accountsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

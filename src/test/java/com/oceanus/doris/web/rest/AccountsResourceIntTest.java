package com.oceanus.doris.web.rest;

import com.oceanus.doris.DorisApp;

import com.oceanus.doris.domain.Accounts;
import com.oceanus.doris.domain.Portfolio;
import com.oceanus.doris.domain.Asset;
import com.oceanus.doris.domain.Institution;
import com.oceanus.doris.repository.AccountsRepository;
import com.oceanus.doris.repository.util.EntityCreation;
import com.oceanus.doris.service.AccountsService;
import com.oceanus.doris.service.dto.AccountsDTO;
import com.oceanus.doris.service.mapper.AccountsMapper;
import com.oceanus.doris.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.oceanus.doris.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AccountsResource REST controller.
 *
 * @see AccountsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DorisApp.class)
public class AccountsResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = EntityCreation.Accounts.DEFAULT_DESCRIPTION;
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private AccountsMapper accountsMapper;

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccountsMockMvc;

    private Accounts accounts;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AccountsResource accountsResource = new AccountsResource(accountsService);
        this.restAccountsMockMvc = MockMvcBuilders.standaloneSetup(accountsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        accounts = EntityCreation.Accounts.createEntity(em);
    }

    @Test
    @Transactional
    public void createAccounts() throws Exception {
        int databaseSizeBeforeCreate = accountsRepository.findAll().size();

        // Create the Accounts
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);
        restAccountsMockMvc.perform(post("/api/accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountsDTO)))
            .andExpect(status().isCreated());

        // Validate the Accounts in the database
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeCreate + 1);
        Accounts testAccounts = accountsList.get(accountsList.size() - 1);
        assertThat(testAccounts.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createAccountsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountsRepository.findAll().size();

        // Create the Accounts with an existing ID
        accounts.setId(1L);
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountsMockMvc.perform(post("/api/accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Accounts in the database
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountsRepository.findAll().size();
        // set the field null
        accounts.setDescription(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        restAccountsMockMvc.perform(post("/api/accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountsDTO)))
            .andExpect(status().isBadRequest());

        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAccounts() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

        // Get all the accountsList
        restAccountsMockMvc.perform(get("/api/accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accounts.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getAccounts() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

        // Get the accounts
        restAccountsMockMvc.perform(get("/api/accounts/{id}", accounts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accounts.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAccounts() throws Exception {
        // Get the accounts
        restAccountsMockMvc.perform(get("/api/accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccounts() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);
        int databaseSizeBeforeUpdate = accountsRepository.findAll().size();

        // Update the accounts
        Accounts updatedAccounts = accountsRepository.findOne(accounts.getId());
        updatedAccounts
            .description(UPDATED_DESCRIPTION);
        AccountsDTO accountsDTO = accountsMapper.toDto(updatedAccounts);

        restAccountsMockMvc.perform(put("/api/accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountsDTO)))
            .andExpect(status().isOk());

        // Validate the Accounts in the database
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeUpdate);
        Accounts testAccounts = accountsList.get(accountsList.size() - 1);
        assertThat(testAccounts.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingAccounts() throws Exception {
        int databaseSizeBeforeUpdate = accountsRepository.findAll().size();

        // Create the Accounts
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccountsMockMvc.perform(put("/api/accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountsDTO)))
            .andExpect(status().isCreated());

        // Validate the Accounts in the database
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccounts() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);
        int databaseSizeBeforeDelete = accountsRepository.findAll().size();

        // Get the accounts
        restAccountsMockMvc.perform(delete("/api/accounts/{id}", accounts.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Accounts> accountsList = accountsRepository.findAll();
        assertThat(accountsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Accounts.class);
        Accounts accounts1 = new Accounts();
        accounts1.setId(1L);
        Accounts accounts2 = new Accounts();
        accounts2.setId(accounts1.getId());
        assertThat(accounts1).isEqualTo(accounts2);
        accounts2.setId(2L);
        assertThat(accounts1).isNotEqualTo(accounts2);
        accounts1.setId(null);
        assertThat(accounts1).isNotEqualTo(accounts2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountsDTO.class);
        AccountsDTO accountsDTO1 = new AccountsDTO();
        accountsDTO1.setId(1L);
        AccountsDTO accountsDTO2 = new AccountsDTO();
        assertThat(accountsDTO1).isNotEqualTo(accountsDTO2);
        accountsDTO2.setId(accountsDTO1.getId());
        assertThat(accountsDTO1).isEqualTo(accountsDTO2);
        accountsDTO2.setId(2L);
        assertThat(accountsDTO1).isNotEqualTo(accountsDTO2);
        accountsDTO1.setId(null);
        assertThat(accountsDTO1).isNotEqualTo(accountsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(accountsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(accountsMapper.fromId(null)).isNull();
    }
}

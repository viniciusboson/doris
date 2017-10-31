package com.oceanus.doris.web.rest;

import com.oceanus.doris.DorisApp;

import com.oceanus.doris.domain.Portfolio;
import com.oceanus.doris.repository.PortfolioRepository;
import com.oceanus.doris.repository.util.EntityCreation;
import com.oceanus.doris.service.PortfolioService;
import com.oceanus.doris.service.dto.PortfolioDTO;
import com.oceanus.doris.service.mapper.PortfolioMapper;
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
 * Test class for the PortfolioResource REST controller.
 *
 * @see PortfolioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DorisApp.class)
public class PortfolioResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = EntityCreation.Portfolio.DEFAULT_DESCRIPTION;
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioMapper portfolioMapper;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPortfolioMockMvc;

    private Portfolio portfolio;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PortfolioResource portfolioResource = new PortfolioResource(portfolioService);
        this.restPortfolioMockMvc = MockMvcBuilders.standaloneSetup(portfolioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        portfolio = EntityCreation.Portfolio.createEntity(em);
    }

    @Test
    @Transactional
    public void createPortfolio() throws Exception {
        int databaseSizeBeforeCreate = portfolioRepository.findAll().size();

        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);
        restPortfolioMockMvc.perform(post("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolioDTO)))
            .andExpect(status().isCreated());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeCreate + 1);
        Portfolio testPortfolio = portfolioList.get(portfolioList.size() - 1);
        assertThat(testPortfolio.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createPortfolioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = portfolioRepository.findAll().size();

        // Create the Portfolio with an existing ID
        portfolio.setId(1L);
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPortfolioMockMvc.perform(post("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = portfolioRepository.findAll().size();
        // set the field null
        portfolio.setDescription(null);

        // Create the Portfolio, which fails.
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        restPortfolioMockMvc.perform(post("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolioDTO)))
            .andExpect(status().isBadRequest());

        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPortfolios() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        // Get all the portfolioList
        restPortfolioMockMvc.perform(get("/api/portfolios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(portfolio.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getPortfolio() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        // Get the portfolio
        restPortfolioMockMvc.perform(get("/api/portfolios/{id}", portfolio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(portfolio.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPortfolio() throws Exception {
        // Get the portfolio
        restPortfolioMockMvc.perform(get("/api/portfolios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePortfolio() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);
        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();

        // Update the portfolio
        Portfolio updatedPortfolio = portfolioRepository.findOne(portfolio.getId());
        updatedPortfolio
            .description(UPDATED_DESCRIPTION);
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(updatedPortfolio);

        restPortfolioMockMvc.perform(put("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolioDTO)))
            .andExpect(status().isOk());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate);
        Portfolio testPortfolio = portfolioList.get(portfolioList.size() - 1);
        assertThat(testPortfolio.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();

        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPortfolioMockMvc.perform(put("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolioDTO)))
            .andExpect(status().isCreated());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePortfolio() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);
        int databaseSizeBeforeDelete = portfolioRepository.findAll().size();

        // Get the portfolio
        restPortfolioMockMvc.perform(delete("/api/portfolios/{id}", portfolio.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Portfolio.class);
        Portfolio portfolio1 = new Portfolio();
        portfolio1.setId(1L);
        Portfolio portfolio2 = new Portfolio();
        portfolio2.setId(portfolio1.getId());
        assertThat(portfolio1).isEqualTo(portfolio2);
        portfolio2.setId(2L);
        assertThat(portfolio1).isNotEqualTo(portfolio2);
        portfolio1.setId(null);
        assertThat(portfolio1).isNotEqualTo(portfolio2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PortfolioDTO.class);
        PortfolioDTO portfolioDTO1 = new PortfolioDTO();
        portfolioDTO1.setId(1L);
        PortfolioDTO portfolioDTO2 = new PortfolioDTO();
        assertThat(portfolioDTO1).isNotEqualTo(portfolioDTO2);
        portfolioDTO2.setId(portfolioDTO1.getId());
        assertThat(portfolioDTO1).isEqualTo(portfolioDTO2);
        portfolioDTO2.setId(2L);
        assertThat(portfolioDTO1).isNotEqualTo(portfolioDTO2);
        portfolioDTO1.setId(null);
        assertThat(portfolioDTO1).isNotEqualTo(portfolioDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(portfolioMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(portfolioMapper.fromId(null)).isNull();
    }
}

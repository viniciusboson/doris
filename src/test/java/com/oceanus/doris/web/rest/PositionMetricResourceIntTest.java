package com.oceanus.doris.web.rest;

import com.oceanus.doris.DorisApp;

import com.oceanus.doris.domain.PositionMetric;
import com.oceanus.doris.repository.PositionMetricRepository;
import com.oceanus.doris.service.PositionMetricService;
import com.oceanus.doris.service.dto.PositionMetricDTO;
import com.oceanus.doris.service.mapper.PositionMetricMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.oceanus.doris.web.rest.TestUtil.sameInstant;
import static com.oceanus.doris.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PositionMetricResource REST controller.
 *
 * @see PositionMetricResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DorisApp.class)
public class PositionMetricResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final Double DEFAULT_ENTRY_AVG_PRICE = 1D;
    private static final Double UPDATED_ENTRY_AVG_PRICE = 2D;

    private static final Double DEFAULT_ENTRY_AMOUNT = 1D;
    private static final Double UPDATED_ENTRY_AMOUNT = 2D;

    private static final Double DEFAULT_EXIT_AVG_PRICE = 1D;
    private static final Double UPDATED_EXIT_AVG_PRICE = 2D;

    private static final Double DEFAULT_EXIT_AMOUNT = 1D;
    private static final Double UPDATED_EXIT_AMOUNT = 2D;

    private static final Double DEFAULT_TX_COSTS = 1D;
    private static final Double UPDATED_TX_COSTS = 2D;

    @Autowired
    private PositionMetricRepository positionMetricRepository;

    @Autowired
    private PositionMetricMapper positionMetricMapper;

    @Autowired
    private PositionMetricService positionMetricService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPositionMetricMockMvc;

    private PositionMetric positionMetric;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PositionMetricResource positionMetricResource = new PositionMetricResource(positionMetricService);
        this.restPositionMetricMockMvc = MockMvcBuilders.standaloneSetup(positionMetricResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PositionMetric createEntity(EntityManager em) {
        PositionMetric positionMetric = new PositionMetric()
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .entryAvgPrice(DEFAULT_ENTRY_AVG_PRICE)
            .entryAmount(DEFAULT_ENTRY_AMOUNT)
            .exitAvgPrice(DEFAULT_EXIT_AVG_PRICE)
            .exitAmount(DEFAULT_EXIT_AMOUNT)
            .txCosts(DEFAULT_TX_COSTS);
        return positionMetric;
    }

    @Before
    public void initTest() {
        positionMetric = createEntity(em);
    }

    @Test
    @Transactional
    public void createPositionMetric() throws Exception {
        int databaseSizeBeforeCreate = positionMetricRepository.findAll().size();

        // Create the PositionMetric
        PositionMetricDTO positionMetricDTO = positionMetricMapper.toDto(positionMetric);
        restPositionMetricMockMvc.perform(post("/api/position-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMetricDTO)))
            .andExpect(status().isCreated());

        // Validate the PositionMetric in the database
        List<PositionMetric> positionMetricList = positionMetricRepository.findAll();
        assertThat(positionMetricList).hasSize(databaseSizeBeforeCreate + 1);
        PositionMetric testPositionMetric = positionMetricList.get(positionMetricList.size() - 1);
        assertThat(testPositionMetric.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPositionMetric.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testPositionMetric.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testPositionMetric.getEntryAvgPrice()).isEqualTo(DEFAULT_ENTRY_AVG_PRICE);
        assertThat(testPositionMetric.getEntryAmount()).isEqualTo(DEFAULT_ENTRY_AMOUNT);
        assertThat(testPositionMetric.getExitAvgPrice()).isEqualTo(DEFAULT_EXIT_AVG_PRICE);
        assertThat(testPositionMetric.getExitAmount()).isEqualTo(DEFAULT_EXIT_AMOUNT);
        assertThat(testPositionMetric.getTxCosts()).isEqualTo(DEFAULT_TX_COSTS);
    }

    @Test
    @Transactional
    public void createPositionMetricWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = positionMetricRepository.findAll().size();

        // Create the PositionMetric with an existing ID
        positionMetric.setId(1L);
        PositionMetricDTO positionMetricDTO = positionMetricMapper.toDto(positionMetric);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionMetricMockMvc.perform(post("/api/position-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMetricDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PositionMetric in the database
        List<PositionMetric> positionMetricList = positionMetricRepository.findAll();
        assertThat(positionMetricList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionMetricRepository.findAll().size();
        // set the field null
        positionMetric.setCreatedAt(null);

        // Create the PositionMetric, which fails.
        PositionMetricDTO positionMetricDTO = positionMetricMapper.toDto(positionMetric);

        restPositionMetricMockMvc.perform(post("/api/position-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMetricDTO)))
            .andExpect(status().isBadRequest());

        List<PositionMetric> positionMetricList = positionMetricRepository.findAll();
        assertThat(positionMetricList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionMetricRepository.findAll().size();
        // set the field null
        positionMetric.setUpdatedAt(null);

        // Create the PositionMetric, which fails.
        PositionMetricDTO positionMetricDTO = positionMetricMapper.toDto(positionMetric);

        restPositionMetricMockMvc.perform(post("/api/position-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMetricDTO)))
            .andExpect(status().isBadRequest());

        List<PositionMetric> positionMetricList = positionMetricRepository.findAll();
        assertThat(positionMetricList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionMetricRepository.findAll().size();
        // set the field null
        positionMetric.setModifiedBy(null);

        // Create the PositionMetric, which fails.
        PositionMetricDTO positionMetricDTO = positionMetricMapper.toDto(positionMetric);

        restPositionMetricMockMvc.perform(post("/api/position-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMetricDTO)))
            .andExpect(status().isBadRequest());

        List<PositionMetric> positionMetricList = positionMetricRepository.findAll();
        assertThat(positionMetricList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPositionMetrics() throws Exception {
        // Initialize the database
        positionMetricRepository.saveAndFlush(positionMetric);

        // Get all the positionMetricList
        restPositionMetricMockMvc.perform(get("/api/position-metrics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionMetric.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].entryAvgPrice").value(hasItem(DEFAULT_ENTRY_AVG_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].entryAmount").value(hasItem(DEFAULT_ENTRY_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].exitAvgPrice").value(hasItem(DEFAULT_EXIT_AVG_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].exitAmount").value(hasItem(DEFAULT_EXIT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].txCosts").value(hasItem(DEFAULT_TX_COSTS.doubleValue())));
    }

    @Test
    @Transactional
    public void getPositionMetric() throws Exception {
        // Initialize the database
        positionMetricRepository.saveAndFlush(positionMetric);

        // Get the positionMetric
        restPositionMetricMockMvc.perform(get("/api/position-metrics/{id}", positionMetric.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(positionMetric.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.entryAvgPrice").value(DEFAULT_ENTRY_AVG_PRICE.doubleValue()))
            .andExpect(jsonPath("$.entryAmount").value(DEFAULT_ENTRY_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.exitAvgPrice").value(DEFAULT_EXIT_AVG_PRICE.doubleValue()))
            .andExpect(jsonPath("$.exitAmount").value(DEFAULT_EXIT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.txCosts").value(DEFAULT_TX_COSTS.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPositionMetric() throws Exception {
        // Get the positionMetric
        restPositionMetricMockMvc.perform(get("/api/position-metrics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePositionMetric() throws Exception {
        // Initialize the database
        positionMetricRepository.saveAndFlush(positionMetric);
        int databaseSizeBeforeUpdate = positionMetricRepository.findAll().size();

        // Update the positionMetric
        PositionMetric updatedPositionMetric = positionMetricRepository.findOne(positionMetric.getId());
        updatedPositionMetric
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .entryAvgPrice(UPDATED_ENTRY_AVG_PRICE)
            .entryAmount(UPDATED_ENTRY_AMOUNT)
            .exitAvgPrice(UPDATED_EXIT_AVG_PRICE)
            .exitAmount(UPDATED_EXIT_AMOUNT)
            .txCosts(UPDATED_TX_COSTS);
        PositionMetricDTO positionMetricDTO = positionMetricMapper.toDto(updatedPositionMetric);

        restPositionMetricMockMvc.perform(put("/api/position-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMetricDTO)))
            .andExpect(status().isOk());

        // Validate the PositionMetric in the database
        List<PositionMetric> positionMetricList = positionMetricRepository.findAll();
        assertThat(positionMetricList).hasSize(databaseSizeBeforeUpdate);
        PositionMetric testPositionMetric = positionMetricList.get(positionMetricList.size() - 1);
        assertThat(testPositionMetric.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPositionMetric.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testPositionMetric.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testPositionMetric.getEntryAvgPrice()).isEqualTo(UPDATED_ENTRY_AVG_PRICE);
        assertThat(testPositionMetric.getEntryAmount()).isEqualTo(UPDATED_ENTRY_AMOUNT);
        assertThat(testPositionMetric.getExitAvgPrice()).isEqualTo(UPDATED_EXIT_AVG_PRICE);
        assertThat(testPositionMetric.getExitAmount()).isEqualTo(UPDATED_EXIT_AMOUNT);
        assertThat(testPositionMetric.getTxCosts()).isEqualTo(UPDATED_TX_COSTS);
    }

    @Test
    @Transactional
    public void updateNonExistingPositionMetric() throws Exception {
        int databaseSizeBeforeUpdate = positionMetricRepository.findAll().size();

        // Create the PositionMetric
        PositionMetricDTO positionMetricDTO = positionMetricMapper.toDto(positionMetric);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPositionMetricMockMvc.perform(put("/api/position-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(positionMetricDTO)))
            .andExpect(status().isCreated());

        // Validate the PositionMetric in the database
        List<PositionMetric> positionMetricList = positionMetricRepository.findAll();
        assertThat(positionMetricList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePositionMetric() throws Exception {
        // Initialize the database
        positionMetricRepository.saveAndFlush(positionMetric);
        int databaseSizeBeforeDelete = positionMetricRepository.findAll().size();

        // Get the positionMetric
        restPositionMetricMockMvc.perform(delete("/api/position-metrics/{id}", positionMetric.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PositionMetric> positionMetricList = positionMetricRepository.findAll();
        assertThat(positionMetricList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionMetric.class);
        PositionMetric positionMetric1 = new PositionMetric();
        positionMetric1.setId(1L);
        PositionMetric positionMetric2 = new PositionMetric();
        positionMetric2.setId(positionMetric1.getId());
        assertThat(positionMetric1).isEqualTo(positionMetric2);
        positionMetric2.setId(2L);
        assertThat(positionMetric1).isNotEqualTo(positionMetric2);
        positionMetric1.setId(null);
        assertThat(positionMetric1).isNotEqualTo(positionMetric2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionMetricDTO.class);
        PositionMetricDTO positionMetricDTO1 = new PositionMetricDTO();
        positionMetricDTO1.setId(1L);
        PositionMetricDTO positionMetricDTO2 = new PositionMetricDTO();
        assertThat(positionMetricDTO1).isNotEqualTo(positionMetricDTO2);
        positionMetricDTO2.setId(positionMetricDTO1.getId());
        assertThat(positionMetricDTO1).isEqualTo(positionMetricDTO2);
        positionMetricDTO2.setId(2L);
        assertThat(positionMetricDTO1).isNotEqualTo(positionMetricDTO2);
        positionMetricDTO1.setId(null);
        assertThat(positionMetricDTO1).isNotEqualTo(positionMetricDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(positionMetricMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(positionMetricMapper.fromId(null)).isNull();
    }
}

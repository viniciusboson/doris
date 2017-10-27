package com.oceanus.doris.web.rest;

import com.oceanus.doris.DorisApp;

import com.oceanus.doris.domain.Operation;
import com.oceanus.doris.domain.Position;
import com.oceanus.doris.domain.Institution;
import com.oceanus.doris.domain.Position;
import com.oceanus.doris.domain.Institution;
import com.oceanus.doris.repository.OperationRepository;
import com.oceanus.doris.service.OperationService;
import com.oceanus.doris.service.dto.OperationDTO;
import com.oceanus.doris.service.mapper.OperationMapper;
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
 * Test class for the OperationResource REST controller.
 *
 * @see OperationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DorisApp.class)
public class OperationResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_EXECUTED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXECUTED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Double DEFAULT_AMOUNT_FROM = 1D;
    private static final Double UPDATED_AMOUNT_FROM = 2D;

    private static final Double DEFAULT_AMOUNT_TO = 1D;
    private static final Double UPDATED_AMOUNT_TO = 2D;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private OperationService operationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOperationMockMvc;

    private Operation operation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OperationResource operationResource = new OperationResource(operationService);
        this.restOperationMockMvc = MockMvcBuilders.standaloneSetup(operationResource)
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
    public static Operation createEntity(EntityManager em) {
        Operation operation = new Operation()
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .executedAt(DEFAULT_EXECUTED_AT)
            .amountFrom(DEFAULT_AMOUNT_FROM)
            .amountTo(DEFAULT_AMOUNT_TO);
        // Add required entity
        Position positionFrom = PositionResourceIntTest.createEntity(em);
        em.persist(positionFrom);
        em.flush();
        operation.setPositionFrom(positionFrom);
        // Add required entity
        Institution institutionFrom = InstitutionResourceIntTest.createEntity(em);
        em.persist(institutionFrom);
        em.flush();
        operation.setInstitutionFrom(institutionFrom);
        // Add required entity
        Position positionTo = PositionResourceIntTest.createEntity(em);
        em.persist(positionTo);
        em.flush();
        operation.setPositionTo(positionTo);
        // Add required entity
        Institution institutionTo = InstitutionResourceIntTest.createEntity(em);
        em.persist(institutionTo);
        em.flush();
        operation.setInstitutionTo(institutionTo);
        return operation;
    }

    @Before
    public void initTest() {
        operation = createEntity(em);
    }

    @Test
    @Transactional
    public void createOperation() throws Exception {
        int databaseSizeBeforeCreate = operationRepository.findAll().size();

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);
        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isCreated());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeCreate + 1);
        Operation testOperation = operationList.get(operationList.size() - 1);
        assertThat(testOperation.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testOperation.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testOperation.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testOperation.getExecutedAt()).isEqualTo(DEFAULT_EXECUTED_AT);
        assertThat(testOperation.getAmountFrom()).isEqualTo(DEFAULT_AMOUNT_FROM);
        assertThat(testOperation.getAmountTo()).isEqualTo(DEFAULT_AMOUNT_TO);
    }

    @Test
    @Transactional
    public void createOperationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = operationRepository.findAll().size();

        // Create the Operation with an existing ID
        operation.setId(1L);
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setCreatedAt(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setUpdatedAt(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setModifiedBy(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExecutedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setExecutedAt(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setAmountFrom(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountToIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setAmountTo(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOperations() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operationList
        restOperationMockMvc.perform(get("/api/operations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].executedAt").value(hasItem(sameInstant(DEFAULT_EXECUTED_AT))))
            .andExpect(jsonPath("$.[*].amountFrom").value(hasItem(DEFAULT_AMOUNT_FROM.doubleValue())))
            .andExpect(jsonPath("$.[*].amountTo").value(hasItem(DEFAULT_AMOUNT_TO.doubleValue())));
    }

    @Test
    @Transactional
    public void getOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get the operation
        restOperationMockMvc.perform(get("/api/operations/{id}", operation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(operation.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.executedAt").value(sameInstant(DEFAULT_EXECUTED_AT)))
            .andExpect(jsonPath("$.amountFrom").value(DEFAULT_AMOUNT_FROM.doubleValue()))
            .andExpect(jsonPath("$.amountTo").value(DEFAULT_AMOUNT_TO.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOperation() throws Exception {
        // Get the operation
        restOperationMockMvc.perform(get("/api/operations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();

        // Update the operation
        Operation updatedOperation = operationRepository.findOne(operation.getId());
        updatedOperation
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .executedAt(UPDATED_EXECUTED_AT)
            .amountFrom(UPDATED_AMOUNT_FROM)
            .amountTo(UPDATED_AMOUNT_TO);
        OperationDTO operationDTO = operationMapper.toDto(updatedOperation);

        restOperationMockMvc.perform(put("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isOk());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
        Operation testOperation = operationList.get(operationList.size() - 1);
        assertThat(testOperation.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOperation.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testOperation.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testOperation.getExecutedAt()).isEqualTo(UPDATED_EXECUTED_AT);
        assertThat(testOperation.getAmountFrom()).isEqualTo(UPDATED_AMOUNT_FROM);
        assertThat(testOperation.getAmountTo()).isEqualTo(UPDATED_AMOUNT_TO);
    }

    @Test
    @Transactional
    public void updateNonExistingOperation() throws Exception {
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOperationMockMvc.perform(put("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isCreated());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);
        int databaseSizeBeforeDelete = operationRepository.findAll().size();

        // Get the operation
        restOperationMockMvc.perform(delete("/api/operations/{id}", operation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operation.class);
        Operation operation1 = new Operation();
        operation1.setId(1L);
        Operation operation2 = new Operation();
        operation2.setId(operation1.getId());
        assertThat(operation1).isEqualTo(operation2);
        operation2.setId(2L);
        assertThat(operation1).isNotEqualTo(operation2);
        operation1.setId(null);
        assertThat(operation1).isNotEqualTo(operation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OperationDTO.class);
        OperationDTO operationDTO1 = new OperationDTO();
        operationDTO1.setId(1L);
        OperationDTO operationDTO2 = new OperationDTO();
        assertThat(operationDTO1).isNotEqualTo(operationDTO2);
        operationDTO2.setId(operationDTO1.getId());
        assertThat(operationDTO1).isEqualTo(operationDTO2);
        operationDTO2.setId(2L);
        assertThat(operationDTO1).isNotEqualTo(operationDTO2);
        operationDTO1.setId(null);
        assertThat(operationDTO1).isNotEqualTo(operationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(operationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(operationMapper.fromId(null)).isNull();
    }
}

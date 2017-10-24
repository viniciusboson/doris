package com.oceanus.doris.web.rest;

import com.oceanus.doris.DorisApp;

import com.oceanus.doris.domain.Charge;
import com.oceanus.doris.repository.ChargeRepository;
import com.oceanus.doris.service.ChargeService;
import com.oceanus.doris.service.dto.ChargeDTO;
import com.oceanus.doris.service.mapper.ChargeMapper;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.oceanus.doris.domain.enumeration.ChargeType;
import com.oceanus.doris.domain.enumeration.OperationType;
/**
 * Test class for the ChargeResource REST controller.
 *
 * @see ChargeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DorisApp.class)
public class ChargeResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ChargeType DEFAULT_CHARGE_TYPE = ChargeType.FLAT_FEE;
    private static final ChargeType UPDATED_CHARGE_TYPE = ChargeType.PERCENTAGE;

    private static final OperationType DEFAULT_OPERATION_TYPE = OperationType.WIRE_TRANSFER;
    private static final OperationType UPDATED_OPERATION_TYPE = OperationType.WITHDRAW;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private ChargeMapper chargeMapper;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restChargeMockMvc;

    private Charge charge;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChargeResource chargeResource = new ChargeResource(chargeService);
        this.restChargeMockMvc = MockMvcBuilders.standaloneSetup(chargeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Charge createEntity(EntityManager em) {
        Charge charge = new Charge()
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .description(DEFAULT_DESCRIPTION)
            .chargeType(DEFAULT_CHARGE_TYPE)
            .operationType(DEFAULT_OPERATION_TYPE)
            .amount(DEFAULT_AMOUNT);
        return charge;
    }

    @Before
    public void initTest() {
        charge = createEntity(em);
    }

    @Test
    @Transactional
    public void createCharge() throws Exception {
        int databaseSizeBeforeCreate = chargeRepository.findAll().size();

        // Create the Charge
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);
        restChargeMockMvc.perform(post("/api/charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chargeDTO)))
            .andExpect(status().isCreated());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeCreate + 1);
        Charge testCharge = chargeList.get(chargeList.size() - 1);
        assertThat(testCharge.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCharge.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testCharge.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testCharge.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCharge.getChargeType()).isEqualTo(DEFAULT_CHARGE_TYPE);
        assertThat(testCharge.getOperationType()).isEqualTo(DEFAULT_OPERATION_TYPE);
        assertThat(testCharge.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createChargeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chargeRepository.findAll().size();

        // Create the Charge with an existing ID
        charge.setId(1L);
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChargeMockMvc.perform(post("/api/charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chargeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = chargeRepository.findAll().size();
        // set the field null
        charge.setCreatedAt(null);

        // Create the Charge, which fails.
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);

        restChargeMockMvc.perform(post("/api/charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chargeDTO)))
            .andExpect(status().isBadRequest());

        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = chargeRepository.findAll().size();
        // set the field null
        charge.setUpdatedAt(null);

        // Create the Charge, which fails.
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);

        restChargeMockMvc.perform(post("/api/charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chargeDTO)))
            .andExpect(status().isBadRequest());

        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = chargeRepository.findAll().size();
        // set the field null
        charge.setModifiedBy(null);

        // Create the Charge, which fails.
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);

        restChargeMockMvc.perform(post("/api/charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chargeDTO)))
            .andExpect(status().isBadRequest());

        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = chargeRepository.findAll().size();
        // set the field null
        charge.setDescription(null);

        // Create the Charge, which fails.
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);

        restChargeMockMvc.perform(post("/api/charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chargeDTO)))
            .andExpect(status().isBadRequest());

        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChargeTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = chargeRepository.findAll().size();
        // set the field null
        charge.setChargeType(null);

        // Create the Charge, which fails.
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);

        restChargeMockMvc.perform(post("/api/charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chargeDTO)))
            .andExpect(status().isBadRequest());

        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOperationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = chargeRepository.findAll().size();
        // set the field null
        charge.setOperationType(null);

        // Create the Charge, which fails.
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);

        restChargeMockMvc.perform(post("/api/charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chargeDTO)))
            .andExpect(status().isBadRequest());

        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = chargeRepository.findAll().size();
        // set the field null
        charge.setAmount(null);

        // Create the Charge, which fails.
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);

        restChargeMockMvc.perform(post("/api/charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chargeDTO)))
            .andExpect(status().isBadRequest());

        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCharges() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList
        restChargeMockMvc.perform(get("/api/charges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(charge.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].chargeType").value(hasItem(DEFAULT_CHARGE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].operationType").value(hasItem(DEFAULT_OPERATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void getCharge() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get the charge
        restChargeMockMvc.perform(get("/api/charges/{id}", charge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(charge.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.chargeType").value(DEFAULT_CHARGE_TYPE.toString()))
            .andExpect(jsonPath("$.operationType").value(DEFAULT_OPERATION_TYPE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCharge() throws Exception {
        // Get the charge
        restChargeMockMvc.perform(get("/api/charges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCharge() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);
        int databaseSizeBeforeUpdate = chargeRepository.findAll().size();

        // Update the charge
        Charge updatedCharge = chargeRepository.findOne(charge.getId());
        updatedCharge
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .description(UPDATED_DESCRIPTION)
            .chargeType(UPDATED_CHARGE_TYPE)
            .operationType(UPDATED_OPERATION_TYPE)
            .amount(UPDATED_AMOUNT);
        ChargeDTO chargeDTO = chargeMapper.toDto(updatedCharge);

        restChargeMockMvc.perform(put("/api/charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chargeDTO)))
            .andExpect(status().isOk());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeUpdate);
        Charge testCharge = chargeList.get(chargeList.size() - 1);
        assertThat(testCharge.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCharge.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testCharge.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testCharge.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCharge.getChargeType()).isEqualTo(UPDATED_CHARGE_TYPE);
        assertThat(testCharge.getOperationType()).isEqualTo(UPDATED_OPERATION_TYPE);
        assertThat(testCharge.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingCharge() throws Exception {
        int databaseSizeBeforeUpdate = chargeRepository.findAll().size();

        // Create the Charge
        ChargeDTO chargeDTO = chargeMapper.toDto(charge);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restChargeMockMvc.perform(put("/api/charges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chargeDTO)))
            .andExpect(status().isCreated());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCharge() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);
        int databaseSizeBeforeDelete = chargeRepository.findAll().size();

        // Get the charge
        restChargeMockMvc.perform(delete("/api/charges/{id}", charge.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Charge.class);
        Charge charge1 = new Charge();
        charge1.setId(1L);
        Charge charge2 = new Charge();
        charge2.setId(charge1.getId());
        assertThat(charge1).isEqualTo(charge2);
        charge2.setId(2L);
        assertThat(charge1).isNotEqualTo(charge2);
        charge1.setId(null);
        assertThat(charge1).isNotEqualTo(charge2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChargeDTO.class);
        ChargeDTO chargeDTO1 = new ChargeDTO();
        chargeDTO1.setId(1L);
        ChargeDTO chargeDTO2 = new ChargeDTO();
        assertThat(chargeDTO1).isNotEqualTo(chargeDTO2);
        chargeDTO2.setId(chargeDTO1.getId());
        assertThat(chargeDTO1).isEqualTo(chargeDTO2);
        chargeDTO2.setId(2L);
        assertThat(chargeDTO1).isNotEqualTo(chargeDTO2);
        chargeDTO1.setId(null);
        assertThat(chargeDTO1).isNotEqualTo(chargeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(chargeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(chargeMapper.fromId(null)).isNull();
    }
}

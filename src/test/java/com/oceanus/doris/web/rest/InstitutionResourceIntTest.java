package com.oceanus.doris.web.rest;

import com.oceanus.doris.DorisApp;

import com.oceanus.doris.domain.Institution;
import com.oceanus.doris.repository.InstitutionRepository;
import com.oceanus.doris.service.InstitutionService;
import com.oceanus.doris.service.dto.InstitutionDTO;
import com.oceanus.doris.service.mapper.InstitutionMapper;
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
 * Test class for the InstitutionResource REST controller.
 *
 * @see InstitutionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DorisApp.class)
public class InstitutionResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private InstitutionMapper institutionMapper;

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInstitutionMockMvc;

    private Institution institution;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InstitutionResource institutionResource = new InstitutionResource(institutionService);
        this.restInstitutionMockMvc = MockMvcBuilders.standaloneSetup(institutionResource)
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
    public static Institution createEntity(EntityManager em) {
        Institution institution = new Institution()
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .modifiedBy(DEFAULT_MODIFIED_BY)
            .description(DEFAULT_DESCRIPTION);
        return institution;
    }

    @Before
    public void initTest() {
        institution = createEntity(em);
    }

    @Test
    @Transactional
    public void createInstitution() throws Exception {
        int databaseSizeBeforeCreate = institutionRepository.findAll().size();

        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);
        restInstitutionMockMvc.perform(post("/api/institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(institutionDTO)))
            .andExpect(status().isCreated());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeCreate + 1);
        Institution testInstitution = institutionList.get(institutionList.size() - 1);
        assertThat(testInstitution.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testInstitution.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testInstitution.getModifiedBy()).isEqualTo(DEFAULT_MODIFIED_BY);
        assertThat(testInstitution.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createInstitutionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = institutionRepository.findAll().size();

        // Create the Institution with an existing ID
        institution.setId(1L);
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstitutionMockMvc.perform(post("/api/institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(institutionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionRepository.findAll().size();
        // set the field null
        institution.setCreatedAt(null);

        // Create the Institution, which fails.
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        restInstitutionMockMvc.perform(post("/api/institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(institutionDTO)))
            .andExpect(status().isBadRequest());

        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionRepository.findAll().size();
        // set the field null
        institution.setUpdatedAt(null);

        // Create the Institution, which fails.
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        restInstitutionMockMvc.perform(post("/api/institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(institutionDTO)))
            .andExpect(status().isBadRequest());

        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionRepository.findAll().size();
        // set the field null
        institution.setModifiedBy(null);

        // Create the Institution, which fails.
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        restInstitutionMockMvc.perform(post("/api/institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(institutionDTO)))
            .andExpect(status().isBadRequest());

        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionRepository.findAll().size();
        // set the field null
        institution.setDescription(null);

        // Create the Institution, which fails.
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        restInstitutionMockMvc.perform(post("/api/institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(institutionDTO)))
            .andExpect(status().isBadRequest());

        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstitutions() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);

        // Get all the institutionList
        restInstitutionMockMvc.perform(get("/api/institutions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institution.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].modifiedBy").value(hasItem(DEFAULT_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getInstitution() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);

        // Get the institution
        restInstitutionMockMvc.perform(get("/api/institutions/{id}", institution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(institution.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.modifiedBy").value(DEFAULT_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstitution() throws Exception {
        // Get the institution
        restInstitutionMockMvc.perform(get("/api/institutions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstitution() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);
        int databaseSizeBeforeUpdate = institutionRepository.findAll().size();

        // Update the institution
        Institution updatedInstitution = institutionRepository.findOne(institution.getId());
        updatedInstitution
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .modifiedBy(UPDATED_MODIFIED_BY)
            .description(UPDATED_DESCRIPTION);
        InstitutionDTO institutionDTO = institutionMapper.toDto(updatedInstitution);

        restInstitutionMockMvc.perform(put("/api/institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(institutionDTO)))
            .andExpect(status().isOk());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate);
        Institution testInstitution = institutionList.get(institutionList.size() - 1);
        assertThat(testInstitution.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testInstitution.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testInstitution.getModifiedBy()).isEqualTo(UPDATED_MODIFIED_BY);
        assertThat(testInstitution.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingInstitution() throws Exception {
        int databaseSizeBeforeUpdate = institutionRepository.findAll().size();

        // Create the Institution
        InstitutionDTO institutionDTO = institutionMapper.toDto(institution);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInstitutionMockMvc.perform(put("/api/institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(institutionDTO)))
            .andExpect(status().isCreated());

        // Validate the Institution in the database
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInstitution() throws Exception {
        // Initialize the database
        institutionRepository.saveAndFlush(institution);
        int databaseSizeBeforeDelete = institutionRepository.findAll().size();

        // Get the institution
        restInstitutionMockMvc.perform(delete("/api/institutions/{id}", institution.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Institution> institutionList = institutionRepository.findAll();
        assertThat(institutionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Institution.class);
        Institution institution1 = new Institution();
        institution1.setId(1L);
        Institution institution2 = new Institution();
        institution2.setId(institution1.getId());
        assertThat(institution1).isEqualTo(institution2);
        institution2.setId(2L);
        assertThat(institution1).isNotEqualTo(institution2);
        institution1.setId(null);
        assertThat(institution1).isNotEqualTo(institution2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstitutionDTO.class);
        InstitutionDTO institutionDTO1 = new InstitutionDTO();
        institutionDTO1.setId(1L);
        InstitutionDTO institutionDTO2 = new InstitutionDTO();
        assertThat(institutionDTO1).isNotEqualTo(institutionDTO2);
        institutionDTO2.setId(institutionDTO1.getId());
        assertThat(institutionDTO1).isEqualTo(institutionDTO2);
        institutionDTO2.setId(2L);
        assertThat(institutionDTO1).isNotEqualTo(institutionDTO2);
        institutionDTO1.setId(null);
        assertThat(institutionDTO1).isNotEqualTo(institutionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(institutionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(institutionMapper.fromId(null)).isNull();
    }
}

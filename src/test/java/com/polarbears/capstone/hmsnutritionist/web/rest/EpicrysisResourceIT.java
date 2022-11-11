package com.polarbears.capstone.hmsnutritionist.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polarbears.capstone.hmsnutritionist.IntegrationTest;
import com.polarbears.capstone.hmsnutritionist.domain.Consultings;
import com.polarbears.capstone.hmsnutritionist.domain.Epicrysis;
import com.polarbears.capstone.hmsnutritionist.repository.EpicrysisRepository;
import com.polarbears.capstone.hmsnutritionist.service.criteria.EpicrysisCriteria;
import com.polarbears.capstone.hmsnutritionist.service.dto.EpicrysisDTO;
import com.polarbears.capstone.hmsnutritionist.service.mapper.EpicrysisMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EpicrysisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EpicrysisResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_NUTRITIONIST_ID = 1L;
    private static final Long UPDATED_NUTRITIONIST_ID = 2L;
    private static final Long SMALLER_NUTRITIONIST_ID = 1L - 1L;

    private static final Long DEFAULT_CUSTOMER_ID = 1L;
    private static final Long UPDATED_CUSTOMER_ID = 2L;
    private static final Long SMALLER_CUSTOMER_ID = 1L - 1L;

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NUTRITIONIST_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NUTRITIONIST_NOTES = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/epicryses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EpicrysisRepository epicrysisRepository;

    @Autowired
    private EpicrysisMapper epicrysisMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEpicrysisMockMvc;

    private Epicrysis epicrysis;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Epicrysis createEntity(EntityManager em) {
        Epicrysis epicrysis = new Epicrysis()
            .name(DEFAULT_NAME)
            .nutritionistId(DEFAULT_NUTRITIONIST_ID)
            .customerId(DEFAULT_CUSTOMER_ID)
            .customerName(DEFAULT_CUSTOMER_NAME)
            .nutritionistNotes(DEFAULT_NUTRITIONIST_NOTES)
            .createdDate(DEFAULT_CREATED_DATE);
        return epicrysis;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Epicrysis createUpdatedEntity(EntityManager em) {
        Epicrysis epicrysis = new Epicrysis()
            .name(UPDATED_NAME)
            .nutritionistId(UPDATED_NUTRITIONIST_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .customerName(UPDATED_CUSTOMER_NAME)
            .nutritionistNotes(UPDATED_NUTRITIONIST_NOTES)
            .createdDate(UPDATED_CREATED_DATE);
        return epicrysis;
    }

    @BeforeEach
    public void initTest() {
        epicrysis = createEntity(em);
    }

    @Test
    @Transactional
    void createEpicrysis() throws Exception {
        int databaseSizeBeforeCreate = epicrysisRepository.findAll().size();
        // Create the Epicrysis
        EpicrysisDTO epicrysisDTO = epicrysisMapper.toDto(epicrysis);
        restEpicrysisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(epicrysisDTO)))
            .andExpect(status().isCreated());

        // Validate the Epicrysis in the database
        List<Epicrysis> epicrysisList = epicrysisRepository.findAll();
        assertThat(epicrysisList).hasSize(databaseSizeBeforeCreate + 1);
        Epicrysis testEpicrysis = epicrysisList.get(epicrysisList.size() - 1);
        assertThat(testEpicrysis.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEpicrysis.getNutritionistId()).isEqualTo(DEFAULT_NUTRITIONIST_ID);
        assertThat(testEpicrysis.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testEpicrysis.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testEpicrysis.getNutritionistNotes()).isEqualTo(DEFAULT_NUTRITIONIST_NOTES);
        assertThat(testEpicrysis.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createEpicrysisWithExistingId() throws Exception {
        // Create the Epicrysis with an existing ID
        epicrysis.setId(1L);
        EpicrysisDTO epicrysisDTO = epicrysisMapper.toDto(epicrysis);

        int databaseSizeBeforeCreate = epicrysisRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEpicrysisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(epicrysisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Epicrysis in the database
        List<Epicrysis> epicrysisList = epicrysisRepository.findAll();
        assertThat(epicrysisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEpicryses() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList
        restEpicrysisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(epicrysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nutritionistId").value(hasItem(DEFAULT_NUTRITIONIST_ID.intValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].nutritionistNotes").value(hasItem(DEFAULT_NUTRITIONIST_NOTES)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getEpicrysis() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get the epicrysis
        restEpicrysisMockMvc
            .perform(get(ENTITY_API_URL_ID, epicrysis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(epicrysis.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.nutritionistId").value(DEFAULT_NUTRITIONIST_ID.intValue()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.intValue()))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME))
            .andExpect(jsonPath("$.nutritionistNotes").value(DEFAULT_NUTRITIONIST_NOTES))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getEpicrysesByIdFiltering() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        Long id = epicrysis.getId();

        defaultEpicrysisShouldBeFound("id.equals=" + id);
        defaultEpicrysisShouldNotBeFound("id.notEquals=" + id);

        defaultEpicrysisShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEpicrysisShouldNotBeFound("id.greaterThan=" + id);

        defaultEpicrysisShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEpicrysisShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEpicrysesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where name equals to DEFAULT_NAME
        defaultEpicrysisShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the epicrysisList where name equals to UPDATED_NAME
        defaultEpicrysisShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEpicrysesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEpicrysisShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the epicrysisList where name equals to UPDATED_NAME
        defaultEpicrysisShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEpicrysesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where name is not null
        defaultEpicrysisShouldBeFound("name.specified=true");

        // Get all the epicrysisList where name is null
        defaultEpicrysisShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllEpicrysesByNameContainsSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where name contains DEFAULT_NAME
        defaultEpicrysisShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the epicrysisList where name contains UPDATED_NAME
        defaultEpicrysisShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEpicrysesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where name does not contain DEFAULT_NAME
        defaultEpicrysisShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the epicrysisList where name does not contain UPDATED_NAME
        defaultEpicrysisShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEpicrysesByNutritionistIdIsEqualToSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where nutritionistId equals to DEFAULT_NUTRITIONIST_ID
        defaultEpicrysisShouldBeFound("nutritionistId.equals=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the epicrysisList where nutritionistId equals to UPDATED_NUTRITIONIST_ID
        defaultEpicrysisShouldNotBeFound("nutritionistId.equals=" + UPDATED_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllEpicrysesByNutritionistIdIsInShouldWork() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where nutritionistId in DEFAULT_NUTRITIONIST_ID or UPDATED_NUTRITIONIST_ID
        defaultEpicrysisShouldBeFound("nutritionistId.in=" + DEFAULT_NUTRITIONIST_ID + "," + UPDATED_NUTRITIONIST_ID);

        // Get all the epicrysisList where nutritionistId equals to UPDATED_NUTRITIONIST_ID
        defaultEpicrysisShouldNotBeFound("nutritionistId.in=" + UPDATED_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllEpicrysesByNutritionistIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where nutritionistId is not null
        defaultEpicrysisShouldBeFound("nutritionistId.specified=true");

        // Get all the epicrysisList where nutritionistId is null
        defaultEpicrysisShouldNotBeFound("nutritionistId.specified=false");
    }

    @Test
    @Transactional
    void getAllEpicrysesByNutritionistIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where nutritionistId is greater than or equal to DEFAULT_NUTRITIONIST_ID
        defaultEpicrysisShouldBeFound("nutritionistId.greaterThanOrEqual=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the epicrysisList where nutritionistId is greater than or equal to UPDATED_NUTRITIONIST_ID
        defaultEpicrysisShouldNotBeFound("nutritionistId.greaterThanOrEqual=" + UPDATED_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllEpicrysesByNutritionistIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where nutritionistId is less than or equal to DEFAULT_NUTRITIONIST_ID
        defaultEpicrysisShouldBeFound("nutritionistId.lessThanOrEqual=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the epicrysisList where nutritionistId is less than or equal to SMALLER_NUTRITIONIST_ID
        defaultEpicrysisShouldNotBeFound("nutritionistId.lessThanOrEqual=" + SMALLER_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllEpicrysesByNutritionistIdIsLessThanSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where nutritionistId is less than DEFAULT_NUTRITIONIST_ID
        defaultEpicrysisShouldNotBeFound("nutritionistId.lessThan=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the epicrysisList where nutritionistId is less than UPDATED_NUTRITIONIST_ID
        defaultEpicrysisShouldBeFound("nutritionistId.lessThan=" + UPDATED_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllEpicrysesByNutritionistIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where nutritionistId is greater than DEFAULT_NUTRITIONIST_ID
        defaultEpicrysisShouldNotBeFound("nutritionistId.greaterThan=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the epicrysisList where nutritionistId is greater than SMALLER_NUTRITIONIST_ID
        defaultEpicrysisShouldBeFound("nutritionistId.greaterThan=" + SMALLER_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllEpicrysesByCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where customerId equals to DEFAULT_CUSTOMER_ID
        defaultEpicrysisShouldBeFound("customerId.equals=" + DEFAULT_CUSTOMER_ID);

        // Get all the epicrysisList where customerId equals to UPDATED_CUSTOMER_ID
        defaultEpicrysisShouldNotBeFound("customerId.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllEpicrysesByCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where customerId in DEFAULT_CUSTOMER_ID or UPDATED_CUSTOMER_ID
        defaultEpicrysisShouldBeFound("customerId.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID);

        // Get all the epicrysisList where customerId equals to UPDATED_CUSTOMER_ID
        defaultEpicrysisShouldNotBeFound("customerId.in=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllEpicrysesByCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where customerId is not null
        defaultEpicrysisShouldBeFound("customerId.specified=true");

        // Get all the epicrysisList where customerId is null
        defaultEpicrysisShouldNotBeFound("customerId.specified=false");
    }

    @Test
    @Transactional
    void getAllEpicrysesByCustomerIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where customerId is greater than or equal to DEFAULT_CUSTOMER_ID
        defaultEpicrysisShouldBeFound("customerId.greaterThanOrEqual=" + DEFAULT_CUSTOMER_ID);

        // Get all the epicrysisList where customerId is greater than or equal to UPDATED_CUSTOMER_ID
        defaultEpicrysisShouldNotBeFound("customerId.greaterThanOrEqual=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllEpicrysesByCustomerIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where customerId is less than or equal to DEFAULT_CUSTOMER_ID
        defaultEpicrysisShouldBeFound("customerId.lessThanOrEqual=" + DEFAULT_CUSTOMER_ID);

        // Get all the epicrysisList where customerId is less than or equal to SMALLER_CUSTOMER_ID
        defaultEpicrysisShouldNotBeFound("customerId.lessThanOrEqual=" + SMALLER_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllEpicrysesByCustomerIdIsLessThanSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where customerId is less than DEFAULT_CUSTOMER_ID
        defaultEpicrysisShouldNotBeFound("customerId.lessThan=" + DEFAULT_CUSTOMER_ID);

        // Get all the epicrysisList where customerId is less than UPDATED_CUSTOMER_ID
        defaultEpicrysisShouldBeFound("customerId.lessThan=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllEpicrysesByCustomerIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where customerId is greater than DEFAULT_CUSTOMER_ID
        defaultEpicrysisShouldNotBeFound("customerId.greaterThan=" + DEFAULT_CUSTOMER_ID);

        // Get all the epicrysisList where customerId is greater than SMALLER_CUSTOMER_ID
        defaultEpicrysisShouldBeFound("customerId.greaterThan=" + SMALLER_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllEpicrysesByCustomerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where customerName equals to DEFAULT_CUSTOMER_NAME
        defaultEpicrysisShouldBeFound("customerName.equals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the epicrysisList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultEpicrysisShouldNotBeFound("customerName.equals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllEpicrysesByCustomerNameIsInShouldWork() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where customerName in DEFAULT_CUSTOMER_NAME or UPDATED_CUSTOMER_NAME
        defaultEpicrysisShouldBeFound("customerName.in=" + DEFAULT_CUSTOMER_NAME + "," + UPDATED_CUSTOMER_NAME);

        // Get all the epicrysisList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultEpicrysisShouldNotBeFound("customerName.in=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllEpicrysesByCustomerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where customerName is not null
        defaultEpicrysisShouldBeFound("customerName.specified=true");

        // Get all the epicrysisList where customerName is null
        defaultEpicrysisShouldNotBeFound("customerName.specified=false");
    }

    @Test
    @Transactional
    void getAllEpicrysesByCustomerNameContainsSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where customerName contains DEFAULT_CUSTOMER_NAME
        defaultEpicrysisShouldBeFound("customerName.contains=" + DEFAULT_CUSTOMER_NAME);

        // Get all the epicrysisList where customerName contains UPDATED_CUSTOMER_NAME
        defaultEpicrysisShouldNotBeFound("customerName.contains=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllEpicrysesByCustomerNameNotContainsSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where customerName does not contain DEFAULT_CUSTOMER_NAME
        defaultEpicrysisShouldNotBeFound("customerName.doesNotContain=" + DEFAULT_CUSTOMER_NAME);

        // Get all the epicrysisList where customerName does not contain UPDATED_CUSTOMER_NAME
        defaultEpicrysisShouldBeFound("customerName.doesNotContain=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    void getAllEpicrysesByNutritionistNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where nutritionistNotes equals to DEFAULT_NUTRITIONIST_NOTES
        defaultEpicrysisShouldBeFound("nutritionistNotes.equals=" + DEFAULT_NUTRITIONIST_NOTES);

        // Get all the epicrysisList where nutritionistNotes equals to UPDATED_NUTRITIONIST_NOTES
        defaultEpicrysisShouldNotBeFound("nutritionistNotes.equals=" + UPDATED_NUTRITIONIST_NOTES);
    }

    @Test
    @Transactional
    void getAllEpicrysesByNutritionistNotesIsInShouldWork() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where nutritionistNotes in DEFAULT_NUTRITIONIST_NOTES or UPDATED_NUTRITIONIST_NOTES
        defaultEpicrysisShouldBeFound("nutritionistNotes.in=" + DEFAULT_NUTRITIONIST_NOTES + "," + UPDATED_NUTRITIONIST_NOTES);

        // Get all the epicrysisList where nutritionistNotes equals to UPDATED_NUTRITIONIST_NOTES
        defaultEpicrysisShouldNotBeFound("nutritionistNotes.in=" + UPDATED_NUTRITIONIST_NOTES);
    }

    @Test
    @Transactional
    void getAllEpicrysesByNutritionistNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where nutritionistNotes is not null
        defaultEpicrysisShouldBeFound("nutritionistNotes.specified=true");

        // Get all the epicrysisList where nutritionistNotes is null
        defaultEpicrysisShouldNotBeFound("nutritionistNotes.specified=false");
    }

    @Test
    @Transactional
    void getAllEpicrysesByNutritionistNotesContainsSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where nutritionistNotes contains DEFAULT_NUTRITIONIST_NOTES
        defaultEpicrysisShouldBeFound("nutritionistNotes.contains=" + DEFAULT_NUTRITIONIST_NOTES);

        // Get all the epicrysisList where nutritionistNotes contains UPDATED_NUTRITIONIST_NOTES
        defaultEpicrysisShouldNotBeFound("nutritionistNotes.contains=" + UPDATED_NUTRITIONIST_NOTES);
    }

    @Test
    @Transactional
    void getAllEpicrysesByNutritionistNotesNotContainsSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where nutritionistNotes does not contain DEFAULT_NUTRITIONIST_NOTES
        defaultEpicrysisShouldNotBeFound("nutritionistNotes.doesNotContain=" + DEFAULT_NUTRITIONIST_NOTES);

        // Get all the epicrysisList where nutritionistNotes does not contain UPDATED_NUTRITIONIST_NOTES
        defaultEpicrysisShouldBeFound("nutritionistNotes.doesNotContain=" + UPDATED_NUTRITIONIST_NOTES);
    }

    @Test
    @Transactional
    void getAllEpicrysesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where createdDate equals to DEFAULT_CREATED_DATE
        defaultEpicrysisShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the epicrysisList where createdDate equals to UPDATED_CREATED_DATE
        defaultEpicrysisShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEpicrysesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultEpicrysisShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the epicrysisList where createdDate equals to UPDATED_CREATED_DATE
        defaultEpicrysisShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEpicrysesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where createdDate is not null
        defaultEpicrysisShouldBeFound("createdDate.specified=true");

        // Get all the epicrysisList where createdDate is null
        defaultEpicrysisShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEpicrysesByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultEpicrysisShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the epicrysisList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultEpicrysisShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEpicrysesByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultEpicrysisShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the epicrysisList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultEpicrysisShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEpicrysesByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where createdDate is less than DEFAULT_CREATED_DATE
        defaultEpicrysisShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the epicrysisList where createdDate is less than UPDATED_CREATED_DATE
        defaultEpicrysisShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEpicrysesByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        // Get all the epicrysisList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultEpicrysisShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the epicrysisList where createdDate is greater than SMALLER_CREATED_DATE
        defaultEpicrysisShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEpicrysesByConsultingsIsEqualToSomething() throws Exception {
        Consultings consultings;
        if (TestUtil.findAll(em, Consultings.class).isEmpty()) {
            epicrysisRepository.saveAndFlush(epicrysis);
            consultings = ConsultingsResourceIT.createEntity(em);
        } else {
            consultings = TestUtil.findAll(em, Consultings.class).get(0);
        }
        em.persist(consultings);
        em.flush();
        epicrysis.addConsultings(consultings);
        epicrysisRepository.saveAndFlush(epicrysis);
        Long consultingsId = consultings.getId();

        // Get all the epicrysisList where consultings equals to consultingsId
        defaultEpicrysisShouldBeFound("consultingsId.equals=" + consultingsId);

        // Get all the epicrysisList where consultings equals to (consultingsId + 1)
        defaultEpicrysisShouldNotBeFound("consultingsId.equals=" + (consultingsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEpicrysisShouldBeFound(String filter) throws Exception {
        restEpicrysisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(epicrysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nutritionistId").value(hasItem(DEFAULT_NUTRITIONIST_ID.intValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].nutritionistNotes").value(hasItem(DEFAULT_NUTRITIONIST_NOTES)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restEpicrysisMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEpicrysisShouldNotBeFound(String filter) throws Exception {
        restEpicrysisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEpicrysisMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEpicrysis() throws Exception {
        // Get the epicrysis
        restEpicrysisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEpicrysis() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        int databaseSizeBeforeUpdate = epicrysisRepository.findAll().size();

        // Update the epicrysis
        Epicrysis updatedEpicrysis = epicrysisRepository.findById(epicrysis.getId()).get();
        // Disconnect from session so that the updates on updatedEpicrysis are not directly saved in db
        em.detach(updatedEpicrysis);
        updatedEpicrysis
            .name(UPDATED_NAME)
            .nutritionistId(UPDATED_NUTRITIONIST_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .customerName(UPDATED_CUSTOMER_NAME)
            .nutritionistNotes(UPDATED_NUTRITIONIST_NOTES)
            .createdDate(UPDATED_CREATED_DATE);
        EpicrysisDTO epicrysisDTO = epicrysisMapper.toDto(updatedEpicrysis);

        restEpicrysisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, epicrysisDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(epicrysisDTO))
            )
            .andExpect(status().isOk());

        // Validate the Epicrysis in the database
        List<Epicrysis> epicrysisList = epicrysisRepository.findAll();
        assertThat(epicrysisList).hasSize(databaseSizeBeforeUpdate);
        Epicrysis testEpicrysis = epicrysisList.get(epicrysisList.size() - 1);
        assertThat(testEpicrysis.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEpicrysis.getNutritionistId()).isEqualTo(UPDATED_NUTRITIONIST_ID);
        assertThat(testEpicrysis.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testEpicrysis.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testEpicrysis.getNutritionistNotes()).isEqualTo(UPDATED_NUTRITIONIST_NOTES);
        assertThat(testEpicrysis.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingEpicrysis() throws Exception {
        int databaseSizeBeforeUpdate = epicrysisRepository.findAll().size();
        epicrysis.setId(count.incrementAndGet());

        // Create the Epicrysis
        EpicrysisDTO epicrysisDTO = epicrysisMapper.toDto(epicrysis);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEpicrysisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, epicrysisDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(epicrysisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Epicrysis in the database
        List<Epicrysis> epicrysisList = epicrysisRepository.findAll();
        assertThat(epicrysisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEpicrysis() throws Exception {
        int databaseSizeBeforeUpdate = epicrysisRepository.findAll().size();
        epicrysis.setId(count.incrementAndGet());

        // Create the Epicrysis
        EpicrysisDTO epicrysisDTO = epicrysisMapper.toDto(epicrysis);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEpicrysisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(epicrysisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Epicrysis in the database
        List<Epicrysis> epicrysisList = epicrysisRepository.findAll();
        assertThat(epicrysisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEpicrysis() throws Exception {
        int databaseSizeBeforeUpdate = epicrysisRepository.findAll().size();
        epicrysis.setId(count.incrementAndGet());

        // Create the Epicrysis
        EpicrysisDTO epicrysisDTO = epicrysisMapper.toDto(epicrysis);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEpicrysisMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(epicrysisDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Epicrysis in the database
        List<Epicrysis> epicrysisList = epicrysisRepository.findAll();
        assertThat(epicrysisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEpicrysisWithPatch() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        int databaseSizeBeforeUpdate = epicrysisRepository.findAll().size();

        // Update the epicrysis using partial update
        Epicrysis partialUpdatedEpicrysis = new Epicrysis();
        partialUpdatedEpicrysis.setId(epicrysis.getId());

        partialUpdatedEpicrysis.name(UPDATED_NAME).customerName(UPDATED_CUSTOMER_NAME).createdDate(UPDATED_CREATED_DATE);

        restEpicrysisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEpicrysis.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEpicrysis))
            )
            .andExpect(status().isOk());

        // Validate the Epicrysis in the database
        List<Epicrysis> epicrysisList = epicrysisRepository.findAll();
        assertThat(epicrysisList).hasSize(databaseSizeBeforeUpdate);
        Epicrysis testEpicrysis = epicrysisList.get(epicrysisList.size() - 1);
        assertThat(testEpicrysis.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEpicrysis.getNutritionistId()).isEqualTo(DEFAULT_NUTRITIONIST_ID);
        assertThat(testEpicrysis.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testEpicrysis.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testEpicrysis.getNutritionistNotes()).isEqualTo(DEFAULT_NUTRITIONIST_NOTES);
        assertThat(testEpicrysis.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateEpicrysisWithPatch() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        int databaseSizeBeforeUpdate = epicrysisRepository.findAll().size();

        // Update the epicrysis using partial update
        Epicrysis partialUpdatedEpicrysis = new Epicrysis();
        partialUpdatedEpicrysis.setId(epicrysis.getId());

        partialUpdatedEpicrysis
            .name(UPDATED_NAME)
            .nutritionistId(UPDATED_NUTRITIONIST_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .customerName(UPDATED_CUSTOMER_NAME)
            .nutritionistNotes(UPDATED_NUTRITIONIST_NOTES)
            .createdDate(UPDATED_CREATED_DATE);

        restEpicrysisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEpicrysis.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEpicrysis))
            )
            .andExpect(status().isOk());

        // Validate the Epicrysis in the database
        List<Epicrysis> epicrysisList = epicrysisRepository.findAll();
        assertThat(epicrysisList).hasSize(databaseSizeBeforeUpdate);
        Epicrysis testEpicrysis = epicrysisList.get(epicrysisList.size() - 1);
        assertThat(testEpicrysis.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEpicrysis.getNutritionistId()).isEqualTo(UPDATED_NUTRITIONIST_ID);
        assertThat(testEpicrysis.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testEpicrysis.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testEpicrysis.getNutritionistNotes()).isEqualTo(UPDATED_NUTRITIONIST_NOTES);
        assertThat(testEpicrysis.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingEpicrysis() throws Exception {
        int databaseSizeBeforeUpdate = epicrysisRepository.findAll().size();
        epicrysis.setId(count.incrementAndGet());

        // Create the Epicrysis
        EpicrysisDTO epicrysisDTO = epicrysisMapper.toDto(epicrysis);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEpicrysisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, epicrysisDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(epicrysisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Epicrysis in the database
        List<Epicrysis> epicrysisList = epicrysisRepository.findAll();
        assertThat(epicrysisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEpicrysis() throws Exception {
        int databaseSizeBeforeUpdate = epicrysisRepository.findAll().size();
        epicrysis.setId(count.incrementAndGet());

        // Create the Epicrysis
        EpicrysisDTO epicrysisDTO = epicrysisMapper.toDto(epicrysis);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEpicrysisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(epicrysisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Epicrysis in the database
        List<Epicrysis> epicrysisList = epicrysisRepository.findAll();
        assertThat(epicrysisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEpicrysis() throws Exception {
        int databaseSizeBeforeUpdate = epicrysisRepository.findAll().size();
        epicrysis.setId(count.incrementAndGet());

        // Create the Epicrysis
        EpicrysisDTO epicrysisDTO = epicrysisMapper.toDto(epicrysis);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEpicrysisMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(epicrysisDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Epicrysis in the database
        List<Epicrysis> epicrysisList = epicrysisRepository.findAll();
        assertThat(epicrysisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEpicrysis() throws Exception {
        // Initialize the database
        epicrysisRepository.saveAndFlush(epicrysis);

        int databaseSizeBeforeDelete = epicrysisRepository.findAll().size();

        // Delete the epicrysis
        restEpicrysisMockMvc
            .perform(delete(ENTITY_API_URL_ID, epicrysis.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Epicrysis> epicrysisList = epicrysisRepository.findAll();
        assertThat(epicrysisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

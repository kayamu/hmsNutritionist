package com.polarbears.capstone.hmsnutritionist.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polarbears.capstone.hmsnutritionist.IntegrationTest;
import com.polarbears.capstone.hmsnutritionist.domain.ConsultingStatus;
import com.polarbears.capstone.hmsnutritionist.domain.Consultings;
import com.polarbears.capstone.hmsnutritionist.domain.Epicrysis;
import com.polarbears.capstone.hmsnutritionist.domain.MenuSuggestions;
import com.polarbears.capstone.hmsnutritionist.domain.enumeration.STATUS;
import com.polarbears.capstone.hmsnutritionist.repository.ConsultingsRepository;
import com.polarbears.capstone.hmsnutritionist.service.ConsultingsService;
import com.polarbears.capstone.hmsnutritionist.service.criteria.ConsultingsCriteria;
import com.polarbears.capstone.hmsnutritionist.service.dto.ConsultingsDTO;
import com.polarbears.capstone.hmsnutritionist.service.mapper.ConsultingsMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ConsultingsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ConsultingsResourceIT {

    private static final Long DEFAULT_CUSTOMER_ID = 1L;
    private static final Long UPDATED_CUSTOMER_ID = 2L;
    private static final Long SMALLER_CUSTOMER_ID = 1L - 1L;

    private static final String DEFAULT_CUSTMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTMER_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_NUTRITIONIST_ID = 1L;
    private static final Long UPDATED_NUTRITIONIST_ID = 2L;
    private static final Long SMALLER_NUTRITIONIST_ID = 1L - 1L;

    private static final String DEFAULT_NUTRITIONIST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NUTRITIONIST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NUTRITIONIST_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NUTRITIONIST_NOTES = "BBBBBBBBBB";

    private static final STATUS DEFAULT_LAST_STATUS = STATUS.WAITING;
    private static final STATUS UPDATED_LAST_STATUS = STATUS.ACCEPTED;

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/consultings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConsultingsRepository consultingsRepository;

    @Mock
    private ConsultingsRepository consultingsRepositoryMock;

    @Autowired
    private ConsultingsMapper consultingsMapper;

    @Mock
    private ConsultingsService consultingsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsultingsMockMvc;

    private Consultings consultings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultings createEntity(EntityManager em) {
        Consultings consultings = new Consultings()
            .customerId(DEFAULT_CUSTOMER_ID)
            .custmerName(DEFAULT_CUSTMER_NAME)
            .nutritionistId(DEFAULT_NUTRITIONIST_ID)
            .nutritionistName(DEFAULT_NUTRITIONIST_NAME)
            .nutritionistNotes(DEFAULT_NUTRITIONIST_NOTES)
            .lastStatus(DEFAULT_LAST_STATUS)
            .createdDate(DEFAULT_CREATED_DATE);
        return consultings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultings createUpdatedEntity(EntityManager em) {
        Consultings consultings = new Consultings()
            .customerId(UPDATED_CUSTOMER_ID)
            .custmerName(UPDATED_CUSTMER_NAME)
            .nutritionistId(UPDATED_NUTRITIONIST_ID)
            .nutritionistName(UPDATED_NUTRITIONIST_NAME)
            .nutritionistNotes(UPDATED_NUTRITIONIST_NOTES)
            .lastStatus(UPDATED_LAST_STATUS)
            .createdDate(UPDATED_CREATED_DATE);
        return consultings;
    }

    @BeforeEach
    public void initTest() {
        consultings = createEntity(em);
    }

    @Test
    @Transactional
    void createConsultings() throws Exception {
        int databaseSizeBeforeCreate = consultingsRepository.findAll().size();
        // Create the Consultings
        ConsultingsDTO consultingsDTO = consultingsMapper.toDto(consultings);
        restConsultingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultingsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Consultings in the database
        List<Consultings> consultingsList = consultingsRepository.findAll();
        assertThat(consultingsList).hasSize(databaseSizeBeforeCreate + 1);
        Consultings testConsultings = consultingsList.get(consultingsList.size() - 1);
        assertThat(testConsultings.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testConsultings.getCustmerName()).isEqualTo(DEFAULT_CUSTMER_NAME);
        assertThat(testConsultings.getNutritionistId()).isEqualTo(DEFAULT_NUTRITIONIST_ID);
        assertThat(testConsultings.getNutritionistName()).isEqualTo(DEFAULT_NUTRITIONIST_NAME);
        assertThat(testConsultings.getNutritionistNotes()).isEqualTo(DEFAULT_NUTRITIONIST_NOTES);
        assertThat(testConsultings.getLastStatus()).isEqualTo(DEFAULT_LAST_STATUS);
        assertThat(testConsultings.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createConsultingsWithExistingId() throws Exception {
        // Create the Consultings with an existing ID
        consultings.setId(1L);
        ConsultingsDTO consultingsDTO = consultingsMapper.toDto(consultings);

        int databaseSizeBeforeCreate = consultingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsultingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultings in the database
        List<Consultings> consultingsList = consultingsRepository.findAll();
        assertThat(consultingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConsultings() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList
        restConsultingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consultings.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].custmerName").value(hasItem(DEFAULT_CUSTMER_NAME)))
            .andExpect(jsonPath("$.[*].nutritionistId").value(hasItem(DEFAULT_NUTRITIONIST_ID.intValue())))
            .andExpect(jsonPath("$.[*].nutritionistName").value(hasItem(DEFAULT_NUTRITIONIST_NAME)))
            .andExpect(jsonPath("$.[*].nutritionistNotes").value(hasItem(DEFAULT_NUTRITIONIST_NOTES)))
            .andExpect(jsonPath("$.[*].lastStatus").value(hasItem(DEFAULT_LAST_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConsultingsWithEagerRelationshipsIsEnabled() throws Exception {
        when(consultingsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConsultingsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(consultingsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConsultingsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(consultingsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConsultingsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(consultingsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getConsultings() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get the consultings
        restConsultingsMockMvc
            .perform(get(ENTITY_API_URL_ID, consultings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consultings.getId().intValue()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.intValue()))
            .andExpect(jsonPath("$.custmerName").value(DEFAULT_CUSTMER_NAME))
            .andExpect(jsonPath("$.nutritionistId").value(DEFAULT_NUTRITIONIST_ID.intValue()))
            .andExpect(jsonPath("$.nutritionistName").value(DEFAULT_NUTRITIONIST_NAME))
            .andExpect(jsonPath("$.nutritionistNotes").value(DEFAULT_NUTRITIONIST_NOTES))
            .andExpect(jsonPath("$.lastStatus").value(DEFAULT_LAST_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getConsultingsByIdFiltering() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        Long id = consultings.getId();

        defaultConsultingsShouldBeFound("id.equals=" + id);
        defaultConsultingsShouldNotBeFound("id.notEquals=" + id);

        defaultConsultingsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConsultingsShouldNotBeFound("id.greaterThan=" + id);

        defaultConsultingsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConsultingsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConsultingsByCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where customerId equals to DEFAULT_CUSTOMER_ID
        defaultConsultingsShouldBeFound("customerId.equals=" + DEFAULT_CUSTOMER_ID);

        // Get all the consultingsList where customerId equals to UPDATED_CUSTOMER_ID
        defaultConsultingsShouldNotBeFound("customerId.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllConsultingsByCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where customerId in DEFAULT_CUSTOMER_ID or UPDATED_CUSTOMER_ID
        defaultConsultingsShouldBeFound("customerId.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID);

        // Get all the consultingsList where customerId equals to UPDATED_CUSTOMER_ID
        defaultConsultingsShouldNotBeFound("customerId.in=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllConsultingsByCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where customerId is not null
        defaultConsultingsShouldBeFound("customerId.specified=true");

        // Get all the consultingsList where customerId is null
        defaultConsultingsShouldNotBeFound("customerId.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultingsByCustomerIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where customerId is greater than or equal to DEFAULT_CUSTOMER_ID
        defaultConsultingsShouldBeFound("customerId.greaterThanOrEqual=" + DEFAULT_CUSTOMER_ID);

        // Get all the consultingsList where customerId is greater than or equal to UPDATED_CUSTOMER_ID
        defaultConsultingsShouldNotBeFound("customerId.greaterThanOrEqual=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllConsultingsByCustomerIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where customerId is less than or equal to DEFAULT_CUSTOMER_ID
        defaultConsultingsShouldBeFound("customerId.lessThanOrEqual=" + DEFAULT_CUSTOMER_ID);

        // Get all the consultingsList where customerId is less than or equal to SMALLER_CUSTOMER_ID
        defaultConsultingsShouldNotBeFound("customerId.lessThanOrEqual=" + SMALLER_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllConsultingsByCustomerIdIsLessThanSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where customerId is less than DEFAULT_CUSTOMER_ID
        defaultConsultingsShouldNotBeFound("customerId.lessThan=" + DEFAULT_CUSTOMER_ID);

        // Get all the consultingsList where customerId is less than UPDATED_CUSTOMER_ID
        defaultConsultingsShouldBeFound("customerId.lessThan=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllConsultingsByCustomerIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where customerId is greater than DEFAULT_CUSTOMER_ID
        defaultConsultingsShouldNotBeFound("customerId.greaterThan=" + DEFAULT_CUSTOMER_ID);

        // Get all the consultingsList where customerId is greater than SMALLER_CUSTOMER_ID
        defaultConsultingsShouldBeFound("customerId.greaterThan=" + SMALLER_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllConsultingsByCustmerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where custmerName equals to DEFAULT_CUSTMER_NAME
        defaultConsultingsShouldBeFound("custmerName.equals=" + DEFAULT_CUSTMER_NAME);

        // Get all the consultingsList where custmerName equals to UPDATED_CUSTMER_NAME
        defaultConsultingsShouldNotBeFound("custmerName.equals=" + UPDATED_CUSTMER_NAME);
    }

    @Test
    @Transactional
    void getAllConsultingsByCustmerNameIsInShouldWork() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where custmerName in DEFAULT_CUSTMER_NAME or UPDATED_CUSTMER_NAME
        defaultConsultingsShouldBeFound("custmerName.in=" + DEFAULT_CUSTMER_NAME + "," + UPDATED_CUSTMER_NAME);

        // Get all the consultingsList where custmerName equals to UPDATED_CUSTMER_NAME
        defaultConsultingsShouldNotBeFound("custmerName.in=" + UPDATED_CUSTMER_NAME);
    }

    @Test
    @Transactional
    void getAllConsultingsByCustmerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where custmerName is not null
        defaultConsultingsShouldBeFound("custmerName.specified=true");

        // Get all the consultingsList where custmerName is null
        defaultConsultingsShouldNotBeFound("custmerName.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultingsByCustmerNameContainsSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where custmerName contains DEFAULT_CUSTMER_NAME
        defaultConsultingsShouldBeFound("custmerName.contains=" + DEFAULT_CUSTMER_NAME);

        // Get all the consultingsList where custmerName contains UPDATED_CUSTMER_NAME
        defaultConsultingsShouldNotBeFound("custmerName.contains=" + UPDATED_CUSTMER_NAME);
    }

    @Test
    @Transactional
    void getAllConsultingsByCustmerNameNotContainsSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where custmerName does not contain DEFAULT_CUSTMER_NAME
        defaultConsultingsShouldNotBeFound("custmerName.doesNotContain=" + DEFAULT_CUSTMER_NAME);

        // Get all the consultingsList where custmerName does not contain UPDATED_CUSTMER_NAME
        defaultConsultingsShouldBeFound("custmerName.doesNotContain=" + UPDATED_CUSTMER_NAME);
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistIdIsEqualToSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistId equals to DEFAULT_NUTRITIONIST_ID
        defaultConsultingsShouldBeFound("nutritionistId.equals=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the consultingsList where nutritionistId equals to UPDATED_NUTRITIONIST_ID
        defaultConsultingsShouldNotBeFound("nutritionistId.equals=" + UPDATED_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistIdIsInShouldWork() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistId in DEFAULT_NUTRITIONIST_ID or UPDATED_NUTRITIONIST_ID
        defaultConsultingsShouldBeFound("nutritionistId.in=" + DEFAULT_NUTRITIONIST_ID + "," + UPDATED_NUTRITIONIST_ID);

        // Get all the consultingsList where nutritionistId equals to UPDATED_NUTRITIONIST_ID
        defaultConsultingsShouldNotBeFound("nutritionistId.in=" + UPDATED_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistId is not null
        defaultConsultingsShouldBeFound("nutritionistId.specified=true");

        // Get all the consultingsList where nutritionistId is null
        defaultConsultingsShouldNotBeFound("nutritionistId.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistId is greater than or equal to DEFAULT_NUTRITIONIST_ID
        defaultConsultingsShouldBeFound("nutritionistId.greaterThanOrEqual=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the consultingsList where nutritionistId is greater than or equal to UPDATED_NUTRITIONIST_ID
        defaultConsultingsShouldNotBeFound("nutritionistId.greaterThanOrEqual=" + UPDATED_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistId is less than or equal to DEFAULT_NUTRITIONIST_ID
        defaultConsultingsShouldBeFound("nutritionistId.lessThanOrEqual=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the consultingsList where nutritionistId is less than or equal to SMALLER_NUTRITIONIST_ID
        defaultConsultingsShouldNotBeFound("nutritionistId.lessThanOrEqual=" + SMALLER_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistIdIsLessThanSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistId is less than DEFAULT_NUTRITIONIST_ID
        defaultConsultingsShouldNotBeFound("nutritionistId.lessThan=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the consultingsList where nutritionistId is less than UPDATED_NUTRITIONIST_ID
        defaultConsultingsShouldBeFound("nutritionistId.lessThan=" + UPDATED_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistId is greater than DEFAULT_NUTRITIONIST_ID
        defaultConsultingsShouldNotBeFound("nutritionistId.greaterThan=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the consultingsList where nutritionistId is greater than SMALLER_NUTRITIONIST_ID
        defaultConsultingsShouldBeFound("nutritionistId.greaterThan=" + SMALLER_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistNameIsEqualToSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistName equals to DEFAULT_NUTRITIONIST_NAME
        defaultConsultingsShouldBeFound("nutritionistName.equals=" + DEFAULT_NUTRITIONIST_NAME);

        // Get all the consultingsList where nutritionistName equals to UPDATED_NUTRITIONIST_NAME
        defaultConsultingsShouldNotBeFound("nutritionistName.equals=" + UPDATED_NUTRITIONIST_NAME);
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistNameIsInShouldWork() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistName in DEFAULT_NUTRITIONIST_NAME or UPDATED_NUTRITIONIST_NAME
        defaultConsultingsShouldBeFound("nutritionistName.in=" + DEFAULT_NUTRITIONIST_NAME + "," + UPDATED_NUTRITIONIST_NAME);

        // Get all the consultingsList where nutritionistName equals to UPDATED_NUTRITIONIST_NAME
        defaultConsultingsShouldNotBeFound("nutritionistName.in=" + UPDATED_NUTRITIONIST_NAME);
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistName is not null
        defaultConsultingsShouldBeFound("nutritionistName.specified=true");

        // Get all the consultingsList where nutritionistName is null
        defaultConsultingsShouldNotBeFound("nutritionistName.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistNameContainsSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistName contains DEFAULT_NUTRITIONIST_NAME
        defaultConsultingsShouldBeFound("nutritionistName.contains=" + DEFAULT_NUTRITIONIST_NAME);

        // Get all the consultingsList where nutritionistName contains UPDATED_NUTRITIONIST_NAME
        defaultConsultingsShouldNotBeFound("nutritionistName.contains=" + UPDATED_NUTRITIONIST_NAME);
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistNameNotContainsSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistName does not contain DEFAULT_NUTRITIONIST_NAME
        defaultConsultingsShouldNotBeFound("nutritionistName.doesNotContain=" + DEFAULT_NUTRITIONIST_NAME);

        // Get all the consultingsList where nutritionistName does not contain UPDATED_NUTRITIONIST_NAME
        defaultConsultingsShouldBeFound("nutritionistName.doesNotContain=" + UPDATED_NUTRITIONIST_NAME);
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistNotes equals to DEFAULT_NUTRITIONIST_NOTES
        defaultConsultingsShouldBeFound("nutritionistNotes.equals=" + DEFAULT_NUTRITIONIST_NOTES);

        // Get all the consultingsList where nutritionistNotes equals to UPDATED_NUTRITIONIST_NOTES
        defaultConsultingsShouldNotBeFound("nutritionistNotes.equals=" + UPDATED_NUTRITIONIST_NOTES);
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistNotesIsInShouldWork() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistNotes in DEFAULT_NUTRITIONIST_NOTES or UPDATED_NUTRITIONIST_NOTES
        defaultConsultingsShouldBeFound("nutritionistNotes.in=" + DEFAULT_NUTRITIONIST_NOTES + "," + UPDATED_NUTRITIONIST_NOTES);

        // Get all the consultingsList where nutritionistNotes equals to UPDATED_NUTRITIONIST_NOTES
        defaultConsultingsShouldNotBeFound("nutritionistNotes.in=" + UPDATED_NUTRITIONIST_NOTES);
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistNotes is not null
        defaultConsultingsShouldBeFound("nutritionistNotes.specified=true");

        // Get all the consultingsList where nutritionistNotes is null
        defaultConsultingsShouldNotBeFound("nutritionistNotes.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistNotesContainsSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistNotes contains DEFAULT_NUTRITIONIST_NOTES
        defaultConsultingsShouldBeFound("nutritionistNotes.contains=" + DEFAULT_NUTRITIONIST_NOTES);

        // Get all the consultingsList where nutritionistNotes contains UPDATED_NUTRITIONIST_NOTES
        defaultConsultingsShouldNotBeFound("nutritionistNotes.contains=" + UPDATED_NUTRITIONIST_NOTES);
    }

    @Test
    @Transactional
    void getAllConsultingsByNutritionistNotesNotContainsSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where nutritionistNotes does not contain DEFAULT_NUTRITIONIST_NOTES
        defaultConsultingsShouldNotBeFound("nutritionistNotes.doesNotContain=" + DEFAULT_NUTRITIONIST_NOTES);

        // Get all the consultingsList where nutritionistNotes does not contain UPDATED_NUTRITIONIST_NOTES
        defaultConsultingsShouldBeFound("nutritionistNotes.doesNotContain=" + UPDATED_NUTRITIONIST_NOTES);
    }

    @Test
    @Transactional
    void getAllConsultingsByLastStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where lastStatus equals to DEFAULT_LAST_STATUS
        defaultConsultingsShouldBeFound("lastStatus.equals=" + DEFAULT_LAST_STATUS);

        // Get all the consultingsList where lastStatus equals to UPDATED_LAST_STATUS
        defaultConsultingsShouldNotBeFound("lastStatus.equals=" + UPDATED_LAST_STATUS);
    }

    @Test
    @Transactional
    void getAllConsultingsByLastStatusIsInShouldWork() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where lastStatus in DEFAULT_LAST_STATUS or UPDATED_LAST_STATUS
        defaultConsultingsShouldBeFound("lastStatus.in=" + DEFAULT_LAST_STATUS + "," + UPDATED_LAST_STATUS);

        // Get all the consultingsList where lastStatus equals to UPDATED_LAST_STATUS
        defaultConsultingsShouldNotBeFound("lastStatus.in=" + UPDATED_LAST_STATUS);
    }

    @Test
    @Transactional
    void getAllConsultingsByLastStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where lastStatus is not null
        defaultConsultingsShouldBeFound("lastStatus.specified=true");

        // Get all the consultingsList where lastStatus is null
        defaultConsultingsShouldNotBeFound("lastStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultingsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where createdDate equals to DEFAULT_CREATED_DATE
        defaultConsultingsShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the consultingsList where createdDate equals to UPDATED_CREATED_DATE
        defaultConsultingsShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllConsultingsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultConsultingsShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the consultingsList where createdDate equals to UPDATED_CREATED_DATE
        defaultConsultingsShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllConsultingsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where createdDate is not null
        defaultConsultingsShouldBeFound("createdDate.specified=true");

        // Get all the consultingsList where createdDate is null
        defaultConsultingsShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultingsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultConsultingsShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the consultingsList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultConsultingsShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllConsultingsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultConsultingsShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the consultingsList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultConsultingsShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllConsultingsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where createdDate is less than DEFAULT_CREATED_DATE
        defaultConsultingsShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the consultingsList where createdDate is less than UPDATED_CREATED_DATE
        defaultConsultingsShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllConsultingsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        // Get all the consultingsList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultConsultingsShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the consultingsList where createdDate is greater than SMALLER_CREATED_DATE
        defaultConsultingsShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllConsultingsByEpicrysisIsEqualToSomething() throws Exception {
        Epicrysis epicrysis;
        if (TestUtil.findAll(em, Epicrysis.class).isEmpty()) {
            consultingsRepository.saveAndFlush(consultings);
            epicrysis = EpicrysisResourceIT.createEntity(em);
        } else {
            epicrysis = TestUtil.findAll(em, Epicrysis.class).get(0);
        }
        em.persist(epicrysis);
        em.flush();
        consultings.addEpicrysis(epicrysis);
        consultingsRepository.saveAndFlush(consultings);
        Long epicrysisId = epicrysis.getId();

        // Get all the consultingsList where epicrysis equals to epicrysisId
        defaultConsultingsShouldBeFound("epicrysisId.equals=" + epicrysisId);

        // Get all the consultingsList where epicrysis equals to (epicrysisId + 1)
        defaultConsultingsShouldNotBeFound("epicrysisId.equals=" + (epicrysisId + 1));
    }

    @Test
    @Transactional
    void getAllConsultingsByMenuSuggestionsIsEqualToSomething() throws Exception {
        MenuSuggestions menuSuggestions;
        if (TestUtil.findAll(em, MenuSuggestions.class).isEmpty()) {
            consultingsRepository.saveAndFlush(consultings);
            menuSuggestions = MenuSuggestionsResourceIT.createEntity(em);
        } else {
            menuSuggestions = TestUtil.findAll(em, MenuSuggestions.class).get(0);
        }
        em.persist(menuSuggestions);
        em.flush();
        consultings.addMenuSuggestions(menuSuggestions);
        consultingsRepository.saveAndFlush(consultings);
        Long menuSuggestionsId = menuSuggestions.getId();

        // Get all the consultingsList where menuSuggestions equals to menuSuggestionsId
        defaultConsultingsShouldBeFound("menuSuggestionsId.equals=" + menuSuggestionsId);

        // Get all the consultingsList where menuSuggestions equals to (menuSuggestionsId + 1)
        defaultConsultingsShouldNotBeFound("menuSuggestionsId.equals=" + (menuSuggestionsId + 1));
    }

    @Test
    @Transactional
    void getAllConsultingsByConsultingStatusIsEqualToSomething() throws Exception {
        ConsultingStatus consultingStatus;
        if (TestUtil.findAll(em, ConsultingStatus.class).isEmpty()) {
            consultingsRepository.saveAndFlush(consultings);
            consultingStatus = ConsultingStatusResourceIT.createEntity(em);
        } else {
            consultingStatus = TestUtil.findAll(em, ConsultingStatus.class).get(0);
        }
        em.persist(consultingStatus);
        em.flush();
        consultings.setConsultingStatus(consultingStatus);
        consultingsRepository.saveAndFlush(consultings);
        Long consultingStatusId = consultingStatus.getId();

        // Get all the consultingsList where consultingStatus equals to consultingStatusId
        defaultConsultingsShouldBeFound("consultingStatusId.equals=" + consultingStatusId);

        // Get all the consultingsList where consultingStatus equals to (consultingStatusId + 1)
        defaultConsultingsShouldNotBeFound("consultingStatusId.equals=" + (consultingStatusId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConsultingsShouldBeFound(String filter) throws Exception {
        restConsultingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consultings.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].custmerName").value(hasItem(DEFAULT_CUSTMER_NAME)))
            .andExpect(jsonPath("$.[*].nutritionistId").value(hasItem(DEFAULT_NUTRITIONIST_ID.intValue())))
            .andExpect(jsonPath("$.[*].nutritionistName").value(hasItem(DEFAULT_NUTRITIONIST_NAME)))
            .andExpect(jsonPath("$.[*].nutritionistNotes").value(hasItem(DEFAULT_NUTRITIONIST_NOTES)))
            .andExpect(jsonPath("$.[*].lastStatus").value(hasItem(DEFAULT_LAST_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restConsultingsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConsultingsShouldNotBeFound(String filter) throws Exception {
        restConsultingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConsultingsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConsultings() throws Exception {
        // Get the consultings
        restConsultingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConsultings() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        int databaseSizeBeforeUpdate = consultingsRepository.findAll().size();

        // Update the consultings
        Consultings updatedConsultings = consultingsRepository.findById(consultings.getId()).get();
        // Disconnect from session so that the updates on updatedConsultings are not directly saved in db
        em.detach(updatedConsultings);
        updatedConsultings
            .customerId(UPDATED_CUSTOMER_ID)
            .custmerName(UPDATED_CUSTMER_NAME)
            .nutritionistId(UPDATED_NUTRITIONIST_ID)
            .nutritionistName(UPDATED_NUTRITIONIST_NAME)
            .nutritionistNotes(UPDATED_NUTRITIONIST_NOTES)
            .lastStatus(UPDATED_LAST_STATUS)
            .createdDate(UPDATED_CREATED_DATE);
        ConsultingsDTO consultingsDTO = consultingsMapper.toDto(updatedConsultings);

        restConsultingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consultingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultingsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Consultings in the database
        List<Consultings> consultingsList = consultingsRepository.findAll();
        assertThat(consultingsList).hasSize(databaseSizeBeforeUpdate);
        Consultings testConsultings = consultingsList.get(consultingsList.size() - 1);
        assertThat(testConsultings.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testConsultings.getCustmerName()).isEqualTo(UPDATED_CUSTMER_NAME);
        assertThat(testConsultings.getNutritionistId()).isEqualTo(UPDATED_NUTRITIONIST_ID);
        assertThat(testConsultings.getNutritionistName()).isEqualTo(UPDATED_NUTRITIONIST_NAME);
        assertThat(testConsultings.getNutritionistNotes()).isEqualTo(UPDATED_NUTRITIONIST_NOTES);
        assertThat(testConsultings.getLastStatus()).isEqualTo(UPDATED_LAST_STATUS);
        assertThat(testConsultings.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingConsultings() throws Exception {
        int databaseSizeBeforeUpdate = consultingsRepository.findAll().size();
        consultings.setId(count.incrementAndGet());

        // Create the Consultings
        ConsultingsDTO consultingsDTO = consultingsMapper.toDto(consultings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consultingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultings in the database
        List<Consultings> consultingsList = consultingsRepository.findAll();
        assertThat(consultingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsultings() throws Exception {
        int databaseSizeBeforeUpdate = consultingsRepository.findAll().size();
        consultings.setId(count.incrementAndGet());

        // Create the Consultings
        ConsultingsDTO consultingsDTO = consultingsMapper.toDto(consultings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultings in the database
        List<Consultings> consultingsList = consultingsRepository.findAll();
        assertThat(consultingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsultings() throws Exception {
        int databaseSizeBeforeUpdate = consultingsRepository.findAll().size();
        consultings.setId(count.incrementAndGet());

        // Create the Consultings
        ConsultingsDTO consultingsDTO = consultingsMapper.toDto(consultings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultingsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultingsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consultings in the database
        List<Consultings> consultingsList = consultingsRepository.findAll();
        assertThat(consultingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsultingsWithPatch() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        int databaseSizeBeforeUpdate = consultingsRepository.findAll().size();

        // Update the consultings using partial update
        Consultings partialUpdatedConsultings = new Consultings();
        partialUpdatedConsultings.setId(consultings.getId());

        partialUpdatedConsultings
            .nutritionistId(UPDATED_NUTRITIONIST_ID)
            .nutritionistName(UPDATED_NUTRITIONIST_NAME)
            .createdDate(UPDATED_CREATED_DATE);

        restConsultingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsultings))
            )
            .andExpect(status().isOk());

        // Validate the Consultings in the database
        List<Consultings> consultingsList = consultingsRepository.findAll();
        assertThat(consultingsList).hasSize(databaseSizeBeforeUpdate);
        Consultings testConsultings = consultingsList.get(consultingsList.size() - 1);
        assertThat(testConsultings.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testConsultings.getCustmerName()).isEqualTo(DEFAULT_CUSTMER_NAME);
        assertThat(testConsultings.getNutritionistId()).isEqualTo(UPDATED_NUTRITIONIST_ID);
        assertThat(testConsultings.getNutritionistName()).isEqualTo(UPDATED_NUTRITIONIST_NAME);
        assertThat(testConsultings.getNutritionistNotes()).isEqualTo(DEFAULT_NUTRITIONIST_NOTES);
        assertThat(testConsultings.getLastStatus()).isEqualTo(DEFAULT_LAST_STATUS);
        assertThat(testConsultings.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateConsultingsWithPatch() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        int databaseSizeBeforeUpdate = consultingsRepository.findAll().size();

        // Update the consultings using partial update
        Consultings partialUpdatedConsultings = new Consultings();
        partialUpdatedConsultings.setId(consultings.getId());

        partialUpdatedConsultings
            .customerId(UPDATED_CUSTOMER_ID)
            .custmerName(UPDATED_CUSTMER_NAME)
            .nutritionistId(UPDATED_NUTRITIONIST_ID)
            .nutritionistName(UPDATED_NUTRITIONIST_NAME)
            .nutritionistNotes(UPDATED_NUTRITIONIST_NOTES)
            .lastStatus(UPDATED_LAST_STATUS)
            .createdDate(UPDATED_CREATED_DATE);

        restConsultingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsultings))
            )
            .andExpect(status().isOk());

        // Validate the Consultings in the database
        List<Consultings> consultingsList = consultingsRepository.findAll();
        assertThat(consultingsList).hasSize(databaseSizeBeforeUpdate);
        Consultings testConsultings = consultingsList.get(consultingsList.size() - 1);
        assertThat(testConsultings.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testConsultings.getCustmerName()).isEqualTo(UPDATED_CUSTMER_NAME);
        assertThat(testConsultings.getNutritionistId()).isEqualTo(UPDATED_NUTRITIONIST_ID);
        assertThat(testConsultings.getNutritionistName()).isEqualTo(UPDATED_NUTRITIONIST_NAME);
        assertThat(testConsultings.getNutritionistNotes()).isEqualTo(UPDATED_NUTRITIONIST_NOTES);
        assertThat(testConsultings.getLastStatus()).isEqualTo(UPDATED_LAST_STATUS);
        assertThat(testConsultings.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingConsultings() throws Exception {
        int databaseSizeBeforeUpdate = consultingsRepository.findAll().size();
        consultings.setId(count.incrementAndGet());

        // Create the Consultings
        ConsultingsDTO consultingsDTO = consultingsMapper.toDto(consultings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consultingsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultings in the database
        List<Consultings> consultingsList = consultingsRepository.findAll();
        assertThat(consultingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsultings() throws Exception {
        int databaseSizeBeforeUpdate = consultingsRepository.findAll().size();
        consultings.setId(count.incrementAndGet());

        // Create the Consultings
        ConsultingsDTO consultingsDTO = consultingsMapper.toDto(consultings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultings in the database
        List<Consultings> consultingsList = consultingsRepository.findAll();
        assertThat(consultingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsultings() throws Exception {
        int databaseSizeBeforeUpdate = consultingsRepository.findAll().size();
        consultings.setId(count.incrementAndGet());

        // Create the Consultings
        ConsultingsDTO consultingsDTO = consultingsMapper.toDto(consultings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultingsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(consultingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consultings in the database
        List<Consultings> consultingsList = consultingsRepository.findAll();
        assertThat(consultingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsultings() throws Exception {
        // Initialize the database
        consultingsRepository.saveAndFlush(consultings);

        int databaseSizeBeforeDelete = consultingsRepository.findAll().size();

        // Delete the consultings
        restConsultingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, consultings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Consultings> consultingsList = consultingsRepository.findAll();
        assertThat(consultingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

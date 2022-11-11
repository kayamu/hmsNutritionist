package com.polarbears.capstone.hmsnutritionist.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polarbears.capstone.hmsnutritionist.IntegrationTest;
import com.polarbears.capstone.hmsnutritionist.domain.Consultings;
import com.polarbears.capstone.hmsnutritionist.domain.MenuSuggestions;
import com.polarbears.capstone.hmsnutritionist.repository.MenuSuggestionsRepository;
import com.polarbears.capstone.hmsnutritionist.service.criteria.MenuSuggestionsCriteria;
import com.polarbears.capstone.hmsnutritionist.service.dto.MenuSuggestionsDTO;
import com.polarbears.capstone.hmsnutritionist.service.mapper.MenuSuggestionsMapper;
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
 * Integration tests for the {@link MenuSuggestionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MenuSuggestionsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_NUTRITIONIST_ID = 1L;
    private static final Long UPDATED_NUTRITIONIST_ID = 2L;
    private static final Long SMALLER_NUTRITIONIST_ID = 1L - 1L;

    private static final Long DEFAULT_CUSTOMER_ID = 1L;
    private static final Long UPDATED_CUSTOMER_ID = 2L;
    private static final Long SMALLER_CUSTOMER_ID = 1L - 1L;

    private static final Integer DEFAULT_MENU_GROUP_ID = 1;
    private static final Integer UPDATED_MENU_GROUP_ID = 2;
    private static final Integer SMALLER_MENU_GROUP_ID = 1 - 1;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/menu-suggestions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MenuSuggestionsRepository menuSuggestionsRepository;

    @Autowired
    private MenuSuggestionsMapper menuSuggestionsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMenuSuggestionsMockMvc;

    private MenuSuggestions menuSuggestions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuSuggestions createEntity(EntityManager em) {
        MenuSuggestions menuSuggestions = new MenuSuggestions()
            .name(DEFAULT_NAME)
            .nutritionistId(DEFAULT_NUTRITIONIST_ID)
            .customerId(DEFAULT_CUSTOMER_ID)
            .menuGroupId(DEFAULT_MENU_GROUP_ID)
            .notes(DEFAULT_NOTES)
            .createdDate(DEFAULT_CREATED_DATE);
        return menuSuggestions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuSuggestions createUpdatedEntity(EntityManager em) {
        MenuSuggestions menuSuggestions = new MenuSuggestions()
            .name(UPDATED_NAME)
            .nutritionistId(UPDATED_NUTRITIONIST_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .menuGroupId(UPDATED_MENU_GROUP_ID)
            .notes(UPDATED_NOTES)
            .createdDate(UPDATED_CREATED_DATE);
        return menuSuggestions;
    }

    @BeforeEach
    public void initTest() {
        menuSuggestions = createEntity(em);
    }

    @Test
    @Transactional
    void createMenuSuggestions() throws Exception {
        int databaseSizeBeforeCreate = menuSuggestionsRepository.findAll().size();
        // Create the MenuSuggestions
        MenuSuggestionsDTO menuSuggestionsDTO = menuSuggestionsMapper.toDto(menuSuggestions);
        restMenuSuggestionsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuSuggestionsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MenuSuggestions in the database
        List<MenuSuggestions> menuSuggestionsList = menuSuggestionsRepository.findAll();
        assertThat(menuSuggestionsList).hasSize(databaseSizeBeforeCreate + 1);
        MenuSuggestions testMenuSuggestions = menuSuggestionsList.get(menuSuggestionsList.size() - 1);
        assertThat(testMenuSuggestions.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMenuSuggestions.getNutritionistId()).isEqualTo(DEFAULT_NUTRITIONIST_ID);
        assertThat(testMenuSuggestions.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testMenuSuggestions.getMenuGroupId()).isEqualTo(DEFAULT_MENU_GROUP_ID);
        assertThat(testMenuSuggestions.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testMenuSuggestions.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createMenuSuggestionsWithExistingId() throws Exception {
        // Create the MenuSuggestions with an existing ID
        menuSuggestions.setId(1L);
        MenuSuggestionsDTO menuSuggestionsDTO = menuSuggestionsMapper.toDto(menuSuggestions);

        int databaseSizeBeforeCreate = menuSuggestionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuSuggestionsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuSuggestionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuSuggestions in the database
        List<MenuSuggestions> menuSuggestionsList = menuSuggestionsRepository.findAll();
        assertThat(menuSuggestionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMenuSuggestions() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList
        restMenuSuggestionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuSuggestions.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nutritionistId").value(hasItem(DEFAULT_NUTRITIONIST_ID.intValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].menuGroupId").value(hasItem(DEFAULT_MENU_GROUP_ID)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getMenuSuggestions() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get the menuSuggestions
        restMenuSuggestionsMockMvc
            .perform(get(ENTITY_API_URL_ID, menuSuggestions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(menuSuggestions.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.nutritionistId").value(DEFAULT_NUTRITIONIST_ID.intValue()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.intValue()))
            .andExpect(jsonPath("$.menuGroupId").value(DEFAULT_MENU_GROUP_ID))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getMenuSuggestionsByIdFiltering() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        Long id = menuSuggestions.getId();

        defaultMenuSuggestionsShouldBeFound("id.equals=" + id);
        defaultMenuSuggestionsShouldNotBeFound("id.notEquals=" + id);

        defaultMenuSuggestionsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMenuSuggestionsShouldNotBeFound("id.greaterThan=" + id);

        defaultMenuSuggestionsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMenuSuggestionsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where name equals to DEFAULT_NAME
        defaultMenuSuggestionsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the menuSuggestionsList where name equals to UPDATED_NAME
        defaultMenuSuggestionsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMenuSuggestionsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the menuSuggestionsList where name equals to UPDATED_NAME
        defaultMenuSuggestionsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where name is not null
        defaultMenuSuggestionsShouldBeFound("name.specified=true");

        // Get all the menuSuggestionsList where name is null
        defaultMenuSuggestionsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNameContainsSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where name contains DEFAULT_NAME
        defaultMenuSuggestionsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the menuSuggestionsList where name contains UPDATED_NAME
        defaultMenuSuggestionsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where name does not contain DEFAULT_NAME
        defaultMenuSuggestionsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the menuSuggestionsList where name does not contain UPDATED_NAME
        defaultMenuSuggestionsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNutritionistIdIsEqualToSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where nutritionistId equals to DEFAULT_NUTRITIONIST_ID
        defaultMenuSuggestionsShouldBeFound("nutritionistId.equals=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the menuSuggestionsList where nutritionistId equals to UPDATED_NUTRITIONIST_ID
        defaultMenuSuggestionsShouldNotBeFound("nutritionistId.equals=" + UPDATED_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNutritionistIdIsInShouldWork() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where nutritionistId in DEFAULT_NUTRITIONIST_ID or UPDATED_NUTRITIONIST_ID
        defaultMenuSuggestionsShouldBeFound("nutritionistId.in=" + DEFAULT_NUTRITIONIST_ID + "," + UPDATED_NUTRITIONIST_ID);

        // Get all the menuSuggestionsList where nutritionistId equals to UPDATED_NUTRITIONIST_ID
        defaultMenuSuggestionsShouldNotBeFound("nutritionistId.in=" + UPDATED_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNutritionistIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where nutritionistId is not null
        defaultMenuSuggestionsShouldBeFound("nutritionistId.specified=true");

        // Get all the menuSuggestionsList where nutritionistId is null
        defaultMenuSuggestionsShouldNotBeFound("nutritionistId.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNutritionistIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where nutritionistId is greater than or equal to DEFAULT_NUTRITIONIST_ID
        defaultMenuSuggestionsShouldBeFound("nutritionistId.greaterThanOrEqual=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the menuSuggestionsList where nutritionistId is greater than or equal to UPDATED_NUTRITIONIST_ID
        defaultMenuSuggestionsShouldNotBeFound("nutritionistId.greaterThanOrEqual=" + UPDATED_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNutritionistIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where nutritionistId is less than or equal to DEFAULT_NUTRITIONIST_ID
        defaultMenuSuggestionsShouldBeFound("nutritionistId.lessThanOrEqual=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the menuSuggestionsList where nutritionistId is less than or equal to SMALLER_NUTRITIONIST_ID
        defaultMenuSuggestionsShouldNotBeFound("nutritionistId.lessThanOrEqual=" + SMALLER_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNutritionistIdIsLessThanSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where nutritionistId is less than DEFAULT_NUTRITIONIST_ID
        defaultMenuSuggestionsShouldNotBeFound("nutritionistId.lessThan=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the menuSuggestionsList where nutritionistId is less than UPDATED_NUTRITIONIST_ID
        defaultMenuSuggestionsShouldBeFound("nutritionistId.lessThan=" + UPDATED_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNutritionistIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where nutritionistId is greater than DEFAULT_NUTRITIONIST_ID
        defaultMenuSuggestionsShouldNotBeFound("nutritionistId.greaterThan=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the menuSuggestionsList where nutritionistId is greater than SMALLER_NUTRITIONIST_ID
        defaultMenuSuggestionsShouldBeFound("nutritionistId.greaterThan=" + SMALLER_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where customerId equals to DEFAULT_CUSTOMER_ID
        defaultMenuSuggestionsShouldBeFound("customerId.equals=" + DEFAULT_CUSTOMER_ID);

        // Get all the menuSuggestionsList where customerId equals to UPDATED_CUSTOMER_ID
        defaultMenuSuggestionsShouldNotBeFound("customerId.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where customerId in DEFAULT_CUSTOMER_ID or UPDATED_CUSTOMER_ID
        defaultMenuSuggestionsShouldBeFound("customerId.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID);

        // Get all the menuSuggestionsList where customerId equals to UPDATED_CUSTOMER_ID
        defaultMenuSuggestionsShouldNotBeFound("customerId.in=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where customerId is not null
        defaultMenuSuggestionsShouldBeFound("customerId.specified=true");

        // Get all the menuSuggestionsList where customerId is null
        defaultMenuSuggestionsShouldNotBeFound("customerId.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByCustomerIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where customerId is greater than or equal to DEFAULT_CUSTOMER_ID
        defaultMenuSuggestionsShouldBeFound("customerId.greaterThanOrEqual=" + DEFAULT_CUSTOMER_ID);

        // Get all the menuSuggestionsList where customerId is greater than or equal to UPDATED_CUSTOMER_ID
        defaultMenuSuggestionsShouldNotBeFound("customerId.greaterThanOrEqual=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByCustomerIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where customerId is less than or equal to DEFAULT_CUSTOMER_ID
        defaultMenuSuggestionsShouldBeFound("customerId.lessThanOrEqual=" + DEFAULT_CUSTOMER_ID);

        // Get all the menuSuggestionsList where customerId is less than or equal to SMALLER_CUSTOMER_ID
        defaultMenuSuggestionsShouldNotBeFound("customerId.lessThanOrEqual=" + SMALLER_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByCustomerIdIsLessThanSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where customerId is less than DEFAULT_CUSTOMER_ID
        defaultMenuSuggestionsShouldNotBeFound("customerId.lessThan=" + DEFAULT_CUSTOMER_ID);

        // Get all the menuSuggestionsList where customerId is less than UPDATED_CUSTOMER_ID
        defaultMenuSuggestionsShouldBeFound("customerId.lessThan=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByCustomerIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where customerId is greater than DEFAULT_CUSTOMER_ID
        defaultMenuSuggestionsShouldNotBeFound("customerId.greaterThan=" + DEFAULT_CUSTOMER_ID);

        // Get all the menuSuggestionsList where customerId is greater than SMALLER_CUSTOMER_ID
        defaultMenuSuggestionsShouldBeFound("customerId.greaterThan=" + SMALLER_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByMenuGroupIdIsEqualToSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where menuGroupId equals to DEFAULT_MENU_GROUP_ID
        defaultMenuSuggestionsShouldBeFound("menuGroupId.equals=" + DEFAULT_MENU_GROUP_ID);

        // Get all the menuSuggestionsList where menuGroupId equals to UPDATED_MENU_GROUP_ID
        defaultMenuSuggestionsShouldNotBeFound("menuGroupId.equals=" + UPDATED_MENU_GROUP_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByMenuGroupIdIsInShouldWork() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where menuGroupId in DEFAULT_MENU_GROUP_ID or UPDATED_MENU_GROUP_ID
        defaultMenuSuggestionsShouldBeFound("menuGroupId.in=" + DEFAULT_MENU_GROUP_ID + "," + UPDATED_MENU_GROUP_ID);

        // Get all the menuSuggestionsList where menuGroupId equals to UPDATED_MENU_GROUP_ID
        defaultMenuSuggestionsShouldNotBeFound("menuGroupId.in=" + UPDATED_MENU_GROUP_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByMenuGroupIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where menuGroupId is not null
        defaultMenuSuggestionsShouldBeFound("menuGroupId.specified=true");

        // Get all the menuSuggestionsList where menuGroupId is null
        defaultMenuSuggestionsShouldNotBeFound("menuGroupId.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByMenuGroupIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where menuGroupId is greater than or equal to DEFAULT_MENU_GROUP_ID
        defaultMenuSuggestionsShouldBeFound("menuGroupId.greaterThanOrEqual=" + DEFAULT_MENU_GROUP_ID);

        // Get all the menuSuggestionsList where menuGroupId is greater than or equal to UPDATED_MENU_GROUP_ID
        defaultMenuSuggestionsShouldNotBeFound("menuGroupId.greaterThanOrEqual=" + UPDATED_MENU_GROUP_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByMenuGroupIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where menuGroupId is less than or equal to DEFAULT_MENU_GROUP_ID
        defaultMenuSuggestionsShouldBeFound("menuGroupId.lessThanOrEqual=" + DEFAULT_MENU_GROUP_ID);

        // Get all the menuSuggestionsList where menuGroupId is less than or equal to SMALLER_MENU_GROUP_ID
        defaultMenuSuggestionsShouldNotBeFound("menuGroupId.lessThanOrEqual=" + SMALLER_MENU_GROUP_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByMenuGroupIdIsLessThanSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where menuGroupId is less than DEFAULT_MENU_GROUP_ID
        defaultMenuSuggestionsShouldNotBeFound("menuGroupId.lessThan=" + DEFAULT_MENU_GROUP_ID);

        // Get all the menuSuggestionsList where menuGroupId is less than UPDATED_MENU_GROUP_ID
        defaultMenuSuggestionsShouldBeFound("menuGroupId.lessThan=" + UPDATED_MENU_GROUP_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByMenuGroupIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where menuGroupId is greater than DEFAULT_MENU_GROUP_ID
        defaultMenuSuggestionsShouldNotBeFound("menuGroupId.greaterThan=" + DEFAULT_MENU_GROUP_ID);

        // Get all the menuSuggestionsList where menuGroupId is greater than SMALLER_MENU_GROUP_ID
        defaultMenuSuggestionsShouldBeFound("menuGroupId.greaterThan=" + SMALLER_MENU_GROUP_ID);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where notes equals to DEFAULT_NOTES
        defaultMenuSuggestionsShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the menuSuggestionsList where notes equals to UPDATED_NOTES
        defaultMenuSuggestionsShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultMenuSuggestionsShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the menuSuggestionsList where notes equals to UPDATED_NOTES
        defaultMenuSuggestionsShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where notes is not null
        defaultMenuSuggestionsShouldBeFound("notes.specified=true");

        // Get all the menuSuggestionsList where notes is null
        defaultMenuSuggestionsShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNotesContainsSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where notes contains DEFAULT_NOTES
        defaultMenuSuggestionsShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the menuSuggestionsList where notes contains UPDATED_NOTES
        defaultMenuSuggestionsShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where notes does not contain DEFAULT_NOTES
        defaultMenuSuggestionsShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the menuSuggestionsList where notes does not contain UPDATED_NOTES
        defaultMenuSuggestionsShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where createdDate equals to DEFAULT_CREATED_DATE
        defaultMenuSuggestionsShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the menuSuggestionsList where createdDate equals to UPDATED_CREATED_DATE
        defaultMenuSuggestionsShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultMenuSuggestionsShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the menuSuggestionsList where createdDate equals to UPDATED_CREATED_DATE
        defaultMenuSuggestionsShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where createdDate is not null
        defaultMenuSuggestionsShouldBeFound("createdDate.specified=true");

        // Get all the menuSuggestionsList where createdDate is null
        defaultMenuSuggestionsShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultMenuSuggestionsShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the menuSuggestionsList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultMenuSuggestionsShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultMenuSuggestionsShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the menuSuggestionsList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultMenuSuggestionsShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where createdDate is less than DEFAULT_CREATED_DATE
        defaultMenuSuggestionsShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the menuSuggestionsList where createdDate is less than UPDATED_CREATED_DATE
        defaultMenuSuggestionsShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        // Get all the menuSuggestionsList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultMenuSuggestionsShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the menuSuggestionsList where createdDate is greater than SMALLER_CREATED_DATE
        defaultMenuSuggestionsShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMenuSuggestionsByConsultingsIsEqualToSomething() throws Exception {
        Consultings consultings;
        if (TestUtil.findAll(em, Consultings.class).isEmpty()) {
            menuSuggestionsRepository.saveAndFlush(menuSuggestions);
            consultings = ConsultingsResourceIT.createEntity(em);
        } else {
            consultings = TestUtil.findAll(em, Consultings.class).get(0);
        }
        em.persist(consultings);
        em.flush();
        menuSuggestions.addConsultings(consultings);
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);
        Long consultingsId = consultings.getId();

        // Get all the menuSuggestionsList where consultings equals to consultingsId
        defaultMenuSuggestionsShouldBeFound("consultingsId.equals=" + consultingsId);

        // Get all the menuSuggestionsList where consultings equals to (consultingsId + 1)
        defaultMenuSuggestionsShouldNotBeFound("consultingsId.equals=" + (consultingsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMenuSuggestionsShouldBeFound(String filter) throws Exception {
        restMenuSuggestionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuSuggestions.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nutritionistId").value(hasItem(DEFAULT_NUTRITIONIST_ID.intValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].menuGroupId").value(hasItem(DEFAULT_MENU_GROUP_ID)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restMenuSuggestionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMenuSuggestionsShouldNotBeFound(String filter) throws Exception {
        restMenuSuggestionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMenuSuggestionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMenuSuggestions() throws Exception {
        // Get the menuSuggestions
        restMenuSuggestionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMenuSuggestions() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        int databaseSizeBeforeUpdate = menuSuggestionsRepository.findAll().size();

        // Update the menuSuggestions
        MenuSuggestions updatedMenuSuggestions = menuSuggestionsRepository.findById(menuSuggestions.getId()).get();
        // Disconnect from session so that the updates on updatedMenuSuggestions are not directly saved in db
        em.detach(updatedMenuSuggestions);
        updatedMenuSuggestions
            .name(UPDATED_NAME)
            .nutritionistId(UPDATED_NUTRITIONIST_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .menuGroupId(UPDATED_MENU_GROUP_ID)
            .notes(UPDATED_NOTES)
            .createdDate(UPDATED_CREATED_DATE);
        MenuSuggestionsDTO menuSuggestionsDTO = menuSuggestionsMapper.toDto(updatedMenuSuggestions);

        restMenuSuggestionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuSuggestionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuSuggestionsDTO))
            )
            .andExpect(status().isOk());

        // Validate the MenuSuggestions in the database
        List<MenuSuggestions> menuSuggestionsList = menuSuggestionsRepository.findAll();
        assertThat(menuSuggestionsList).hasSize(databaseSizeBeforeUpdate);
        MenuSuggestions testMenuSuggestions = menuSuggestionsList.get(menuSuggestionsList.size() - 1);
        assertThat(testMenuSuggestions.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenuSuggestions.getNutritionistId()).isEqualTo(UPDATED_NUTRITIONIST_ID);
        assertThat(testMenuSuggestions.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testMenuSuggestions.getMenuGroupId()).isEqualTo(UPDATED_MENU_GROUP_ID);
        assertThat(testMenuSuggestions.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testMenuSuggestions.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingMenuSuggestions() throws Exception {
        int databaseSizeBeforeUpdate = menuSuggestionsRepository.findAll().size();
        menuSuggestions.setId(count.incrementAndGet());

        // Create the MenuSuggestions
        MenuSuggestionsDTO menuSuggestionsDTO = menuSuggestionsMapper.toDto(menuSuggestions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuSuggestionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuSuggestionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuSuggestionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuSuggestions in the database
        List<MenuSuggestions> menuSuggestionsList = menuSuggestionsRepository.findAll();
        assertThat(menuSuggestionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMenuSuggestions() throws Exception {
        int databaseSizeBeforeUpdate = menuSuggestionsRepository.findAll().size();
        menuSuggestions.setId(count.incrementAndGet());

        // Create the MenuSuggestions
        MenuSuggestionsDTO menuSuggestionsDTO = menuSuggestionsMapper.toDto(menuSuggestions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuSuggestionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuSuggestionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuSuggestions in the database
        List<MenuSuggestions> menuSuggestionsList = menuSuggestionsRepository.findAll();
        assertThat(menuSuggestionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMenuSuggestions() throws Exception {
        int databaseSizeBeforeUpdate = menuSuggestionsRepository.findAll().size();
        menuSuggestions.setId(count.incrementAndGet());

        // Create the MenuSuggestions
        MenuSuggestionsDTO menuSuggestionsDTO = menuSuggestionsMapper.toDto(menuSuggestions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuSuggestionsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuSuggestionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuSuggestions in the database
        List<MenuSuggestions> menuSuggestionsList = menuSuggestionsRepository.findAll();
        assertThat(menuSuggestionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMenuSuggestionsWithPatch() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        int databaseSizeBeforeUpdate = menuSuggestionsRepository.findAll().size();

        // Update the menuSuggestions using partial update
        MenuSuggestions partialUpdatedMenuSuggestions = new MenuSuggestions();
        partialUpdatedMenuSuggestions.setId(menuSuggestions.getId());

        partialUpdatedMenuSuggestions.name(UPDATED_NAME).menuGroupId(UPDATED_MENU_GROUP_ID).createdDate(UPDATED_CREATED_DATE);

        restMenuSuggestionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuSuggestions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenuSuggestions))
            )
            .andExpect(status().isOk());

        // Validate the MenuSuggestions in the database
        List<MenuSuggestions> menuSuggestionsList = menuSuggestionsRepository.findAll();
        assertThat(menuSuggestionsList).hasSize(databaseSizeBeforeUpdate);
        MenuSuggestions testMenuSuggestions = menuSuggestionsList.get(menuSuggestionsList.size() - 1);
        assertThat(testMenuSuggestions.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenuSuggestions.getNutritionistId()).isEqualTo(DEFAULT_NUTRITIONIST_ID);
        assertThat(testMenuSuggestions.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testMenuSuggestions.getMenuGroupId()).isEqualTo(UPDATED_MENU_GROUP_ID);
        assertThat(testMenuSuggestions.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testMenuSuggestions.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateMenuSuggestionsWithPatch() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        int databaseSizeBeforeUpdate = menuSuggestionsRepository.findAll().size();

        // Update the menuSuggestions using partial update
        MenuSuggestions partialUpdatedMenuSuggestions = new MenuSuggestions();
        partialUpdatedMenuSuggestions.setId(menuSuggestions.getId());

        partialUpdatedMenuSuggestions
            .name(UPDATED_NAME)
            .nutritionistId(UPDATED_NUTRITIONIST_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .menuGroupId(UPDATED_MENU_GROUP_ID)
            .notes(UPDATED_NOTES)
            .createdDate(UPDATED_CREATED_DATE);

        restMenuSuggestionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuSuggestions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenuSuggestions))
            )
            .andExpect(status().isOk());

        // Validate the MenuSuggestions in the database
        List<MenuSuggestions> menuSuggestionsList = menuSuggestionsRepository.findAll();
        assertThat(menuSuggestionsList).hasSize(databaseSizeBeforeUpdate);
        MenuSuggestions testMenuSuggestions = menuSuggestionsList.get(menuSuggestionsList.size() - 1);
        assertThat(testMenuSuggestions.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMenuSuggestions.getNutritionistId()).isEqualTo(UPDATED_NUTRITIONIST_ID);
        assertThat(testMenuSuggestions.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testMenuSuggestions.getMenuGroupId()).isEqualTo(UPDATED_MENU_GROUP_ID);
        assertThat(testMenuSuggestions.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testMenuSuggestions.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingMenuSuggestions() throws Exception {
        int databaseSizeBeforeUpdate = menuSuggestionsRepository.findAll().size();
        menuSuggestions.setId(count.incrementAndGet());

        // Create the MenuSuggestions
        MenuSuggestionsDTO menuSuggestionsDTO = menuSuggestionsMapper.toDto(menuSuggestions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuSuggestionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, menuSuggestionsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menuSuggestionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuSuggestions in the database
        List<MenuSuggestions> menuSuggestionsList = menuSuggestionsRepository.findAll();
        assertThat(menuSuggestionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMenuSuggestions() throws Exception {
        int databaseSizeBeforeUpdate = menuSuggestionsRepository.findAll().size();
        menuSuggestions.setId(count.incrementAndGet());

        // Create the MenuSuggestions
        MenuSuggestionsDTO menuSuggestionsDTO = menuSuggestionsMapper.toDto(menuSuggestions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuSuggestionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menuSuggestionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuSuggestions in the database
        List<MenuSuggestions> menuSuggestionsList = menuSuggestionsRepository.findAll();
        assertThat(menuSuggestionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMenuSuggestions() throws Exception {
        int databaseSizeBeforeUpdate = menuSuggestionsRepository.findAll().size();
        menuSuggestions.setId(count.incrementAndGet());

        // Create the MenuSuggestions
        MenuSuggestionsDTO menuSuggestionsDTO = menuSuggestionsMapper.toDto(menuSuggestions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuSuggestionsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menuSuggestionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuSuggestions in the database
        List<MenuSuggestions> menuSuggestionsList = menuSuggestionsRepository.findAll();
        assertThat(menuSuggestionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMenuSuggestions() throws Exception {
        // Initialize the database
        menuSuggestionsRepository.saveAndFlush(menuSuggestions);

        int databaseSizeBeforeDelete = menuSuggestionsRepository.findAll().size();

        // Delete the menuSuggestions
        restMenuSuggestionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, menuSuggestions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MenuSuggestions> menuSuggestionsList = menuSuggestionsRepository.findAll();
        assertThat(menuSuggestionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

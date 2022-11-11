package com.polarbears.capstone.hmsnutritionist.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polarbears.capstone.hmsnutritionist.IntegrationTest;
import com.polarbears.capstone.hmsnutritionist.domain.ConsultingStatus;
import com.polarbears.capstone.hmsnutritionist.domain.Consultings;
import com.polarbears.capstone.hmsnutritionist.domain.enumeration.STATUS;
import com.polarbears.capstone.hmsnutritionist.repository.ConsultingStatusRepository;
import com.polarbears.capstone.hmsnutritionist.service.criteria.ConsultingStatusCriteria;
import com.polarbears.capstone.hmsnutritionist.service.dto.ConsultingStatusDTO;
import com.polarbears.capstone.hmsnutritionist.service.mapper.ConsultingStatusMapper;
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
 * Integration tests for the {@link ConsultingStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsultingStatusResourceIT {

    private static final Integer DEFAULT_NUTRITIONIST_ID = 1;
    private static final Integer UPDATED_NUTRITIONIST_ID = 2;
    private static final Integer SMALLER_NUTRITIONIST_ID = 1 - 1;

    private static final STATUS DEFAULT_LAST_STATUS = STATUS.WAITING;
    private static final STATUS UPDATED_LAST_STATUS = STATUS.ACCEPTED;

    private static final String ENTITY_API_URL = "/api/consulting-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConsultingStatusRepository consultingStatusRepository;

    @Autowired
    private ConsultingStatusMapper consultingStatusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsultingStatusMockMvc;

    private ConsultingStatus consultingStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConsultingStatus createEntity(EntityManager em) {
        ConsultingStatus consultingStatus = new ConsultingStatus().nutritionistId(DEFAULT_NUTRITIONIST_ID).lastStatus(DEFAULT_LAST_STATUS);
        return consultingStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConsultingStatus createUpdatedEntity(EntityManager em) {
        ConsultingStatus consultingStatus = new ConsultingStatus().nutritionistId(UPDATED_NUTRITIONIST_ID).lastStatus(UPDATED_LAST_STATUS);
        return consultingStatus;
    }

    @BeforeEach
    public void initTest() {
        consultingStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createConsultingStatus() throws Exception {
        int databaseSizeBeforeCreate = consultingStatusRepository.findAll().size();
        // Create the ConsultingStatus
        ConsultingStatusDTO consultingStatusDTO = consultingStatusMapper.toDto(consultingStatus);
        restConsultingStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultingStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ConsultingStatus in the database
        List<ConsultingStatus> consultingStatusList = consultingStatusRepository.findAll();
        assertThat(consultingStatusList).hasSize(databaseSizeBeforeCreate + 1);
        ConsultingStatus testConsultingStatus = consultingStatusList.get(consultingStatusList.size() - 1);
        assertThat(testConsultingStatus.getNutritionistId()).isEqualTo(DEFAULT_NUTRITIONIST_ID);
        assertThat(testConsultingStatus.getLastStatus()).isEqualTo(DEFAULT_LAST_STATUS);
    }

    @Test
    @Transactional
    void createConsultingStatusWithExistingId() throws Exception {
        // Create the ConsultingStatus with an existing ID
        consultingStatus.setId(1L);
        ConsultingStatusDTO consultingStatusDTO = consultingStatusMapper.toDto(consultingStatus);

        int databaseSizeBeforeCreate = consultingStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsultingStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultingStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsultingStatus in the database
        List<ConsultingStatus> consultingStatusList = consultingStatusRepository.findAll();
        assertThat(consultingStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConsultingStatuses() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        // Get all the consultingStatusList
        restConsultingStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consultingStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].nutritionistId").value(hasItem(DEFAULT_NUTRITIONIST_ID)))
            .andExpect(jsonPath("$.[*].lastStatus").value(hasItem(DEFAULT_LAST_STATUS.toString())));
    }

    @Test
    @Transactional
    void getConsultingStatus() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        // Get the consultingStatus
        restConsultingStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, consultingStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consultingStatus.getId().intValue()))
            .andExpect(jsonPath("$.nutritionistId").value(DEFAULT_NUTRITIONIST_ID))
            .andExpect(jsonPath("$.lastStatus").value(DEFAULT_LAST_STATUS.toString()));
    }

    @Test
    @Transactional
    void getConsultingStatusesByIdFiltering() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        Long id = consultingStatus.getId();

        defaultConsultingStatusShouldBeFound("id.equals=" + id);
        defaultConsultingStatusShouldNotBeFound("id.notEquals=" + id);

        defaultConsultingStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConsultingStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultConsultingStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConsultingStatusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConsultingStatusesByNutritionistIdIsEqualToSomething() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        // Get all the consultingStatusList where nutritionistId equals to DEFAULT_NUTRITIONIST_ID
        defaultConsultingStatusShouldBeFound("nutritionistId.equals=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the consultingStatusList where nutritionistId equals to UPDATED_NUTRITIONIST_ID
        defaultConsultingStatusShouldNotBeFound("nutritionistId.equals=" + UPDATED_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllConsultingStatusesByNutritionistIdIsInShouldWork() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        // Get all the consultingStatusList where nutritionistId in DEFAULT_NUTRITIONIST_ID or UPDATED_NUTRITIONIST_ID
        defaultConsultingStatusShouldBeFound("nutritionistId.in=" + DEFAULT_NUTRITIONIST_ID + "," + UPDATED_NUTRITIONIST_ID);

        // Get all the consultingStatusList where nutritionistId equals to UPDATED_NUTRITIONIST_ID
        defaultConsultingStatusShouldNotBeFound("nutritionistId.in=" + UPDATED_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllConsultingStatusesByNutritionistIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        // Get all the consultingStatusList where nutritionistId is not null
        defaultConsultingStatusShouldBeFound("nutritionistId.specified=true");

        // Get all the consultingStatusList where nutritionistId is null
        defaultConsultingStatusShouldNotBeFound("nutritionistId.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultingStatusesByNutritionistIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        // Get all the consultingStatusList where nutritionistId is greater than or equal to DEFAULT_NUTRITIONIST_ID
        defaultConsultingStatusShouldBeFound("nutritionistId.greaterThanOrEqual=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the consultingStatusList where nutritionistId is greater than or equal to UPDATED_NUTRITIONIST_ID
        defaultConsultingStatusShouldNotBeFound("nutritionistId.greaterThanOrEqual=" + UPDATED_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllConsultingStatusesByNutritionistIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        // Get all the consultingStatusList where nutritionistId is less than or equal to DEFAULT_NUTRITIONIST_ID
        defaultConsultingStatusShouldBeFound("nutritionistId.lessThanOrEqual=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the consultingStatusList where nutritionistId is less than or equal to SMALLER_NUTRITIONIST_ID
        defaultConsultingStatusShouldNotBeFound("nutritionistId.lessThanOrEqual=" + SMALLER_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllConsultingStatusesByNutritionistIdIsLessThanSomething() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        // Get all the consultingStatusList where nutritionistId is less than DEFAULT_NUTRITIONIST_ID
        defaultConsultingStatusShouldNotBeFound("nutritionistId.lessThan=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the consultingStatusList where nutritionistId is less than UPDATED_NUTRITIONIST_ID
        defaultConsultingStatusShouldBeFound("nutritionistId.lessThan=" + UPDATED_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllConsultingStatusesByNutritionistIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        // Get all the consultingStatusList where nutritionistId is greater than DEFAULT_NUTRITIONIST_ID
        defaultConsultingStatusShouldNotBeFound("nutritionistId.greaterThan=" + DEFAULT_NUTRITIONIST_ID);

        // Get all the consultingStatusList where nutritionistId is greater than SMALLER_NUTRITIONIST_ID
        defaultConsultingStatusShouldBeFound("nutritionistId.greaterThan=" + SMALLER_NUTRITIONIST_ID);
    }

    @Test
    @Transactional
    void getAllConsultingStatusesByLastStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        // Get all the consultingStatusList where lastStatus equals to DEFAULT_LAST_STATUS
        defaultConsultingStatusShouldBeFound("lastStatus.equals=" + DEFAULT_LAST_STATUS);

        // Get all the consultingStatusList where lastStatus equals to UPDATED_LAST_STATUS
        defaultConsultingStatusShouldNotBeFound("lastStatus.equals=" + UPDATED_LAST_STATUS);
    }

    @Test
    @Transactional
    void getAllConsultingStatusesByLastStatusIsInShouldWork() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        // Get all the consultingStatusList where lastStatus in DEFAULT_LAST_STATUS or UPDATED_LAST_STATUS
        defaultConsultingStatusShouldBeFound("lastStatus.in=" + DEFAULT_LAST_STATUS + "," + UPDATED_LAST_STATUS);

        // Get all the consultingStatusList where lastStatus equals to UPDATED_LAST_STATUS
        defaultConsultingStatusShouldNotBeFound("lastStatus.in=" + UPDATED_LAST_STATUS);
    }

    @Test
    @Transactional
    void getAllConsultingStatusesByLastStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        // Get all the consultingStatusList where lastStatus is not null
        defaultConsultingStatusShouldBeFound("lastStatus.specified=true");

        // Get all the consultingStatusList where lastStatus is null
        defaultConsultingStatusShouldNotBeFound("lastStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultingStatusesByConsultingsIsEqualToSomething() throws Exception {
        Consultings consultings;
        if (TestUtil.findAll(em, Consultings.class).isEmpty()) {
            consultingStatusRepository.saveAndFlush(consultingStatus);
            consultings = ConsultingsResourceIT.createEntity(em);
        } else {
            consultings = TestUtil.findAll(em, Consultings.class).get(0);
        }
        em.persist(consultings);
        em.flush();
        consultingStatus.addConsultings(consultings);
        consultingStatusRepository.saveAndFlush(consultingStatus);
        Long consultingsId = consultings.getId();

        // Get all the consultingStatusList where consultings equals to consultingsId
        defaultConsultingStatusShouldBeFound("consultingsId.equals=" + consultingsId);

        // Get all the consultingStatusList where consultings equals to (consultingsId + 1)
        defaultConsultingStatusShouldNotBeFound("consultingsId.equals=" + (consultingsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConsultingStatusShouldBeFound(String filter) throws Exception {
        restConsultingStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consultingStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].nutritionistId").value(hasItem(DEFAULT_NUTRITIONIST_ID)))
            .andExpect(jsonPath("$.[*].lastStatus").value(hasItem(DEFAULT_LAST_STATUS.toString())));

        // Check, that the count call also returns 1
        restConsultingStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConsultingStatusShouldNotBeFound(String filter) throws Exception {
        restConsultingStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConsultingStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConsultingStatus() throws Exception {
        // Get the consultingStatus
        restConsultingStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConsultingStatus() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        int databaseSizeBeforeUpdate = consultingStatusRepository.findAll().size();

        // Update the consultingStatus
        ConsultingStatus updatedConsultingStatus = consultingStatusRepository.findById(consultingStatus.getId()).get();
        // Disconnect from session so that the updates on updatedConsultingStatus are not directly saved in db
        em.detach(updatedConsultingStatus);
        updatedConsultingStatus.nutritionistId(UPDATED_NUTRITIONIST_ID).lastStatus(UPDATED_LAST_STATUS);
        ConsultingStatusDTO consultingStatusDTO = consultingStatusMapper.toDto(updatedConsultingStatus);

        restConsultingStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consultingStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultingStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the ConsultingStatus in the database
        List<ConsultingStatus> consultingStatusList = consultingStatusRepository.findAll();
        assertThat(consultingStatusList).hasSize(databaseSizeBeforeUpdate);
        ConsultingStatus testConsultingStatus = consultingStatusList.get(consultingStatusList.size() - 1);
        assertThat(testConsultingStatus.getNutritionistId()).isEqualTo(UPDATED_NUTRITIONIST_ID);
        assertThat(testConsultingStatus.getLastStatus()).isEqualTo(UPDATED_LAST_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingConsultingStatus() throws Exception {
        int databaseSizeBeforeUpdate = consultingStatusRepository.findAll().size();
        consultingStatus.setId(count.incrementAndGet());

        // Create the ConsultingStatus
        ConsultingStatusDTO consultingStatusDTO = consultingStatusMapper.toDto(consultingStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultingStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consultingStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultingStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsultingStatus in the database
        List<ConsultingStatus> consultingStatusList = consultingStatusRepository.findAll();
        assertThat(consultingStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsultingStatus() throws Exception {
        int databaseSizeBeforeUpdate = consultingStatusRepository.findAll().size();
        consultingStatus.setId(count.incrementAndGet());

        // Create the ConsultingStatus
        ConsultingStatusDTO consultingStatusDTO = consultingStatusMapper.toDto(consultingStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultingStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultingStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsultingStatus in the database
        List<ConsultingStatus> consultingStatusList = consultingStatusRepository.findAll();
        assertThat(consultingStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsultingStatus() throws Exception {
        int databaseSizeBeforeUpdate = consultingStatusRepository.findAll().size();
        consultingStatus.setId(count.incrementAndGet());

        // Create the ConsultingStatus
        ConsultingStatusDTO consultingStatusDTO = consultingStatusMapper.toDto(consultingStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultingStatusMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultingStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConsultingStatus in the database
        List<ConsultingStatus> consultingStatusList = consultingStatusRepository.findAll();
        assertThat(consultingStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsultingStatusWithPatch() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        int databaseSizeBeforeUpdate = consultingStatusRepository.findAll().size();

        // Update the consultingStatus using partial update
        ConsultingStatus partialUpdatedConsultingStatus = new ConsultingStatus();
        partialUpdatedConsultingStatus.setId(consultingStatus.getId());

        partialUpdatedConsultingStatus.nutritionistId(UPDATED_NUTRITIONIST_ID).lastStatus(UPDATED_LAST_STATUS);

        restConsultingStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultingStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsultingStatus))
            )
            .andExpect(status().isOk());

        // Validate the ConsultingStatus in the database
        List<ConsultingStatus> consultingStatusList = consultingStatusRepository.findAll();
        assertThat(consultingStatusList).hasSize(databaseSizeBeforeUpdate);
        ConsultingStatus testConsultingStatus = consultingStatusList.get(consultingStatusList.size() - 1);
        assertThat(testConsultingStatus.getNutritionistId()).isEqualTo(UPDATED_NUTRITIONIST_ID);
        assertThat(testConsultingStatus.getLastStatus()).isEqualTo(UPDATED_LAST_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateConsultingStatusWithPatch() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        int databaseSizeBeforeUpdate = consultingStatusRepository.findAll().size();

        // Update the consultingStatus using partial update
        ConsultingStatus partialUpdatedConsultingStatus = new ConsultingStatus();
        partialUpdatedConsultingStatus.setId(consultingStatus.getId());

        partialUpdatedConsultingStatus.nutritionistId(UPDATED_NUTRITIONIST_ID).lastStatus(UPDATED_LAST_STATUS);

        restConsultingStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultingStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsultingStatus))
            )
            .andExpect(status().isOk());

        // Validate the ConsultingStatus in the database
        List<ConsultingStatus> consultingStatusList = consultingStatusRepository.findAll();
        assertThat(consultingStatusList).hasSize(databaseSizeBeforeUpdate);
        ConsultingStatus testConsultingStatus = consultingStatusList.get(consultingStatusList.size() - 1);
        assertThat(testConsultingStatus.getNutritionistId()).isEqualTo(UPDATED_NUTRITIONIST_ID);
        assertThat(testConsultingStatus.getLastStatus()).isEqualTo(UPDATED_LAST_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingConsultingStatus() throws Exception {
        int databaseSizeBeforeUpdate = consultingStatusRepository.findAll().size();
        consultingStatus.setId(count.incrementAndGet());

        // Create the ConsultingStatus
        ConsultingStatusDTO consultingStatusDTO = consultingStatusMapper.toDto(consultingStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultingStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consultingStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultingStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsultingStatus in the database
        List<ConsultingStatus> consultingStatusList = consultingStatusRepository.findAll();
        assertThat(consultingStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsultingStatus() throws Exception {
        int databaseSizeBeforeUpdate = consultingStatusRepository.findAll().size();
        consultingStatus.setId(count.incrementAndGet());

        // Create the ConsultingStatus
        ConsultingStatusDTO consultingStatusDTO = consultingStatusMapper.toDto(consultingStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultingStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultingStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsultingStatus in the database
        List<ConsultingStatus> consultingStatusList = consultingStatusRepository.findAll();
        assertThat(consultingStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsultingStatus() throws Exception {
        int databaseSizeBeforeUpdate = consultingStatusRepository.findAll().size();
        consultingStatus.setId(count.incrementAndGet());

        // Create the ConsultingStatus
        ConsultingStatusDTO consultingStatusDTO = consultingStatusMapper.toDto(consultingStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultingStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultingStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConsultingStatus in the database
        List<ConsultingStatus> consultingStatusList = consultingStatusRepository.findAll();
        assertThat(consultingStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsultingStatus() throws Exception {
        // Initialize the database
        consultingStatusRepository.saveAndFlush(consultingStatus);

        int databaseSizeBeforeDelete = consultingStatusRepository.findAll().size();

        // Delete the consultingStatus
        restConsultingStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, consultingStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConsultingStatus> consultingStatusList = consultingStatusRepository.findAll();
        assertThat(consultingStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

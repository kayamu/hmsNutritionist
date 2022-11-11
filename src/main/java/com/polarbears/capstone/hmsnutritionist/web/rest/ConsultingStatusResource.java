package com.polarbears.capstone.hmsnutritionist.web.rest;

import com.polarbears.capstone.hmsnutritionist.repository.ConsultingStatusRepository;
import com.polarbears.capstone.hmsnutritionist.service.ConsultingStatusQueryService;
import com.polarbears.capstone.hmsnutritionist.service.ConsultingStatusService;
import com.polarbears.capstone.hmsnutritionist.service.criteria.ConsultingStatusCriteria;
import com.polarbears.capstone.hmsnutritionist.service.dto.ConsultingStatusDTO;
import com.polarbears.capstone.hmsnutritionist.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.polarbears.capstone.hmsnutritionist.domain.ConsultingStatus}.
 */
@RestController
@RequestMapping("/api")
public class ConsultingStatusResource {

    private final Logger log = LoggerFactory.getLogger(ConsultingStatusResource.class);

    private static final String ENTITY_NAME = "hmsnutritionistConsultingStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsultingStatusService consultingStatusService;

    private final ConsultingStatusRepository consultingStatusRepository;

    private final ConsultingStatusQueryService consultingStatusQueryService;

    public ConsultingStatusResource(
        ConsultingStatusService consultingStatusService,
        ConsultingStatusRepository consultingStatusRepository,
        ConsultingStatusQueryService consultingStatusQueryService
    ) {
        this.consultingStatusService = consultingStatusService;
        this.consultingStatusRepository = consultingStatusRepository;
        this.consultingStatusQueryService = consultingStatusQueryService;
    }

    /**
     * {@code POST  /consulting-statuses} : Create a new consultingStatus.
     *
     * @param consultingStatusDTO the consultingStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consultingStatusDTO, or with status {@code 400 (Bad Request)} if the consultingStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/consulting-statuses")
    public ResponseEntity<ConsultingStatusDTO> createConsultingStatus(@RequestBody ConsultingStatusDTO consultingStatusDTO)
        throws URISyntaxException {
        log.debug("REST request to save ConsultingStatus : {}", consultingStatusDTO);
        if (consultingStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new consultingStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConsultingStatusDTO result = consultingStatusService.save(consultingStatusDTO);
        return ResponseEntity
            .created(new URI("/api/consulting-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /consulting-statuses/:id} : Updates an existing consultingStatus.
     *
     * @param id the id of the consultingStatusDTO to save.
     * @param consultingStatusDTO the consultingStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultingStatusDTO,
     * or with status {@code 400 (Bad Request)} if the consultingStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consultingStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/consulting-statuses/{id}")
    public ResponseEntity<ConsultingStatusDTO> updateConsultingStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConsultingStatusDTO consultingStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ConsultingStatus : {}, {}", id, consultingStatusDTO);
        if (consultingStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultingStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultingStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConsultingStatusDTO result = consultingStatusService.update(consultingStatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consultingStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /consulting-statuses/:id} : Partial updates given fields of an existing consultingStatus, field will ignore if it is null
     *
     * @param id the id of the consultingStatusDTO to save.
     * @param consultingStatusDTO the consultingStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultingStatusDTO,
     * or with status {@code 400 (Bad Request)} if the consultingStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the consultingStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the consultingStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/consulting-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConsultingStatusDTO> partialUpdateConsultingStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConsultingStatusDTO consultingStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ConsultingStatus partially : {}, {}", id, consultingStatusDTO);
        if (consultingStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultingStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultingStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConsultingStatusDTO> result = consultingStatusService.partialUpdate(consultingStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consultingStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /consulting-statuses} : get all the consultingStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of consultingStatuses in body.
     */
    @GetMapping("/consulting-statuses")
    public ResponseEntity<List<ConsultingStatusDTO>> getAllConsultingStatuses(ConsultingStatusCriteria criteria) {
        log.debug("REST request to get ConsultingStatuses by criteria: {}", criteria);
        List<ConsultingStatusDTO> entityList = consultingStatusQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /consulting-statuses/count} : count all the consultingStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/consulting-statuses/count")
    public ResponseEntity<Long> countConsultingStatuses(ConsultingStatusCriteria criteria) {
        log.debug("REST request to count ConsultingStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(consultingStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /consulting-statuses/:id} : get the "id" consultingStatus.
     *
     * @param id the id of the consultingStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consultingStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/consulting-statuses/{id}")
    public ResponseEntity<ConsultingStatusDTO> getConsultingStatus(@PathVariable Long id) {
        log.debug("REST request to get ConsultingStatus : {}", id);
        Optional<ConsultingStatusDTO> consultingStatusDTO = consultingStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consultingStatusDTO);
    }

    /**
     * {@code DELETE  /consulting-statuses/:id} : delete the "id" consultingStatus.
     *
     * @param id the id of the consultingStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/consulting-statuses/{id}")
    public ResponseEntity<Void> deleteConsultingStatus(@PathVariable Long id) {
        log.debug("REST request to delete ConsultingStatus : {}", id);
        consultingStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

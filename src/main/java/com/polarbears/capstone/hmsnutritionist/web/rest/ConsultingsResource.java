package com.polarbears.capstone.hmsnutritionist.web.rest;

import com.polarbears.capstone.hmsnutritionist.repository.ConsultingsRepository;
import com.polarbears.capstone.hmsnutritionist.service.ConsultingsQueryService;
import com.polarbears.capstone.hmsnutritionist.service.ConsultingsService;
import com.polarbears.capstone.hmsnutritionist.service.criteria.ConsultingsCriteria;
import com.polarbears.capstone.hmsnutritionist.service.dto.ConsultingsDTO;
import com.polarbears.capstone.hmsnutritionist.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.polarbears.capstone.hmsnutritionist.domain.Consultings}.
 */
@RestController
@RequestMapping("/api")
public class ConsultingsResource {

    private final Logger log = LoggerFactory.getLogger(ConsultingsResource.class);

    private static final String ENTITY_NAME = "hmsnutritionistConsultings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsultingsService consultingsService;

    private final ConsultingsRepository consultingsRepository;

    private final ConsultingsQueryService consultingsQueryService;

    public ConsultingsResource(
        ConsultingsService consultingsService,
        ConsultingsRepository consultingsRepository,
        ConsultingsQueryService consultingsQueryService
    ) {
        this.consultingsService = consultingsService;
        this.consultingsRepository = consultingsRepository;
        this.consultingsQueryService = consultingsQueryService;
    }

    /**
     * {@code POST  /consultings} : Create a new consultings.
     *
     * @param consultingsDTO the consultingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consultingsDTO, or with status {@code 400 (Bad Request)} if the consultings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/consultings")
    public ResponseEntity<ConsultingsDTO> createConsultings(@Valid @RequestBody ConsultingsDTO consultingsDTO) throws URISyntaxException {
        log.debug("REST request to save Consultings : {}", consultingsDTO);
        if (consultingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new consultings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConsultingsDTO result = consultingsService.save(consultingsDTO);
        return ResponseEntity
            .created(new URI("/api/consultings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /consultings/:id} : Updates an existing consultings.
     *
     * @param id the id of the consultingsDTO to save.
     * @param consultingsDTO the consultingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultingsDTO,
     * or with status {@code 400 (Bad Request)} if the consultingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consultingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/consultings/{id}")
    public ResponseEntity<ConsultingsDTO> updateConsultings(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConsultingsDTO consultingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Consultings : {}, {}", id, consultingsDTO);
        if (consultingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConsultingsDTO result = consultingsService.update(consultingsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consultingsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /consultings/:id} : Partial updates given fields of an existing consultings, field will ignore if it is null
     *
     * @param id the id of the consultingsDTO to save.
     * @param consultingsDTO the consultingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultingsDTO,
     * or with status {@code 400 (Bad Request)} if the consultingsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the consultingsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the consultingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/consultings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConsultingsDTO> partialUpdateConsultings(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConsultingsDTO consultingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Consultings partially : {}, {}", id, consultingsDTO);
        if (consultingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConsultingsDTO> result = consultingsService.partialUpdate(consultingsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consultingsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /consultings} : get all the consultings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of consultings in body.
     */
    @GetMapping("/consultings")
    public ResponseEntity<List<ConsultingsDTO>> getAllConsultings(ConsultingsCriteria criteria) {
        log.debug("REST request to get Consultings by criteria: {}", criteria);
        List<ConsultingsDTO> entityList = consultingsQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /consultings/count} : count all the consultings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/consultings/count")
    public ResponseEntity<Long> countConsultings(ConsultingsCriteria criteria) {
        log.debug("REST request to count Consultings by criteria: {}", criteria);
        return ResponseEntity.ok().body(consultingsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /consultings/:id} : get the "id" consultings.
     *
     * @param id the id of the consultingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consultingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/consultings/{id}")
    public ResponseEntity<ConsultingsDTO> getConsultings(@PathVariable Long id) {
        log.debug("REST request to get Consultings : {}", id);
        Optional<ConsultingsDTO> consultingsDTO = consultingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consultingsDTO);
    }

    /**
     * {@code DELETE  /consultings/:id} : delete the "id" consultings.
     *
     * @param id the id of the consultingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/consultings/{id}")
    public ResponseEntity<Void> deleteConsultings(@PathVariable Long id) {
        log.debug("REST request to delete Consultings : {}", id);
        consultingsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

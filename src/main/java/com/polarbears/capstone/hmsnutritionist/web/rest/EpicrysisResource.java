package com.polarbears.capstone.hmsnutritionist.web.rest;

import com.polarbears.capstone.hmsnutritionist.repository.EpicrysisRepository;
import com.polarbears.capstone.hmsnutritionist.service.EpicrysisQueryService;
import com.polarbears.capstone.hmsnutritionist.service.EpicrysisService;
import com.polarbears.capstone.hmsnutritionist.service.criteria.EpicrysisCriteria;
import com.polarbears.capstone.hmsnutritionist.service.dto.EpicrysisDTO;
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
 * REST controller for managing {@link com.polarbears.capstone.hmsnutritionist.domain.Epicrysis}.
 */
@RestController
@RequestMapping("/api")
public class EpicrysisResource {

    private final Logger log = LoggerFactory.getLogger(EpicrysisResource.class);

    private static final String ENTITY_NAME = "hmsnutritionistEpicrysis";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EpicrysisService epicrysisService;

    private final EpicrysisRepository epicrysisRepository;

    private final EpicrysisQueryService epicrysisQueryService;

    public EpicrysisResource(
        EpicrysisService epicrysisService,
        EpicrysisRepository epicrysisRepository,
        EpicrysisQueryService epicrysisQueryService
    ) {
        this.epicrysisService = epicrysisService;
        this.epicrysisRepository = epicrysisRepository;
        this.epicrysisQueryService = epicrysisQueryService;
    }

    /**
     * {@code POST  /epicryses} : Create a new epicrysis.
     *
     * @param epicrysisDTO the epicrysisDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new epicrysisDTO, or with status {@code 400 (Bad Request)} if the epicrysis has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/epicryses")
    public ResponseEntity<EpicrysisDTO> createEpicrysis(@Valid @RequestBody EpicrysisDTO epicrysisDTO) throws URISyntaxException {
        log.debug("REST request to save Epicrysis : {}", epicrysisDTO);
        if (epicrysisDTO.getId() != null) {
            throw new BadRequestAlertException("A new epicrysis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EpicrysisDTO result = epicrysisService.save(epicrysisDTO);
        return ResponseEntity
            .created(new URI("/api/epicryses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /epicryses/:id} : Updates an existing epicrysis.
     *
     * @param id the id of the epicrysisDTO to save.
     * @param epicrysisDTO the epicrysisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated epicrysisDTO,
     * or with status {@code 400 (Bad Request)} if the epicrysisDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the epicrysisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/epicryses/{id}")
    public ResponseEntity<EpicrysisDTO> updateEpicrysis(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EpicrysisDTO epicrysisDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Epicrysis : {}, {}", id, epicrysisDTO);
        if (epicrysisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, epicrysisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!epicrysisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EpicrysisDTO result = epicrysisService.update(epicrysisDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, epicrysisDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /epicryses/:id} : Partial updates given fields of an existing epicrysis, field will ignore if it is null
     *
     * @param id the id of the epicrysisDTO to save.
     * @param epicrysisDTO the epicrysisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated epicrysisDTO,
     * or with status {@code 400 (Bad Request)} if the epicrysisDTO is not valid,
     * or with status {@code 404 (Not Found)} if the epicrysisDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the epicrysisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/epicryses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EpicrysisDTO> partialUpdateEpicrysis(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EpicrysisDTO epicrysisDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Epicrysis partially : {}, {}", id, epicrysisDTO);
        if (epicrysisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, epicrysisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!epicrysisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EpicrysisDTO> result = epicrysisService.partialUpdate(epicrysisDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, epicrysisDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /epicryses} : get all the epicryses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of epicryses in body.
     */
    @GetMapping("/epicryses")
    public ResponseEntity<List<EpicrysisDTO>> getAllEpicryses(EpicrysisCriteria criteria) {
        log.debug("REST request to get Epicryses by criteria: {}", criteria);
        List<EpicrysisDTO> entityList = epicrysisQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /epicryses/count} : count all the epicryses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/epicryses/count")
    public ResponseEntity<Long> countEpicryses(EpicrysisCriteria criteria) {
        log.debug("REST request to count Epicryses by criteria: {}", criteria);
        return ResponseEntity.ok().body(epicrysisQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /epicryses/:id} : get the "id" epicrysis.
     *
     * @param id the id of the epicrysisDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the epicrysisDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/epicryses/{id}")
    public ResponseEntity<EpicrysisDTO> getEpicrysis(@PathVariable Long id) {
        log.debug("REST request to get Epicrysis : {}", id);
        Optional<EpicrysisDTO> epicrysisDTO = epicrysisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(epicrysisDTO);
    }

    /**
     * {@code DELETE  /epicryses/:id} : delete the "id" epicrysis.
     *
     * @param id the id of the epicrysisDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/epicryses/{id}")
    public ResponseEntity<Void> deleteEpicrysis(@PathVariable Long id) {
        log.debug("REST request to delete Epicrysis : {}", id);
        epicrysisService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

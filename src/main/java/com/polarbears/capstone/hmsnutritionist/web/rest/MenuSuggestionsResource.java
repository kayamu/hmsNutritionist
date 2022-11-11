package com.polarbears.capstone.hmsnutritionist.web.rest;

import com.polarbears.capstone.hmsnutritionist.repository.MenuSuggestionsRepository;
import com.polarbears.capstone.hmsnutritionist.service.MenuSuggestionsQueryService;
import com.polarbears.capstone.hmsnutritionist.service.MenuSuggestionsService;
import com.polarbears.capstone.hmsnutritionist.service.criteria.MenuSuggestionsCriteria;
import com.polarbears.capstone.hmsnutritionist.service.dto.MenuSuggestionsDTO;
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
 * REST controller for managing {@link com.polarbears.capstone.hmsnutritionist.domain.MenuSuggestions}.
 */
@RestController
@RequestMapping("/api")
public class MenuSuggestionsResource {

    private final Logger log = LoggerFactory.getLogger(MenuSuggestionsResource.class);

    private static final String ENTITY_NAME = "hmsnutritionistMenuSuggestions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenuSuggestionsService menuSuggestionsService;

    private final MenuSuggestionsRepository menuSuggestionsRepository;

    private final MenuSuggestionsQueryService menuSuggestionsQueryService;

    public MenuSuggestionsResource(
        MenuSuggestionsService menuSuggestionsService,
        MenuSuggestionsRepository menuSuggestionsRepository,
        MenuSuggestionsQueryService menuSuggestionsQueryService
    ) {
        this.menuSuggestionsService = menuSuggestionsService;
        this.menuSuggestionsRepository = menuSuggestionsRepository;
        this.menuSuggestionsQueryService = menuSuggestionsQueryService;
    }

    /**
     * {@code POST  /menu-suggestions} : Create a new menuSuggestions.
     *
     * @param menuSuggestionsDTO the menuSuggestionsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new menuSuggestionsDTO, or with status {@code 400 (Bad Request)} if the menuSuggestions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/menu-suggestions")
    public ResponseEntity<MenuSuggestionsDTO> createMenuSuggestions(@Valid @RequestBody MenuSuggestionsDTO menuSuggestionsDTO)
        throws URISyntaxException {
        log.debug("REST request to save MenuSuggestions : {}", menuSuggestionsDTO);
        if (menuSuggestionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new menuSuggestions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MenuSuggestionsDTO result = menuSuggestionsService.save(menuSuggestionsDTO);
        return ResponseEntity
            .created(new URI("/api/menu-suggestions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /menu-suggestions/:id} : Updates an existing menuSuggestions.
     *
     * @param id the id of the menuSuggestionsDTO to save.
     * @param menuSuggestionsDTO the menuSuggestionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuSuggestionsDTO,
     * or with status {@code 400 (Bad Request)} if the menuSuggestionsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the menuSuggestionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/menu-suggestions/{id}")
    public ResponseEntity<MenuSuggestionsDTO> updateMenuSuggestions(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MenuSuggestionsDTO menuSuggestionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MenuSuggestions : {}, {}", id, menuSuggestionsDTO);
        if (menuSuggestionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuSuggestionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuSuggestionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MenuSuggestionsDTO result = menuSuggestionsService.update(menuSuggestionsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuSuggestionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /menu-suggestions/:id} : Partial updates given fields of an existing menuSuggestions, field will ignore if it is null
     *
     * @param id the id of the menuSuggestionsDTO to save.
     * @param menuSuggestionsDTO the menuSuggestionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuSuggestionsDTO,
     * or with status {@code 400 (Bad Request)} if the menuSuggestionsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the menuSuggestionsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the menuSuggestionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/menu-suggestions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MenuSuggestionsDTO> partialUpdateMenuSuggestions(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MenuSuggestionsDTO menuSuggestionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MenuSuggestions partially : {}, {}", id, menuSuggestionsDTO);
        if (menuSuggestionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuSuggestionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuSuggestionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MenuSuggestionsDTO> result = menuSuggestionsService.partialUpdate(menuSuggestionsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuSuggestionsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /menu-suggestions} : get all the menuSuggestions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menuSuggestions in body.
     */
    @GetMapping("/menu-suggestions")
    public ResponseEntity<List<MenuSuggestionsDTO>> getAllMenuSuggestions(MenuSuggestionsCriteria criteria) {
        log.debug("REST request to get MenuSuggestions by criteria: {}", criteria);
        List<MenuSuggestionsDTO> entityList = menuSuggestionsQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /menu-suggestions/count} : count all the menuSuggestions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/menu-suggestions/count")
    public ResponseEntity<Long> countMenuSuggestions(MenuSuggestionsCriteria criteria) {
        log.debug("REST request to count MenuSuggestions by criteria: {}", criteria);
        return ResponseEntity.ok().body(menuSuggestionsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /menu-suggestions/:id} : get the "id" menuSuggestions.
     *
     * @param id the id of the menuSuggestionsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the menuSuggestionsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/menu-suggestions/{id}")
    public ResponseEntity<MenuSuggestionsDTO> getMenuSuggestions(@PathVariable Long id) {
        log.debug("REST request to get MenuSuggestions : {}", id);
        Optional<MenuSuggestionsDTO> menuSuggestionsDTO = menuSuggestionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(menuSuggestionsDTO);
    }

    /**
     * {@code DELETE  /menu-suggestions/:id} : delete the "id" menuSuggestions.
     *
     * @param id the id of the menuSuggestionsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/menu-suggestions/{id}")
    public ResponseEntity<Void> deleteMenuSuggestions(@PathVariable Long id) {
        log.debug("REST request to delete MenuSuggestions : {}", id);
        menuSuggestionsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

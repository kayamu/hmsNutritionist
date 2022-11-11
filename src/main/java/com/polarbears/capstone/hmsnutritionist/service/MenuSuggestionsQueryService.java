package com.polarbears.capstone.hmsnutritionist.service;

import com.polarbears.capstone.hmsnutritionist.domain.*; // for static metamodels
import com.polarbears.capstone.hmsnutritionist.domain.MenuSuggestions;
import com.polarbears.capstone.hmsnutritionist.repository.MenuSuggestionsRepository;
import com.polarbears.capstone.hmsnutritionist.service.criteria.MenuSuggestionsCriteria;
import com.polarbears.capstone.hmsnutritionist.service.dto.MenuSuggestionsDTO;
import com.polarbears.capstone.hmsnutritionist.service.mapper.MenuSuggestionsMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MenuSuggestions} entities in the database.
 * The main input is a {@link MenuSuggestionsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MenuSuggestionsDTO} or a {@link Page} of {@link MenuSuggestionsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MenuSuggestionsQueryService extends QueryService<MenuSuggestions> {

    private final Logger log = LoggerFactory.getLogger(MenuSuggestionsQueryService.class);

    private final MenuSuggestionsRepository menuSuggestionsRepository;

    private final MenuSuggestionsMapper menuSuggestionsMapper;

    public MenuSuggestionsQueryService(MenuSuggestionsRepository menuSuggestionsRepository, MenuSuggestionsMapper menuSuggestionsMapper) {
        this.menuSuggestionsRepository = menuSuggestionsRepository;
        this.menuSuggestionsMapper = menuSuggestionsMapper;
    }

    /**
     * Return a {@link List} of {@link MenuSuggestionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MenuSuggestionsDTO> findByCriteria(MenuSuggestionsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MenuSuggestions> specification = createSpecification(criteria);
        return menuSuggestionsMapper.toDto(menuSuggestionsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MenuSuggestionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MenuSuggestionsDTO> findByCriteria(MenuSuggestionsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MenuSuggestions> specification = createSpecification(criteria);
        return menuSuggestionsRepository.findAll(specification, page).map(menuSuggestionsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MenuSuggestionsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MenuSuggestions> specification = createSpecification(criteria);
        return menuSuggestionsRepository.count(specification);
    }

    /**
     * Function to convert {@link MenuSuggestionsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MenuSuggestions> createSpecification(MenuSuggestionsCriteria criteria) {
        Specification<MenuSuggestions> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MenuSuggestions_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MenuSuggestions_.name));
            }
            if (criteria.getNutritionistId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNutritionistId(), MenuSuggestions_.nutritionistId));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCustomerId(), MenuSuggestions_.customerId));
            }
            if (criteria.getMenuGroupId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMenuGroupId(), MenuSuggestions_.menuGroupId));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), MenuSuggestions_.notes));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), MenuSuggestions_.createdDate));
            }
            if (criteria.getConsultingsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConsultingsId(),
                            root -> root.join(MenuSuggestions_.consultings, JoinType.LEFT).get(Consultings_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

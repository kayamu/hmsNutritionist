package com.polarbears.capstone.hmsnutritionist.service;

import com.polarbears.capstone.hmsnutritionist.domain.*; // for static metamodels
import com.polarbears.capstone.hmsnutritionist.domain.Consultings;
import com.polarbears.capstone.hmsnutritionist.repository.ConsultingsRepository;
import com.polarbears.capstone.hmsnutritionist.service.criteria.ConsultingsCriteria;
import com.polarbears.capstone.hmsnutritionist.service.dto.ConsultingsDTO;
import com.polarbears.capstone.hmsnutritionist.service.mapper.ConsultingsMapper;
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
 * Service for executing complex queries for {@link Consultings} entities in the database.
 * The main input is a {@link ConsultingsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ConsultingsDTO} or a {@link Page} of {@link ConsultingsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConsultingsQueryService extends QueryService<Consultings> {

    private final Logger log = LoggerFactory.getLogger(ConsultingsQueryService.class);

    private final ConsultingsRepository consultingsRepository;

    private final ConsultingsMapper consultingsMapper;

    public ConsultingsQueryService(ConsultingsRepository consultingsRepository, ConsultingsMapper consultingsMapper) {
        this.consultingsRepository = consultingsRepository;
        this.consultingsMapper = consultingsMapper;
    }

    /**
     * Return a {@link List} of {@link ConsultingsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ConsultingsDTO> findByCriteria(ConsultingsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Consultings> specification = createSpecification(criteria);
        return consultingsMapper.toDto(consultingsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ConsultingsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConsultingsDTO> findByCriteria(ConsultingsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Consultings> specification = createSpecification(criteria);
        return consultingsRepository.findAll(specification, page).map(consultingsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConsultingsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Consultings> specification = createSpecification(criteria);
        return consultingsRepository.count(specification);
    }

    /**
     * Function to convert {@link ConsultingsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Consultings> createSpecification(ConsultingsCriteria criteria) {
        Specification<Consultings> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Consultings_.id));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCustomerId(), Consultings_.customerId));
            }
            if (criteria.getCustmerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustmerName(), Consultings_.custmerName));
            }
            if (criteria.getNutritionistId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNutritionistId(), Consultings_.nutritionistId));
            }
            if (criteria.getNutritionistName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNutritionistName(), Consultings_.nutritionistName));
            }
            if (criteria.getNutritionistNotes() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getNutritionistNotes(), Consultings_.nutritionistNotes));
            }
            if (criteria.getLastStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getLastStatus(), Consultings_.lastStatus));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Consultings_.createdDate));
            }
            if (criteria.getEpicrysisId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEpicrysisId(),
                            root -> root.join(Consultings_.epicryses, JoinType.LEFT).get(Epicrysis_.id)
                        )
                    );
            }
            if (criteria.getMenuSuggestionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMenuSuggestionsId(),
                            root -> root.join(Consultings_.menuSuggestions, JoinType.LEFT).get(MenuSuggestions_.id)
                        )
                    );
            }
            if (criteria.getConsultingStatusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConsultingStatusId(),
                            root -> root.join(Consultings_.consultingStatus, JoinType.LEFT).get(ConsultingStatus_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

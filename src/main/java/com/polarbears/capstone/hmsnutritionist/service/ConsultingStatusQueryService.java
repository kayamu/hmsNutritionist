package com.polarbears.capstone.hmsnutritionist.service;

import com.polarbears.capstone.hmsnutritionist.domain.*; // for static metamodels
import com.polarbears.capstone.hmsnutritionist.domain.ConsultingStatus;
import com.polarbears.capstone.hmsnutritionist.repository.ConsultingStatusRepository;
import com.polarbears.capstone.hmsnutritionist.service.criteria.ConsultingStatusCriteria;
import com.polarbears.capstone.hmsnutritionist.service.dto.ConsultingStatusDTO;
import com.polarbears.capstone.hmsnutritionist.service.mapper.ConsultingStatusMapper;
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
 * Service for executing complex queries for {@link ConsultingStatus} entities in the database.
 * The main input is a {@link ConsultingStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ConsultingStatusDTO} or a {@link Page} of {@link ConsultingStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConsultingStatusQueryService extends QueryService<ConsultingStatus> {

    private final Logger log = LoggerFactory.getLogger(ConsultingStatusQueryService.class);

    private final ConsultingStatusRepository consultingStatusRepository;

    private final ConsultingStatusMapper consultingStatusMapper;

    public ConsultingStatusQueryService(
        ConsultingStatusRepository consultingStatusRepository,
        ConsultingStatusMapper consultingStatusMapper
    ) {
        this.consultingStatusRepository = consultingStatusRepository;
        this.consultingStatusMapper = consultingStatusMapper;
    }

    /**
     * Return a {@link List} of {@link ConsultingStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ConsultingStatusDTO> findByCriteria(ConsultingStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ConsultingStatus> specification = createSpecification(criteria);
        return consultingStatusMapper.toDto(consultingStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ConsultingStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConsultingStatusDTO> findByCriteria(ConsultingStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ConsultingStatus> specification = createSpecification(criteria);
        return consultingStatusRepository.findAll(specification, page).map(consultingStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConsultingStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ConsultingStatus> specification = createSpecification(criteria);
        return consultingStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link ConsultingStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ConsultingStatus> createSpecification(ConsultingStatusCriteria criteria) {
        Specification<ConsultingStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ConsultingStatus_.id));
            }
            if (criteria.getNutritionistId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNutritionistId(), ConsultingStatus_.nutritionistId));
            }
            if (criteria.getLastStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getLastStatus(), ConsultingStatus_.lastStatus));
            }
            if (criteria.getConsultingsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConsultingsId(),
                            root -> root.join(ConsultingStatus_.consultings, JoinType.LEFT).get(Consultings_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

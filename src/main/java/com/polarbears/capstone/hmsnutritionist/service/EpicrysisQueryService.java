package com.polarbears.capstone.hmsnutritionist.service;

import com.polarbears.capstone.hmsnutritionist.domain.*; // for static metamodels
import com.polarbears.capstone.hmsnutritionist.domain.Epicrysis;
import com.polarbears.capstone.hmsnutritionist.repository.EpicrysisRepository;
import com.polarbears.capstone.hmsnutritionist.service.criteria.EpicrysisCriteria;
import com.polarbears.capstone.hmsnutritionist.service.dto.EpicrysisDTO;
import com.polarbears.capstone.hmsnutritionist.service.mapper.EpicrysisMapper;
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
 * Service for executing complex queries for {@link Epicrysis} entities in the database.
 * The main input is a {@link EpicrysisCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EpicrysisDTO} or a {@link Page} of {@link EpicrysisDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EpicrysisQueryService extends QueryService<Epicrysis> {

    private final Logger log = LoggerFactory.getLogger(EpicrysisQueryService.class);

    private final EpicrysisRepository epicrysisRepository;

    private final EpicrysisMapper epicrysisMapper;

    public EpicrysisQueryService(EpicrysisRepository epicrysisRepository, EpicrysisMapper epicrysisMapper) {
        this.epicrysisRepository = epicrysisRepository;
        this.epicrysisMapper = epicrysisMapper;
    }

    /**
     * Return a {@link List} of {@link EpicrysisDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EpicrysisDTO> findByCriteria(EpicrysisCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Epicrysis> specification = createSpecification(criteria);
        return epicrysisMapper.toDto(epicrysisRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EpicrysisDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EpicrysisDTO> findByCriteria(EpicrysisCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Epicrysis> specification = createSpecification(criteria);
        return epicrysisRepository.findAll(specification, page).map(epicrysisMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EpicrysisCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Epicrysis> specification = createSpecification(criteria);
        return epicrysisRepository.count(specification);
    }

    /**
     * Function to convert {@link EpicrysisCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Epicrysis> createSpecification(EpicrysisCriteria criteria) {
        Specification<Epicrysis> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Epicrysis_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Epicrysis_.name));
            }
            if (criteria.getNutritionistId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNutritionistId(), Epicrysis_.nutritionistId));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCustomerId(), Epicrysis_.customerId));
            }
            if (criteria.getCustomerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerName(), Epicrysis_.customerName));
            }
            if (criteria.getNutritionistNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNutritionistNotes(), Epicrysis_.nutritionistNotes));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Epicrysis_.createdDate));
            }
            if (criteria.getConsultingsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConsultingsId(),
                            root -> root.join(Epicrysis_.consultings, JoinType.LEFT).get(Consultings_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

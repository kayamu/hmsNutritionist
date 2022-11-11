package com.polarbears.capstone.hmsnutritionist.repository;

import com.polarbears.capstone.hmsnutritionist.domain.Consultings;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Consultings entity.
 *
 * When extending this class, extend ConsultingsRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ConsultingsRepository
    extends ConsultingsRepositoryWithBagRelationships, JpaRepository<Consultings, Long>, JpaSpecificationExecutor<Consultings> {
    default Optional<Consultings> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Consultings> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Consultings> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}

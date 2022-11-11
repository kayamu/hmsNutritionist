package com.polarbears.capstone.hmsnutritionist.repository;

import com.polarbears.capstone.hmsnutritionist.domain.Epicrysis;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Epicrysis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EpicrysisRepository extends JpaRepository<Epicrysis, Long>, JpaSpecificationExecutor<Epicrysis> {}

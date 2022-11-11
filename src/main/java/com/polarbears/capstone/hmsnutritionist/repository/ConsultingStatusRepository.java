package com.polarbears.capstone.hmsnutritionist.repository;

import com.polarbears.capstone.hmsnutritionist.domain.ConsultingStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConsultingStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsultingStatusRepository extends JpaRepository<ConsultingStatus, Long>, JpaSpecificationExecutor<ConsultingStatus> {}

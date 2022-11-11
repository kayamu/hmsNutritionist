package com.polarbears.capstone.hmsnutritionist.repository;

import com.polarbears.capstone.hmsnutritionist.domain.Consultings;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ConsultingsRepositoryWithBagRelationships {
    Optional<Consultings> fetchBagRelationships(Optional<Consultings> consultings);

    List<Consultings> fetchBagRelationships(List<Consultings> consultings);

    Page<Consultings> fetchBagRelationships(Page<Consultings> consultings);
}

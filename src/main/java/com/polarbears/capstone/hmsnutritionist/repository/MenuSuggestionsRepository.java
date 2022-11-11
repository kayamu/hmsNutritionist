package com.polarbears.capstone.hmsnutritionist.repository;

import com.polarbears.capstone.hmsnutritionist.domain.MenuSuggestions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MenuSuggestions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuSuggestionsRepository extends JpaRepository<MenuSuggestions, Long>, JpaSpecificationExecutor<MenuSuggestions> {}

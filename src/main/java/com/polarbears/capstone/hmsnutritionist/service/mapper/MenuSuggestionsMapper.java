package com.polarbears.capstone.hmsnutritionist.service.mapper;

import com.polarbears.capstone.hmsnutritionist.domain.MenuSuggestions;
import com.polarbears.capstone.hmsnutritionist.service.dto.MenuSuggestionsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MenuSuggestions} and its DTO {@link MenuSuggestionsDTO}.
 */
@Mapper(componentModel = "spring")
public interface MenuSuggestionsMapper extends EntityMapper<MenuSuggestionsDTO, MenuSuggestions> {}

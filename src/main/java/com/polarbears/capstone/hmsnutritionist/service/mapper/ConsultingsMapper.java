package com.polarbears.capstone.hmsnutritionist.service.mapper;

import com.polarbears.capstone.hmsnutritionist.domain.ConsultingStatus;
import com.polarbears.capstone.hmsnutritionist.domain.Consultings;
import com.polarbears.capstone.hmsnutritionist.domain.Epicrysis;
import com.polarbears.capstone.hmsnutritionist.domain.MenuSuggestions;
import com.polarbears.capstone.hmsnutritionist.service.dto.ConsultingStatusDTO;
import com.polarbears.capstone.hmsnutritionist.service.dto.ConsultingsDTO;
import com.polarbears.capstone.hmsnutritionist.service.dto.EpicrysisDTO;
import com.polarbears.capstone.hmsnutritionist.service.dto.MenuSuggestionsDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Consultings} and its DTO {@link ConsultingsDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConsultingsMapper extends EntityMapper<ConsultingsDTO, Consultings> {
    @Mapping(target = "epicryses", source = "epicryses", qualifiedByName = "epicrysisNameSet")
    @Mapping(target = "menuSuggestions", source = "menuSuggestions", qualifiedByName = "menuSuggestionsNameSet")
    @Mapping(target = "consultingStatus", source = "consultingStatus", qualifiedByName = "consultingStatusId")
    ConsultingsDTO toDto(Consultings s);

    @Mapping(target = "removeEpicrysis", ignore = true)
    @Mapping(target = "removeMenuSuggestions", ignore = true)
    Consultings toEntity(ConsultingsDTO consultingsDTO);

    @Named("epicrysisName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    EpicrysisDTO toDtoEpicrysisName(Epicrysis epicrysis);

    @Named("epicrysisNameSet")
    default Set<EpicrysisDTO> toDtoEpicrysisNameSet(Set<Epicrysis> epicrysis) {
        return epicrysis.stream().map(this::toDtoEpicrysisName).collect(Collectors.toSet());
    }

    @Named("menuSuggestionsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MenuSuggestionsDTO toDtoMenuSuggestionsName(MenuSuggestions menuSuggestions);

    @Named("menuSuggestionsNameSet")
    default Set<MenuSuggestionsDTO> toDtoMenuSuggestionsNameSet(Set<MenuSuggestions> menuSuggestions) {
        return menuSuggestions.stream().map(this::toDtoMenuSuggestionsName).collect(Collectors.toSet());
    }

    @Named("consultingStatusId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConsultingStatusDTO toDtoConsultingStatusId(ConsultingStatus consultingStatus);
}

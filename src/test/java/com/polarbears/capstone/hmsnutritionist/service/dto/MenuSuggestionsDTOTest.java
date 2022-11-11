package com.polarbears.capstone.hmsnutritionist.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsnutritionist.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MenuSuggestionsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuSuggestionsDTO.class);
        MenuSuggestionsDTO menuSuggestionsDTO1 = new MenuSuggestionsDTO();
        menuSuggestionsDTO1.setId(1L);
        MenuSuggestionsDTO menuSuggestionsDTO2 = new MenuSuggestionsDTO();
        assertThat(menuSuggestionsDTO1).isNotEqualTo(menuSuggestionsDTO2);
        menuSuggestionsDTO2.setId(menuSuggestionsDTO1.getId());
        assertThat(menuSuggestionsDTO1).isEqualTo(menuSuggestionsDTO2);
        menuSuggestionsDTO2.setId(2L);
        assertThat(menuSuggestionsDTO1).isNotEqualTo(menuSuggestionsDTO2);
        menuSuggestionsDTO1.setId(null);
        assertThat(menuSuggestionsDTO1).isNotEqualTo(menuSuggestionsDTO2);
    }
}

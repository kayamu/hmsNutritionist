package com.polarbears.capstone.hmsnutritionist.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsnutritionist.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MenuSuggestionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuSuggestions.class);
        MenuSuggestions menuSuggestions1 = new MenuSuggestions();
        menuSuggestions1.setId(1L);
        MenuSuggestions menuSuggestions2 = new MenuSuggestions();
        menuSuggestions2.setId(menuSuggestions1.getId());
        assertThat(menuSuggestions1).isEqualTo(menuSuggestions2);
        menuSuggestions2.setId(2L);
        assertThat(menuSuggestions1).isNotEqualTo(menuSuggestions2);
        menuSuggestions1.setId(null);
        assertThat(menuSuggestions1).isNotEqualTo(menuSuggestions2);
    }
}

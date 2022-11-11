package com.polarbears.capstone.hmsnutritionist.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MenuSuggestionsMapperTest {

    private MenuSuggestionsMapper menuSuggestionsMapper;

    @BeforeEach
    public void setUp() {
        menuSuggestionsMapper = new MenuSuggestionsMapperImpl();
    }
}

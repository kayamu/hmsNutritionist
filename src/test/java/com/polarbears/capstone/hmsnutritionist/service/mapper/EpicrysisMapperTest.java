package com.polarbears.capstone.hmsnutritionist.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicrysisMapperTest {

    private EpicrysisMapper epicrysisMapper;

    @BeforeEach
    public void setUp() {
        epicrysisMapper = new EpicrysisMapperImpl();
    }
}

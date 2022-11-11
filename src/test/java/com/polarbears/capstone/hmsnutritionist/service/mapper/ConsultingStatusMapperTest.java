package com.polarbears.capstone.hmsnutritionist.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsultingStatusMapperTest {

    private ConsultingStatusMapper consultingStatusMapper;

    @BeforeEach
    public void setUp() {
        consultingStatusMapper = new ConsultingStatusMapperImpl();
    }
}

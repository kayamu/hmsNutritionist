package com.polarbears.capstone.hmsnutritionist.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsnutritionist.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsultingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Consultings.class);
        Consultings consultings1 = new Consultings();
        consultings1.setId(1L);
        Consultings consultings2 = new Consultings();
        consultings2.setId(consultings1.getId());
        assertThat(consultings1).isEqualTo(consultings2);
        consultings2.setId(2L);
        assertThat(consultings1).isNotEqualTo(consultings2);
        consultings1.setId(null);
        assertThat(consultings1).isNotEqualTo(consultings2);
    }
}

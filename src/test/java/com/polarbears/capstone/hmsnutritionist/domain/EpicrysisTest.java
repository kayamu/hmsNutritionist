package com.polarbears.capstone.hmsnutritionist.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsnutritionist.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EpicrysisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Epicrysis.class);
        Epicrysis epicrysis1 = new Epicrysis();
        epicrysis1.setId(1L);
        Epicrysis epicrysis2 = new Epicrysis();
        epicrysis2.setId(epicrysis1.getId());
        assertThat(epicrysis1).isEqualTo(epicrysis2);
        epicrysis2.setId(2L);
        assertThat(epicrysis1).isNotEqualTo(epicrysis2);
        epicrysis1.setId(null);
        assertThat(epicrysis1).isNotEqualTo(epicrysis2);
    }
}

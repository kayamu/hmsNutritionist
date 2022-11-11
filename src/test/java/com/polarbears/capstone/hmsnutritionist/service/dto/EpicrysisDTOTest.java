package com.polarbears.capstone.hmsnutritionist.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsnutritionist.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EpicrysisDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EpicrysisDTO.class);
        EpicrysisDTO epicrysisDTO1 = new EpicrysisDTO();
        epicrysisDTO1.setId(1L);
        EpicrysisDTO epicrysisDTO2 = new EpicrysisDTO();
        assertThat(epicrysisDTO1).isNotEqualTo(epicrysisDTO2);
        epicrysisDTO2.setId(epicrysisDTO1.getId());
        assertThat(epicrysisDTO1).isEqualTo(epicrysisDTO2);
        epicrysisDTO2.setId(2L);
        assertThat(epicrysisDTO1).isNotEqualTo(epicrysisDTO2);
        epicrysisDTO1.setId(null);
        assertThat(epicrysisDTO1).isNotEqualTo(epicrysisDTO2);
    }
}

package com.polarbears.capstone.hmsnutritionist.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsnutritionist.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsultingsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsultingsDTO.class);
        ConsultingsDTO consultingsDTO1 = new ConsultingsDTO();
        consultingsDTO1.setId(1L);
        ConsultingsDTO consultingsDTO2 = new ConsultingsDTO();
        assertThat(consultingsDTO1).isNotEqualTo(consultingsDTO2);
        consultingsDTO2.setId(consultingsDTO1.getId());
        assertThat(consultingsDTO1).isEqualTo(consultingsDTO2);
        consultingsDTO2.setId(2L);
        assertThat(consultingsDTO1).isNotEqualTo(consultingsDTO2);
        consultingsDTO1.setId(null);
        assertThat(consultingsDTO1).isNotEqualTo(consultingsDTO2);
    }
}

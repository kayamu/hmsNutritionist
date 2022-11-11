package com.polarbears.capstone.hmsnutritionist.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsnutritionist.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsultingStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsultingStatusDTO.class);
        ConsultingStatusDTO consultingStatusDTO1 = new ConsultingStatusDTO();
        consultingStatusDTO1.setId(1L);
        ConsultingStatusDTO consultingStatusDTO2 = new ConsultingStatusDTO();
        assertThat(consultingStatusDTO1).isNotEqualTo(consultingStatusDTO2);
        consultingStatusDTO2.setId(consultingStatusDTO1.getId());
        assertThat(consultingStatusDTO1).isEqualTo(consultingStatusDTO2);
        consultingStatusDTO2.setId(2L);
        assertThat(consultingStatusDTO1).isNotEqualTo(consultingStatusDTO2);
        consultingStatusDTO1.setId(null);
        assertThat(consultingStatusDTO1).isNotEqualTo(consultingStatusDTO2);
    }
}

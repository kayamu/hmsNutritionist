package com.polarbears.capstone.hmsnutritionist.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmsnutritionist.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsultingStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsultingStatus.class);
        ConsultingStatus consultingStatus1 = new ConsultingStatus();
        consultingStatus1.setId(1L);
        ConsultingStatus consultingStatus2 = new ConsultingStatus();
        consultingStatus2.setId(consultingStatus1.getId());
        assertThat(consultingStatus1).isEqualTo(consultingStatus2);
        consultingStatus2.setId(2L);
        assertThat(consultingStatus1).isNotEqualTo(consultingStatus2);
        consultingStatus1.setId(null);
        assertThat(consultingStatus1).isNotEqualTo(consultingStatus2);
    }
}

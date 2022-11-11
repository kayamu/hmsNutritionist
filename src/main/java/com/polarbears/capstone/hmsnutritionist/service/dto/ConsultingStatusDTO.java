package com.polarbears.capstone.hmsnutritionist.service.dto;

import com.polarbears.capstone.hmsnutritionist.domain.enumeration.STATUS;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.polarbears.capstone.hmsnutritionist.domain.ConsultingStatus} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsultingStatusDTO implements Serializable {

    private Long id;

    private Integer nutritionistId;

    private STATUS lastStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNutritionistId() {
        return nutritionistId;
    }

    public void setNutritionistId(Integer nutritionistId) {
        this.nutritionistId = nutritionistId;
    }

    public STATUS getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(STATUS lastStatus) {
        this.lastStatus = lastStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsultingStatusDTO)) {
            return false;
        }

        ConsultingStatusDTO consultingStatusDTO = (ConsultingStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, consultingStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsultingStatusDTO{" +
            "id=" + getId() +
            ", nutritionistId=" + getNutritionistId() +
            ", lastStatus='" + getLastStatus() + "'" +
            "}";
    }
}

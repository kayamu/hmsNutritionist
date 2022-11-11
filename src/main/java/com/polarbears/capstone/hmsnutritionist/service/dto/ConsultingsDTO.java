package com.polarbears.capstone.hmsnutritionist.service.dto;

import com.polarbears.capstone.hmsnutritionist.domain.enumeration.STATUS;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.polarbears.capstone.hmsnutritionist.domain.Consultings} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsultingsDTO implements Serializable {

    private Long id;

    private Long customerId;

    private String custmerName;

    private Long nutritionistId;

    private String nutritionistName;

    @Size(max = 1024)
    private String nutritionistNotes;

    private STATUS lastStatus;

    private LocalDate createdDate;

    private Set<EpicrysisDTO> epicryses = new HashSet<>();

    private Set<MenuSuggestionsDTO> menuSuggestions = new HashSet<>();

    private ConsultingStatusDTO consultingStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustmerName() {
        return custmerName;
    }

    public void setCustmerName(String custmerName) {
        this.custmerName = custmerName;
    }

    public Long getNutritionistId() {
        return nutritionistId;
    }

    public void setNutritionistId(Long nutritionistId) {
        this.nutritionistId = nutritionistId;
    }

    public String getNutritionistName() {
        return nutritionistName;
    }

    public void setNutritionistName(String nutritionistName) {
        this.nutritionistName = nutritionistName;
    }

    public String getNutritionistNotes() {
        return nutritionistNotes;
    }

    public void setNutritionistNotes(String nutritionistNotes) {
        this.nutritionistNotes = nutritionistNotes;
    }

    public STATUS getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(STATUS lastStatus) {
        this.lastStatus = lastStatus;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<EpicrysisDTO> getEpicryses() {
        return epicryses;
    }

    public void setEpicryses(Set<EpicrysisDTO> epicryses) {
        this.epicryses = epicryses;
    }

    public Set<MenuSuggestionsDTO> getMenuSuggestions() {
        return menuSuggestions;
    }

    public void setMenuSuggestions(Set<MenuSuggestionsDTO> menuSuggestions) {
        this.menuSuggestions = menuSuggestions;
    }

    public ConsultingStatusDTO getConsultingStatus() {
        return consultingStatus;
    }

    public void setConsultingStatus(ConsultingStatusDTO consultingStatus) {
        this.consultingStatus = consultingStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsultingsDTO)) {
            return false;
        }

        ConsultingsDTO consultingsDTO = (ConsultingsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, consultingsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsultingsDTO{" +
            "id=" + getId() +
            ", customerId=" + getCustomerId() +
            ", custmerName='" + getCustmerName() + "'" +
            ", nutritionistId=" + getNutritionistId() +
            ", nutritionistName='" + getNutritionistName() + "'" +
            ", nutritionistNotes='" + getNutritionistNotes() + "'" +
            ", lastStatus='" + getLastStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", epicryses=" + getEpicryses() +
            ", menuSuggestions=" + getMenuSuggestions() +
            ", consultingStatus=" + getConsultingStatus() +
            "}";
    }
}

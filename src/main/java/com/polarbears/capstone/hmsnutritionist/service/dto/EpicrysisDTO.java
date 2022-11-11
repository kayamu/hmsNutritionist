package com.polarbears.capstone.hmsnutritionist.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.polarbears.capstone.hmsnutritionist.domain.Epicrysis} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EpicrysisDTO implements Serializable {

    private Long id;

    private String name;

    private Long nutritionistId;

    private Long customerId;

    private String customerName;

    @Size(max = 1024)
    private String nutritionistNotes;

    private LocalDate createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNutritionistId() {
        return nutritionistId;
    }

    public void setNutritionistId(Long nutritionistId) {
        this.nutritionistId = nutritionistId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getNutritionistNotes() {
        return nutritionistNotes;
    }

    public void setNutritionistNotes(String nutritionistNotes) {
        this.nutritionistNotes = nutritionistNotes;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EpicrysisDTO)) {
            return false;
        }

        EpicrysisDTO epicrysisDTO = (EpicrysisDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, epicrysisDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EpicrysisDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nutritionistId=" + getNutritionistId() +
            ", customerId=" + getCustomerId() +
            ", customerName='" + getCustomerName() + "'" +
            ", nutritionistNotes='" + getNutritionistNotes() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}

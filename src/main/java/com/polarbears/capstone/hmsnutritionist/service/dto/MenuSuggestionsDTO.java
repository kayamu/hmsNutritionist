package com.polarbears.capstone.hmsnutritionist.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.polarbears.capstone.hmsnutritionist.domain.MenuSuggestions} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenuSuggestionsDTO implements Serializable {

    private Long id;

    private String name;

    private Long nutritionistId;

    private Long customerId;

    private Integer menuGroupId;

    @Size(max = 1024)
    private String notes;

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

    public Integer getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(Integer menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
        if (!(o instanceof MenuSuggestionsDTO)) {
            return false;
        }

        MenuSuggestionsDTO menuSuggestionsDTO = (MenuSuggestionsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, menuSuggestionsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuSuggestionsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nutritionistId=" + getNutritionistId() +
            ", customerId=" + getCustomerId() +
            ", menuGroupId=" + getMenuGroupId() +
            ", notes='" + getNotes() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}

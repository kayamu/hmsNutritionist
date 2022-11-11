package com.polarbears.capstone.hmsnutritionist.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.polarbears.capstone.hmsnutritionist.domain.MenuSuggestions} entity. This class is used
 * in {@link com.polarbears.capstone.hmsnutritionist.web.rest.MenuSuggestionsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /menu-suggestions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenuSuggestionsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter nutritionistId;

    private LongFilter customerId;

    private IntegerFilter menuGroupId;

    private StringFilter notes;

    private LocalDateFilter createdDate;

    private LongFilter consultingsId;

    private Boolean distinct;

    public MenuSuggestionsCriteria() {}

    public MenuSuggestionsCriteria(MenuSuggestionsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.nutritionistId = other.nutritionistId == null ? null : other.nutritionistId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.menuGroupId = other.menuGroupId == null ? null : other.menuGroupId.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.consultingsId = other.consultingsId == null ? null : other.consultingsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MenuSuggestionsCriteria copy() {
        return new MenuSuggestionsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getNutritionistId() {
        return nutritionistId;
    }

    public LongFilter nutritionistId() {
        if (nutritionistId == null) {
            nutritionistId = new LongFilter();
        }
        return nutritionistId;
    }

    public void setNutritionistId(LongFilter nutritionistId) {
        this.nutritionistId = nutritionistId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public IntegerFilter getMenuGroupId() {
        return menuGroupId;
    }

    public IntegerFilter menuGroupId() {
        if (menuGroupId == null) {
            menuGroupId = new IntegerFilter();
        }
        return menuGroupId;
    }

    public void setMenuGroupId(IntegerFilter menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public LocalDateFilter getCreatedDate() {
        return createdDate;
    }

    public LocalDateFilter createdDate() {
        if (createdDate == null) {
            createdDate = new LocalDateFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(LocalDateFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getConsultingsId() {
        return consultingsId;
    }

    public LongFilter consultingsId() {
        if (consultingsId == null) {
            consultingsId = new LongFilter();
        }
        return consultingsId;
    }

    public void setConsultingsId(LongFilter consultingsId) {
        this.consultingsId = consultingsId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MenuSuggestionsCriteria that = (MenuSuggestionsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(nutritionistId, that.nutritionistId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(menuGroupId, that.menuGroupId) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(consultingsId, that.consultingsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nutritionistId, customerId, menuGroupId, notes, createdDate, consultingsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuSuggestionsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (nutritionistId != null ? "nutritionistId=" + nutritionistId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (menuGroupId != null ? "menuGroupId=" + menuGroupId + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (consultingsId != null ? "consultingsId=" + consultingsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

package com.polarbears.capstone.hmsnutritionist.service.criteria;

import com.polarbears.capstone.hmsnutritionist.domain.enumeration.STATUS;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.polarbears.capstone.hmsnutritionist.domain.Consultings} entity. This class is used
 * in {@link com.polarbears.capstone.hmsnutritionist.web.rest.ConsultingsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /consultings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsultingsCriteria implements Serializable, Criteria {

    /**
     * Class for filtering STATUS
     */
    public static class STATUSFilter extends Filter<STATUS> {

        public STATUSFilter() {}

        public STATUSFilter(STATUSFilter filter) {
            super(filter);
        }

        @Override
        public STATUSFilter copy() {
            return new STATUSFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter customerId;

    private StringFilter custmerName;

    private LongFilter nutritionistId;

    private StringFilter nutritionistName;

    private StringFilter nutritionistNotes;

    private STATUSFilter lastStatus;

    private LocalDateFilter createdDate;

    private LongFilter epicrysisId;

    private LongFilter menuSuggestionsId;

    private LongFilter consultingStatusId;

    private Boolean distinct;

    public ConsultingsCriteria() {}

    public ConsultingsCriteria(ConsultingsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.custmerName = other.custmerName == null ? null : other.custmerName.copy();
        this.nutritionistId = other.nutritionistId == null ? null : other.nutritionistId.copy();
        this.nutritionistName = other.nutritionistName == null ? null : other.nutritionistName.copy();
        this.nutritionistNotes = other.nutritionistNotes == null ? null : other.nutritionistNotes.copy();
        this.lastStatus = other.lastStatus == null ? null : other.lastStatus.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.epicrysisId = other.epicrysisId == null ? null : other.epicrysisId.copy();
        this.menuSuggestionsId = other.menuSuggestionsId == null ? null : other.menuSuggestionsId.copy();
        this.consultingStatusId = other.consultingStatusId == null ? null : other.consultingStatusId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ConsultingsCriteria copy() {
        return new ConsultingsCriteria(this);
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

    public StringFilter getCustmerName() {
        return custmerName;
    }

    public StringFilter custmerName() {
        if (custmerName == null) {
            custmerName = new StringFilter();
        }
        return custmerName;
    }

    public void setCustmerName(StringFilter custmerName) {
        this.custmerName = custmerName;
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

    public StringFilter getNutritionistName() {
        return nutritionistName;
    }

    public StringFilter nutritionistName() {
        if (nutritionistName == null) {
            nutritionistName = new StringFilter();
        }
        return nutritionistName;
    }

    public void setNutritionistName(StringFilter nutritionistName) {
        this.nutritionistName = nutritionistName;
    }

    public StringFilter getNutritionistNotes() {
        return nutritionistNotes;
    }

    public StringFilter nutritionistNotes() {
        if (nutritionistNotes == null) {
            nutritionistNotes = new StringFilter();
        }
        return nutritionistNotes;
    }

    public void setNutritionistNotes(StringFilter nutritionistNotes) {
        this.nutritionistNotes = nutritionistNotes;
    }

    public STATUSFilter getLastStatus() {
        return lastStatus;
    }

    public STATUSFilter lastStatus() {
        if (lastStatus == null) {
            lastStatus = new STATUSFilter();
        }
        return lastStatus;
    }

    public void setLastStatus(STATUSFilter lastStatus) {
        this.lastStatus = lastStatus;
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

    public LongFilter getEpicrysisId() {
        return epicrysisId;
    }

    public LongFilter epicrysisId() {
        if (epicrysisId == null) {
            epicrysisId = new LongFilter();
        }
        return epicrysisId;
    }

    public void setEpicrysisId(LongFilter epicrysisId) {
        this.epicrysisId = epicrysisId;
    }

    public LongFilter getMenuSuggestionsId() {
        return menuSuggestionsId;
    }

    public LongFilter menuSuggestionsId() {
        if (menuSuggestionsId == null) {
            menuSuggestionsId = new LongFilter();
        }
        return menuSuggestionsId;
    }

    public void setMenuSuggestionsId(LongFilter menuSuggestionsId) {
        this.menuSuggestionsId = menuSuggestionsId;
    }

    public LongFilter getConsultingStatusId() {
        return consultingStatusId;
    }

    public LongFilter consultingStatusId() {
        if (consultingStatusId == null) {
            consultingStatusId = new LongFilter();
        }
        return consultingStatusId;
    }

    public void setConsultingStatusId(LongFilter consultingStatusId) {
        this.consultingStatusId = consultingStatusId;
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
        final ConsultingsCriteria that = (ConsultingsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(custmerName, that.custmerName) &&
            Objects.equals(nutritionistId, that.nutritionistId) &&
            Objects.equals(nutritionistName, that.nutritionistName) &&
            Objects.equals(nutritionistNotes, that.nutritionistNotes) &&
            Objects.equals(lastStatus, that.lastStatus) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(epicrysisId, that.epicrysisId) &&
            Objects.equals(menuSuggestionsId, that.menuSuggestionsId) &&
            Objects.equals(consultingStatusId, that.consultingStatusId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            customerId,
            custmerName,
            nutritionistId,
            nutritionistName,
            nutritionistNotes,
            lastStatus,
            createdDate,
            epicrysisId,
            menuSuggestionsId,
            consultingStatusId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsultingsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (custmerName != null ? "custmerName=" + custmerName + ", " : "") +
            (nutritionistId != null ? "nutritionistId=" + nutritionistId + ", " : "") +
            (nutritionistName != null ? "nutritionistName=" + nutritionistName + ", " : "") +
            (nutritionistNotes != null ? "nutritionistNotes=" + nutritionistNotes + ", " : "") +
            (lastStatus != null ? "lastStatus=" + lastStatus + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (epicrysisId != null ? "epicrysisId=" + epicrysisId + ", " : "") +
            (menuSuggestionsId != null ? "menuSuggestionsId=" + menuSuggestionsId + ", " : "") +
            (consultingStatusId != null ? "consultingStatusId=" + consultingStatusId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

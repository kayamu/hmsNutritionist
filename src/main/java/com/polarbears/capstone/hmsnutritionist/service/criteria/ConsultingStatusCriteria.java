package com.polarbears.capstone.hmsnutritionist.service.criteria;

import com.polarbears.capstone.hmsnutritionist.domain.enumeration.STATUS;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.polarbears.capstone.hmsnutritionist.domain.ConsultingStatus} entity. This class is used
 * in {@link com.polarbears.capstone.hmsnutritionist.web.rest.ConsultingStatusResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /consulting-statuses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsultingStatusCriteria implements Serializable, Criteria {

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

    private IntegerFilter nutritionistId;

    private STATUSFilter lastStatus;

    private LongFilter consultingsId;

    private Boolean distinct;

    public ConsultingStatusCriteria() {}

    public ConsultingStatusCriteria(ConsultingStatusCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nutritionistId = other.nutritionistId == null ? null : other.nutritionistId.copy();
        this.lastStatus = other.lastStatus == null ? null : other.lastStatus.copy();
        this.consultingsId = other.consultingsId == null ? null : other.consultingsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ConsultingStatusCriteria copy() {
        return new ConsultingStatusCriteria(this);
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

    public IntegerFilter getNutritionistId() {
        return nutritionistId;
    }

    public IntegerFilter nutritionistId() {
        if (nutritionistId == null) {
            nutritionistId = new IntegerFilter();
        }
        return nutritionistId;
    }

    public void setNutritionistId(IntegerFilter nutritionistId) {
        this.nutritionistId = nutritionistId;
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
        final ConsultingStatusCriteria that = (ConsultingStatusCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nutritionistId, that.nutritionistId) &&
            Objects.equals(lastStatus, that.lastStatus) &&
            Objects.equals(consultingsId, that.consultingsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nutritionistId, lastStatus, consultingsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsultingStatusCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nutritionistId != null ? "nutritionistId=" + nutritionistId + ", " : "") +
            (lastStatus != null ? "lastStatus=" + lastStatus + ", " : "") +
            (consultingsId != null ? "consultingsId=" + consultingsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

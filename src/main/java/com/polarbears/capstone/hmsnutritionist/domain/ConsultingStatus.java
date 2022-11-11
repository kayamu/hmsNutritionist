package com.polarbears.capstone.hmsnutritionist.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.polarbears.capstone.hmsnutritionist.domain.enumeration.STATUS;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ConsultingStatus.
 */
@Entity
@Table(name = "consulting_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsultingStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nutritionist_id")
    private Integer nutritionistId;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_status")
    private STATUS lastStatus;

    @OneToMany(mappedBy = "consultingStatus")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "epicryses", "menuSuggestions", "consultingStatus" }, allowSetters = true)
    private Set<Consultings> consultings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ConsultingStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNutritionistId() {
        return this.nutritionistId;
    }

    public ConsultingStatus nutritionistId(Integer nutritionistId) {
        this.setNutritionistId(nutritionistId);
        return this;
    }

    public void setNutritionistId(Integer nutritionistId) {
        this.nutritionistId = nutritionistId;
    }

    public STATUS getLastStatus() {
        return this.lastStatus;
    }

    public ConsultingStatus lastStatus(STATUS lastStatus) {
        this.setLastStatus(lastStatus);
        return this;
    }

    public void setLastStatus(STATUS lastStatus) {
        this.lastStatus = lastStatus;
    }

    public Set<Consultings> getConsultings() {
        return this.consultings;
    }

    public void setConsultings(Set<Consultings> consultings) {
        if (this.consultings != null) {
            this.consultings.forEach(i -> i.setConsultingStatus(null));
        }
        if (consultings != null) {
            consultings.forEach(i -> i.setConsultingStatus(this));
        }
        this.consultings = consultings;
    }

    public ConsultingStatus consultings(Set<Consultings> consultings) {
        this.setConsultings(consultings);
        return this;
    }

    public ConsultingStatus addConsultings(Consultings consultings) {
        this.consultings.add(consultings);
        consultings.setConsultingStatus(this);
        return this;
    }

    public ConsultingStatus removeConsultings(Consultings consultings) {
        this.consultings.remove(consultings);
        consultings.setConsultingStatus(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsultingStatus)) {
            return false;
        }
        return id != null && id.equals(((ConsultingStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsultingStatus{" +
            "id=" + getId() +
            ", nutritionistId=" + getNutritionistId() +
            ", lastStatus='" + getLastStatus() + "'" +
            "}";
    }
}

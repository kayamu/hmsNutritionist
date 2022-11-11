package com.polarbears.capstone.hmsnutritionist.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Epicrysis.
 */
@Entity
@Table(name = "epicrysis")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Epicrysis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "nutritionist_id")
    private Long nutritionistId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Size(max = 1024)
    @Column(name = "nutritionist_notes", length = 1024)
    private String nutritionistNotes;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToMany(mappedBy = "epicryses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "epicryses", "menuSuggestions", "consultingStatus" }, allowSetters = true)
    private Set<Consultings> consultings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Epicrysis id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Epicrysis name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNutritionistId() {
        return this.nutritionistId;
    }

    public Epicrysis nutritionistId(Long nutritionistId) {
        this.setNutritionistId(nutritionistId);
        return this;
    }

    public void setNutritionistId(Long nutritionistId) {
        this.nutritionistId = nutritionistId;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public Epicrysis customerId(Long customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public Epicrysis customerName(String customerName) {
        this.setCustomerName(customerName);
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getNutritionistNotes() {
        return this.nutritionistNotes;
    }

    public Epicrysis nutritionistNotes(String nutritionistNotes) {
        this.setNutritionistNotes(nutritionistNotes);
        return this;
    }

    public void setNutritionistNotes(String nutritionistNotes) {
        this.nutritionistNotes = nutritionistNotes;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Epicrysis createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<Consultings> getConsultings() {
        return this.consultings;
    }

    public void setConsultings(Set<Consultings> consultings) {
        if (this.consultings != null) {
            this.consultings.forEach(i -> i.removeEpicrysis(this));
        }
        if (consultings != null) {
            consultings.forEach(i -> i.addEpicrysis(this));
        }
        this.consultings = consultings;
    }

    public Epicrysis consultings(Set<Consultings> consultings) {
        this.setConsultings(consultings);
        return this;
    }

    public Epicrysis addConsultings(Consultings consultings) {
        this.consultings.add(consultings);
        consultings.getEpicryses().add(this);
        return this;
    }

    public Epicrysis removeConsultings(Consultings consultings) {
        this.consultings.remove(consultings);
        consultings.getEpicryses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Epicrysis)) {
            return false;
        }
        return id != null && id.equals(((Epicrysis) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Epicrysis{" +
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

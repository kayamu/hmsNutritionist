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
 * A MenuSuggestions.
 */
@Entity
@Table(name = "menu_suggestions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MenuSuggestions implements Serializable {

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

    @Column(name = "menu_group_id")
    private Integer menuGroupId;

    @Size(max = 1024)
    @Column(name = "notes", length = 1024)
    private String notes;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToMany(mappedBy = "menuSuggestions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "epicryses", "menuSuggestions", "consultingStatus" }, allowSetters = true)
    private Set<Consultings> consultings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MenuSuggestions id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MenuSuggestions name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNutritionistId() {
        return this.nutritionistId;
    }

    public MenuSuggestions nutritionistId(Long nutritionistId) {
        this.setNutritionistId(nutritionistId);
        return this;
    }

    public void setNutritionistId(Long nutritionistId) {
        this.nutritionistId = nutritionistId;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public MenuSuggestions customerId(Long customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getMenuGroupId() {
        return this.menuGroupId;
    }

    public MenuSuggestions menuGroupId(Integer menuGroupId) {
        this.setMenuGroupId(menuGroupId);
        return this;
    }

    public void setMenuGroupId(Integer menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public String getNotes() {
        return this.notes;
    }

    public MenuSuggestions notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public MenuSuggestions createdDate(LocalDate createdDate) {
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
            this.consultings.forEach(i -> i.removeMenuSuggestions(this));
        }
        if (consultings != null) {
            consultings.forEach(i -> i.addMenuSuggestions(this));
        }
        this.consultings = consultings;
    }

    public MenuSuggestions consultings(Set<Consultings> consultings) {
        this.setConsultings(consultings);
        return this;
    }

    public MenuSuggestions addConsultings(Consultings consultings) {
        this.consultings.add(consultings);
        consultings.getMenuSuggestions().add(this);
        return this;
    }

    public MenuSuggestions removeConsultings(Consultings consultings) {
        this.consultings.remove(consultings);
        consultings.getMenuSuggestions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuSuggestions)) {
            return false;
        }
        return id != null && id.equals(((MenuSuggestions) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuSuggestions{" +
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

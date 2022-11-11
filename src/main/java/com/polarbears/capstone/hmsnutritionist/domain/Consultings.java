package com.polarbears.capstone.hmsnutritionist.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.polarbears.capstone.hmsnutritionist.domain.enumeration.STATUS;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Consultings.
 */
@Entity
@Table(name = "consultings")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Consultings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "custmer_name")
    private String custmerName;

    @Column(name = "nutritionist_id")
    private Long nutritionistId;

    @Column(name = "nutritionist_name")
    private String nutritionistName;

    @Size(max = 1024)
    @Column(name = "nutritionist_notes", length = 1024)
    private String nutritionistNotes;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_status")
    private STATUS lastStatus;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToMany
    @JoinTable(
        name = "rel_consultings__epicrysis",
        joinColumns = @JoinColumn(name = "consultings_id"),
        inverseJoinColumns = @JoinColumn(name = "epicrysis_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "consultings" }, allowSetters = true)
    private Set<Epicrysis> epicryses = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_consultings__menu_suggestions",
        joinColumns = @JoinColumn(name = "consultings_id"),
        inverseJoinColumns = @JoinColumn(name = "menu_suggestions_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "consultings" }, allowSetters = true)
    private Set<MenuSuggestions> menuSuggestions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "consultings" }, allowSetters = true)
    private ConsultingStatus consultingStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Consultings id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public Consultings customerId(Long customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustmerName() {
        return this.custmerName;
    }

    public Consultings custmerName(String custmerName) {
        this.setCustmerName(custmerName);
        return this;
    }

    public void setCustmerName(String custmerName) {
        this.custmerName = custmerName;
    }

    public Long getNutritionistId() {
        return this.nutritionistId;
    }

    public Consultings nutritionistId(Long nutritionistId) {
        this.setNutritionistId(nutritionistId);
        return this;
    }

    public void setNutritionistId(Long nutritionistId) {
        this.nutritionistId = nutritionistId;
    }

    public String getNutritionistName() {
        return this.nutritionistName;
    }

    public Consultings nutritionistName(String nutritionistName) {
        this.setNutritionistName(nutritionistName);
        return this;
    }

    public void setNutritionistName(String nutritionistName) {
        this.nutritionistName = nutritionistName;
    }

    public String getNutritionistNotes() {
        return this.nutritionistNotes;
    }

    public Consultings nutritionistNotes(String nutritionistNotes) {
        this.setNutritionistNotes(nutritionistNotes);
        return this;
    }

    public void setNutritionistNotes(String nutritionistNotes) {
        this.nutritionistNotes = nutritionistNotes;
    }

    public STATUS getLastStatus() {
        return this.lastStatus;
    }

    public Consultings lastStatus(STATUS lastStatus) {
        this.setLastStatus(lastStatus);
        return this;
    }

    public void setLastStatus(STATUS lastStatus) {
        this.lastStatus = lastStatus;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Consultings createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Set<Epicrysis> getEpicryses() {
        return this.epicryses;
    }

    public void setEpicryses(Set<Epicrysis> epicryses) {
        this.epicryses = epicryses;
    }

    public Consultings epicryses(Set<Epicrysis> epicryses) {
        this.setEpicryses(epicryses);
        return this;
    }

    public Consultings addEpicrysis(Epicrysis epicrysis) {
        this.epicryses.add(epicrysis);
        epicrysis.getConsultings().add(this);
        return this;
    }

    public Consultings removeEpicrysis(Epicrysis epicrysis) {
        this.epicryses.remove(epicrysis);
        epicrysis.getConsultings().remove(this);
        return this;
    }

    public Set<MenuSuggestions> getMenuSuggestions() {
        return this.menuSuggestions;
    }

    public void setMenuSuggestions(Set<MenuSuggestions> menuSuggestions) {
        this.menuSuggestions = menuSuggestions;
    }

    public Consultings menuSuggestions(Set<MenuSuggestions> menuSuggestions) {
        this.setMenuSuggestions(menuSuggestions);
        return this;
    }

    public Consultings addMenuSuggestions(MenuSuggestions menuSuggestions) {
        this.menuSuggestions.add(menuSuggestions);
        menuSuggestions.getConsultings().add(this);
        return this;
    }

    public Consultings removeMenuSuggestions(MenuSuggestions menuSuggestions) {
        this.menuSuggestions.remove(menuSuggestions);
        menuSuggestions.getConsultings().remove(this);
        return this;
    }

    public ConsultingStatus getConsultingStatus() {
        return this.consultingStatus;
    }

    public void setConsultingStatus(ConsultingStatus consultingStatus) {
        this.consultingStatus = consultingStatus;
    }

    public Consultings consultingStatus(ConsultingStatus consultingStatus) {
        this.setConsultingStatus(consultingStatus);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Consultings)) {
            return false;
        }
        return id != null && id.equals(((Consultings) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Consultings{" +
            "id=" + getId() +
            ", customerId=" + getCustomerId() +
            ", custmerName='" + getCustmerName() + "'" +
            ", nutritionistId=" + getNutritionistId() +
            ", nutritionistName='" + getNutritionistName() + "'" +
            ", nutritionistNotes='" + getNutritionistNotes() + "'" +
            ", lastStatus='" + getLastStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}

package touba.xassaide.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Entity
@Table(name = "cuisine")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cuisine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false, unique = true)
    private String nom;

    @Column(name = "capacite")
    private Integer capacite;

    @JsonIgnoreProperties(value = { "drousses" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Lieu lieu;

    @OneToMany(mappedBy = "cuisine")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cuisine" }, allowSetters = true)
    private Set<Mouride> mourides = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cuisine id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Cuisine nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getCapacite() {
        return this.capacite;
    }

    public Cuisine capacite(Integer capacite) {
        this.capacite = capacite;
        return this;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public Lieu getLieu() {
        return this.lieu;
    }

    public Cuisine lieu(Lieu lieu) {
        this.setLieu(lieu);
        return this;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }

    public Set<Mouride> getMourides() {
        return this.mourides;
    }

    public Cuisine mourides(Set<Mouride> mourides) {
        this.setMourides(mourides);
        return this;
    }

    public Cuisine addMourides(Mouride mouride) {
        this.mourides.add(mouride);
        mouride.setCuisine(this);
        return this;
    }

    public Cuisine removeMourides(Mouride mouride) {
        this.mourides.remove(mouride);
        mouride.setCuisine(null);
        return this;
    }

    public void setMourides(Set<Mouride> mourides) {
        if (this.mourides != null) {
            this.mourides.forEach(i -> i.setCuisine(null));
        }
        if (mourides != null) {
            mourides.forEach(i -> i.setCuisine(this));
        }
        this.mourides = mourides;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cuisine)) {
            return false;
        }
        return id != null && id.equals(((Cuisine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cuisine{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", capacite=" + getCapacite() +
            "}";
    }
}

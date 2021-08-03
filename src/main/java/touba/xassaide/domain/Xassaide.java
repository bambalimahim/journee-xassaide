package touba.xassaide.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Xassaide.
 */
@Entity
@Table(name = "xassaide")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Xassaide implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @OneToMany(mappedBy = "xassaide")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "xassaide", "lieu" }, allowSetters = true)
    private Set<Drouss> drousses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Xassaide id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Xassaide nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Drouss> getDrousses() {
        return this.drousses;
    }

    public Xassaide drousses(Set<Drouss> drousses) {
        this.setDrousses(drousses);
        return this;
    }

    public Xassaide addDrouss(Drouss drouss) {
        this.drousses.add(drouss);
        drouss.setXassaide(this);
        return this;
    }

    public Xassaide removeDrouss(Drouss drouss) {
        this.drousses.remove(drouss);
        drouss.setXassaide(null);
        return this;
    }

    public void setDrousses(Set<Drouss> drousses) {
        if (this.drousses != null) {
            this.drousses.forEach(i -> i.setXassaide(null));
        }
        if (drousses != null) {
            drousses.forEach(i -> i.setXassaide(this));
        }
        this.drousses = drousses;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Xassaide)) {
            return false;
        }
        return id != null && id.equals(((Xassaide) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Xassaide{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}

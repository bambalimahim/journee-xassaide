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
 * A Lieu.
 */
@Entity
@Table(name = "lieu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Lieu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false, unique = true)
    private String nom;

    @OneToMany(mappedBy = "lieu")
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

    public Lieu id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Lieu nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Drouss> getDrousses() {
        return this.drousses;
    }

    public Lieu drousses(Set<Drouss> drousses) {
        this.setDrousses(drousses);
        return this;
    }

    public Lieu addDrouss(Drouss drouss) {
        this.drousses.add(drouss);
        drouss.setLieu(this);
        return this;
    }

    public Lieu removeDrouss(Drouss drouss) {
        this.drousses.remove(drouss);
        drouss.setLieu(null);
        return this;
    }

    public void setDrousses(Set<Drouss> drousses) {
        if (this.drousses != null) {
            this.drousses.forEach(i -> i.setLieu(null));
        }
        if (drousses != null) {
            drousses.forEach(i -> i.setLieu(this));
        }
        this.drousses = drousses;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lieu)) {
            return false;
        }
        return id != null && id.equals(((Lieu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lieu{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}

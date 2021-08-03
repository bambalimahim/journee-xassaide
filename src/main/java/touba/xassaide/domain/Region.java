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
 * A Region.
 */
@Entity
@Table(name = "region")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false, unique = true)
    private String nom;

    @JsonIgnoreProperties(value = { "drousses" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Lieu lieu;

    @OneToMany(mappedBy = "region")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "kourels", "region" }, allowSetters = true)
    private Set<Daara> daaras = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Region id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Region nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Lieu getLieu() {
        return this.lieu;
    }

    public Region lieu(Lieu lieu) {
        this.setLieu(lieu);
        return this;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }

    public Set<Daara> getDaaras() {
        return this.daaras;
    }

    public Region daaras(Set<Daara> daaras) {
        this.setDaaras(daaras);
        return this;
    }

    public Region addDaaras(Daara daara) {
        this.daaras.add(daara);
        daara.setRegion(this);
        return this;
    }

    public Region removeDaaras(Daara daara) {
        this.daaras.remove(daara);
        daara.setRegion(null);
        return this;
    }

    public void setDaaras(Set<Daara> daaras) {
        if (this.daaras != null) {
            this.daaras.forEach(i -> i.setRegion(null));
        }
        if (daaras != null) {
            daaras.forEach(i -> i.setRegion(this));
        }
        this.daaras = daaras;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Region)) {
            return false;
        }
        return id != null && id.equals(((Region) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Region{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}

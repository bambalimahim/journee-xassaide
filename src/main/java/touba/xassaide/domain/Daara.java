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
 * A Daara.
 */
@Entity
@Table(name = "daara")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Daara implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @OneToMany(mappedBy = "daara")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "daara" }, allowSetters = true)
    private Set<Kourel> kourels = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "lieu", "daaras" }, allowSetters = true)
    private Region region;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Daara id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Daara nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Kourel> getKourels() {
        return this.kourels;
    }

    public Daara kourels(Set<Kourel> kourels) {
        this.setKourels(kourels);
        return this;
    }

    public Daara addKourels(Kourel kourel) {
        this.kourels.add(kourel);
        kourel.setDaara(this);
        return this;
    }

    public Daara removeKourels(Kourel kourel) {
        this.kourels.remove(kourel);
        kourel.setDaara(null);
        return this;
    }

    public void setKourels(Set<Kourel> kourels) {
        if (this.kourels != null) {
            this.kourels.forEach(i -> i.setDaara(null));
        }
        if (kourels != null) {
            kourels.forEach(i -> i.setDaara(this));
        }
        this.kourels = kourels;
    }

    public Region getRegion() {
        return this.region;
    }

    public Daara region(Region region) {
        this.setRegion(region);
        return this;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Daara)) {
            return false;
        }
        return id != null && id.equals(((Daara) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Daara{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}

package touba.xassaide.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Drouss.
 */
@Entity
@Table(name = "drouss")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Drouss implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private Integer nombre;

    @ManyToOne
    @JsonIgnoreProperties(value = { "drousses" }, allowSetters = true)
    private Xassaide xassaide;

    @ManyToOne
    @JsonIgnoreProperties(value = { "drousses" }, allowSetters = true)
    private Lieu lieu;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Drouss id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getNombre() {
        return this.nombre;
    }

    public Drouss nombre(Integer nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }

    public Xassaide getXassaide() {
        return this.xassaide;
    }

    public Drouss xassaide(Xassaide xassaide) {
        this.setXassaide(xassaide);
        return this;
    }

    public void setXassaide(Xassaide xassaide) {
        this.xassaide = xassaide;
    }

    public Lieu getLieu() {
        return this.lieu;
    }

    public Drouss lieu(Lieu lieu) {
        this.setLieu(lieu);
        return this;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Drouss)) {
            return false;
        }
        return id != null && id.equals(((Drouss) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Drouss{" +
            "id=" + getId() +
            ", nombre=" + getNombre() +
            "}";
    }
}

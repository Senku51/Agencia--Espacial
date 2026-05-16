package agencia.Entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Clave primaria compuesta de la tabla intermedia mision_astronauta.
 * Es obligatorio que implemente Serializable y sobrescriba equals() y hashCode()
 * para que JPA pueda gestionar correctamente la identidad de la entidad.
 *
 * Compuesta por:
 *   - idMision     → FK a misiones
 *   - idAstronauta → FK a astronautas
 */
@Embeddable
public class MisionAstronautaId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id_mision", nullable = false)
    private int idMision;

    @Column(name = "id_astronauta", nullable = false)
    private int idAstronauta;

    // -------------------------------------------------------------------------
    // Constructores
    // -------------------------------------------------------------------------

    public MisionAstronautaId() {}

    public MisionAstronautaId(int idMision, int idAstronauta) {
        this.idMision = idMision;
        this.idAstronauta = idAstronauta;
    }

    // -------------------------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------------------------

    public int getIdMision() {
        return idMision;
    }

    public void setIdMision(int idMision) {
        this.idMision = idMision;
    }

    public int getIdAstronauta() {
        return idAstronauta;
    }

    public void setIdAstronauta(int idAstronauta) {
        this.idAstronauta = idAstronauta;
    }

    // -------------------------------------------------------------------------
    // equals y hashCode — imprescindibles para claves compuestas en JPA
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MisionAstronautaId)) return false;
        MisionAstronautaId that = (MisionAstronautaId) o;
        return idMision == that.idMision && idAstronauta == that.idAstronauta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMision, idAstronauta);
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "MisionAstronautaId{" +
                "idMision=" + idMision +
                ", idAstronauta=" + idAstronauta +
                '}';
    }
}

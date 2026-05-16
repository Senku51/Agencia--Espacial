package agencia.Entidades;

import jakarta.persistence.*;

/**
 * Entidad que representa la participación de un astronauta en una misión concreta.
 * Es la tabla intermedia de la relación N:M entre Mision y Astronauta.
 *
 * Al tener un atributo propio (rol_desempenado), no puede modelarse como una
 * simple @ManyToMany, sino que requiere una entidad independiente con clave
 * compuesta embebida (MisionAstronautaId).
 *
 * Tabla: mision_astronauta
 * Clave primaria compuesta: (id_mision, id_astronauta)
 */
@Entity
@Table(name = "mision_astronauta")
public class MisionAstronauta {

    /**
     * Clave primaria compuesta: (id_mision, id_astronauta).
     * Se usa @EmbeddedId junto con la clase MisionAstronautaId.
     */
    @EmbeddedId
    private MisionAstronautaId id;

    /**
     * Referencia a la misión. Se usa @MapsId para que JPA sincronice
     * automáticamente el campo idMision del EmbeddedId con esta FK.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idMision")
    @JoinColumn(name = "id_mision", nullable = false)
    private Mision mision;

    /**
     * Referencia al astronauta. Se usa @MapsId para sincronizar idAstronauta.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idAstronauta")
    @JoinColumn(name = "id_astronauta", nullable = false)
    private Astronauta astronauta;

    /**
     * Rol que desempeña el astronauta en esta misión concreta
     * (ej. "Comandante", "Piloto", "Especialista de carga útil").
     * Obligatorio.
     */
    @Column(name = "rol_desempenado", nullable = false, length = 100)
    private String rolDesempenado;

    // -------------------------------------------------------------------------
    // Constructores
    // -------------------------------------------------------------------------

    public MisionAstronauta() {}

    /**
     * Constructor de conveniencia para crear una participación completa.
     *
     * @param mision         misión en la que participa el astronauta
     * @param astronauta     astronauta participante
     * @param rolDesempenado rol que desempeña en esta misión
     */
    public MisionAstronauta(Mision mision, Astronauta astronauta, String rolDesempenado) {
        // Construimos el ID compuesto a partir de los IDs de ambas entidades
        this.id = new MisionAstronautaId(mision.getIdMision(), astronauta.getIdAstronauta());
        this.mision = mision;
        this.astronauta = astronauta;
        setRolDesempenado(rolDesempenado);
    }

    // -------------------------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------------------------

    public MisionAstronautaId getId() {
        return id;
    }

    public void setId(MisionAstronautaId id) {
        this.id = id;
    }

    public Mision getMision() {
        return mision;
    }

    public void setMision(Mision mision) {
        this.mision = mision;
        // Sincronizamos la parte del ID compuesto correspondiente a la misión
        if (id == null) id = new MisionAstronautaId();
        if (mision != null) id.setIdMision(mision.getIdMision());
    }

    public Astronauta getAstronauta() {
        return astronauta;
    }

    public void setAstronauta(Astronauta astronauta) {
        this.astronauta = astronauta;
        // Sincronizamos la parte del ID compuesto correspondiente al astronauta
        if (id == null) id = new MisionAstronautaId();
        if (astronauta != null) id.setIdAstronauta(astronauta.getIdAstronauta());
    }

    public String getRolDesempenado() {
        return rolDesempenado;
    }

    public void setRolDesempenado(String rolDesempenado) {
        if (rolDesempenado == null || rolDesempenado.isBlank()) {
            throw new IllegalArgumentException("El rol desempeñado en la misión no puede estar vacío.");
        }
        this.rolDesempenado = rolDesempenado.trim();
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "MisionAstronauta{" +
                "mision=" + (mision != null ? mision.getNombre() : "null") +
                ", astronauta=" + (astronauta != null ? astronauta.getNombreCompleto() : "null") +
                ", rolDesempenado='" + rolDesempenado + '\'' +
                '}';
    }
}

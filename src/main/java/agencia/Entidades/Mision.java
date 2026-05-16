package agencia.Entidades;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad principal que representa una misión espacial.
 *
 * Restricciones aplicadas:
 *   RS-001 — nombre, fecha_lanzamiento y estado_actual son obligatorios.
 *   RS-005 — el nombre de la misión es único en el sistema.
 *
 * Una misión puede ser tripulada (es_tripulada = true) o no tripulada.
 * Utiliza un único vehículo de lanzamiento (ManyToOne) y puede tener
 * varios satélites asociados (OneToMany) y varias participaciones de
 * astronautas a través de MisionAstronauta.
 */
@Entity
@Table(
    name = "misiones",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_misiones_nombre", columnNames = "nombre")
    }
)
public class Mision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mision")
    private int idMision;

    /**
     * Nombre único de la misión (RS-001, RS-005).
     */
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    /**
     * Descripción del objetivo principal de la misión.
     */
    @Column(name = "objetivo_principal", nullable = false, columnDefinition = "TEXT")
    private String objetivoPrincipal;

    /**
     * Fecha de lanzamiento — obligatoria (RS-001).
     */
    @Column(name = "fecha_lanzamiento", nullable = false)
    private LocalDate fechaLanzamiento;

    /**
     * Fecha de finalización prevista — opcional.
     */
    @Column(name = "fecha_fin_prevista")
    private LocalDate fechaFinPrevista;

    /**
     * Estado actual de la misión (ej. PLANIFICADA, EN_CURSO, COMPLETADA, ABORTADA).
     * Obligatorio (RS-001).
     */
    @Column(name = "estado_actual", nullable = false, length = 50)
    private String estadoActual;

    /**
     * Indica si la misión es tripulada. Por defecto false.
     */
    @Column(name = "es_tripulada", nullable = false)
    private boolean esTripulada = false;

    /**
     * Vehículo de lanzamiento utilizado en esta misión.
     * Relación Many-to-One: varios misiones pueden usar vehículos del mismo modelo,
     * pero cada misión usa un único vehículo concreto.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private VehiculoLanzamiento vehiculoLanzamiento;

    /**
     * Satélites puestos en órbita en el marco de esta misión.
     * Un satélite pertenece a una única misión (OneToMany).
     */
    @OneToMany(mappedBy = "mision", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Satelite> satelites = new ArrayList<>();

    /**
     * Participaciones de astronautas en esta misión (tabla intermedia con atributo rol).
     * Solo relevante si esTripulada == true.
     */
    @OneToMany(mappedBy = "mision", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MisionAstronauta> participaciones = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Constructores
    // -------------------------------------------------------------------------

    public Mision() {}

    /**
     * Constructor completo para misiones no tripuladas.
     */
    public Mision(String nombre, String objetivoPrincipal, LocalDate fechaLanzamiento,
                  String estadoActual, VehiculoLanzamiento vehiculoLanzamiento) {
        setNombre(nombre);
        setObjetivoPrincipal(objetivoPrincipal);
        setFechaLanzamiento(fechaLanzamiento);
        setEstadoActual(estadoActual);
        setVehiculoLanzamiento(vehiculoLanzamiento);
    }

    /**
     * Constructor completo con todos los campos.
     */
    public Mision(String nombre, String objetivoPrincipal, LocalDate fechaLanzamiento,
                  LocalDate fechaFinPrevista, String estadoActual, boolean esTripulada,
                  VehiculoLanzamiento vehiculoLanzamiento) {
        this(nombre, objetivoPrincipal, fechaLanzamiento, estadoActual, vehiculoLanzamiento);
        setFechaFinPrevista(fechaFinPrevista);
        this.esTripulada = esTripulada;
    }

    // -------------------------------------------------------------------------
    // Getters y Setters con validaciones (RS-001, RS-005)
    // -------------------------------------------------------------------------

    public int getIdMision() {
        return idMision;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * RS-001 / RS-005: nombre obligatorio y único.
     * La unicidad a nivel de BD la garantiza la constraint @UniqueConstraint;
     * aquí se valida que no sea vacío.
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("RS-001: El nombre de la misión es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public String getObjetivoPrincipal() {
        return objetivoPrincipal;
    }

    public void setObjetivoPrincipal(String objetivoPrincipal) {
        if (objetivoPrincipal == null || objetivoPrincipal.isBlank()) {
            throw new IllegalArgumentException("El objetivo principal de la misión es obligatorio.");
        }
        this.objetivoPrincipal = objetivoPrincipal.trim();
    }

    public LocalDate getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    /**
     * RS-001: fecha de lanzamiento obligatoria.
     */
    public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
        if (fechaLanzamiento == null) {
            throw new IllegalArgumentException("RS-001: La fecha de lanzamiento es obligatoria.");
        }
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public LocalDate getFechaFinPrevista() {
        return fechaFinPrevista;
    }

    public void setFechaFinPrevista(LocalDate fechaFinPrevista) {
        // Opcional, pero si se proporciona debe ser posterior a la fecha de lanzamiento
        if (fechaFinPrevista != null && fechaLanzamiento != null
                && fechaFinPrevista.isBefore(fechaLanzamiento)) {
            throw new IllegalArgumentException(
                    "La fecha de finalización prevista no puede ser anterior a la fecha de lanzamiento.");
        }
        this.fechaFinPrevista = fechaFinPrevista;
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    /**
     * RS-001: estado actual obligatorio.
     */
    public void setEstadoActual(String estadoActual) {
        if (estadoActual == null || estadoActual.isBlank()) {
            throw new IllegalArgumentException("RS-001: El estado actual de la misión es obligatorio.");
        }
        this.estadoActual = estadoActual.trim();
    }

    public boolean isEsTripulada() {
        return esTripulada;
    }

    public void setEsTripulada(boolean esTripulada) {
        this.esTripulada = esTripulada;
    }

    public VehiculoLanzamiento getVehiculoLanzamiento() {
        return vehiculoLanzamiento;
    }

    public void setVehiculoLanzamiento(VehiculoLanzamiento vehiculoLanzamiento) {
        if (vehiculoLanzamiento == null) {
            throw new IllegalArgumentException("El vehículo de lanzamiento no puede ser nulo.");
        }
        this.vehiculoLanzamiento = vehiculoLanzamiento;
    }

    public List<Satelite> getSatelites() {
        return satelites;
    }

    public void setSatelites(List<Satelite> satelites) {
        this.satelites = satelites;
    }

    public List<MisionAstronauta> getParticipaciones() {
        return participaciones;
    }

    public void setParticipaciones(List<MisionAstronauta> participaciones) {
        this.participaciones = participaciones;
    }

    // -------------------------------------------------------------------------
    // Métodos de conveniencia
    // -------------------------------------------------------------------------

    /**
     * Añade una participación de astronauta a esta misión.
     * Mantiene la coherencia bidireccional de la relación.
     */
    public void agregarParticipacion(MisionAstronauta participacion) {
        participaciones.add(participacion);
        participacion.setMision(this);
    }

    /**
     * Elimina una participación de astronauta de esta misión.
     */
    public void eliminarParticipacion(MisionAstronauta participacion) {
        participaciones.remove(participacion);
        participacion.setMision(null);
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Mision{" +
                "idMision=" + idMision +
                ", nombre='" + nombre + '\'' +
                ", fechaLanzamiento=" + fechaLanzamiento +
                ", estadoActual='" + estadoActual + '\'' +
                ", esTripulada=" + esTripulada +
                ", vehiculo=" + (vehiculoLanzamiento != null ? vehiculoLanzamiento.getNombreSerial() : "null") +
                '}';
    }
}

package agencia.Entidades;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un vehículo de lanzamiento concreto (instancia física).
 * Resultado de la normalización a 2FN: las características del modelo se
 * trasladaron a ModeloVehiculo, aquí solo se guarda el identificador serial
 * único del vehículo y su referencia al modelo.
 */
@Entity
@Table(name = "vehiculos_lanzamiento")
public class VehiculoLanzamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private int idVehiculo;

    /**
     * Nombre serial único del cohete/vehículo concreto (ej. "Falcon 9 B1060").
     */
    @Column(name = "nombre_serial", nullable = false, unique = true, length = 100)
    private String nombreSerial;

    /**
     * Modelo al que pertenece este vehículo. Relación Many-to-One con ModeloVehiculo.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_modelo", nullable = false)
    private ModeloVehiculo modeloVehiculo;

    /**
     * Misiones en las que ha sido utilizado este vehículo.
     * Un mismo modelo (y por extensión distintos vehículos de ese modelo)
     * puede haber sido empleado en varias misiones.
     */
    @OneToMany(mappedBy = "vehiculoLanzamiento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mision> misiones = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Constructores
    // -------------------------------------------------------------------------

    public VehiculoLanzamiento() {}

    public VehiculoLanzamiento(String nombreSerial, ModeloVehiculo modeloVehiculo) {
        setNombreSerial(nombreSerial);
        setModeloVehiculo(modeloVehiculo);
    }

    // -------------------------------------------------------------------------
    // Getters y Setters con validaciones básicas
    // -------------------------------------------------------------------------

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public String getNombreSerial() {
        return nombreSerial;
    }

    public void setNombreSerial(String nombreSerial) {
        if (nombreSerial == null || nombreSerial.isBlank()) {
            throw new IllegalArgumentException("El nombre serial del vehículo no puede estar vacío.");
        }
        this.nombreSerial = nombreSerial.trim();
    }

    public ModeloVehiculo getModeloVehiculo() {
        return modeloVehiculo;
    }

    public void setModeloVehiculo(ModeloVehiculo modeloVehiculo) {
        if (modeloVehiculo == null) {
            throw new IllegalArgumentException("El modelo del vehículo no puede ser nulo.");
        }
        this.modeloVehiculo = modeloVehiculo;
    }

    public List<Mision> getMisiones() {
        return misiones;
    }

    public void setMisiones(List<Mision> misiones) {
        this.misiones = misiones;
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "VehiculoLanzamiento{" +
                "idVehiculo=" + idVehiculo +
                ", nombreSerial='" + nombreSerial + '\'' +
                ", modelo=" + (modeloVehiculo != null ? modeloVehiculo.getNombreModelo() : "null") +
                '}';
    }
}

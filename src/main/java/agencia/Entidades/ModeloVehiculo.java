package agencia.Entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un modelo de vehículo de lanzamiento.
 * Un mismo modelo puede haber sido utilizado en varios vehículos concretos.
 * Resultado de la normalización a 2FN/3FN: se separó de VehiculoLanzamiento
 * para evitar dependencias parciales.
 */
@Entity
@Table(name = "modelos_vehiculos")
public class ModeloVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private int idModelo;

    @Column(name = "nombre_modelo", nullable = false, length = 100)
    private String nombreModelo;

    /**
     * Capacidad de carga en kilogramos. Debe ser mayor que 0.
     */
    @Column(name = "capacidad_carga_kg", nullable = false, precision = 10, scale = 2)
    private BigDecimal capacidadCargaKg;

    @Column(name = "pais_fabricacion", nullable = false, length = 100)
    private String paisFabricacion;

    /**
     * Un modelo puede estar asociado a varios vehículos físicos concretos.
     */
    @OneToMany(mappedBy = "modeloVehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VehiculoLanzamiento> vehiculos = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Constructores
    // -------------------------------------------------------------------------

    public ModeloVehiculo() {}

    public ModeloVehiculo(String nombreModelo, BigDecimal capacidadCargaKg, String paisFabricacion) {
        setNombreModelo(nombreModelo);
        setCapacidadCargaKg(capacidadCargaKg);
        setPaisFabricacion(paisFabricacion);
    }

    // -------------------------------------------------------------------------
    // Getters y Setters con validaciones básicas
    // -------------------------------------------------------------------------

    public int getIdModelo() {
        return idModelo;
    }

    public String getNombreModelo() {
        return nombreModelo;
    }

    public void setNombreModelo(String nombreModelo) {
        if (nombreModelo == null || nombreModelo.isBlank()) {
            throw new IllegalArgumentException("El nombre del modelo no puede estar vacío.");
        }
        this.nombreModelo = nombreModelo.trim();
    }

    public BigDecimal getCapacidadCargaKg() {
        return capacidadCargaKg;
    }

    public void setCapacidadCargaKg(BigDecimal capacidadCargaKg) {
        if (capacidadCargaKg == null || capacidadCargaKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La capacidad de carga debe ser mayor que 0.");
        }
        this.capacidadCargaKg = capacidadCargaKg;
    }

    public String getPaisFabricacion() {
        return paisFabricacion;
    }

    public void setPaisFabricacion(String paisFabricacion) {
        if (paisFabricacion == null || paisFabricacion.isBlank()) {
            throw new IllegalArgumentException("El país de fabricación no puede estar vacío.");
        }
        this.paisFabricacion = paisFabricacion.trim();
    }

    public List<VehiculoLanzamiento> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<VehiculoLanzamiento> vehiculos) {
        this.vehiculos = vehiculos;
    }

    // -------------------------------------------------------------------------
    // toString
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ModeloVehiculo{" +
                "idModelo=" + idModelo +
                ", nombreModelo='" + nombreModelo + '\'' +
                ", capacidadCargaKg=" + capacidadCargaKg +
                ", paisFabricacion='" + paisFabricacion + '\'' +
                '}';
    }
}

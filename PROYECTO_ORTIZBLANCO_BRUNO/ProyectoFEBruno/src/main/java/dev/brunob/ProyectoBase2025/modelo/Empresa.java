package dev.brunob.ProyectoBase2025.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entidad Empresa.
 * Representa a una empresa colaboradora
 */
@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa", updatable = false, nullable = false)
    private Long idEmpresa;

    private String nombre;

    private String direccion;

    @OneToMany(mappedBy = "empresa")
    private List<Tutor> tutores = new ArrayList<>();

    public Empresa() {
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Tutor> getTutores() {
        return tutores;
    }

    public void setTutores(List<Tutor> tutores) {
        this.tutores = tutores;
    }

    @Override
    public String toString() {
        return "Empresa [idEmpresa=" + idEmpresa + ", nombre=" + nombre + ", direccion=" + direccion + "]";
    }
}

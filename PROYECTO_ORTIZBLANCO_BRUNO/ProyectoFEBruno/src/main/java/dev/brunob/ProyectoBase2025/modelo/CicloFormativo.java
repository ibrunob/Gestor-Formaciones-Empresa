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
 * Entidad CicloFormativo
 */
@Entity
@Table(name = "ciclo_formativo")
public class CicloFormativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciclo", updatable = false, nullable = false)
    private Long idCiclo;

    private String nombre;

    private String descripcion;

    @OneToMany(mappedBy = "cicloFormativo")
    private List<Curso> cursos = new ArrayList<>();

    public CicloFormativo() {
    }

    public Long getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(Long idCiclo) {
        this.idCiclo = idCiclo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    @Override
    public String toString() {
        return "CicloFormativo [idCiclo=" + idCiclo + ", nombre=" + nombre + "]";
    }
}

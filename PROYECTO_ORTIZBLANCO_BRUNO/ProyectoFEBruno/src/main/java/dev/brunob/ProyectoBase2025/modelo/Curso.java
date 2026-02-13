package dev.brunob.ProyectoBase2025.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entidad Curso.
 * Representa un curso académico dentro de un ciclo formativo
 * (ej: 2º DAM 2025/2026)
 */
@Entity
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso", updatable = false, nullable = false)
    private Long idCurso;

    private String nombre;

    @Column(name = "anio")
    private int anio;

    @ManyToOne
    @JoinColumn(name = "ciclo_formativo_id")
    private CicloFormativo cicloFormativo;

    @OneToMany(mappedBy = "curso")
    private List<Modulo> modulos = new ArrayList<>();

    @OneToMany(mappedBy = "curso")
    private List<Estudiante> estudiantes = new ArrayList<>();

    @OneToMany(mappedBy = "curso")
    private List<FormacionEmpresa> formaciones = new ArrayList<>();

    public Curso() {
    }

    public Long getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Long idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public CicloFormativo getCicloFormativo() {
        return cicloFormativo;
    }

    public void setCicloFormativo(CicloFormativo cicloFormativo) {
        this.cicloFormativo = cicloFormativo;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    public void setModulos(List<Modulo> modulos) {
        this.modulos = modulos;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public List<FormacionEmpresa> getFormaciones() {
        return formaciones;
    }

    public void setFormaciones(List<FormacionEmpresa> formaciones) {
        this.formaciones = formaciones;
    }

    @Override
    public String toString() {
        return "Curso [idCurso=" + idCurso + ", nombre=" + nombre + ", anio=" + anio + "]";
    }
}

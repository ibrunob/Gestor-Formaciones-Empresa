package dev.brunob.ProyectoBase2025.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entidad Estudiante extiende de Usuario
 * Representa a un alumno matriculado en un curso
 */
@Entity
@Table(name = "estudiante")
public class Estudiante extends User {

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @OneToMany(mappedBy = "estudiante")
    private List<FormacionEmpresa> formaciones = new ArrayList<>();

    public Estudiante() {
        setRole("Estudiante");
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<FormacionEmpresa> getFormaciones() {
        return formaciones;
    }

    public void setFormaciones(List<FormacionEmpresa> formaciones) {
        this.formaciones = formaciones;
    }

    @Override
    public String toString() {
        return "Estudiante [id=" + getId() + ", nombre=" + getFirstName() + " " + getLastName() + ", email=" + getEmail() + "]";
    }
}

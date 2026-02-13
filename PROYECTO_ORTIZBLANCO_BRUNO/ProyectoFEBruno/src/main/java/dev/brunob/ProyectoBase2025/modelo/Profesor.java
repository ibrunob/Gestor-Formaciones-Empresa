package dev.brunob.ProyectoBase2025.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entidad Profesor extiende Usuario
 * Representa a un profesor 
 */
@Entity
@Table(name = "profesor")
public class Profesor extends User {

    @Column(name = "es_coordinador")
    private Boolean esCoordinador;

    @OneToMany(mappedBy = "profesor")
    private List<FormacionEmpresa> formaciones = new ArrayList<>();

    @OneToMany(mappedBy = "profesor")
    private List<Modulo> modulos = new ArrayList<>();

    public Profesor() {
        setRole("Profesor/Tutor");
    }

    public Boolean getEsCoordinador() {
        return esCoordinador;
    }

    public void setEsCoordinador(Boolean esCoordinador) {
        this.esCoordinador = esCoordinador;
    }

    public List<FormacionEmpresa> getFormaciones() {
        return formaciones;
    }

    public void setFormaciones(List<FormacionEmpresa> formaciones) {
        this.formaciones = formaciones;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    public void setModulos(List<Modulo> modulos) {
        this.modulos = modulos;
    }

    @Override
    public String toString() {
        return "Profesor [id=" + getId() + ", nombre=" + getFirstName() + " " + getLastName()
                + ", esCoordinador=" + esCoordinador + "]";
    }
}

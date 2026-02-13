package dev.brunob.ProyectoBase2025.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entidad Tutor de empresa extiende Usuario
 * Representa al tutor asignado por la empresa
 */
@Entity
@Table(name = "tutor")
public class Tutor extends User {

    private String telefono;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @OneToMany(mappedBy = "tutor")
    private List<FormacionEmpresa> formaciones = new ArrayList<>();

    public Tutor() {
        setRole("Tutor de Empresa");
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<FormacionEmpresa> getFormaciones() {
        return formaciones;
    }

    public void setFormaciones(List<FormacionEmpresa> formaciones) {
        this.formaciones = formaciones;
    }

    @Override
    public String toString() {
        return "Tutor [id=" + getId() + ", nombre=" + getFirstName() + " " + getLastName()
                + ", telefono=" + telefono + "]";
    }
}

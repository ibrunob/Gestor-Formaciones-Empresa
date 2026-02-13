package dev.brunob.ProyectoBase2025.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Entidad Administrador
 */
@Entity
@Table(name = "administrador")
public class Administrador extends User {

    public Administrador() {
        setRole("Administrador");
    }

    @Override
    public String toString() {
        return "Administrador [id=" + getId() + ", nombre=" + getFirstName() + " " + getLastName()
                + ", email=" + getEmail() + "]";
    }
}

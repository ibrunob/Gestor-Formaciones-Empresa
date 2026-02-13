package dev.brunob.ProyectoBase2025.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.brunob.ProyectoBase2025.modelo.Administrador;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    Administrador findByEmail(String email);
}

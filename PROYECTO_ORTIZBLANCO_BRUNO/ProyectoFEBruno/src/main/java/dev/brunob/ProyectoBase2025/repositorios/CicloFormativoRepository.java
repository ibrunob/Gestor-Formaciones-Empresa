package dev.brunob.ProyectoBase2025.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.brunob.ProyectoBase2025.modelo.CicloFormativo;

@Repository
public interface CicloFormativoRepository extends JpaRepository<CicloFormativo, Long> {

    CicloFormativo findByNombre(String nombre);
}

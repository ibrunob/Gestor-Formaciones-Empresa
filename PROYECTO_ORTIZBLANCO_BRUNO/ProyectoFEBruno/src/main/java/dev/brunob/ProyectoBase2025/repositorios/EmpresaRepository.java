package dev.brunob.ProyectoBase2025.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.brunob.ProyectoBase2025.modelo.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Empresa findByNombre(String nombre);
}

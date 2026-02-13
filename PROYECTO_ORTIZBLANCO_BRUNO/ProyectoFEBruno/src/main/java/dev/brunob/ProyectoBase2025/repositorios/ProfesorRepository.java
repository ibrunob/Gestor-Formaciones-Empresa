package dev.brunob.ProyectoBase2025.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.brunob.ProyectoBase2025.modelo.Profesor;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

    List<Profesor> findByEsCoordinador(Boolean esCoordinador);

    Profesor findByEmail(String email);
}

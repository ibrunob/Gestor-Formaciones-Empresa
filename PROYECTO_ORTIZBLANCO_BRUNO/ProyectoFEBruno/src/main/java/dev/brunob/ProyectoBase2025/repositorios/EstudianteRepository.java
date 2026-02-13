package dev.brunob.ProyectoBase2025.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.brunob.ProyectoBase2025.modelo.Estudiante;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    List<Estudiante> findByCursoIdCurso(Long cursoId);

    Estudiante findByEmail(String email);
}

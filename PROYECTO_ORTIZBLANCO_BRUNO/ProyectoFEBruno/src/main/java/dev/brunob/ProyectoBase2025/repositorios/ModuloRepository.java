package dev.brunob.ProyectoBase2025.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.brunob.ProyectoBase2025.modelo.Modulo;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo, Long> {

    List<Modulo> findByCursoIdCurso(Long cursoId);

    List<Modulo> findByProfesorId(Long profesorId);

    Modulo findByCodigo(String codigo);
}

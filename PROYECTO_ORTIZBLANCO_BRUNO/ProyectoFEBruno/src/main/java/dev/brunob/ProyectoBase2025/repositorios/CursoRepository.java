package dev.brunob.ProyectoBase2025.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.brunob.ProyectoBase2025.modelo.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findByCicloFormativoIdCiclo(Long cicloId);

    List<Curso> findByAnio(int anio);

    Curso findByNombre(String nombre);
}

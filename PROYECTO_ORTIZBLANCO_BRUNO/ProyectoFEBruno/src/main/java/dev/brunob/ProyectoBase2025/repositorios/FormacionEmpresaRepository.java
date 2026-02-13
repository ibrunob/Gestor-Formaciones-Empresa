package dev.brunob.ProyectoBase2025.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.brunob.ProyectoBase2025.modelo.FormacionEmpresa;

@Repository
public interface FormacionEmpresaRepository extends JpaRepository<FormacionEmpresa, Long> {

    List<FormacionEmpresa> findByEstudianteId(Long estudianteId);

    List<FormacionEmpresa> findByProfesorId(Long profesorId);

    List<FormacionEmpresa> findByTutorId(Long tutorId);

    List<FormacionEmpresa> findByCursoIdCurso(Long cursoId);

    List<FormacionEmpresa> findByEstado(String estado);
}

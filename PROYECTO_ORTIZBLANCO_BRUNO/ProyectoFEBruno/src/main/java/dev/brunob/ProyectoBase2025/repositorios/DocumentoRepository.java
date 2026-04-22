package dev.brunob.ProyectoBase2025.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.brunob.ProyectoBase2025.modelo.Documento;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    List<Documento> findByFormacionEmpresaIdFormacion(Long formacionId);

    List<Documento> findByTipo(String tipo);

    List<Documento> findByFormacionEmpresaProfesorId(Long profesorId);

    List<Documento> findByFormacionEmpresaTutorId(Long tutorId);

    List<Documento> findByFormacionEmpresaEstudianteId(Long estudianteId);
}

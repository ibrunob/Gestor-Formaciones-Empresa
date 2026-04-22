package dev.brunob.ProyectoBase2025.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.brunob.ProyectoBase2025.modelo.Documento;
import dev.brunob.ProyectoBase2025.repositorios.DocumentoRepository;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    public Documento save(Documento entity) {
        return documentoRepository.save(entity);
    }

    public Documento update(Documento entity) {
        return documentoRepository.save(entity);
    }

    public void delete(Documento entity) {
        documentoRepository.delete(entity);
    }

    public void delete(Long id) {
        documentoRepository.deleteById(id);
    }

    public Documento find(Long id) {
        return documentoRepository.findById(id).orElse(null);
    }

    public List<Documento> findAll() {
        return documentoRepository.findAll();
    }

    public List<Documento> findByFormacionEmpresa(Long formacionId) {
        return documentoRepository.findByFormacionEmpresaIdFormacion(formacionId);
    }

    public List<Documento> findByTipo(String tipo) {
        return documentoRepository.findByTipo(tipo);
    }

    public List<Documento> findByProfesor(Long profesorId) {
        return documentoRepository.findByFormacionEmpresaProfesorId(profesorId);
    }

    public List<Documento> findByTutor(Long tutorId) {
        return documentoRepository.findByFormacionEmpresaTutorId(tutorId);
    }

    public List<Documento> findByEstudiante(Long estudianteId) {
        return documentoRepository.findByFormacionEmpresaEstudianteId(estudianteId);
    }

    public void deleteInBatch(List<Documento> documentos) {
        documentoRepository.deleteAll(documentos);
    }
}

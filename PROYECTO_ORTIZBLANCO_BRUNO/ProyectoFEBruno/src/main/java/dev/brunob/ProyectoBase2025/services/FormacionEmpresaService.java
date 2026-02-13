package dev.brunob.ProyectoBase2025.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.brunob.ProyectoBase2025.modelo.FormacionEmpresa;
import dev.brunob.ProyectoBase2025.repositorios.FormacionEmpresaRepository;

@Service
public class FormacionEmpresaService {

    @Autowired
    private FormacionEmpresaRepository formacionEmpresaRepository;

    public FormacionEmpresa save(FormacionEmpresa entity) {
        return formacionEmpresaRepository.save(entity);
    }

    public FormacionEmpresa update(FormacionEmpresa entity) {
        return formacionEmpresaRepository.save(entity);
    }

    public void delete(FormacionEmpresa entity) {
        formacionEmpresaRepository.delete(entity);
    }

    public void delete(Long id) {
        formacionEmpresaRepository.deleteById(id);
    }

    public FormacionEmpresa find(Long id) {
        return formacionEmpresaRepository.findById(id).orElse(null);
    }

    public List<FormacionEmpresa> findAll() {
        return formacionEmpresaRepository.findAll();
    }

    public List<FormacionEmpresa> findByEstudiante(Long estudianteId) {
        return formacionEmpresaRepository.findByEstudianteId(estudianteId);
    }

    public List<FormacionEmpresa> findByProfesor(Long profesorId) {
        return formacionEmpresaRepository.findByProfesorId(profesorId);
    }

    public List<FormacionEmpresa> findByTutor(Long tutorId) {
        return formacionEmpresaRepository.findByTutorId(tutorId);
    }

    public List<FormacionEmpresa> findByCurso(Long cursoId) {
        return formacionEmpresaRepository.findByCursoIdCurso(cursoId);
    }

    public List<FormacionEmpresa> findByEstado(String estado) {
        return formacionEmpresaRepository.findByEstado(estado);
    }

    public void deleteInBatch(List<FormacionEmpresa> formaciones) {
        formacionEmpresaRepository.deleteAll(formaciones);
    }
}

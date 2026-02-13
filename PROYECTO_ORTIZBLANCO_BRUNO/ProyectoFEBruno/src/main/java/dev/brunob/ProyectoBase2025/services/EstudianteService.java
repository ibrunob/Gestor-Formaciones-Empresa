package dev.brunob.ProyectoBase2025.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.brunob.ProyectoBase2025.modelo.Estudiante;
import dev.brunob.ProyectoBase2025.repositorios.EstudianteRepository;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    public Estudiante save(Estudiante entity) {
        return estudianteRepository.save(entity);
    }

    public Estudiante update(Estudiante entity) {
        return estudianteRepository.save(entity);
    }

    public void delete(Estudiante entity) {
        estudianteRepository.delete(entity);
    }

    public void delete(Long id) {
        estudianteRepository.deleteById(id);
    }

    public Estudiante find(Long id) {
        return estudianteRepository.findById(id).orElse(null);
    }

    public List<Estudiante> findAll() {
        return estudianteRepository.findAll();
    }

    public List<Estudiante> findByCurso(Long cursoId) {
        return estudianteRepository.findByCursoIdCurso(cursoId);
    }

    public Estudiante findByEmail(String email) {
        return estudianteRepository.findByEmail(email);
    }

    public void deleteInBatch(List<Estudiante> estudiantes) {
        estudianteRepository.deleteAll(estudiantes);
    }
}

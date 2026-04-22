package dev.brunob.ProyectoBase2025.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.brunob.ProyectoBase2025.modelo.Evaluacion;
import dev.brunob.ProyectoBase2025.repositorios.EvaluacionRepository;

@Service
public class EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    public Evaluacion save(Evaluacion entity) { return evaluacionRepository.save(entity); }

    public Evaluacion update(Evaluacion entity) { return evaluacionRepository.save(entity); }

    public void delete(Evaluacion entity) { evaluacionRepository.delete(entity); }

    public void delete(Long id) { evaluacionRepository.deleteById(id); }

    public Evaluacion find(Long id) { return evaluacionRepository.findById(id).orElse(null); }

    public List<Evaluacion> findAll() { return evaluacionRepository.findAll(); }

    public List<Evaluacion> findByEstudiante(Long estudianteId) {
        return evaluacionRepository.findByEstudianteIdOrderByFechaDesc(estudianteId);
    }

    public List<Evaluacion> findByTutor(Long tutorId) {
        return evaluacionRepository.findByTutorIdOrderByFechaDesc(tutorId);
    }
}

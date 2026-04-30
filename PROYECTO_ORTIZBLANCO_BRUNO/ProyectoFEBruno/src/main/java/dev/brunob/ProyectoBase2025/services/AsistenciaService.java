package dev.brunob.ProyectoBase2025.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.brunob.ProyectoBase2025.modelo.Asistencia;
import dev.brunob.ProyectoBase2025.repositorios.AsistenciaRepository;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    public Asistencia save(Asistencia entity) { return asistenciaRepository.save(entity); }

    public Asistencia update(Asistencia entity) { return asistenciaRepository.save(entity); }

    public void delete(Asistencia entity) { asistenciaRepository.delete(entity); }

    public Asistencia find(Long id) { return asistenciaRepository.findById(id).orElse(null); }

    public List<Asistencia> findByEstudiante(Long estudianteId) {
        return asistenciaRepository.findByEstudianteIdOrderByFechaDesc(estudianteId);
    }
}

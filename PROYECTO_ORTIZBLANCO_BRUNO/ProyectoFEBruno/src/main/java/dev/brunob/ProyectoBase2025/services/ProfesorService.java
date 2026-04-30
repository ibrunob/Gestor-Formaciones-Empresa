package dev.brunob.ProyectoBase2025.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.brunob.ProyectoBase2025.modelo.Profesor;
import dev.brunob.ProyectoBase2025.repositorios.ProfesorRepository;

@Service
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    public Profesor save(Profesor entity) {
        return profesorRepository.save(entity);
    }

    public Profesor update(Profesor entity) {
        return profesorRepository.save(entity);
    }

    public void delete(Profesor entity) {
        profesorRepository.delete(entity);
    }

    public void delete(Long id) {
        profesorRepository.deleteById(id);
    }

    public Profesor find(Long id) {
        return profesorRepository.findById(id).orElse(null);
    }

    /**
     * Devuelve el profesor con su colección de módulos (y el curso de cada
     * módulo) ya inicializada, para poder usarla fuera del contexto
     * transaccional sin disparar {@code LazyInitializationException}.
     */
    @Transactional(readOnly = true)
    public Profesor findWithModulos(Long id) {
        return profesorRepository.findByIdWithModulos(id).orElse(null);
    }

    public List<Profesor> findAll() {
        return profesorRepository.findAll();
    }

    public List<Profesor> findCoordinadores() {
        return profesorRepository.findByEsCoordinador(true);
    }

    public Profesor findByEmail(String email) {
        return profesorRepository.findByEmail(email);
    }

    public void deleteInBatch(List<Profesor> profesores) {
        profesorRepository.deleteAll(profesores);
    }
}

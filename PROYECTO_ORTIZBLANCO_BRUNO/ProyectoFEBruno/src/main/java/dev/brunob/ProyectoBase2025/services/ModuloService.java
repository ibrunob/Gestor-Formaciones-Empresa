package dev.brunob.ProyectoBase2025.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.brunob.ProyectoBase2025.modelo.Modulo;
import dev.brunob.ProyectoBase2025.repositorios.ModuloRepository;

@Service
public class ModuloService {

    @Autowired
    private ModuloRepository moduloRepository;

    public Modulo save(Modulo entity) {
        return moduloRepository.save(entity);
    }

    public Modulo update(Modulo entity) {
        return moduloRepository.save(entity);
    }

    public void delete(Modulo entity) {
        moduloRepository.delete(entity);
    }

    public void delete(Long id) {
        moduloRepository.deleteById(id);
    }

    public Modulo find(Long id) {
        return moduloRepository.findById(id).orElse(null);
    }

    public List<Modulo> findAll() {
        return moduloRepository.findAll();
    }

    public List<Modulo> findByCurso(Long cursoId) {
        return moduloRepository.findByCursoIdCurso(cursoId);
    }

    public List<Modulo> findByProfesor(Long profesorId) {
        return moduloRepository.findByProfesorId(profesorId);
    }

    public Modulo findByCodigo(String codigo) {
        return moduloRepository.findByCodigo(codigo);
    }

    public void deleteInBatch(List<Modulo> modulos) {
        moduloRepository.deleteAll(modulos);
    }
}

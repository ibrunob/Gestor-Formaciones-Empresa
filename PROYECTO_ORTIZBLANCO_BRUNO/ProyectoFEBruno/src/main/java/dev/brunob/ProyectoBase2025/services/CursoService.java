package dev.brunob.ProyectoBase2025.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.brunob.ProyectoBase2025.modelo.Curso;
import dev.brunob.ProyectoBase2025.repositorios.CursoRepository;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public Curso save(Curso entity) {
        return cursoRepository.save(entity);
    }

    public Curso update(Curso entity) {
        return cursoRepository.save(entity);
    }

    public void delete(Curso entity) {
        cursoRepository.delete(entity);
    }

    public void delete(Long id) {
        cursoRepository.deleteById(id);
    }

    public Curso find(Long id) {
        return cursoRepository.findById(id).orElse(null);
    }

    public List<Curso> findAll() {
        return cursoRepository.findAll();
    }

    public List<Curso> findByCicloFormativo(Long cicloId) {
        return cursoRepository.findByCicloFormativoIdCiclo(cicloId);
    }

    public List<Curso> findByAnio(int anio) {
        return cursoRepository.findByAnio(anio);
    }

    public Curso findByNombre(String nombre) {
        return cursoRepository.findByNombre(nombre);
    }

    public void deleteInBatch(List<Curso> cursos) {
        cursoRepository.deleteAll(cursos);
    }
}

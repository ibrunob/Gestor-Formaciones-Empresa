package dev.brunob.ProyectoBase2025.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.brunob.ProyectoBase2025.modelo.CicloFormativo;
import dev.brunob.ProyectoBase2025.repositorios.CicloFormativoRepository;

@Service
public class CicloFormativoService {

    @Autowired
    private CicloFormativoRepository cicloFormativoRepository;

    public CicloFormativo save(CicloFormativo entity) {
        return cicloFormativoRepository.save(entity);
    }

    public CicloFormativo update(CicloFormativo entity) {
        return cicloFormativoRepository.save(entity);
    }

    public void delete(CicloFormativo entity) {
        cicloFormativoRepository.delete(entity);
    }

    public void delete(Long id) {
        cicloFormativoRepository.deleteById(id);
    }

    public CicloFormativo find(Long id) {
        return cicloFormativoRepository.findById(id).orElse(null);
    }

    public List<CicloFormativo> findAll() {
        return cicloFormativoRepository.findAll();
    }

    public CicloFormativo findByNombre(String nombre) {
        return cicloFormativoRepository.findByNombre(nombre);
    }

    public void deleteInBatch(List<CicloFormativo> ciclos) {
        cicloFormativoRepository.deleteAll(ciclos);
    }
}

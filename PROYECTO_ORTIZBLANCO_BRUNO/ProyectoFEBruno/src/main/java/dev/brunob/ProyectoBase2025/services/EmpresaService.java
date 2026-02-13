package dev.brunob.ProyectoBase2025.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.brunob.ProyectoBase2025.modelo.Empresa;
import dev.brunob.ProyectoBase2025.repositorios.EmpresaRepository;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public Empresa save(Empresa entity) {
        return empresaRepository.save(entity);
    }

    public Empresa update(Empresa entity) {
        return empresaRepository.save(entity);
    }

    public void delete(Empresa entity) {
        empresaRepository.delete(entity);
    }

    public void delete(Long id) {
        empresaRepository.deleteById(id);
    }

    public Empresa find(Long id) {
        return empresaRepository.findById(id).orElse(null);
    }

    public List<Empresa> findAll() {
        return empresaRepository.findAll();
    }

    public Empresa findByNombre(String nombre) {
        return empresaRepository.findByNombre(nombre);
    }

    public void deleteInBatch(List<Empresa> empresas) {
        empresaRepository.deleteAll(empresas);
    }
}

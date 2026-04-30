package dev.brunob.ProyectoBase2025.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.brunob.ProyectoBase2025.modelo.Tutor;
import dev.brunob.ProyectoBase2025.repositorios.TutorRepository;

@Service
public class TutorService {

    @Autowired
    private TutorRepository tutorRepository;

    public Tutor save(Tutor entity) {
        return tutorRepository.save(entity);
    }

    public Tutor update(Tutor entity) {
        return tutorRepository.save(entity);
    }

    public void delete(Tutor entity) {
        tutorRepository.delete(entity);
    }

    public void delete(Long id) {
        tutorRepository.deleteById(id);
    }

    public Tutor find(Long id) {
        return tutorRepository.findById(id).orElse(null);
    }

    public List<Tutor> findAll() {
        return tutorRepository.findAllWithEmpresa();
    }

    public List<Tutor> findByEmpresa(Long empresaId) {
        return tutorRepository.findByEmpresaIdEmpresa(empresaId);
    }

    public Tutor findByEmail(String email) {
        return tutorRepository.findByEmail(email);
    }

    public void deleteInBatch(List<Tutor> tutores) {
        tutorRepository.deleteAll(tutores);
    }
}

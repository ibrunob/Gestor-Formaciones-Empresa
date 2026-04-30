package dev.brunob.ProyectoBase2025.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.brunob.ProyectoBase2025.modelo.Tutor;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    List<Tutor> findByEmpresaIdEmpresa(Long empresaId);

    Tutor findByEmail(String email);

    @Query("SELECT DISTINCT t FROM Tutor t LEFT JOIN FETCH t.empresa ORDER BY t.id")
    List<Tutor> findAllWithEmpresa();
}

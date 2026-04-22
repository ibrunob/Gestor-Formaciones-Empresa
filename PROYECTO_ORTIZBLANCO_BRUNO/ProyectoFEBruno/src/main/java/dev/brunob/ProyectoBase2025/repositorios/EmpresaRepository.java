package dev.brunob.ProyectoBase2025.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.brunob.ProyectoBase2025.modelo.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Empresa findByNombre(String nombre);

    @Query("SELECT DISTINCT e FROM Empresa e LEFT JOIN FETCH e.tutores ORDER BY e.idEmpresa")
    List<Empresa> findAllWithTutores();
}

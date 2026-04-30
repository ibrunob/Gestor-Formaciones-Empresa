package dev.brunob.ProyectoBase2025.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.brunob.ProyectoBase2025.modelo.Profesor;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

    List<Profesor> findByEsCoordinador(Boolean esCoordinador);

    Profesor findByEmail(String email);

    /**
     * Carga el profesor inicializando la colección de módulos y el curso
     * asociado a cada módulo, para evitar {@code LazyInitializationException}
     * al usarlos fuera del contexto transaccional.
     */
    @Query("select distinct p from Profesor p "
         + "left join fetch p.modulos m "
         + "left join fetch m.curso "
         + "where p.id = :id")
    Optional<Profesor> findByIdWithModulos(@Param("id") Long id);
}

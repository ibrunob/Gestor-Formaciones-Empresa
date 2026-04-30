package dev.brunob.ProyectoBase2025.modelo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Evaluacion / observacion de un Tutor de Empresa sobre un Estudiante.
 * Permite registrar valoraciones (1-5) sobre actitud, puntualidad,
 * competencias y una valoracion general, junto con observaciones libres.
 */
@Entity
@Table(name = "evaluacion")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluacion", updatable = false, nullable = false)
    private Long idEvaluacion;

    @Column(name = "fecha")
    private LocalDate fecha;

    private Integer actitud;
    private Integer puntualidad;
    private Integer competencias;

    @Column(name = "valoracion_general")
    private Integer valoracionGeneral;

    @Column(name = "observaciones", length = 4000)
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    @ManyToOne
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;

    public Evaluacion() {
    }

    public Long getIdEvaluacion() { return idEvaluacion; }
    public void setIdEvaluacion(Long idEvaluacion) { this.idEvaluacion = idEvaluacion; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Integer getActitud() { return actitud; }
    public void setActitud(Integer actitud) { this.actitud = actitud; }

    public Integer getPuntualidad() { return puntualidad; }
    public void setPuntualidad(Integer puntualidad) { this.puntualidad = puntualidad; }

    public Integer getCompetencias() { return competencias; }
    public void setCompetencias(Integer competencias) { this.competencias = competencias; }

    public Integer getValoracionGeneral() { return valoracionGeneral; }
    public void setValoracionGeneral(Integer valoracionGeneral) { this.valoracionGeneral = valoracionGeneral; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public Tutor getTutor() { return tutor; }
    public void setTutor(Tutor tutor) { this.tutor = tutor; }

    public Profesor getProfesor() { return profesor; }
    public void setProfesor(Profesor profesor) { this.profesor = profesor; }
}

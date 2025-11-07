/**
 * @author Bruno Ortiz Blanco
 * @version 1.0
 * @since 2025-11-5
 */
package model;

import java.time.LocalDate;
import java.util.List;

public class FormacionEmpresa {
	//attr
	private Long idFormacion;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private String estado;
	
	private Estudiante estudiante;
	private Tutor tutor;
	private Profesor profesorCoordinador;
	private List<Documento> documentos;
	
	//contr
	public FormacionEmpresa(Long idFormacion, LocalDate fechaInicio, LocalDate fechaFin, String estado,
			Estudiante estudiante, Tutor tutor, Profesor profesorCoordinador, List<Documento> documentos) {
		super();
		this.idFormacion = idFormacion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.estado = estado;
		this.estudiante = estudiante;
		this.tutor = tutor;
		this.profesorCoordinador = profesorCoordinador;
		this.documentos = documentos;
	}
	
	public FormacionEmpresa() {
	}

	//getters & setters
	public Long getIdFormacion() {
		return idFormacion;
	}

	public void setIdFormacion(Long idFormacion) {
		this.idFormacion = idFormacion;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Estudiante getEstudiante() {
		return estudiante;
	}

	public void setEstudiante(Estudiante estudiante) {
		this.estudiante = estudiante;
	}

	public Tutor getTutor() {
		return tutor;
	}

	public void setTutor(Tutor tutor) {
		this.tutor = tutor;
	}

	public Profesor getProfesorCoordinador() {
		return profesorCoordinador;
	}

	public void setProfesorCoordinador(Profesor profesorCoordinador) {
		this.profesorCoordinador = profesorCoordinador;
	}

	public List<Documento> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(List<Documento> documentos) {
		this.documentos = documentos;
	}
	
	
	
}

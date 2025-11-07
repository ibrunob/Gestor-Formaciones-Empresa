/**
 * @author Bruno Ortiz Blanco
 * @version 1.0
 * @since 2025-11-5
 */
package model;

import java.util.List;

public class Curso {
	//attr
	private Long idCurso;
	private String nombre;
	private int anio;
	private CicloFormativo cicloFormativo;
	private List<Modulo> modulos;
	private List<Estudiante> estudiantes;

	//constr
	public Curso(Long idCurso, String nombre, int anio, CicloFormativo cicloFormativo, List<Modulo> modulos,
			List<Estudiante> estudiantes) {
		this.idCurso = idCurso;
		this.nombre = nombre;
		this.anio = anio;
		this.cicloFormativo = cicloFormativo;
		this.modulos = modulos;
		this.estudiantes = estudiantes;
	}
	public Curso() {
	}
	//getters & setters
	public Long getIdCurso() {
		return idCurso;
	}
	public void setIdCurso(Long idCurso) {
		this.idCurso = idCurso;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getAnio() {
		return anio;
	}
	public void setAnio(int anio) {
		this.anio = anio;
	}
	public CicloFormativo getCicloFormativo() {
		return cicloFormativo;
	}
	public void setCicloFormativo(CicloFormativo cicloFormativo) {
		this.cicloFormativo = cicloFormativo;
	}
	public List<Modulo> getModulos() {
		return modulos;
	}
	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}
	public List<Estudiante> getEstudiantes() {
		return estudiantes;
	}
	public void setEstudiantes(List<Estudiante> estudiantes) {
		this.estudiantes = estudiantes;
	}
	
}

/**
 * @author Bruno Ortiz Blanco
 * @version 1.0
 * @since 2025-11-5
 */
package model;

import java.util.List;

public class CicloFormativo {
	//attr
	private Long idCiclo;
	private String nombre;
	private String descripcion;
	private List<Curso> cursos;
	//constr
	public CicloFormativo(Long idCiclo, String nombre, String descripcion, List<Curso> cursos) {
		this.idCiclo = idCiclo;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.cursos = cursos;
	}
	public CicloFormativo() {
	}
	//getters & setters
	public Long getIdCiclo() {
		return idCiclo;
	}
	public void setIdCiclo(Long idCiclo) {
		this.idCiclo = idCiclo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public List<Curso> getCursos() {
		return cursos;
	}
	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}
	
}

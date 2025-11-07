/**
 * @author Bruno Ortiz Blanco
 * @version 1.0
 * @since 2025-11-5
 */
package model;

public class Modulo {
	//attr
	private Long idModulo;
	private String nombre;
	private String codigo;
	private int horas;
	private Profesor profesor;
	//constr
	public Modulo(Long idModulo, String nombre, String codigo, int horas, Profesor profesor) {
		this.idModulo = idModulo;
		this.nombre = nombre;
		this.codigo = codigo;
		this.horas = horas;
		this.profesor = profesor;
	}
	public Modulo() {	
	}
	//getters & setters
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public int getHoras() {
		return horas;
	}
	public void setHoras(int horas) {
		this.horas = horas;
	}
	public Profesor getProfesor() {
		return profesor;
	}
	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}
}

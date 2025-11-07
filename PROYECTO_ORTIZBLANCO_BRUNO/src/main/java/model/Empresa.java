/**
 * @author Bruno Ortiz Blanco
 * @version 1.0
 * @since 2025-11-5
 */
package model;

import java.util.List;

public class Empresa {
	//attr
	private Long idEmpresa;
	private String nombre;
	private String direccion;
	private List<Tutor> tutores;
	
	//constr
	public Empresa(Long idEmpresa, String nombre, String direccion, List<Tutor> tutores) {
		this.idEmpresa = idEmpresa;
		this.nombre = nombre;
		this.direccion = direccion;
		this.tutores = tutores;
	}
	public Empresa() {
		
	}
	//getters & setters
	public Long getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public List<Tutor> getTutores() {
		return tutores;
	}
	public void setTutores(List<Tutor> tutores) {
		this.tutores = tutores;
	}
	
	
	
}

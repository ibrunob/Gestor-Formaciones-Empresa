/**
 * @author Bruno Ortiz Blanco
 * @version 1.0
 * @since 2025-11-5
 */
package model;

import java.util.List;

public class Profesor extends Usuario{
	//attr
	private boolean esCoordinador;
	private List<Modulo> modulos;
	private List<FormacionEmpresa> formacionesEmpresa;
	//constr
	public Profesor(Long id, String nombre, String email, String perfil, boolean esCoordinador, List<Modulo> modulos,
			List<FormacionEmpresa> formacionesEmpresa) {
		super(id, nombre, email, perfil);
		this.esCoordinador = esCoordinador;
		this.modulos = modulos;
		this.formacionesEmpresa = formacionesEmpresa;
	}
	public Profesor() {
		super();
	}
	//getters & setters
	public boolean isEsCoordinador() {
		return esCoordinador;
	}
	public void setEsCoordinador(boolean esCoordinador) {
		this.esCoordinador = esCoordinador;
	}
	public List<Modulo> getModulos() {
		return modulos;
	}
	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}
	public List<FormacionEmpresa> getFormacionesEmpresa() {
		return formacionesEmpresa;
	}
	public void setFormacionesEmpresa(List<FormacionEmpresa> formacionesEmpresa) {
		this.formacionesEmpresa = formacionesEmpresa;
	}
	
	
}

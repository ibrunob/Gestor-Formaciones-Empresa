/**
 * @author Bruno Ortiz Blanco
 * @version 1.0
 * @since 2025-11-5
 */
package model;

import java.util.List;

public class Tutor extends Usuario{
	//attr
	private Empresa empresa;
	private List<FormacionEmpresa> formacionesEmpresa;
	//constr
	public Tutor(Long id, String nombre, String email, String perfil, Empresa empresa) {
		super(id, nombre, email, perfil);
		this.empresa = empresa;
	}
	public Tutor() {
	}
	//getters & setters
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public List<FormacionEmpresa> getFormacionesEmpresa() {
		return formacionesEmpresa;
	}
	public void setFormacionesEmpresa(List<FormacionEmpresa> formacionesEmpresa) {
		this.formacionesEmpresa = formacionesEmpresa;
	}
	
	
}

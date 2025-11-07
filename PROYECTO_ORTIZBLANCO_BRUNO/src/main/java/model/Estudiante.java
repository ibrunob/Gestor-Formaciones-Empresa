/**
 * @author Bruno Ortiz Blanco
 * @version 1.0
 * @since 2025-11-5
 */
package model;

public class Estudiante extends Usuario{
	//attr
	private Curso curso;
	private FormacionEmpresa formacionEmpresa;
	//constr
	public Estudiante(Long id, String nombre, String email, String perfil, Curso curso,
			FormacionEmpresa formacionEmpresa) {
		super(id, nombre, email, perfil);
		this.curso = curso;
	}
	public Estudiante() {
	}
	
	//getters & setters
	public Curso getCurso() {
		return curso;
	}
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	public FormacionEmpresa getFormacionEmpresa() {
		return formacionEmpresa;
	}
	public void setFormacionEmpresa(FormacionEmpresa formacionEmpresa) {
		this.formacionEmpresa = formacionEmpresa;
	}
	
	
	
}

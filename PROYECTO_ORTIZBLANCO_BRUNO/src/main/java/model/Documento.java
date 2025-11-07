/**
 * @author Bruno Ortiz Blanco
 * @version 1.0
 * @since 2025-11-5
 */
package model;

public class Documento {
	//attr
	private Long idDocumento;
	private String nombre;
	private String tipo;
	private String ruta;
	private FormacionEmpresa formacionEmpresa;
	
	//constr
	public Documento(Long idDocumento, String nombre, String tipo, String ruta, FormacionEmpresa formacionEmpresa) {
		super();
		this.idDocumento = idDocumento;
		this.nombre = nombre;
		this.tipo = tipo;
		this.ruta = ruta;
		this.formacionEmpresa = formacionEmpresa;
	}
	public Documento() {
	}
	//getters & setters
	public Long getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public FormacionEmpresa getFormacionEmpresa() {
		return formacionEmpresa;
	}
	public void setFormacionEmpresa(FormacionEmpresa formacionEmpresa) {
		this.formacionEmpresa = formacionEmpresa;
	}
	
}

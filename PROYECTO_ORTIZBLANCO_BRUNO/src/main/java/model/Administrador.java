/**
 * @author Bruno Ortiz Blanco
 * @version 1.0
 * @since 2025-11-5
 */
package model;

public class Administrador extends Usuario{
	public Administrador(Long id, String nombre, String email, String perfil) {
		super(id, nombre, email, perfil);
	}
	public Administrador() {
		super();
	}
	
}

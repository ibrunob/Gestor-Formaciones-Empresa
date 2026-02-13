package dev.brunob.ProyectoBase2025.modelo;

/**
 * Enum que define los roles de usuario en el sistema de gestión de FEs
 * 
 * @author Bruno Ortiz Blanco
 */
public enum Role {
    ADMINISTRADOR("Administrador"),
    PROFESOR_TUTOR("Profesor/Tutor"),
    TUTOR_EMPRESA("Tutor de Empresa"),
    ESTUDIANTE("Estudiante");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Obtiene el rol a partir de su nombre
     * 
     * @param displayName El nombre para mostrar el rol
     * @return El rol correspondiente o null si no se encuentra
     */
    public static Role fromDisplayName(String displayName) {
        for (Role role : Role.values()) {
            if (role.getDisplayName().equalsIgnoreCase(displayName)) {
                return role;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

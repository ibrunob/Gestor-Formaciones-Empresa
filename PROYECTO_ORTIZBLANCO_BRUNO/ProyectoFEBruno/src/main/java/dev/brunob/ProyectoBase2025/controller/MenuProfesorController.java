package dev.brunob.ProyectoBase2025.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import dev.brunob.ProyectoBase2025.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

/**
 * Controlador para el menú del Profesor-Tutor.
 * 
 * @author Bruno Ortiz Blanco
 */
@Controller
public class MenuProfesorController extends BaseMenuController implements Initializable {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Ver estudiantes del tutor
     */
    @FXML
    public void verEstudiantes(MouseEvent event) {
        mostrarNoImplementado("Ver Estudiantes");
    }

    @FXML
    public void verEstudiantes() {
        mostrarNoImplementado("Ver Estudiantes");
    }

    /**
     * Acceder al seguimiento de la FE
     */
    @FXML
    public void seguimientoFE(MouseEvent event) {
        mostrarNoImplementado("Seguimiento FE");
    }

    @FXML
    public void seguimientoFE() {
        mostrarNoImplementado("Seguimiento FE");
    }

    /**
     * Ver las empresas asignadas.
     */
    @FXML
    public void verEmpresas(MouseEvent event) {
        mostrarNoImplementado("Ver Empresas");
    }

    @FXML
    public void verEmpresas() {
        mostrarNoImplementado("Ver Empresas");
    }

    /**
     * Contactar con la empresa
     */
    @FXML
    public void contactarEmpresa() {
        mostrarNoImplementado("Contactar Empresa");
    }

    /**
     * Calificar a los estudiantes
     */
    @FXML
    public void calificarEstudiantes(MouseEvent event) {
        mostrarNoImplementado("Calificar Estudiantes");
    }

    @FXML
    public void calificarEstudiantes() {
        mostrarNoImplementado("Calificar Estudiantes");
    }

    /**
     * Registrar la asistencia de los estudiantes
     */
    @FXML
    public void registrarAsistencia(MouseEvent event) {
        mostrarNoImplementado("Registrar Asistencia");
    }

    @FXML
    public void registrarAsistencia() {
        mostrarNoImplementado("Registrar Asistencia");
    }

    /**
     * Gestión documental
     */
    @FXML
    public void gestionDocumentos(MouseEvent event) {
        mostrarNoImplementado("Gestión Documental");
    }
}

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
 * Controlador para el menú de Estudiantes
 * 
 * @author Bruno Ortiz Blanco
 */
@Controller
public class MenuEstudianteController extends BaseMenuController implements Initializable {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Ver información de las practicas del estudiante
     */
    @FXML
    public void verInfoPracticas(MouseEvent event) {
        mostrarNoImplementado("Ver Información de Prácticas");
    }

    @FXML
    public void verInfoPracticas() {
        mostrarNoImplementado("Ver Información de Prácticas");
    }

    /**
     * Ver información de la empresa
     */
    @FXML
    public void verEmpresa(MouseEvent event) {
        mostrarNoImplementado("Ver Mi Empresa");
    }

    @FXML
    public void verEmpresa() {
        mostrarNoImplementado("Ver Mi Empresa");
    }

    /**
     * Ver información del tutor
     */
    @FXML
    public void verTutor(MouseEvent event) {
        mostrarNoImplementado("Ver Mi Tutor");
    }

    @FXML
    public void verTutor() {
        mostrarNoImplementado("Ver Mi Tutor");
    }

    /**
     * Ver el registro de asistencia
     */
    @FXML
    public void verAsistencia(MouseEvent event) {
        mostrarNoImplementado("Ver Mi Asistencia");
    }

    @FXML
    public void verAsistencia() {
        mostrarNoImplementado("Ver Mi Asistencia");
    }

    /**
     * Justificar una falta
     */
    @FXML
    public void justificarFalta(MouseEvent event) {
        mostrarNoImplementado("Justificar Falta");
    }

    @FXML
    public void justificarFalta() {
        mostrarNoImplementado("Justificar Falta");
    }

    /**
     * Gestión de documentos
     */
    @FXML
    public void gestionDocumentos(MouseEvent event) {
        mostrarNoImplementado("Mis Documentos");
    }
}

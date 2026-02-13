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
 * Controlador para el menu del Tutor de Empresa
 * Gestiona las funcionalidades del tutor en la empresa
 * 
 * @author Bruno Ortiz Blanco
 */
@Controller
public class MenuTutorEmpresaController extends BaseMenuController implements Initializable {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Ver los estudiantes asignados a la empresa
     */
    @FXML
    public void verEstudiantes(MouseEvent event) {
        mostrarNoImplementado("Ver Estudiantes Asignados");
    }

    @FXML
    public void verEstudiantes() {
        mostrarNoImplementado("Ver Estudiantes Asignados");
    }

    /**
     * Registrar la asistencia diaria de los estudiantes
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
     * Evaluar el desempeño del estudiante.
     */
    @FXML
    public void evaluarDesempeno(MouseEvent event) {
        mostrarNoImplementado("Evaluar Desempeño");
    }

    @FXML
    public void evaluarDesempeno() {
        mostrarNoImplementado("Evaluar Desempeño");
    }

    /**
     * Ver el historial de asistencia
     */
    @FXML
    public void verHistorialAsistencia(MouseEvent event) {
        mostrarNoImplementado("Ver Historial de Asistencia");
    }

    @FXML
    public void verHistorialAsistencia() {
        mostrarNoImplementado("Ver Historial de Asistencia");
    }

    /**
     * Contactar con el tutor del centro.
     */
    @FXML
    public void contactarTutor(MouseEvent event) {
        mostrarNoImplementado("Contactar Tutor Docente");
    }

    @FXML
    public void contactarTutor() {
        mostrarNoImplementado("Contactar Tutor Docente");
    }

    /**
     * Gestión documental.
     */
    @FXML
    public void gestionDocumentos(MouseEvent event) {
        mostrarNoImplementado("Gestión Documental");
    }
}

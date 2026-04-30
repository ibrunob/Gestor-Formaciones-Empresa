package dev.brunob.ProyectoBase2025.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import dev.brunob.ProyectoBase2025.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * Controlador para el menu del Tutor de Empresa
 * Gestiona las funcionalidades del tutor en la empresa
 * 
 * @author Bruno Ortiz Blanco
 */
@Controller
public class MenuTutorEmpresaController extends BaseMenuController implements Initializable {

    @Override
    protected String getPaginaAyuda() { return "menu-tutor.html"; }


    @Autowired
    private UserService userService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Ver los estudiantes asignados a la empresa
     */
    @FXML
    public void verEstudiantes() {
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.ESTUDIANTES_TUTOR);
    }

    /**
     * Registrar la asistencia diaria de los estudiantes
     */
    @FXML
    public void registrarAsistencia() {
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.ASISTENCIA);
    }

    /**
     * Evaluar el desempeño del estudiante.
     */
    @FXML
    public void evaluarDesempeno() {
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.EVALUACIONES);
    }

    /**
     * Ver el historial de asistencia
     */
    @FXML
    public void verHistorialAsistencia() {
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.ASISTENCIA);
    }

    /**
     * Contactar con el tutor del centro.
     */
    @FXML
    public void contactarTutor() {
        mostrarNoImplementado("Contactar Tutor Docente");
    }

    /**
     * Gestión documental.
     */
    @FXML
    public void gestionDocumentos() {
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.DOCUMENTOS);
    }
}

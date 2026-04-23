package dev.brunob.ProyectoBase2025.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import dev.brunob.ProyectoBase2025.services.UserService;
import dev.brunob.ProyectoBase2025.view.FxmlView;
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
        stageManager.switchScene(FxmlView.MI_INFORMACION);
    }

    @FXML
    public void verInfoPracticas() {
        stageManager.switchScene(FxmlView.MI_INFORMACION);
    }

    /**
     * Ver información de la empresa
     */
    @FXML
    public void verEmpresa(MouseEvent event) {
        stageManager.switchScene(FxmlView.MI_INFORMACION);
    }

    @FXML
    public void verEmpresa() {
        stageManager.switchScene(FxmlView.MI_INFORMACION);
    }

    /**
     * Ver información del tutor
     */
    @FXML
    public void verTutor(MouseEvent event) {
        stageManager.switchScene(FxmlView.MI_INFORMACION);
    }

    @FXML
    public void verTutor() {
        stageManager.switchScene(FxmlView.MI_INFORMACION);
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
        stageManager.switchScene(FxmlView.DOCUMENTOS);
    }

    @FXML
    public void gestionDocumentos() {
        stageManager.switchScene(FxmlView.DOCUMENTOS);
    }
}

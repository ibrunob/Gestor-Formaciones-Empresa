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

    @Override
    protected String getPaginaAyuda() { return "menu-profesor.html"; }


    @Autowired
    private UserService userService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Ver estudiantes del tutor → pantalla de asignaciones
     */
    @FXML
    public void verEstudiantes(MouseEvent event) {
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.ASIGNACIONES);
    }

    @FXML
    public void verEstudiantes() {
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.ASIGNACIONES);
    }

    /**
     * Gestionar asignaciones de estudiantes (coordinador)
     */
    @FXML
    public void gestionAsignaciones(MouseEvent event) {
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.ASIGNACIONES);
    }

    @FXML
    public void gestionAsignaciones() {
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.ASIGNACIONES);
    }

    /**
     * Acceder al seguimiento de la FE
     */
    @FXML
    public void seguimientoFE(MouseEvent event) {
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.EVALUACIONES);
    }

    @FXML
    public void seguimientoFE() {
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.EVALUACIONES);
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
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.DOCUMENTOS);
    }

    @FXML
    public void gestionDocumentos() {
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.DOCUMENTOS);
    }

    /**
     * Generar informes de las formaciones bajo su tutela.
     */
    @FXML
    public void generarInformes(MouseEvent event) {
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.INFORMES);
    }

    @FXML
    public void generarInformes() {
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.INFORMES);
    }

    /**
     * Abrir la gestion de formaciones en empresa
     */
    @FXML
    public void gestionFormaciones(MouseEvent event) {
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.FORMACION);
    }

    @FXML
    public void gestionFormaciones() {
        stageManager.switchScene(dev.brunob.ProyectoBase2025.view.FxmlView.FORMACION);
    }
}

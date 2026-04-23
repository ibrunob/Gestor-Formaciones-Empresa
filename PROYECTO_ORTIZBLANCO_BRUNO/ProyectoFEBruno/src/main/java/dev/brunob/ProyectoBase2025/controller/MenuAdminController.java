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
 * Controlador para el menú del Administrador
 * Gestiona todas las funcionalidades del admin
 * 
 * @author Bruno Ortiz Blanco
 */
@Controller
public class MenuAdminController extends BaseMenuController implements Initializable {

    @Override
    protected String getPaginaAyuda() { return "menu-admin.html"; }


    @Autowired
    private UserService userService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Abrir la gestion de usuarios
     */
    @FXML
    public void gestionUsuarios(MouseEvent event) {
        stageManager.switchScene(FxmlView.USER);
    }

    @FXML
    public void gestionUsuarios() {
        stageManager.switchScene(FxmlView.USER);
    }

    /**
     * Abrir la gestion de empresas
     */
    @FXML
    public void gestionEmpresas(MouseEvent event) {
        stageManager.switchScene(FxmlView.EMPRESA);
    }

    @FXML
    public void gestionEmpresas() {
        stageManager.switchScene(FxmlView.EMPRESA);
    }

    /**
     * Abrir la gestión de estudiantes
     */
    @FXML
    public void gestionEstudiantes(MouseEvent event) {
        stageManager.switchScene(FxmlView.ASIGNACIONES);
    }

    @FXML
    public void gestionEstudiantes() {
        stageManager.switchScene(FxmlView.ASIGNACIONES);
    }

    /**
     * Abrir la pantalla de asignaciones de estudiantes (alias)
     */
    @FXML
    public void gestionAsignaciones(MouseEvent event) {
        stageManager.switchScene(FxmlView.ASIGNACIONES);
    }

    @FXML
    public void gestionAsignaciones() {
        stageManager.switchScene(FxmlView.ASIGNACIONES);
    }

    /**
     * Abrir la gestión de tutores.
     */
    @FXML
    public void gestionTutores(MouseEvent event) {
        mostrarNoImplementado("Gestión de Tutores");
    }

    @FXML
    public void gestionTutores() {
        mostrarNoImplementado("Gestión de Tutores");
    }

    /**
     * Abrir la generación de informes
     */
    @FXML
    public void generarInformes(MouseEvent event) {
        stageManager.switchScene(FxmlView.INFORMES);
    }

    @FXML
    public void generarInformes() {
        stageManager.switchScene(FxmlView.INFORMES);
    }

    /**
     * Abrir la gestion de documentos
     */
    @FXML
    public void gestionDocumentos(MouseEvent event) {
        stageManager.switchScene(FxmlView.DOCUMENTOS);
    }

    @FXML
    public void gestionDocumentos() {
        stageManager.switchScene(FxmlView.DOCUMENTOS);
    }

    /**
     * Abrir la gestion de formaciones en empresa
     */
    @FXML
    public void gestionFormaciones(MouseEvent event) {
        stageManager.switchScene(FxmlView.FORMACION);
    }

    @FXML
    public void gestionFormaciones() {
        stageManager.switchScene(FxmlView.FORMACION);
    }
}

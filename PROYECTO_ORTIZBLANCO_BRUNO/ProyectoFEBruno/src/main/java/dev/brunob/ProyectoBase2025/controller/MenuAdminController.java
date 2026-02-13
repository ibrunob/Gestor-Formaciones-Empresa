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
        mostrarNoImplementado("Gestión de Empresas");
    }

    @FXML
    public void gestionEmpresas() {
        mostrarNoImplementado("Gestión de Empresas");
    }

    /**
     * Abrir la gestión de estudiantes
     */
    @FXML
    public void gestionEstudiantes(MouseEvent event) {
        mostrarNoImplementado("Gestión de Estudiantes");
    }

    @FXML
    public void gestionEstudiantes() {
        mostrarNoImplementado("Gestión de Estudiantes");
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
        mostrarNoImplementado("Generación de Informes");
    }

    @FXML
    public void generarInformes() {
        mostrarNoImplementado("Generación de Informes");
    }

    /**
     * Abrir la gestion de documentos
     */
    @FXML
    public void gestionDocumentos(MouseEvent event) {
        mostrarNoImplementado("Gestión Documental");
    }
}

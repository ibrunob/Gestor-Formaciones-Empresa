package dev.brunob.ProyectoBase2025.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import dev.brunob.ProyectoBase2025.config.StageManager;
import dev.brunob.ProyectoBase2025.modelo.User;
import dev.brunob.ProyectoBase2025.view.FxmlView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

/**
 * Controlador que da funcionalidades comunes
 * para todos los menús de roles.
 * 
 * @author Bruno Ortiz Blanco
 */
public abstract class BaseMenuController {

    @Lazy
    @Autowired
    protected StageManager stageManager;

    @FXML
    protected Label lblUserInfo;

    protected User currentUser;

    /**
     * Establece el usuario actual y actualiza la interfaz. TODO: Fix No muestra el nombre
     * 
     * @param user El usuario logueado
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (lblUserInfo != null && user != null) {
            lblUserInfo.setText("Usuario: " + user.getFirstName() + " " + 
                (user.getLastName() != null ? user.getLastName() : ""));
        }
    }

    /**
     * Cierra la sesion y vuelve a la pantalla de login.
     */
    @FXML
    protected void logout(ActionEvent event) {
        currentUser = null;
        stageManager.switchScene(FxmlView.LOGIN);
    }

    /**
     * Cierra la aplicacion
     */
    @FXML
    protected void exit(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Muestra el dialogo Acerca de
     */
    @FXML
    protected void acercaDe(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText("Sistema de Gestión de Formaciones en Empresa");
        alert.setContentText("Versión: 1.0\n" +
                "Desarrollado por: Bruno Ortiz Blanco\n" +
                "CIFP La Laboral\n\n" +
                "© 2026 Todos los derechos reservados.");
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de funcionalidad no implementada
     * 
     * @param funcionalidad Nombre de la funcionalidad
     */
    protected void mostrarNoImplementado(String funcionalidad) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Funcionalidad en desarrollo");
        alert.setHeaderText(null);
        alert.setContentText("La funcionalidad '" + funcionalidad + "' está en desarrollo");
        alert.showAndWait();
    }

    /**
     * Gestión de documentos
     */
    @FXML
    protected void gestionDocumentos() {
        mostrarNoImplementado("Gestión Documental");
    }
}

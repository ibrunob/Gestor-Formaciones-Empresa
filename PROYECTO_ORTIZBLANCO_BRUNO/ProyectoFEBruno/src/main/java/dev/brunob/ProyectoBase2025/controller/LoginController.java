package dev.brunob.ProyectoBase2025.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import dev.brunob.ProyectoBase2025.config.StageManager;
import dev.brunob.ProyectoBase2025.modelo.Role;
import dev.brunob.ProyectoBase2025.modelo.User;
import dev.brunob.ProyectoBase2025.services.UserService;
import dev.brunob.ProyectoBase2025.view.FxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controlador para la pantalla de login.
 * Gestiona la autenticacion y redirección según el rol del usuario
 * 
 * @author Bruno Ortiz Blanco
 */
@Controller
public class LoginController implements Initializable {

    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Label lblLogin;

    @Autowired
    private UserService userService;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    private void login(ActionEvent event) {
        if (userService.authenticate(getUsername(), getPassword())) {
            User user = userService.findByEmail(getUsername());
            redirectToMenuByRole(user);
        } else {
            lblLogin.setText("Credenciales incorrectas");
        }
    }

    /**
     * Redirige al menú correspondiente segun el rol del usuario
     * 
     * @param user El usuario autenticado
     */
    private void redirectToMenuByRole(User user) {
        String roleStr = user.getRole();
        Role role = Role.fromDisplayName(roleStr);

        if (role == null) {
            // Si no coincide con el displayName intentar por nombre del enum
            try {
                role = Role.valueOf(roleStr.toUpperCase().replace("/", "_").replace(" ", "_"));
            } catch (IllegalArgumentException e) {
                // usar menu por defecto basado en el valor del campo
                if (roleStr != null) {
                    switch (roleStr.toLowerCase()) {
                        case "admin":
                        case "administrador":
                            role = Role.ADMINISTRADOR;
                            break;
                        case "profesor":
                        case "tutor":
                        case "profesor/tutor":
                            role = Role.PROFESOR_TUTOR;
                            break;
                        case "tutor_empresa":
                        case "tutor de empresa":
                            role = Role.TUTOR_EMPRESA;
                            break;
                        case "estudiante":
                        case "alumno":
                            role = Role.ESTUDIANTE;
                            break;
                        default:
                            role = Role.ESTUDIANTE;
                    }
                }
            }
        }

        if (role != null) {
            switch (role) {
                case ADMINISTRADOR:
                    stageManager.switchScene(FxmlView.MENU_ADMIN);
                    break;
                case PROFESOR_TUTOR:
                    stageManager.switchScene(FxmlView.MENU_PROFESOR);
                    break;
                case TUTOR_EMPRESA:
                    stageManager.switchScene(FxmlView.MENU_TUTOR_EMPRESA);
                    break;
                case ESTUDIANTE:
                    stageManager.switchScene(FxmlView.MENU_ESTUDIANTE);
                    break;
                default:
                    stageManager.switchScene(FxmlView.MENU_ESTUDIANTE);
            }
        } else {
            stageManager.switchScene(FxmlView.INICIO);
        }
    }

    public String getPassword() {
        return password.getText();
    }

    public String getUsername() {
        return username.getText();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
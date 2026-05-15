package dev.brunob.ProyectoBase2025.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import dev.brunob.ProyectoBase2025.config.StageManager;
import dev.brunob.ProyectoBase2025.modelo.Role;
import dev.brunob.ProyectoBase2025.modelo.User;
import dev.brunob.ProyectoBase2025.services.PasswordRecoveryService;
import dev.brunob.ProyectoBase2025.services.UserService;
import dev.brunob.ProyectoBase2025.view.FxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.shape.SVGPath;

/**
 * Controlador para la pantalla de login.
 * Gestiona la autenticacion y redirección según el rol del usuario
 * 
 * @author Bruno Ortiz Blanco
 */
@Controller
public class LoginController implements Initializable {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.!#$%&'*+/=?`{|}~-]+@[\\w-]+(?:\\.[\\w-]+)+$");

    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField password;

    @FXML
    private TextField visiblePassword;

    @FXML
    private TextField username;

    @FXML
    private Label lblLogin;

    @FXML
    private Button btnTogglePassword;

    @FXML
    private SVGPath passwordToggleSlash;

    @FXML
    private Hyperlink linkForgotPassword;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordRecoveryService passwordRecoveryService;

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
        stageManager.setCurrentUser(user);

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
        return visiblePassword != null && visiblePassword.isVisible() ? visiblePassword.getText() : password.getText();
    }

    public String getUsername() {
        return username.getText();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        visiblePassword.textProperty().bindBidirectional(password.textProperty());
        updatePasswordVisibility(false);
    }

    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
        boolean showPlainText = !visiblePassword.isVisible();
        updatePasswordVisibility(showPlainText);

        if (showPlainText) {
            visiblePassword.requestFocus();
            visiblePassword.positionCaret(visiblePassword.getText().length());
        } else {
            password.requestFocus();
            password.positionCaret(password.getText().length());
        }
    }

    @FXML
    private void forgotPassword(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog(getUsername() != null ? getUsername().trim() : "");
        dialog.setTitle("Recuperar contraseña");
        dialog.setHeaderText("Introduce el email asociado a tu cuenta");
        dialog.setContentText("Email:");

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) {
            return;
        }

        String email = result.get().trim();
        if (!isValidEmail(email)) {
            showAlert(AlertType.WARNING, "Email no válido", "Introduce un email válido para recuperar la contraseña.");
            return;
        }

        lblLogin.setText("Enviando nueva contraseña...");
        setPasswordRecoveryControlsDisabled(true);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                passwordRecoveryService.resetPasswordAndSendEmail(email);
                return null;
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            setPasswordRecoveryControlsDisabled(false);
            lblLogin.setText("");
            showAlert(AlertType.INFORMATION, "Contraseña restablecida",
                    "Se ha enviado una nueva contraseña al email indicado.");
        });

        task.setOnFailed(workerStateEvent -> {
            setPasswordRecoveryControlsDisabled(false);
            lblLogin.setText("No se pudo restablecer la contraseña");
            showAlert(AlertType.ERROR, "Error al enviar el email", getPasswordRecoveryErrorMessage(task.getException()));
        });

        Thread thread = new Thread(task, "password-recovery");
        thread.setDaemon(true);
        thread.start();
    }

    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private void setPasswordRecoveryControlsDisabled(boolean disabled) {
        if (linkForgotPassword != null) {
            linkForgotPassword.setDisable(disabled);
        }
        if (btnLogin != null) {
            btnLogin.setDisable(disabled);
        }
    }

    private String getPasswordRecoveryErrorMessage(Throwable exception) {
        if (exception instanceof IllegalArgumentException) {
            return exception.getMessage();
        }

        return "No se pudo enviar el correo. Revisa la configuración SMTP y vuelve a intentarlo.";
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updatePasswordVisibility(boolean showPlainText) {
        visiblePassword.setVisible(showPlainText);
        visiblePassword.setManaged(showPlainText);
        password.setVisible(!showPlainText);
        password.setManaged(!showPlainText);

        if (passwordToggleSlash != null) {
            passwordToggleSlash.setVisible(showPlainText);
            passwordToggleSlash.setManaged(showPlainText);
        }

        if (btnTogglePassword != null) {
            btnTogglePassword.setAccessibleText(showPlainText ? "Ocultar contraseña" : "Mostrar contraseña");
        }
    }
}
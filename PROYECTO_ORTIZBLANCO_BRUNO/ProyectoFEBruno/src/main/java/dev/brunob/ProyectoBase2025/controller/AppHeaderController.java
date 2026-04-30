package dev.brunob.ProyectoBase2025.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controlador del componente visual reutilizable {@code AppHeader.fxml}.
 *
 * <p>Sirve como cabecera común de todas las pantallas internas de la
 * aplicación (logo, título, datos del usuario y botón de acción primaria),
 * de forma que el aspecto y la posición sean consistentes en todas las
 * vistas y la guía de estilos resulte coherente.</p>
 *
 * <p>Está marcado con {@code @Scope("prototype")} porque cada
 * {@code <fx:include>} debe recibir su propia instancia del controlador
 * (los beans Spring son singleton por defecto).</p>
 *
 * @author Bruno Ortiz Blanco
 */
@Controller
@Scope("prototype")
public class AppHeaderController {

    @FXML private Label lblTitulo;
    @FXML private Label lblUserInfo;
    @FXML private Button btnAccion;

    /**
     * Establece el título principal de la pantalla.
     */
    public void setTitulo(String titulo) {
        if (lblTitulo != null) {
            lblTitulo.setText(titulo == null ? "" : titulo);
        }
    }

    /**
     * Establece la línea con la información del usuario autenticado.
     */
    public void setUserInfo(String info) {
        if (lblUserInfo != null) {
            lblUserInfo.setText(info == null ? "" : info);
        }
    }

    /**
     * Configura el botón de acción primaria del encabezado: texto y manejador.
     */
    public void setAccion(String texto, EventHandler<ActionEvent> handler) {
        if (btnAccion != null) {
            btnAccion.setText(texto);
            btnAccion.setOnAction(handler);
        }
    }
}

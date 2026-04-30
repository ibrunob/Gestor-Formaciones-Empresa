package dev.brunob.ProyectoBase2025.controller;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import dev.brunob.ProyectoBase2025.config.StageManager;
import dev.brunob.ProyectoBase2025.modelo.Asistencia;
import dev.brunob.ProyectoBase2025.modelo.Estudiante;
import dev.brunob.ProyectoBase2025.modelo.User;
import dev.brunob.ProyectoBase2025.services.AsistenciaService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

/**
 * Vista del Estudiante: consulta su historial de asistencia y puede justificar
 * una falta seleccionando el registro y enviando una explicación.
 */
@Controller
public class MiAsistenciaController extends BaseMenuController implements Initializable {

    @Override
    protected String getPaginaAyuda() { return "mi-asistencia.html"; }

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML private Label lblTotal;
    @FXML private Label lblPresentes;
    @FXML private Label lblFaltas;
    @FXML private Label lblJustificadas;
    @FXML private Label lblFaltaSel;

    @FXML private TableView<Asistencia> historialTable;
    @FXML private TableColumn<Asistencia, String> colFecha;
    @FXML private TableColumn<Asistencia, String> colEstado;
    @FXML private TableColumn<Asistencia, String> colJustificada;
    @FXML private TableColumn<Asistencia, String> colMotivo;
    @FXML private TableColumn<Asistencia, String> colAutor;

    @FXML private TextArea txtJustificacion;
    @FXML private Button btnJustificar;
    @FXML private Button btnLimpiar;

    @Lazy @Autowired private StageManager stageManager;
    @Autowired private AsistenciaService asistenciaService;

    private final ObservableList<Asistencia> historial = FXCollections.observableArrayList();
    private Asistencia seleccionada;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHeader("Mi Asistencia");

        colFecha.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getFecha() != null ? c.getValue().getFecha().format(FMT) : ""));
        colEstado.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                Boolean.TRUE.equals(c.getValue().getPresente()) ? "Presente" : "Falta"));
        colJustificada.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                Boolean.TRUE.equals(c.getValue().getJustificada()) ? "Sí" : "No"));
        colMotivo.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getMotivo() != null ? c.getValue().getMotivo() : ""));
        colAutor.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getRegistradoPor() != null ? nombreCompleto(c.getValue().getRegistradoPor()) : ""));

        historialTable.setItems(historial);
        historialTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        historialTable.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> seleccionar(n));
    }

    @Override
    public void setCurrentUser(User user) {
        super.setCurrentUser(user);
        cargarHistorial();
    }

    private void cargarHistorial() {
        historial.clear();
        seleccionada = null;
        lblFaltaSel.setText("—");
        txtJustificacion.clear();
        btnJustificar.setDisable(true);
        if (!(currentUser instanceof Estudiante)) {
            actualizarResumen();
            return;
        }
        List<Asistencia> lista = asistenciaService.findByEstudiante(currentUser.getId());
        historial.addAll(lista);
        actualizarResumen();
    }

    private void actualizarResumen() {
        long total = historial.size();
        long presentes = historial.stream().filter(a -> Boolean.TRUE.equals(a.getPresente())).count();
        long faltas = total - presentes;
        long justificadas = historial.stream().filter(a -> !Boolean.TRUE.equals(a.getPresente())
                && Boolean.TRUE.equals(a.getJustificada())).count();
        lblTotal.setText("Total: " + total);
        lblPresentes.setText("Presentes: " + presentes);
        lblFaltas.setText("Faltas: " + faltas);
        lblJustificadas.setText("Justificadas: " + justificadas);
    }

    private void seleccionar(Asistencia a) {
        seleccionada = a;
        if (a == null) {
            lblFaltaSel.setText("—");
            txtJustificacion.clear();
            btnJustificar.setDisable(true);
            return;
        }
        String estado = Boolean.TRUE.equals(a.getPresente()) ? "Presente" : "Falta";
        lblFaltaSel.setText(estado + " del " + (a.getFecha() != null ? a.getFecha().format(FMT) : "?")
                + (Boolean.TRUE.equals(a.getJustificada()) ? "  (ya justificada)" : ""));
        txtJustificacion.setText(a.getMotivo() != null ? a.getMotivo() : "");
        // Solo se puede justificar si es una falta
        boolean esFalta = !Boolean.TRUE.equals(a.getPresente());
        btnJustificar.setDisable(!esFalta);
    }

    @FXML
    private void justificar(ActionEvent event) {
        if (seleccionada == null) {
            info("Seleccione una falta del historial.");
            return;
        }
        if (Boolean.TRUE.equals(seleccionada.getPresente())) {
            info("Solo puede justificar registros de falta.");
            return;
        }
        String texto = txtJustificacion.getText() != null ? txtJustificacion.getText().trim() : "";
        if (texto.isEmpty()) {
            info("Escriba el motivo de la justificación.");
            return;
        }
        seleccionada.setJustificada(true);
        seleccionada.setMotivo(texto);
        asistenciaService.save(seleccionada);
        info("Justificación enviada.");
        cargarHistorial();
    }

    @FXML
    private void reset(ActionEvent event) {
        historialTable.getSelectionModel().clearSelection();
        txtJustificacion.clear();
    }

    @FXML
    private void volverMenu(ActionEvent event) {
        volverAlMenuPorRol();
    }

    private String nombreCompleto(User u) {
        if (u == null) return "";
        String fn = u.getFirstName() != null ? u.getFirstName() : "";
        String ln = u.getLastName() != null ? u.getLastName() : "";
        return (fn + " " + ln).trim();
    }

    private void info(String msg) {
        Alert a = new Alert(AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}

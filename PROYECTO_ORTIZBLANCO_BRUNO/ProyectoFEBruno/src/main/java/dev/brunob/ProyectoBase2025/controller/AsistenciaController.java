package dev.brunob.ProyectoBase2025.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import dev.brunob.ProyectoBase2025.config.StageManager;
import dev.brunob.ProyectoBase2025.modelo.Administrador;
import dev.brunob.ProyectoBase2025.modelo.Asistencia;
import dev.brunob.ProyectoBase2025.modelo.Estudiante;
import dev.brunob.ProyectoBase2025.modelo.FormacionEmpresa;
import dev.brunob.ProyectoBase2025.modelo.Profesor;
import dev.brunob.ProyectoBase2025.modelo.Tutor;
import dev.brunob.ProyectoBase2025.modelo.User;
import dev.brunob.ProyectoBase2025.services.AsistenciaService;
import dev.brunob.ProyectoBase2025.services.EstudianteService;
import dev.brunob.ProyectoBase2025.services.FormacionEmpresaService;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Caso de uso: Registrar asistencia de los estudiantes asignados a la formación
 * en empresa. El profesor puede registrar/editar/eliminar; el resto consulta.
 */
@Controller
public class AsistenciaController extends BaseMenuController implements Initializable {

    @Override
    protected String getPaginaAyuda() { return "asistencia.html"; }

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML private Label lblScope;
    @FXML private Label lblEstudianteSel;

    @FXML private TableView<Estudiante> estudiantesTable;
    @FXML private TableColumn<Estudiante, Long> colEstId;
    @FXML private TableColumn<Estudiante, String> colEstNombre;
    @FXML private TableColumn<Estudiante, String> colEstCurso;

    @FXML private TableView<Asistencia> historialTable;
    @FXML private TableColumn<Asistencia, String> colFecha;
    @FXML private TableColumn<Asistencia, String> colEstado;
    @FXML private TableColumn<Asistencia, String> colJustificada;
    @FXML private TableColumn<Asistencia, String> colMotivo;
    @FXML private TableColumn<Asistencia, String> colAutor;

    @FXML private DatePicker dpFecha;
    @FXML private CheckBox chkPresente;
    @FXML private CheckBox chkJustificada;
    @FXML private TextArea txtMotivo;
    @FXML private Button btnGuardar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    @Lazy @Autowired private StageManager stageManager;
    @Autowired private EstudianteService estudianteService;
    @Autowired private FormacionEmpresaService formacionEmpresaService;
    @Autowired private AsistenciaService asistenciaService;

    private final ObservableList<Estudiante> estudiantes = FXCollections.observableArrayList();
    private final ObservableList<Asistencia> historial = FXCollections.observableArrayList();

    private Estudiante seleccionado;
    private Asistencia editando;
    private boolean soloLectura;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHeader("Registro de Asistencia");

        colEstId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEstNombre.setCellValueFactory(c -> new ReadOnlyStringWrapper(nombreCompleto(c.getValue())));
        colEstCurso.setCellValueFactory(c -> new ReadOnlyStringWrapper(safeNombreCurso(c.getValue())));
        estudiantesTable.setItems(estudiantes);
        estudiantesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        estudiantesTable.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> seleccionarEstudiante(n));

        colFecha.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getFecha() != null ? c.getValue().getFecha().format(FMT) : ""));
        colEstado.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                Boolean.TRUE.equals(c.getValue().getPresente()) ? "Presente" : "Falta"));
        colJustificada.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                Boolean.TRUE.equals(c.getValue().getJustificada()) ? "Sí" : "No"));
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colAutor.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getRegistradoPor() != null ? nombreCompleto(c.getValue().getRegistradoPor()) : ""));
        historialTable.setItems(historial);
        historialTable.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> cargarEnForm(n));

        dpFecha.setValue(LocalDate.now());
    }

    @Override
    public void setCurrentUser(User user) {
        super.setCurrentUser(user);
        configurarPermisos();
        cargarEstudiantes();
    }

    private void configurarPermisos() {
        soloLectura = !(currentUser instanceof Profesor) && !(currentUser instanceof Tutor);
        boolean editable = !soloLectura;
        dpFecha.setDisable(!editable);
        chkPresente.setDisable(!editable);
        chkJustificada.setDisable(!editable);
        txtMotivo.setDisable(!editable);
        btnGuardar.setDisable(!editable);
        btnEliminar.setDisable(!editable);
        btnLimpiar.setDisable(!editable);

        if (currentUser instanceof Profesor) {
            lblScope.setText("Mostrando los estudiantes con formación en empresa bajo su tutoría docente.");
        } else if (currentUser instanceof Tutor) {
            lblScope.setText("Mostrando los estudiantes asignados a su tutoría en la empresa.");
        } else if (currentUser instanceof Administrador) {
            lblScope.setText("Acceso de sólo lectura: todos los estudiantes con asistencia registrada.");
        } else {
            lblScope.setText("Acceso de sólo lectura.");
        }
    }

    private void cargarEstudiantes() {
        estudiantes.clear();
        historial.clear();
        seleccionado = null;
        editando = null;
        lblEstudianteSel.setText("—");

        List<Estudiante> lista = new ArrayList<>();
        if (currentUser instanceof Profesor) {
            List<FormacionEmpresa> fes = formacionEmpresaService.findByProfesor(currentUser.getId());
            Set<Long> ids = new HashSet<>();
            for (FormacionEmpresa fe : fes) {
                Estudiante e = fe.getEstudiante();
                if (e != null && ids.add(e.getId())) lista.add(e);
            }
        } else if (currentUser instanceof Tutor) {
            List<FormacionEmpresa> fes = formacionEmpresaService.findByTutor(currentUser.getId());
            Set<Long> ids = new HashSet<>();
            for (FormacionEmpresa fe : fes) {
                Estudiante e = fe.getEstudiante();
                if (e != null && ids.add(e.getId())) lista.add(e);
            }
        } else {
            lista.addAll(estudianteService.findAll());
        }
        lista.sort(Comparator.comparing(this::nombreCompleto, String.CASE_INSENSITIVE_ORDER));
        estudiantes.addAll(lista);
    }

    private void seleccionarEstudiante(Estudiante est) {
        seleccionado = est;
        editando = null;
        if (est == null) {
            lblEstudianteSel.setText("—");
            historial.clear();
            return;
        }
        lblEstudianteSel.setText(nombreCompleto(est));
        cargarHistorial(est);
        limpiarFormulario(false);
    }

    private void cargarHistorial(Estudiante est) {
        historial.clear();
        historial.addAll(asistenciaService.findByEstudiante(est.getId()));
    }

    private void cargarEnForm(Asistencia a) {
        if (a == null) return;
        editando = soloLectura ? null : a;
        dpFecha.setValue(a.getFecha());
        chkPresente.setSelected(Boolean.TRUE.equals(a.getPresente()));
        chkJustificada.setSelected(Boolean.TRUE.equals(a.getJustificada()));
        txtMotivo.setText(a.getMotivo() != null ? a.getMotivo() : "");
    }

    @FXML
    private void guardar(ActionEvent event) {
        if (soloLectura) return;
        if (seleccionado == null) { info("Seleccione un estudiante."); return; }
        if (dpFecha.getValue() == null) { info("Indique la fecha."); return; }

        Asistencia a = editando != null ? editando : new Asistencia();
        a.setEstudiante(seleccionado);
        a.setFecha(dpFecha.getValue());
        a.setPresente(chkPresente.isSelected());
        a.setJustificada(chkJustificada.isSelected());
        a.setMotivo(txtMotivo.getText());
        if (a.getRegistradoPor() == null) a.setRegistradoPor(currentUser);
        asistenciaService.save(a);

        cargarHistorial(seleccionado);
        limpiarFormulario(false);
        info(editando != null ? "Asistencia actualizada." : "Asistencia registrada.");
    }

    @FXML
    private void eliminar(ActionEvent event) {
        if (soloLectura || editando == null) {
            info("Seleccione un registro del historial para eliminarlo.");
            return;
        }
        Alert confirm = new Alert(AlertType.CONFIRMATION,
                "¿Eliminar el registro de asistencia seleccionado?",
                ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText(null);
        confirm.showAndWait().filter(b -> b == ButtonType.OK).ifPresent(b -> {
            asistenciaService.delete(editando);
            editando = null;
            if (seleccionado != null) cargarHistorial(seleccionado);
            limpiarFormulario(false);
        });
    }

    @FXML
    private void reset(ActionEvent event) { limpiarFormulario(true); }

    private void limpiarFormulario(boolean limpiarSeleccionTabla) {
        editando = null;
        dpFecha.setValue(LocalDate.now());
        chkPresente.setSelected(true);
        chkJustificada.setSelected(false);
        txtMotivo.clear();
        if (limpiarSeleccionTabla) historialTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void volverMenu(ActionEvent event) { volverAlMenuPorRol(); }

    private String nombreCompleto(User u) {
        if (u == null) return "";
        String fn = u.getFirstName() != null ? u.getFirstName() : "";
        String ln = u.getLastName() != null ? u.getLastName() : "";
        return (fn + " " + ln).trim();
    }

    private String safeNombreCurso(Estudiante e) {
        try {
            return e.getCurso() != null && e.getCurso().getNombre() != null
                    ? e.getCurso().getNombre() + " (" + e.getCurso().getAnio() + ")"
                    : "—";
        } catch (Exception ex) {
            return "—";
        }
    }

    private void info(String msg) {
        Alert a = new Alert(AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}

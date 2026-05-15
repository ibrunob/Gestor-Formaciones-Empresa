package dev.brunob.ProyectoBase2025.controller;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import dev.brunob.ProyectoBase2025.config.StageManager;
import dev.brunob.ProyectoBase2025.modelo.Documento;
import dev.brunob.ProyectoBase2025.modelo.Estudiante;
import dev.brunob.ProyectoBase2025.modelo.Evaluacion;
import dev.brunob.ProyectoBase2025.modelo.FormacionEmpresa;
import dev.brunob.ProyectoBase2025.modelo.User;
import dev.brunob.ProyectoBase2025.services.DocumentoService;
import dev.brunob.ProyectoBase2025.services.EvaluacionService;
import dev.brunob.ProyectoBase2025.services.FormacionEmpresaService;
import dev.brunob.ProyectoBase2025.view.FxmlView;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Caso de uso: Consultar información personal y de formación.
 * Vista de solo lectura para el estudiante.
 */
@Controller
public class MiInformacionController extends BaseMenuController implements Initializable {

    @Override
    protected String getPaginaAyuda() { return "mi-informacion.html"; }


    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML private Label lblNombre;
    @FXML private Label lblEmail;
    @FXML private Label lblDob;
    @FXML private Label lblGenero;
    @FXML private Label lblCurso;
    @FXML private Label lblCiclo;

    @FXML private Label lblFEEstado;
    @FXML private Label lblFEFechas;
    @FXML private Label lblFEEmpresa;
    @FXML private Label lblFETutorEmp;
    @FXML private Label lblFETelTutor;
    @FXML private Label lblFEProfesor;

    @FXML private TableView<Documento> documentosTable;
    @FXML private TableColumn<Documento, String> colDocNombre;
    @FXML private TableColumn<Documento, String> colDocTipo;
    @FXML private TableColumn<Documento, String> colDocFecha;
    @FXML private TableColumn<Documento, String> colDocSubidoPor;

    @FXML private TableView<Evaluacion> evaluacionesTable;
    @FXML private TableColumn<Evaluacion, String> colEvFecha;
    @FXML private TableColumn<Evaluacion, String> colEvTutor;
    @FXML private TableColumn<Evaluacion, String> colEvActitud;
    @FXML private TableColumn<Evaluacion, String> colEvPuntualidad;
    @FXML private TableColumn<Evaluacion, String> colEvCompetencias;
    @FXML private TableColumn<Evaluacion, String> colEvValoracion;
    @FXML private TableColumn<Evaluacion, String> colEvObs;

    @Lazy @Autowired private StageManager stageManager;
    @Autowired private FormacionEmpresaService formacionEmpresaService;
    @Autowired private DocumentoService documentoService;
    @Autowired private EvaluacionService evaluacionService;

    private final ObservableList<Documento> docs = FXCollections.observableArrayList();
    private final ObservableList<Evaluacion> evals = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHeader("Mi Información");
        colDocNombre.setCellValueFactory(c -> str(c.getValue().getNombre()));
        colDocTipo.setCellValueFactory(c -> str(c.getValue().getTipo()));
        colDocFecha.setCellValueFactory(c -> str(c.getValue().getFechaSubida() != null
                ? c.getValue().getFechaSubida().format(FMT) : ""));
        colDocSubidoPor.setCellValueFactory(c -> str(nombreCompleto(c.getValue().getSubidoPor())));
        documentosTable.setItems(docs);

        colEvFecha.setCellValueFactory(c -> str(c.getValue().getFecha() != null ? c.getValue().getFecha().format(FMT) : ""));
        colEvTutor.setCellValueFactory(c -> str(nombreCompleto(c.getValue().getTutor())));
        colEvActitud.setCellValueFactory(c -> str(formatNum(c.getValue().getActitud())));
        colEvPuntualidad.setCellValueFactory(c -> str(formatNum(c.getValue().getPuntualidad())));
        colEvCompetencias.setCellValueFactory(c -> str(formatNum(c.getValue().getCompetencias())));
        colEvValoracion.setCellValueFactory(c -> str(formatNum(c.getValue().getValoracionGeneral())));
        colEvObs.setCellValueFactory(c -> str(c.getValue().getObservaciones()));
        evaluacionesTable.setItems(evals);
    }

    @Override
    public void setCurrentUser(User user) {
        super.setCurrentUser(user);
        cargarDatos();
    }

    private void cargarDatos() {
        docs.clear();
        evals.clear();
        if (currentUser == null) return;

        lblNombre.setText(nombreCompleto(currentUser));
        lblEmail.setText(safe(currentUser.getEmail()));
        lblDob.setText(currentUser.getDob() != null ? currentUser.getDob().format(FMT) : "—");
        lblGenero.setText(safe(currentUser.getGender()));

        if (!(currentUser instanceof Estudiante)) {
            lblCurso.setText("—");
            lblCiclo.setText("—");
            limpiarFormacion();
            return;
        }
        Estudiante est = (Estudiante) currentUser;

        if (est.getCurso() != null) {
            lblCurso.setText(safe(est.getCurso().getNombre()));
            if (est.getCurso().getCicloFormativo() != null) {
                lblCiclo.setText(safe(est.getCurso().getCicloFormativo().getNombre()));
            } else {
                lblCiclo.setText("—");
            }
        } else {
            lblCurso.setText("—");
            lblCiclo.setText("—");
        }

        List<FormacionEmpresa> fes = formacionEmpresaService.findByEstudiante(est.getId());
        FormacionEmpresa actual = elegirActual(fes);
        if (actual == null) {
            limpiarFormacion();
        } else {
            lblFEEstado.setText(safe(actual.getEstado()));
            String ini = actual.getFechaInicio() != null ? actual.getFechaInicio().format(FMT) : "—";
            String fin = actual.getFechaFin() != null ? actual.getFechaFin().format(FMT) : "—";
            lblFEFechas.setText(ini + " - " + fin);
            String empresa = "—";
            String tutorNombre = "—";
            String tutorTel = "—";
            if (actual.getTutor() != null) {
                tutorNombre = nombreCompleto(actual.getTutor());
                tutorTel = safe(actual.getTutor().getTelefono());
                if (actual.getTutor().getEmpresa() != null) {
                    empresa = safe(actual.getTutor().getEmpresa().getNombre());
                }
            }
            lblFEEmpresa.setText(empresa);
            lblFETutorEmp.setText(tutorNombre);
            lblFETelTutor.setText(tutorTel);
            lblFEProfesor.setText(actual.getProfesor() != null ? nombreCompleto(actual.getProfesor()) : "—");
        }

        List<Documento> ldocs = documentoService.findByEstudiante(est.getId());
        ldocs.sort(Comparator.comparing((Documento d) -> d.getFechaSubida() != null
                ? d.getFechaSubida().toEpochDay() : Long.MIN_VALUE).reversed());
        docs.setAll(ldocs);

        List<Evaluacion> levals = evaluacionService.findByEstudiante(est.getId());
        evals.setAll(levals);
    }

    private FormacionEmpresa elegirActual(List<FormacionEmpresa> fes) {
        if (fes == null || fes.isEmpty()) return null;
        return fes.stream()
                .filter(f -> f.getEstado() != null && f.getEstado().toLowerCase().contains("curso"))
                .findFirst()
                .orElse(fes.get(0));
    }

    private void limpiarFormacion() {
        lblFEEstado.setText("—");
        lblFEFechas.setText("—");
        lblFEEmpresa.setText("—");
        lblFETutorEmp.setText("—");
        lblFETelTutor.setText("—");
        lblFEProfesor.setText("—");
    }

    @FXML
    private void abrirDocumento(ActionEvent event) {
        Documento d = documentosTable.getSelectionModel().getSelectedItem();
        if (d == null) {
            info("Seleccione un documento del listado.");
            return;
        }
        if (d.getRuta() == null || d.getRuta().isEmpty()) {
            info("Este documento no tiene archivo asociado.");
            return;
        }
        File f = new File(d.getRuta());
        if (!f.exists()) {
            info("El archivo no se encuentra en la ruta indicada:\n" + d.getRuta());
            return;
        }
        try {
            Desktop.getDesktop().open(f);
        } catch (Exception ex) {
            info("No se pudo abrir el archivo: " + ex.getMessage());
        }
    }

    @FXML
    private void volverMenu(ActionEvent event) {
        stageManager.switchScene(FxmlView.MENU_ESTUDIANTE);
    }

    private static ReadOnlyStringWrapper str(String s) {
        return new ReadOnlyStringWrapper(s != null ? s : "");
    }

    private static String formatNum(Integer i) {
        return i == null ? "—" : i.toString();
    }

    private static String safe(String s) { return s != null ? s : "—"; }

    private static String nombreCompleto(User u) {
        if (u == null) return "—";
        String fn = u.getFirstName() != null ? u.getFirstName() : "";
        String ln = u.getLastName() != null ? u.getLastName() : "";
        String r = (fn + " " + ln).trim();
        return r.isEmpty() ? "—" : r;
    }

    private void info(String msg) {
        Alert a = new Alert(AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}

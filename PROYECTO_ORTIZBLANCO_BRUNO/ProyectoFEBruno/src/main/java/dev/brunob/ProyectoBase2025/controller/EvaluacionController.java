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
import dev.brunob.ProyectoBase2025.modelo.Estudiante;
import dev.brunob.ProyectoBase2025.modelo.Evaluacion;
import dev.brunob.ProyectoBase2025.modelo.FormacionEmpresa;
import dev.brunob.ProyectoBase2025.modelo.Profesor;
import dev.brunob.ProyectoBase2025.modelo.Tutor;
import dev.brunob.ProyectoBase2025.modelo.User;
import dev.brunob.ProyectoBase2025.services.EstudianteService;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Caso de uso: Evaluar y Supervisar Estudiantes.
 *
 * Tutor de Empresa: ve sus estudiantes asignados y registra evaluaciones (actitud,
 * puntualidad, competencias, valoracion general y observaciones).
 * Profesor / Administrador: acceso de solo lectura al historial de evaluaciones.
 */
@Controller
public class EvaluacionController extends BaseMenuController implements Initializable {

    @Override
    protected String getPaginaAyuda() { return "evaluaciones.html"; }


    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final ObservableList<Integer> ESCALA = FXCollections.observableArrayList(1, 2, 3, 4, 5);

    @FXML private Label lblScopeInfo;
    @FXML private TableView<Estudiante> estudiantesTable;
    @FXML private TableColumn<Estudiante, Long> colEstId;
    @FXML private TableColumn<Estudiante, String> colEstNombre;
    @FXML private TableColumn<Estudiante, String> colEstCurso;

    @FXML private Label lblFichaNombre;
    @FXML private Label lblFichaEmail;
    @FXML private Label lblFichaCurso;
    @FXML private Label lblFichaFE;

    @FXML private TableView<Evaluacion> evaluacionesTable;
    @FXML private TableColumn<Evaluacion, String> colEvFecha;
    @FXML private TableColumn<Evaluacion, String> colEvTutor;
    @FXML private TableColumn<Evaluacion, Integer> colEvActitud;
    @FXML private TableColumn<Evaluacion, Integer> colEvPuntualidad;
    @FXML private TableColumn<Evaluacion, Integer> colEvCompetencias;
    @FXML private TableColumn<Evaluacion, Integer> colEvValoracion;
    @FXML private TableColumn<Evaluacion, String> colEvObs;

    @FXML private Label lblEstudianteSel;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<Integer> cbActitud;
    @FXML private ComboBox<Integer> cbPuntualidad;
    @FXML private ComboBox<Integer> cbCompetencias;
    @FXML private ComboBox<Integer> cbValoracion;
    @FXML private TextArea txtObservaciones;
    @FXML private Button btnGuardar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    @Lazy @Autowired private StageManager stageManager;

    @Autowired private EstudianteService estudianteService;
    @Autowired private FormacionEmpresaService formacionEmpresaService;
    @Autowired private EvaluacionService evaluacionService;

    private final ObservableList<Estudiante> estudiantes = FXCollections.observableArrayList();
    private final ObservableList<Evaluacion> evaluaciones = FXCollections.observableArrayList();

    private Estudiante seleccionado;
    private Evaluacion editando;
    private boolean soloLectura;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHeader("Evaluación y Supervisión de Estudiantes");
        colEstId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEstNombre.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                nombreCompleto(c.getValue())));
        colEstCurso.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue() != null && c.getValue().getCurso() != null
                        ? safeNombreCurso(c.getValue())
                        : "—"));

        estudiantesTable.setItems(estudiantes);
        estudiantesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        estudiantesTable.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            seleccionarEstudiante(n);
        });

        colEvFecha.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getFecha() != null ? c.getValue().getFecha().format(FMT) : ""));
        colEvTutor.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                autor(c.getValue())));
        colEvActitud.setCellValueFactory(new PropertyValueFactory<>("actitud"));
        colEvPuntualidad.setCellValueFactory(new PropertyValueFactory<>("puntualidad"));
        colEvCompetencias.setCellValueFactory(new PropertyValueFactory<>("competencias"));
        colEvValoracion.setCellValueFactory(new PropertyValueFactory<>("valoracionGeneral"));
        colEvObs.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        evaluacionesTable.setItems(evaluaciones);
        evaluacionesTable.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            cargarEvaluacionEnForm(n);
        });

        cbActitud.setItems(ESCALA);
        cbPuntualidad.setItems(ESCALA);
        cbCompetencias.setItems(ESCALA);
        cbValoracion.setItems(ESCALA);

        dpFecha.setValue(LocalDate.now());
    }

    @Override
    public void setCurrentUser(User user) {
        super.setCurrentUser(user);
        configurarPermisos();
        cargarEstudiantes();
    }

    private void configurarPermisos() {
        soloLectura = !(currentUser instanceof Tutor) && !(currentUser instanceof Profesor);
        // Tutor de Empresa y Profesor: edicion. Administrador: lectura.
        boolean editable = !soloLectura;
        dpFecha.setDisable(!editable);
        cbActitud.setDisable(!editable);
        cbPuntualidad.setDisable(!editable);
        cbCompetencias.setDisable(!editable);
        cbValoracion.setDisable(!editable);
        txtObservaciones.setDisable(!editable);
        btnGuardar.setDisable(!editable);
        btnEliminar.setDisable(!editable);
        btnLimpiar.setDisable(!editable);

        if (currentUser instanceof Tutor) {
            lblScopeInfo.setText("Mostrando los estudiantes asignados a su tutoría.");
        } else if (currentUser instanceof Profesor) {
            lblScopeInfo.setText("Puede registrar calificaciones de los estudiantes a su cargo.");
        } else if (currentUser instanceof Administrador) {
            lblScopeInfo.setText("Acceso de solo lectura: todos los estudiantes.");
        } else {
            lblScopeInfo.setText("");
        }
    }

    private void cargarEstudiantes() {
        estudiantes.clear();
        evaluaciones.clear();
        seleccionado = null;
        editando = null;
        actualizarFicha(null);
        lblEstudianteSel.setText("—");

        if (currentUser instanceof Tutor) {
            Tutor tutor = (Tutor) currentUser;
            List<FormacionEmpresa> fes = formacionEmpresaService.findByTutor(tutor.getId());
            Set<Long> ids = new HashSet<>();
            List<Estudiante> lista = new ArrayList<>();
            for (FormacionEmpresa fe : fes) {
                Estudiante e = fe.getEstudiante();
                if (e != null && ids.add(e.getId())) {
                    lista.add(e);
                }
            }
            lista.sort(Comparator.comparing(this::nombreCompleto, String.CASE_INSENSITIVE_ORDER));
            estudiantes.addAll(lista);
        } else {
            List<Estudiante> lista = estudianteService.findAll();
            lista.sort(Comparator.comparing(this::nombreCompleto, String.CASE_INSENSITIVE_ORDER));
            estudiantes.addAll(lista);
        }
    }

    private void seleccionarEstudiante(Estudiante est) {
        seleccionado = est;
        editando = null;
        if (est == null) {
            lblEstudianteSel.setText("—");
            evaluaciones.clear();
            actualizarFicha(null);
            return;
        }
        lblEstudianteSel.setText(nombreCompleto(est));
        actualizarFicha(est);
        cargarEvaluaciones(est);
        limpiarFormulario(false);
    }

    private void cargarEvaluaciones(Estudiante est) {
        evaluaciones.clear();
        evaluaciones.addAll(evaluacionService.findByEstudiante(est.getId()));
    }

    private void cargarEvaluacionEnForm(Evaluacion ev) {
        if (ev == null) {
            return;
        }
        // Solo el autor (tutor o profesor) puede editar / eliminar la suya.
        boolean esAutor = !soloLectura && currentUser != null && (
                (ev.getTutor() != null && ev.getTutor().getId() == currentUser.getId())
             || (ev.getProfesor() != null && ev.getProfesor().getId() == currentUser.getId()));
        editando = esAutor ? ev : null;
        dpFecha.setValue(ev.getFecha());
        cbActitud.setValue(ev.getActitud());
        cbPuntualidad.setValue(ev.getPuntualidad());
        cbCompetencias.setValue(ev.getCompetencias());
        cbValoracion.setValue(ev.getValoracionGeneral());
        txtObservaciones.setText(ev.getObservaciones() != null ? ev.getObservaciones() : "");
    }

    private void actualizarFicha(Estudiante est) {
        if (est == null) {
            lblFichaNombre.setText("—");
            lblFichaEmail.setText("");
            lblFichaCurso.setText("");
            lblFichaFE.setText("");
            return;
        }
        lblFichaNombre.setText(nombreCompleto(est));
        lblFichaEmail.setText(est.getEmail() != null ? "Email: " + est.getEmail() : "");
        lblFichaCurso.setText("Curso: " + safeNombreCurso(est));

        List<FormacionEmpresa> fes = formacionEmpresaService.findByEstudiante(est.getId());
        FormacionEmpresa actual = fes.stream()
                .max(Comparator.comparing(f -> f.getFechaInicio() != null ? f.getFechaInicio() : LocalDate.MIN))
                .orElse(null);
        if (actual == null) {
            lblFichaFE.setText("Sin formación en empresa registrada.");
        } else {
            String empresa = actual.getTutor() != null && actual.getTutor().getEmpresa() != null
                    ? safeNombreEmpresa(actual)
                    : "—";
            String estado = actual.getEstado() != null ? actual.getEstado() : "—";
            String fechas = (actual.getFechaInicio() != null ? actual.getFechaInicio().format(FMT) : "?")
                    + " → "
                    + (actual.getFechaFin() != null ? actual.getFechaFin().format(FMT) : "?");
            lblFichaFE.setText("Empresa: " + empresa + "\nEstado: " + estado + "\nPeriodo: " + fechas);
        }
    }

    @FXML
    private void guardar(ActionEvent event) {
        if (soloLectura) {
            return;
        }
        if (seleccionado == null) {
            info("Seleccione un estudiante en la lista de la izquierda.");
            return;
        }
        if (!(currentUser instanceof Tutor) && !(currentUser instanceof Profesor)) {
            info("Solo el Tutor de Empresa o el Profesor pueden registrar evaluaciones.");
            return;
        }
        if (dpFecha.getValue() == null) {
            info("Indique la fecha de la evaluación.");
            return;
        }
        Evaluacion ev = editando != null ? editando : new Evaluacion();
        ev.setEstudiante(seleccionado);
        if (currentUser instanceof Tutor) {
            ev.setTutor((Tutor) currentUser);
        } else if (currentUser instanceof Profesor) {
            ev.setProfesor((Profesor) currentUser);
        }
        ev.setFecha(dpFecha.getValue());
        ev.setActitud(cbActitud.getValue());
        ev.setPuntualidad(cbPuntualidad.getValue());
        ev.setCompetencias(cbCompetencias.getValue());
        ev.setValoracionGeneral(cbValoracion.getValue());
        ev.setObservaciones(txtObservaciones.getText());
        evaluacionService.save(ev);

        cargarEvaluaciones(seleccionado);
        limpiarFormulario(false);
        info(editando != null ? "Evaluación actualizada." : "Evaluación registrada.");
    }

    @FXML
    private void eliminar(ActionEvent event) {
        if (soloLectura || editando == null) {
            info("Seleccione una evaluación propia del historial para eliminarla.");
            return;
        }
        Alert confirm = new Alert(AlertType.CONFIRMATION,
                "¿Eliminar la evaluación seleccionada?", ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText(null);
        confirm.showAndWait().filter(b -> b == ButtonType.OK).ifPresent(b -> {
            evaluacionService.delete(editando);
            editando = null;
            if (seleccionado != null) {
                cargarEvaluaciones(seleccionado);
            }
            limpiarFormulario(false);
        });
    }

    @FXML
    private void reset(ActionEvent event) {
        limpiarFormulario(true);
    }

    private void limpiarFormulario(boolean limpiarSeleccionTabla) {
        editando = null;
        dpFecha.setValue(LocalDate.now());
        cbActitud.setValue(null);
        cbPuntualidad.setValue(null);
        cbCompetencias.setValue(null);
        cbValoracion.setValue(null);
        txtObservaciones.clear();
        if (limpiarSeleccionTabla) {
            evaluacionesTable.getSelectionModel().clearSelection();
        }
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

    private String autor(Evaluacion ev) {
        if (ev == null) return "";
        if (ev.getTutor() != null) return nombreCompleto(ev.getTutor()) + " (Tutor)";
        if (ev.getProfesor() != null) return nombreCompleto(ev.getProfesor()) + " (Profesor)";
        return "";
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

    private String safeNombreEmpresa(FormacionEmpresa fe) {
        try {
            return fe.getTutor().getEmpresa().getNombre();
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

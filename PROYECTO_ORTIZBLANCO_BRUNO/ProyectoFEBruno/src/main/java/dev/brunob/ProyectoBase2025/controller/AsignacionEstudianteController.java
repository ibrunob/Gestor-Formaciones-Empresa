package dev.brunob.ProyectoBase2025.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import dev.brunob.ProyectoBase2025.config.StageManager;
import dev.brunob.ProyectoBase2025.modelo.Administrador;
import dev.brunob.ProyectoBase2025.modelo.Curso;
import dev.brunob.ProyectoBase2025.modelo.Empresa;
import dev.brunob.ProyectoBase2025.modelo.Estudiante;
import dev.brunob.ProyectoBase2025.modelo.FormacionEmpresa;
import dev.brunob.ProyectoBase2025.modelo.Profesor;
import dev.brunob.ProyectoBase2025.modelo.Tutor;
import dev.brunob.ProyectoBase2025.modelo.User;
import dev.brunob.ProyectoBase2025.services.CursoService;
import dev.brunob.ProyectoBase2025.services.EmpresaService;
import dev.brunob.ProyectoBase2025.services.EstudianteService;
import dev.brunob.ProyectoBase2025.services.FormacionEmpresaService;
import dev.brunob.ProyectoBase2025.services.ProfesorService;
import dev.brunob.ProyectoBase2025.services.TutorService;
import dev.brunob.ProyectoBase2025.view.FxmlView;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Gestionar Asignaciones de Estudiantes a empresa + tutor de empresa.
 * Admin: ve todos los estudiantes y todos los cursos.
 * Profesor (coordinador): ve los estudiantes de los cursos donde imparte algún módulo.
 * Permite asignar empresa+tutor o reasignar (con motivo del cambio) cuando ya existe.
 */
@Controller
public class AsignacionEstudianteController extends BaseMenuController implements Initializable {

    @Override
    protected String getPaginaAyuda() { return "asignaciones.html"; }


    private static final List<String> ESTADOS = List.of("Pendiente", "En curso", "Finalizada", "Cancelada");
    private static final String CURSO_TODOS = "__TODOS__";

    @FXML private ComboBox<Curso> cbFiltroCurso;
    @FXML private CheckBox chkSoloSinAsignar;

    @FXML private TableView<EstudianteRow> estudiantesTable;
    @FXML private TableColumn<EstudianteRow, Long> colId;
    @FXML private TableColumn<EstudianteRow, String> colNombre;
    @FXML private TableColumn<EstudianteRow, String> colCurso;
    @FXML private TableColumn<EstudianteRow, String> colEmpresaActual;
    @FXML private TableColumn<EstudianteRow, String> colTutorActual;
    @FXML private TableColumn<EstudianteRow, String> colEstadoActual;
    @FXML private TableColumn<EstudianteRow, Boolean> colSeleccionar;

    @FXML private Label lblEstudiante;
    @FXML private Label lblAsignacionActual;
    @FXML private ComboBox<Empresa> cbEmpresa;
    @FXML private ComboBox<Tutor> cbTutor;
    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFin;
    @FXML private ComboBox<String> cbEstado;
    @FXML private TextArea txtMotivo;

    @Lazy @Autowired private StageManager stageManager;

    @Autowired private EstudianteService estudianteService;
    @Autowired private FormacionEmpresaService formacionEmpresaService;
    @Autowired private EmpresaService empresaService;
    @Autowired private TutorService tutorService;
    @Autowired private CursoService cursoService;
    @Autowired private ProfesorService profesorService;

    private final ObservableList<EstudianteRow> rows = FXCollections.observableArrayList();
    private final ObservableList<Curso> cursos = FXCollections.observableArrayList();
    private final ObservableList<Empresa> empresas = FXCollections.observableArrayList();
    private final ObservableList<Tutor> tutoresFiltrados = FXCollections.observableArrayList();
    private final ObservableList<String> estados = FXCollections.observableArrayList(ESTADOS);

    private List<Tutor> todosTutores = new ArrayList<>();
    private EstudianteRow seleccionado;

    /** Fila auxiliar que enlaza el estudiante con su asignación actual (la formación más reciente). */
    public static class EstudianteRow {
        private final Estudiante estudiante;
        private final FormacionEmpresa actual; // puede ser null

        public EstudianteRow(Estudiante e, FormacionEmpresa actual) {
            this.estudiante = e;
            this.actual = actual;
        }

        public Estudiante getEstudiante() { return estudiante; }
        public FormacionEmpresa getActual() { return actual; }

        public Long getId() { return estudiante.getId(); }
        public String getNombre() {
            return estudiante.getFirstName() + " " + (estudiante.getLastName() == null ? "" : estudiante.getLastName());
        }
        public String getCursoNombre() {
            if (estudiante.getCurso() == null) return "—";
            String n = estudiante.getCurso().getNombre();
            return n != null ? n : ("Curso " + estudiante.getCurso().getIdCurso());
        }
        public boolean tieneAsignacion() {
            return actual != null && !"Cancelada".equalsIgnoreCase(actual.getEstado());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbFiltroCurso.setItems(cursos);
        cbEmpresa.setItems(empresas);
        cbTutor.setItems(tutoresFiltrados);
        cbEstado.setItems(estados);

        configureConverters();

        cbFiltroCurso.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> reload());
        chkSoloSinAsignar.selectedProperty().addListener((o, ov, nv) -> reload());

        cbEmpresa.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> filtrarTutoresPorEmpresa(nv));

        setColumnProperties();
    }

    @Override
    public void setCurrentUser(User user) {
        super.setCurrentUser(user);
        loadCombos();
        reload();
    }

    private void configureConverters() {
        cbFiltroCurso.setConverter(new StringConverter<Curso>() {
            @Override public String toString(Curso c) {
                if (c == null) return "Todos los cursos";
                String n = c.getNombre() != null ? c.getNombre() : ("Curso " + c.getIdCurso());
                return n + " (" + c.getAnio() + ")";
            }
            @Override public Curso fromString(String s) { return null; }
        });
        cbEmpresa.setConverter(new StringConverter<Empresa>() {
            @Override public String toString(Empresa e) {
                return e == null ? "" : (e.getNombre() != null ? e.getNombre() : "Empresa " + e.getIdEmpresa());
            }
            @Override public Empresa fromString(String s) { return null; }
        });
        cbTutor.setConverter(new StringConverter<Tutor>() {
            @Override public String toString(Tutor t) {
                if (t == null) return "";
                return t.getFirstName() + " " + (t.getLastName() == null ? "" : t.getLastName());
            }
            @Override public Tutor fromString(String s) { return null; }
        });
    }

    private void loadCombos() {
        cursos.clear();
        cursos.add(null); // "Todos los cursos"
        cursos.addAll(cursosVisibles());

        empresas.clear();
        empresas.addAll(empresaService.findAll());

        todosTutores = tutorService.findAll();
        filtrarTutoresPorEmpresa(cbEmpresa.getValue());
    }

    private List<Curso> cursosVisibles() {
        List<Curso> all = cursoService.findAll();
        if (currentUser instanceof Profesor && !(currentUser instanceof Administrador)) {
            // Profesor coordinador: solo cursos en los que imparte algún módulo
            Profesor p = (Profesor) profesorService.find(currentUser.getId());
            if (p == null || p.getModulos() == null || p.getModulos().isEmpty()) {
                return all; // fallback: si no se sabe, dejar todos
            }
            Set<Long> ids = new HashSet<>();
            try {
                p.getModulos().forEach(m -> {
                    if (m.getCurso() != null) ids.add(m.getCurso().getIdCurso());
                });
            } catch (Exception ignore) {
                return all;
            }
            return all.stream().filter(c -> ids.contains(c.getIdCurso())).collect(Collectors.toList());
        }
        return all;
    }

    private void filtrarTutoresPorEmpresa(Empresa empresa) {
        tutoresFiltrados.clear();
        if (empresa == null) {
            tutoresFiltrados.addAll(todosTutores);
            return;
        }
        for (Tutor t : todosTutores) {
            if (t.getEmpresa() != null && t.getEmpresa().getIdEmpresa() != null
                    && t.getEmpresa().getIdEmpresa().equals(empresa.getIdEmpresa())) {
                tutoresFiltrados.add(t);
            }
        }
    }

    private void setColumnProperties() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCurso.setCellValueFactory(new PropertyValueFactory<>("cursoNombre"));

        colEmpresaActual.setCellValueFactory(cd -> {
            FormacionEmpresa f = cd.getValue().getActual();
            if (f == null || f.getTutor() == null || f.getTutor().getEmpresa() == null) {
                return new ReadOnlyStringWrapper("Sin asignar");
            }
            String n = f.getTutor().getEmpresa().getNombre();
            return new ReadOnlyStringWrapper(n != null ? n : "—");
        });
        colTutorActual.setCellValueFactory(cd -> {
            FormacionEmpresa f = cd.getValue().getActual();
            if (f == null || f.getTutor() == null) return new ReadOnlyStringWrapper("—");
            Tutor t = f.getTutor();
            return new ReadOnlyStringWrapper(t.getFirstName() + " " + (t.getLastName() == null ? "" : t.getLastName()));
        });
        colEstadoActual.setCellValueFactory(cd -> {
            FormacionEmpresa f = cd.getValue().getActual();
            return new ReadOnlyStringWrapper(f == null ? "—" : (f.getEstado() == null ? "—" : f.getEstado()));
        });

        colSeleccionar.setCellFactory(seleccionarCellFactory);
    }

    private final Callback<TableColumn<EstudianteRow, Boolean>, TableCell<EstudianteRow, Boolean>> seleccionarCellFactory =
            new Callback<TableColumn<EstudianteRow, Boolean>, TableCell<EstudianteRow, Boolean>>() {
        @Override
        public TableCell<EstudianteRow, Boolean> call(TableColumn<EstudianteRow, Boolean> param) {
            return new TableCell<EstudianteRow, Boolean>() {
                final Button btn = new Button();
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                        setGraphic(null);
                        setText(null);
                        return;
                    }
                    EstudianteRow row = getTableView().getItems().get(getIndex());
                    btn.setText(row.tieneAsignacion() ? "Reasignar" : "Asignar");
                    btn.setOnAction(e -> seleccionarEstudiante(row));
                    setGraphic(btn);
                    setAlignment(Pos.CENTER);
                    setText(null);
                }
            };
        }
    };

    private void seleccionarEstudiante(EstudianteRow row) {
        seleccionado = row;
        lblEstudiante.setText(row.getNombre() + "  (ID " + row.getId() + ")");
        FormacionEmpresa f = row.getActual();
        if (f == null) {
            lblAsignacionActual.setText("Sin asignación previa");
            cbEmpresa.setValue(null);
            cbTutor.setValue(null);
            dpInicio.setValue(LocalDate.now());
            dpFin.setValue(null);
            cbEstado.setValue("Pendiente");
            txtMotivo.clear();
        } else {
            String empresaTxt = (f.getTutor() != null && f.getTutor().getEmpresa() != null
                    && f.getTutor().getEmpresa().getNombre() != null)
                    ? f.getTutor().getEmpresa().getNombre() : "—";
            String tutorTxt = (f.getTutor() != null)
                    ? f.getTutor().getFirstName() + " " + (f.getTutor().getLastName() == null ? "" : f.getTutor().getLastName())
                    : "—";
            lblAsignacionActual.setText("Empresa: " + empresaTxt + "\nTutor: " + tutorTxt
                    + "\nEstado: " + (f.getEstado() == null ? "—" : f.getEstado()));
            // Pre-llenar con valores actuales
            if (f.getTutor() != null) {
                cbEmpresa.setValue(f.getTutor().getEmpresa());
                cbTutor.setValue(f.getTutor());
            }
            dpInicio.setValue(f.getFechaInicio() != null ? f.getFechaInicio() : LocalDate.now());
            dpFin.setValue(f.getFechaFin());
            cbEstado.setValue(f.getEstado() != null ? f.getEstado() : "En curso");
            txtMotivo.clear();
        }
    }

    @FXML
    private void asignar(ActionEvent event) {
        if (!validarSeleccionYForm(false)) return;

        FormacionEmpresa actual = seleccionado.getActual();
        if (actual != null && !"Cancelada".equalsIgnoreCase(actual.getEstado())) {
            // Hay asignación activa: actualizar en sitio (sin cambio de tutor permitido aquí)
            if (actual.getTutor() == null
                    || actual.getTutor().getId() != cbTutor.getValue().getId()) {
                warn("El tutor seleccionado es distinto al actual. Use \"Reasignar\" para cambiar de empresa o tutor.");
                return;
            }
            actual.setFechaInicio(dpInicio.getValue());
            actual.setFechaFin(dpFin.getValue());
            actual.setEstado(cbEstado.getValue());
            formacionEmpresaService.update(actual);
            infoAlert("Asignación actualizada",
                    "Se ha actualizado la asignación de " + seleccionado.getNombre() + ".");
        } else {
            FormacionEmpresa nueva = construirNueva(null);
            FormacionEmpresa saved = formacionEmpresaService.save(nueva);
            infoAlert("Asignación creada",
                    "Se ha asignado a " + seleccionado.getNombre() + " con la formación ID " + saved.getIdFormacion() + ".");
        }
        reload();
        clearForm();
    }

    @FXML
    private void reasignar(ActionEvent event) {
        if (!validarSeleccionYForm(true)) return;
        FormacionEmpresa actual = seleccionado.getActual();
        String motivo = txtMotivo.getText().trim();

        if (actual != null && !"Cancelada".equalsIgnoreCase(actual.getEstado())) {
            // Marcar la actual como cancelada y registrar motivo
            String prev = actual.getMotivoCambio();
            String log = "Reasignado el " + LocalDate.now() + ": " + motivo;
            actual.setMotivoCambio(prev == null || prev.isEmpty() ? log : prev + " | " + log);
            actual.setEstado("Cancelada");
            actual.setFechaFin(LocalDate.now());
            formacionEmpresaService.update(actual);
        }

        FormacionEmpresa nueva = construirNueva(motivo);
        FormacionEmpresa saved = formacionEmpresaService.save(nueva);
        infoAlert("Reasignación realizada",
                "Se ha reasignado a " + seleccionado.getNombre()
                        + ".\nNueva formación ID " + saved.getIdFormacion() + ".\nMotivo: " + motivo);
        reload();
        clearForm();
    }

    private boolean validarSeleccionYForm(boolean exigirMotivo) {
        if (seleccionado == null) {
            warn("Seleccione un estudiante de la tabla.");
            return false;
        }
        if (cbEmpresa.getValue() == null) {
            warn("Seleccione una empresa.");
            return false;
        }
        if (cbTutor.getValue() == null) {
            warn("Seleccione un tutor de empresa.");
            return false;
        }
        if (dpInicio.getValue() == null) {
            warn("Indique la fecha de inicio.");
            return false;
        }
        if (dpFin.getValue() != null && dpFin.getValue().isBefore(dpInicio.getValue())) {
            warn("La fecha de fin no puede ser anterior a la de inicio.");
            return false;
        }
        if (cbEstado.getValue() == null) {
            warn("Seleccione el estado.");
            return false;
        }
        if (exigirMotivo && (txtMotivo.getText() == null || txtMotivo.getText().trim().isEmpty())) {
            warn("Indique el motivo de la reasignación (incidencia).");
            return false;
        }
        return true;
    }

    private FormacionEmpresa construirNueva(String motivo) {
        FormacionEmpresa f = new FormacionEmpresa();
        f.setEstudiante(seleccionado.getEstudiante());
        f.setTutor(cbTutor.getValue());
        f.setCurso(seleccionado.getEstudiante().getCurso());
        f.setFechaInicio(dpInicio.getValue());
        f.setFechaFin(dpFin.getValue());
        f.setEstado(cbEstado.getValue());
        f.setMotivoCambio(motivo);
        // Profesor responsable
        if (currentUser instanceof Profesor && !(currentUser instanceof Administrador)) {
            Profesor p = (Profesor) profesorService.find(currentUser.getId());
            f.setProfesor(p);
        } else if (seleccionado.getActual() != null && seleccionado.getActual().getProfesor() != null) {
            f.setProfesor(seleccionado.getActual().getProfesor());
        }
        return f;
    }

    @FXML
    private void reset(ActionEvent event) {
        clearForm();
    }

    @FXML
    private void refresh(ActionEvent event) {
        loadCombos();
        reload();
    }

    private void clearForm() {
        seleccionado = null;
        lblEstudiante.setText("—");
        lblAsignacionActual.setText("Sin asignación previa");
        cbEmpresa.setValue(null);
        cbTutor.setValue(null);
        dpInicio.setValue(null);
        dpFin.setValue(null);
        cbEstado.setValue(null);
        txtMotivo.clear();
    }

    private void reload() {
        rows.clear();
        List<Estudiante> estudiantes = estudianteService.findAll();
        Curso filtro = cbFiltroCurso.getValue();
        boolean soloSinAsignar = chkSoloSinAsignar.isSelected();

        // Restringir cursos visibles para profesor coordinador
        Set<Long> cursosPermitidos = null;
        if (currentUser instanceof Profesor && !(currentUser instanceof Administrador)) {
            cursosPermitidos = new HashSet<>();
            for (Curso c : cursosVisibles()) cursosPermitidos.add(c.getIdCurso());
        }

        List<FormacionEmpresa> todas = formacionEmpresaService.findAll();

        for (Estudiante e : estudiantes) {
            if (cursosPermitidos != null) {
                if (e.getCurso() == null || !cursosPermitidos.contains(e.getCurso().getIdCurso())) continue;
            }
            if (filtro != null) {
                if (e.getCurso() == null || !e.getCurso().getIdCurso().equals(filtro.getIdCurso())) continue;
            }
            FormacionEmpresa actual = encontrarActual(todas, e.getId());
            boolean asignado = actual != null && !"Cancelada".equalsIgnoreCase(actual.getEstado());
            if (soloSinAsignar && asignado) continue;
            rows.add(new EstudianteRow(e, actual));
        }
        estudiantesTable.setItems(rows);
    }

    private FormacionEmpresa encontrarActual(List<FormacionEmpresa> todas, Long estudianteId) {
        return todas.stream()
                .filter(f -> f.getEstudiante() != null && f.getEstudiante().getId() == estudianteId)
                .max(Comparator.comparing(FormacionEmpresa::getIdFormacion))
                .orElse(null);
    }

    @Override
    @FXML
    protected void exit(ActionEvent event) { super.exit(event); }

    @Override
    @FXML
    protected void logout(ActionEvent event) {
        stageManager.clearCurrentUser();
        stageManager.switchScene(FxmlView.LOGIN);
    }

    @FXML
    private void volverMenu(ActionEvent event) {
        if (currentUser instanceof Profesor && !(currentUser instanceof Administrador)) {
            stageManager.switchScene(FxmlView.MENU_PROFESOR);
        } else {
            stageManager.switchScene(FxmlView.MENU_ADMIN);
        }
    }

    @Override
    @FXML
    protected void acercaDe(ActionEvent event) { super.acercaDe(event); }

    private void warn(String msg) {
        Alert a = new Alert(AlertType.WARNING);
        a.setTitle("Validación"); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
    private void infoAlert(String title, String msg) {
        Alert a = new Alert(AlertType.INFORMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}

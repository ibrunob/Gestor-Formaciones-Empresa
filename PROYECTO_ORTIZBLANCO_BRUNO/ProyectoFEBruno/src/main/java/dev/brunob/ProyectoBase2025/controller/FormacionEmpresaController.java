package dev.brunob.ProyectoBase2025.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import dev.brunob.ProyectoBase2025.config.StageManager;
import dev.brunob.ProyectoBase2025.modelo.Administrador;
import dev.brunob.ProyectoBase2025.modelo.Curso;
import dev.brunob.ProyectoBase2025.modelo.Estudiante;
import dev.brunob.ProyectoBase2025.modelo.FormacionEmpresa;
import dev.brunob.ProyectoBase2025.modelo.Profesor;
import dev.brunob.ProyectoBase2025.modelo.Tutor;
import dev.brunob.ProyectoBase2025.modelo.User;
import dev.brunob.ProyectoBase2025.services.CursoService;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Controlador para la gestión de Formaciones en Empresa.
 * Permite crear, consultar, modificar y eliminar formaciones.
 * Administrador: ve y opera sobre todas las formaciones.
 * Profesor coordinador: ve y opera sobre las formaciones de las que es profesor responsable.
 */
@Controller
public class FormacionEmpresaController extends BaseMenuController implements Initializable {

    @Override
    protected String getPaginaAyuda() { return "formacion.html"; }


    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final List<String> ESTADOS = List.of("Pendiente", "En curso", "Finalizada", "Cancelada");

    @FXML 
    private Label formacionId;

    @FXML 
    private ComboBox<Estudiante> cbEstudiante;
    @FXML 
    private ComboBox<Profesor> cbProfesor;
    @FXML 
    private ComboBox<Tutor> cbTutor;
    @FXML 
    private Label lblEmpresa;
    @FXML 
    private ComboBox<Curso> cbCurso;
    @FXML 
    private DatePicker fechaInicio;
    @FXML 
    private DatePicker fechaFin;
    @FXML 
    private ComboBox<String> cbEstado;

    @FXML 
    private Button reset;
    @FXML 
    private Button saveFormacion;

    @FXML 
    private TableView<FormacionEmpresa> formacionTable;
    @FXML 
    private TableColumn<FormacionEmpresa, Long> colId;
    @FXML 
    private TableColumn<FormacionEmpresa, String> colEstudiante;
    @FXML 
    private TableColumn<FormacionEmpresa, String> colEmpresa;
    @FXML 
    private TableColumn<FormacionEmpresa, String> colProfesor;
    @FXML 
    private TableColumn<FormacionEmpresa, String> colTutor;
    @FXML 
    private TableColumn<FormacionEmpresa, String> colCurso;
    @FXML 
    private TableColumn<FormacionEmpresa, LocalDate> colFechaInicio;
    @FXML 
    private TableColumn<FormacionEmpresa, LocalDate> colFechaFin;
    @FXML 
    private TableColumn<FormacionEmpresa, String> colEstado;
    @FXML 
    private TableColumn<FormacionEmpresa, Boolean> colEdit;

    @FXML 
    private MenuItem deleteFormaciones;

    @Lazy @Autowired 
    private StageManager stageManager;

    @Autowired 
    private FormacionEmpresaService formacionEmpresaService;
    @Autowired 
    private EstudianteService estudianteService;
    @Autowired 
    private ProfesorService profesorService;
    @Autowired 
    private TutorService tutorService;
    @Autowired 
    private CursoService cursoService;

    private final ObservableList<FormacionEmpresa> formacionList = FXCollections.observableArrayList();
    private final ObservableList<Estudiante> estudiantes = FXCollections.observableArrayList();
    private final ObservableList<Profesor> profesores = FXCollections.observableArrayList();
    private final ObservableList<Tutor> tutores = FXCollections.observableArrayList();
    private final ObservableList<Curso> cursos = FXCollections.observableArrayList();
    private final ObservableList<String> estados = FXCollections.observableArrayList();

    @Override
    @FXML
    protected void exit(ActionEvent event) {
        super.exit(event);
    }

    @Override
    @FXML
    protected void logout(ActionEvent event) {
        stageManager.clearCurrentUser();
        stageManager.switchScene(FxmlView.LOGIN);
    }

    @FXML
    private void volverMenu(ActionEvent event) {
        stageManager.switchScene(targetMenu());
    }

    private FxmlView targetMenu() {
        if (currentUser instanceof Profesor) {
            return FxmlView.MENU_PROFESOR;
        }
        return FxmlView.MENU_ADMIN;
    }

    @Override
    @FXML
    protected void acercaDe(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText("Sistema de Gestión FE");
        alert.setContentText("Sistema de Gestión de Formaciones en Empresa\n" +
                "Versión 1.0\n\n" +
                "CIFP La Laboral - Gijón\n" +
                "Desarrollado por Bruno Ortiz Blanco");
        alert.showAndWait();
    }

    @FXML
    void reset(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void saveFormacion(ActionEvent event) {
        if (!emptyValidation("Estudiante", cbEstudiante.getValue() == null)) return;
        if (!emptyValidation("Profesor", cbProfesor.getValue() == null)) return;
        if (!emptyValidation("Tutor de Empresa", cbTutor.getValue() == null)) return;
        if (!emptyValidation("Curso", cbCurso.getValue() == null)) return;
        if (!emptyValidation("Fecha de Inicio", fechaInicio.getValue() == null)) return;
        if (!emptyValidation("Fecha de Fin", fechaFin.getValue() == null)) return;
        if (!emptyValidation("Estado", cbEstado.getValue() == null)) return;

        if (fechaFin.getValue().isBefore(fechaInicio.getValue())) {
            warn("La fecha de fin no puede ser anterior a la fecha de inicio.");
            return;
        }

        boolean isUpdate = formacionId.getText() != null && !formacionId.getText().isEmpty();
        FormacionEmpresa formacion;
        if (isUpdate) {
            formacion = formacionEmpresaService.find(Long.parseLong(formacionId.getText()));
            if (formacion == null) {
                warn("La formación seleccionada ya no existe.");
                clearFields();
                loadFormacionDetails();
                return;
            }
        } else {
            formacion = new FormacionEmpresa();
        }

        formacion.setEstudiante(cbEstudiante.getValue());
        formacion.setProfesor(cbProfesor.getValue());
        formacion.setTutor(cbTutor.getValue());
        formacion.setCurso(cbCurso.getValue());
        formacion.setFechaInicio(fechaInicio.getValue());
        formacion.setFechaFin(fechaFin.getValue());
        formacion.setEstado(cbEstado.getValue());

        if (isUpdate) {
            FormacionEmpresa updated = formacionEmpresaService.update(formacion);
            infoAlert("Formación actualizada", "La formación con ID " + updated.getIdFormacion() + " se ha actualizado correctamente.");
        } else {
            FormacionEmpresa saved = formacionEmpresaService.save(formacion);
            infoAlert("Formación creada", "La formación se ha creado correctamente con ID " + saved.getIdFormacion() + ".");
        }

        clearFields();
        loadFormacionDetails();
    }

    @FXML
    private void deleteFormaciones(ActionEvent event) {
        List<FormacionEmpresa> selected = formacionTable.getSelectionModel().getSelectedItems();
        if (selected == null || selected.isEmpty()) return;

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro de que desea eliminar la(s) formación(es) seleccionada(s)?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.isPresent() && action.get() == ButtonType.OK) {
            formacionEmpresaService.deleteInBatch(List.copyOf(selected));
            loadFormacionDetails();
        }
    }

    private void clearFields() {
        formacionId.setText(null);
        cbEstudiante.getSelectionModel().clearSelection();
        cbEstudiante.setValue(null);
        cbProfesor.getSelectionModel().clearSelection();
        cbProfesor.setValue(null);
        cbTutor.getSelectionModel().clearSelection();
        cbTutor.setValue(null);
        updateEmpresaLabel(null);
        cbCurso.getSelectionModel().clearSelection();
        cbCurso.setValue(null);
        fechaInicio.setValue(null);
        fechaInicio.getEditor().clear();
        fechaFin.setValue(null);
        fechaFin.getEditor().clear();
        cbEstado.getSelectionModel().clearSelection();
        cbEstado.setValue(null);

        // Si es profesor coordinador, pre-seleccionarlo como profesor por defecto
        preselectProfesorIfCoordinador();
    }

    private void preselectProfesorIfCoordinador() {
        if (currentUser instanceof Profesor) {
            for (Profesor p : profesores) {
                if (p.getId() == currentUser.getId()) {
                    cbProfesor.setValue(p);
                    break;
                }
            }
        }
    }

    @Override
    public void setCurrentUser(User user) {
        super.setCurrentUser(user);
        // Cuando se inyecta el usuario, recargar lista filtrada y reestablecer permisos
        loadCombos();
        loadFormacionDetails();
        applyRolePermissions();
        preselectProfesorIfCoordinador();
    }

    private void applyRolePermissions() {
        if (currentUser instanceof Profesor && !(currentUser instanceof Administrador)) {
            // El profesor coordinador siempre opera como tutor docente sobre sus propias formaciones
            cbProfesor.setDisable(true);
        } else {
            cbProfesor.setDisable(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbEstudiante.setItems(estudiantes);
        cbProfesor.setItems(profesores);
        cbTutor.setItems(tutores);
        cbCurso.setItems(cursos);
        cbEstado.setItems(estados);

        cbTutor.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateEmpresaLabel(newVal));

        configureConverters();

        estados.clear();
        estados.addAll(ESTADOS);

        formacionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();
        loadCombos();
        loadFormacionDetails();
    }

    private void configureConverters() {
        cbEstudiante.setConverter(new StringConverter<Estudiante>() {
            @Override public String toString(Estudiante e) {
                return e == null ? "" : e.getFirstName() + " " + (e.getLastName() == null ? "" : e.getLastName());
            }
            @Override public Estudiante fromString(String s) { return null; }
        });
        cbProfesor.setConverter(new StringConverter<Profesor>() {
            @Override public String toString(Profesor p) {
                if (p == null) return "";
                String coord = (p.getEsCoordinador() != null && p.getEsCoordinador()) ? " (Coord.)" : "";
                return p.getFirstName() + " " + (p.getLastName() == null ? "" : p.getLastName()) + coord;
            }
            @Override public Profesor fromString(String s) { return null; }
        });
        cbTutor.setConverter(new StringConverter<Tutor>() {
            @Override public String toString(Tutor t) {
                if (t == null) return "";
                String empresa = (t.getEmpresa() != null && t.getEmpresa().getNombre() != null)
                        ? " - " + t.getEmpresa().getNombre() : "";
                return t.getFirstName() + " " + (t.getLastName() == null ? "" : t.getLastName()) + empresa;
            }
            @Override public Tutor fromString(String s) { return null; }
        });
        cbCurso.setConverter(new StringConverter<Curso>() {
            @Override public String toString(Curso c) {
                if (c == null) return "";
                String nombre = c.getNombre() != null ? c.getNombre() : ("Curso " + c.getIdCurso());
                return nombre + " (" + c.getAnio() + ")";
            }
            @Override public Curso fromString(String s) { return null; }
        });
    }

    private void loadCombos() {
        estudiantes.clear();
        estudiantes.addAll(estudianteService.findAll());

        profesores.clear();
        profesores.addAll(profesorService.findAll());

        tutores.clear();
        tutores.addAll(tutorService.findAll());

        cursos.clear();
        cursos.addAll(cursoService.findAll());
    }

    private void loadFormacionDetails() {
        formacionList.clear();
        List<FormacionEmpresa> all = formacionEmpresaService.findAll();
        if (currentUser instanceof Profesor && !(currentUser instanceof Administrador)) {
            // Profesor coordinador: solo formaciones en las que es profesor responsable
            for (FormacionEmpresa f : all) {
                if (f.getProfesor() != null && f.getProfesor().getId() == currentUser.getId()) {
                    formacionList.add(f);
                }
            }
        } else {
            formacionList.addAll(all);
        }
        formacionTable.setItems(formacionList);
    }

    private void setColumnProperties() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idFormacion"));

        colEstudiante.setCellValueFactory(cd -> {
            Estudiante e = cd.getValue().getEstudiante();
            return new ReadOnlyStringWrapper(e == null ? "" : e.getFirstName() + " " + (e.getLastName() == null ? "" : e.getLastName()));
        });

        colEmpresa.setCellValueFactory(cd -> {
            Tutor t = cd.getValue().getTutor();
            String nombre = (t != null && t.getEmpresa() != null && t.getEmpresa().getNombre() != null)
                    ? t.getEmpresa().getNombre() : "";
            return new ReadOnlyStringWrapper(nombre);
        });

        colProfesor.setCellValueFactory(cd -> {
            Profesor p = cd.getValue().getProfesor();
            return new ReadOnlyStringWrapper(p == null ? "" : p.getFirstName() + " " + (p.getLastName() == null ? "" : p.getLastName()));
        });

        colTutor.setCellValueFactory(cd -> {
            Tutor t = cd.getValue().getTutor();
            return new ReadOnlyStringWrapper(t == null ? "" : t.getFirstName() + " " + (t.getLastName() == null ? "" : t.getLastName()));
        });

        colCurso.setCellValueFactory(cd -> {
            Curso c = cd.getValue().getCurso();
            String nombre = (c == null) ? "" : (c.getNombre() != null ? c.getNombre() : ("Curso " + c.getIdCurso()));
            return new ReadOnlyStringWrapper(nombre);
        });

        colFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colFechaFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        colFechaInicio.setCellFactory(c -> dateCell());
        colFechaFin.setCellFactory(c -> dateCell());

        colEdit.setCellFactory(editCellFactory);
    }

    private TableCell<FormacionEmpresa, LocalDate> dateCell() {
        return new TableCell<FormacionEmpresa, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : DATE_FMT.format(item));
            }
        };
    }

    private final Callback<TableColumn<FormacionEmpresa, Boolean>, TableCell<FormacionEmpresa, Boolean>> editCellFactory =
            new Callback<TableColumn<FormacionEmpresa, Boolean>, TableCell<FormacionEmpresa, Boolean>>() {
        @Override
        public TableCell<FormacionEmpresa, Boolean> call(final TableColumn<FormacionEmpresa, Boolean> param) {
            return new TableCell<FormacionEmpresa, Boolean>() {
                final Image imgEdit = loadEditIcon();
                final Button btnEdit = new Button();

                @Override
                public void updateItem(Boolean check, boolean empty) {
                    super.updateItem(check, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btnEdit.setOnAction(e -> {
                            FormacionEmpresa f = getTableView().getItems().get(getIndex());
                            populateForm(f);
                        });
                        btnEdit.setStyle("-fx-background-color: transparent;");
                        if (imgEdit != null) {
                            ImageView iv = new ImageView(imgEdit);
                            iv.setPreserveRatio(true);
                            iv.setSmooth(true);
                            iv.setCache(true);
                            iv.setFitWidth(20);
                            btnEdit.setGraphic(iv);
                        } else {
                            btnEdit.setText("Editar");
                        }
                        setGraphic(btnEdit);
                        setAlignment(Pos.CENTER);
                        setText(null);
                    }
                }
            };
        }
    };

    private Image loadEditIcon() {
        try {
            return new Image(getClass().getResourceAsStream("/images/edit.png"));
        } catch (Exception ex) {
            return null;
        }
    }

    private void populateForm(FormacionEmpresa f) {
        formacionId.setText(Long.toString(f.getIdFormacion()));
        selectInCombo(cbEstudiante, estudiantes, f.getEstudiante() == null ? null : f.getEstudiante().getId());
        selectInCombo(cbProfesor, profesores, f.getProfesor() == null ? null : f.getProfesor().getId());
        selectInCombo(cbTutor, tutores, f.getTutor() == null ? null : f.getTutor().getId());
        if (f.getCurso() != null) {
            for (Curso c : cursos) {
                if (c.getIdCurso() != null && c.getIdCurso().equals(f.getCurso().getIdCurso())) {
                    cbCurso.setValue(c);
                    break;
                }
            }
        }
        fechaInicio.setValue(f.getFechaInicio());
        fechaFin.setValue(f.getFechaFin());
        cbEstado.setValue(f.getEstado());
    }

    private <T extends User> void selectInCombo(ComboBox<T> cb, ObservableList<T> items, Long id) {
        if (id == null) return;
        for (T item : items) {
            if (item.getId() == id) {
                cb.setValue(item);
                return;
            }
        }
    }

    private void updateEmpresaLabel(Tutor tutor) {
        if (lblEmpresa == null) return;
        if (tutor != null && tutor.getEmpresa() != null && tutor.getEmpresa().getNombre() != null) {
            lblEmpresa.setText(tutor.getEmpresa().getNombre());
        } else {
            lblEmpresa.setText("—");
        }
    }

    private boolean emptyValidation(String field, boolean empty) {
        if (empty) {
            warn("Por favor, indique " + field + ".");
            return false;
        }
        return true;
    }

    private void warn(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validación");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void infoAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

package dev.brunob.ProyectoBase2025.controller;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import dev.brunob.ProyectoBase2025.modelo.Documento;
import dev.brunob.ProyectoBase2025.modelo.Estudiante;
import dev.brunob.ProyectoBase2025.modelo.FormacionEmpresa;
import dev.brunob.ProyectoBase2025.modelo.Profesor;
import dev.brunob.ProyectoBase2025.modelo.Tutor;
import dev.brunob.ProyectoBase2025.modelo.User;
import dev.brunob.ProyectoBase2025.services.DocumentoService;
import dev.brunob.ProyectoBase2025.services.FormacionEmpresaService;
import dev.brunob.ProyectoBase2025.view.FxmlView;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

/**
 * Caso de uso: Gestionar Documentación.
 *
 * Permisos:
 *  - Administrador: ve todos los documentos. CRUD sin restricciones.
 *  - Profesor (coordinador): solo documentos cuyas FE le pertenecen. CRUD sobre esas.
 *  - Tutor de Empresa: solo documentos de FE de sus estudiantes. Puede subir; solo
 *    puede modificar/eliminar los suyos.
 *  - Estudiante: solo lectura sobre los documentos de sus FE.
 */
@Controller
public class DocumentoController extends BaseMenuController implements Initializable {

    @Override
    protected String getPaginaAyuda() { return "documentos.html"; }


    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final ObservableList<String> TIPOS_DEFECTO = FXCollections.observableArrayList(
            "Convenio", "Programa formativo", "Informe seguimiento", "Anexo", "Acta",
            "Informe valoración", "Registro asistencia", "Justificante", "Otro");
    private static final String TIPO_TODOS = "__TODOS__";

    @FXML private Label lblScopeInfo;
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cbFiltroTipo;

    @FXML private TableView<Documento> documentosTable;
    @FXML private TableColumn<Documento, Long> colId;
    @FXML private TableColumn<Documento, String> colNombre;
    @FXML private TableColumn<Documento, String> colTipo;
    @FXML private TableColumn<Documento, String> colEstudiante;
    @FXML private TableColumn<Documento, String> colEmpresa;
    @FXML private TableColumn<Documento, String> colSubidoPor;
    @FXML private TableColumn<Documento, String> colFecha;

    @FXML private ComboBox<FormacionEmpresa> cbFormacion;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField txtRuta;
    @FXML private Button btnExaminar;
    @FXML private Button btnAbrir;
    @FXML private Button btnLimpiar;
    @FXML private Button btnEliminar;
    @FXML private Button btnGuardar;

    @Lazy @Autowired private StageManager stageManager;
    @Autowired private DocumentoService documentoService;
    @Autowired private FormacionEmpresaService formacionEmpresaService;

    private final ObservableList<Documento> docs = FXCollections.observableArrayList();
    private FilteredList<Documento> filtrados;
    private final ObservableList<FormacionEmpresa> formaciones = FXCollections.observableArrayList();
    private final ObservableList<String> tiposFiltro = FXCollections.observableArrayList();

    private Documento editando;
    private boolean readOnly;
    private boolean ownershipRestricted; // tutor empresa: solo edita lo suyo

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHeader("Gestión Documental");
        colId.setCellValueFactory(new PropertyValueFactory<>("idDocumento"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colEstudiante.setCellValueFactory(c -> new ReadOnlyStringWrapper(nombreEstudiante(c.getValue())));
        colEmpresa.setCellValueFactory(c -> new ReadOnlyStringWrapper(nombreEmpresa(c.getValue())));
        colSubidoPor.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getSubidoPor() != null ? nombreCompleto(c.getValue().getSubidoPor()) : "—"));
        colFecha.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().getFechaSubida() != null ? c.getValue().getFechaSubida().format(FMT) : ""));

        filtrados = new FilteredList<>(docs, d -> true);
        documentosTable.setItems(filtrados);
        documentosTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        documentosTable.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> cargarEnForm(n));

        cbFormacion.setItems(formaciones);
        cbFormacion.setConverter(new StringConverter<FormacionEmpresa>() {
            @Override public String toString(FormacionEmpresa f) { return f == null ? "" : describeFormacion(f); }
            @Override public FormacionEmpresa fromString(String s) { return null; }
        });

        cbTipo.setItems(TIPOS_DEFECTO);
        cbFiltroTipo.setItems(tiposFiltro);
        cbFiltroTipo.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> aplicarFiltro());
        txtBuscar.textProperty().addListener((obs, o, n) -> aplicarFiltro());
    }

    @Override
    public void setCurrentUser(User user) {
        super.setCurrentUser(user);
        configurarPermisos();
        cargarDatos();
    }

    private void configurarPermisos() {
        readOnly = currentUser instanceof Estudiante;
        ownershipRestricted = currentUser instanceof Tutor;

        boolean editable = !readOnly;
        cbFormacion.setDisable(!editable);
        txtNombre.setDisable(!editable);
        cbTipo.setDisable(!editable);
        txtRuta.setDisable(!editable);
        btnExaminar.setDisable(!editable);
        btnGuardar.setDisable(!editable);
        btnEliminar.setDisable(!editable);
        btnLimpiar.setDisable(!editable);

        if (currentUser instanceof Administrador) {
            lblScopeInfo.setText("Administrador: acceso completo a toda la documentación.");
        } else if (currentUser instanceof Profesor) {
            lblScopeInfo.setText("Profesor: documentación de las formaciones bajo su tutela.");
        } else if (currentUser instanceof Tutor) {
            lblScopeInfo.setText("Tutor de empresa: documentación de los estudiantes asignados. "
                    + "Solo puede modificar o eliminar los documentos que ha subido.");
        } else if (currentUser instanceof Estudiante) {
            lblScopeInfo.setText("Estudiante: consulta y descarga de la documentación de su formación.");
        } else {
            lblScopeInfo.setText("");
        }
    }

    private void cargarDatos() {
        docs.clear();
        formaciones.clear();
        editando = null;
        limpiarFormulario(true);

        List<Documento> lista;
        List<FormacionEmpresa> fes;
        if (currentUser instanceof Administrador || currentUser == null) {
            lista = documentoService.findAll();
            fes = formacionEmpresaService.findAll();
        } else if (currentUser instanceof Profesor) {
            lista = documentoService.findByProfesor(currentUser.getId());
            fes = formacionEmpresaService.findByProfesor(currentUser.getId());
        } else if (currentUser instanceof Tutor) {
            lista = documentoService.findByTutor(currentUser.getId());
            fes = formacionEmpresaService.findByTutor(currentUser.getId());
        } else if (currentUser instanceof Estudiante) {
            lista = documentoService.findByEstudiante(currentUser.getId());
            fes = formacionEmpresaService.findByEstudiante(currentUser.getId());
        } else {
            lista = new ArrayList<>();
            fes = new ArrayList<>();
        }

        lista.sort(Comparator.comparing((Documento d) -> d.getFechaSubida() != null ? d.getFechaSubida() : LocalDate.MIN).reversed());
        docs.addAll(lista);
        formaciones.addAll(fes);

        // tipos del combo de filtro: todos los presentes
        Set<String> tipos = new HashSet<>();
        for (Documento d : docs) {
            if (d.getTipo() != null && !d.getTipo().isEmpty()) tipos.add(d.getTipo());
        }
        tiposFiltro.setAll(new ArrayList<>(tipos));
        tiposFiltro.sort(Comparator.naturalOrder());
        tiposFiltro.add(0, TIPO_TODOS);
        cbFiltroTipo.setConverter(new StringConverter<String>() {
            @Override public String toString(String s) { return TIPO_TODOS.equals(s) ? "Todos los tipos" : (s == null ? "" : s); }
            @Override public String fromString(String s) { return s; }
        });
        cbFiltroTipo.getSelectionModel().select(TIPO_TODOS);
        aplicarFiltro();
    }

    private void aplicarFiltro() {
        if (filtrados == null) return;
        String texto = txtBuscar.getText() == null ? "" : txtBuscar.getText().trim().toLowerCase();
        String tipo = cbFiltroTipo.getValue();
        filtrados.setPredicate(d -> {
            if (tipo != null && !TIPO_TODOS.equals(tipo) && !tipo.equals(d.getTipo())) return false;
            if (texto.isEmpty()) return true;
            String nombre = d.getNombre() != null ? d.getNombre().toLowerCase() : "";
            String est = nombreEstudiante(d).toLowerCase();
            String emp = nombreEmpresa(d).toLowerCase();
            return nombre.contains(texto) || est.contains(texto) || emp.contains(texto);
        });
    }

    private void cargarEnForm(Documento d) {
        editando = d;
        if (d == null) {
            limpiarFormulario(false);
            return;
        }
        txtNombre.setText(d.getNombre() != null ? d.getNombre() : "");
        cbTipo.setValue(d.getTipo());
        txtRuta.setText(d.getRuta() != null ? d.getRuta() : "");
        FormacionEmpresa fe = d.getFormacionEmpresa();
        if (fe != null) {
            for (FormacionEmpresa f : formaciones) {
                if (f.getIdFormacion() != null && f.getIdFormacion().equals(fe.getIdFormacion())) {
                    cbFormacion.setValue(f);
                    break;
                }
            }
        } else {
            cbFormacion.setValue(null);
        }

        boolean puedeEditar = !readOnly && (!ownershipRestricted || esAutor(d));
        txtNombre.setDisable(!puedeEditar);
        cbTipo.setDisable(!puedeEditar);
        txtRuta.setDisable(!puedeEditar);
        cbFormacion.setDisable(!puedeEditar);
        btnExaminar.setDisable(!puedeEditar);
        btnGuardar.setDisable(!puedeEditar);
        btnEliminar.setDisable(!puedeEditar);
    }

    private boolean esAutor(Documento d) {
        return d.getSubidoPor() != null && currentUser != null
                && d.getSubidoPor().getId() == currentUser.getId();
    }

    @FXML
    private void examinar(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar archivo");
        File f = fc.showOpenDialog(documentosTable.getScene().getWindow());
        if (f != null) {
            txtRuta.setText(f.getAbsolutePath());
            if (txtNombre.getText() == null || txtNombre.getText().isEmpty()) {
                txtNombre.setText(f.getName());
            }
        }
    }

    @FXML
    private void abrir(ActionEvent event) {
        Documento d = documentosTable.getSelectionModel().getSelectedItem();
        String ruta = d != null && d.getRuta() != null ? d.getRuta() : (txtRuta.getText() != null ? txtRuta.getText() : "");
        if (ruta == null || ruta.isEmpty()) {
            info("No hay archivo asociado.");
            return;
        }
        File f = new File(ruta);
        if (!f.exists()) {
            info("El archivo no se encuentra en la ruta indicada:\n" + ruta);
            return;
        }
        try {
            Desktop.getDesktop().open(f);
        } catch (Exception ex) {
            info("No se pudo abrir el archivo: " + ex.getMessage());
        }
    }

    @FXML
    private void guardar(ActionEvent event) {
        if (readOnly) return;
        if (editando != null && ownershipRestricted && !esAutor(editando)) {
            info("Solo puede modificar documentos que usted haya subido.");
            return;
        }
        FormacionEmpresa fe = cbFormacion.getValue();
        if (fe == null) {
            info("Seleccione la formación en empresa a la que pertenece el documento.");
            return;
        }
        String nombre = txtNombre.getText() != null ? txtNombre.getText().trim() : "";
        if (nombre.isEmpty()) {
            info("Indique un nombre para el documento.");
            return;
        }

        Documento d = editando != null ? editando : new Documento();
        d.setNombre(nombre);
        d.setTipo(cbTipo.getValue());
        d.setRuta(txtRuta.getText());
        d.setFormacionEmpresa(fe);
        if (d.getFechaSubida() == null) d.setFechaSubida(LocalDate.now());
        if (d.getSubidoPor() == null) d.setSubidoPor(currentUser);
        documentoService.save(d);

        info(editando != null ? "Documento actualizado." : "Documento subido correctamente.");
        cargarDatos();
    }

    @FXML
    private void eliminar(ActionEvent event) {
        if (readOnly || editando == null) {
            info("Seleccione un documento del listado para eliminarlo.");
            return;
        }
        if (ownershipRestricted && !esAutor(editando)) {
            info("Solo puede eliminar documentos que usted haya subido.");
            return;
        }
        Alert confirm = new Alert(AlertType.CONFIRMATION,
                "¿Eliminar el documento '" + editando.getNombre() + "'?", ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText(null);
        confirm.showAndWait().filter(b -> b == ButtonType.OK).ifPresent(b -> {
            documentoService.delete(editando);
            editando = null;
            cargarDatos();
        });
    }

    @FXML
    private void reset(ActionEvent event) {
        limpiarFormulario(true);
    }

    private void limpiarFormulario(boolean limpiarSeleccion) {
        editando = null;
        txtNombre.clear();
        cbTipo.setValue(null);
        txtRuta.clear();
        cbFormacion.setValue(null);
        if (limpiarSeleccion) documentosTable.getSelectionModel().clearSelection();
        // Restaurar disable según permiso global
        boolean editable = !readOnly;
        txtNombre.setDisable(!editable);
        cbTipo.setDisable(!editable);
        txtRuta.setDisable(!editable);
        cbFormacion.setDisable(!editable);
        btnExaminar.setDisable(!editable);
        btnGuardar.setDisable(!editable);
        btnEliminar.setDisable(!editable);
    }

    @FXML
    private void refresh(ActionEvent event) {
        cargarDatos();
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

    private String nombreEstudiante(Documento d) {
        try {
            return d != null && d.getFormacionEmpresa() != null && d.getFormacionEmpresa().getEstudiante() != null
                    ? nombreCompleto(d.getFormacionEmpresa().getEstudiante())
                    : "—";
        } catch (Exception ex) { return "—"; }
    }

    private String nombreEmpresa(Documento d) {
        try {
            FormacionEmpresa fe = d.getFormacionEmpresa();
            if (fe != null && fe.getTutor() != null && fe.getTutor().getEmpresa() != null) {
                return fe.getTutor().getEmpresa().getNombre();
            }
            return "—";
        } catch (Exception ex) { return "—"; }
    }

    private String describeFormacion(FormacionEmpresa f) {
        String est = f.getEstudiante() != null ? nombreCompleto(f.getEstudiante()) : "?";
        String emp = "—";
        try {
            if (f.getTutor() != null && f.getTutor().getEmpresa() != null) emp = f.getTutor().getEmpresa().getNombre();
        } catch (Exception ignored) {}
        return "FE#" + f.getIdFormacion() + " · " + est + " · " + emp;
    }

    private void info(String msg) {
        Alert a = new Alert(AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    @SuppressWarnings("unused")
    private List<Documento> noUsado() { return docs.stream().collect(Collectors.toList()); }
}

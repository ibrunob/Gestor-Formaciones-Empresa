package dev.brunob.ProyectoBase2025.controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import dev.brunob.ProyectoBase2025.config.StageManager;
import dev.brunob.ProyectoBase2025.modelo.Empresa;
import dev.brunob.ProyectoBase2025.modelo.Tutor;
import dev.brunob.ProyectoBase2025.services.EmpresaService;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Controlador para la gestión de Tutores de empresa.
 */
@Controller
public class TutorController extends BaseMenuController implements Initializable {

    @Override
    protected String getPaginaAyuda() { return "tutores.html"; }

    @FXML private Label tutorId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<Empresa> cmbEmpresa;

    @FXML private Button reset;
    @FXML private Button saveTutor;

    @FXML private TableView<Tutor> tutorTable;
    @FXML private TableColumn<Tutor, Long> colId;
    @FXML private TableColumn<Tutor, String> colNombre;
    @FXML private TableColumn<Tutor, String> colApellidos;
    @FXML private TableColumn<Tutor, String> colEmail;
    @FXML private TableColumn<Tutor, String> colTelefono;
    @FXML private TableColumn<Tutor, String> colEmpresa;
    @FXML private TableColumn<Tutor, Boolean> colEdit;

    @FXML private MenuItem deleteTutores;

    @Lazy @Autowired private StageManager stageManager;
    @Autowired private TutorService tutorService;
    @Autowired private EmpresaService empresaService;

    private final ObservableList<Tutor> tutorList = FXCollections.observableArrayList();
    private final ObservableList<Empresa> empresaList = FXCollections.observableArrayList();

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
        volverAlMenuPorRol();
    }

    @Override
    @FXML
    protected void acercaDe(ActionEvent event) {
        super.acercaDe(event);
    }

    @FXML
    void reset(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void saveTutor(ActionEvent event) {
        String nombre = txtNombre.getText() == null ? "" : txtNombre.getText().trim();
        String apellidos = txtApellidos.getText() == null ? "" : txtApellidos.getText().trim();
        String email = txtEmail.getText() == null ? "" : txtEmail.getText().trim();
        String telefono = txtTelefono.getText() == null ? "" : txtTelefono.getText().trim();
        String password = txtPassword.getText() == null ? "" : txtPassword.getText();
        Empresa empresa = cmbEmpresa.getSelectionModel().getSelectedItem();

        if (nombre.isEmpty()) {
            warn("Por favor, indique el nombre del tutor.");
            return;
        }
        if (email.isEmpty()) {
            warn("Por favor, indique el email del tutor.");
            return;
        }

        boolean isUpdate = tutorId.getText() != null && !tutorId.getText().isEmpty();
        Tutor tutor;
        if (isUpdate) {
            tutor = tutorService.find(Long.parseLong(tutorId.getText()));
            if (tutor == null) {
                warn("El tutor seleccionado ya no existe.");
                clearFields();
                loadTutorDetails();
                return;
            }
        } else {
            tutor = new Tutor();
            if (password.isEmpty()) {
                warn("Indique una contraseña para el nuevo tutor.");
                return;
            }
        }

        // Validación de email único cuando cambia
        if (!email.equalsIgnoreCase(tutor.getEmail())) {
            Tutor existing = tutorService.findByEmail(email);
            if (existing != null && existing.getId() != tutor.getId()) {
                warn("Ya existe un tutor con ese email.");
                return;
            }
        }

        tutor.setFirstName(nombre);
        tutor.setLastName(apellidos);
        tutor.setEmail(email);
        tutor.setTelefono(telefono);
        tutor.setEmpresa(empresa);
        if (!password.isEmpty()) {
            tutor.setPassword(password);
        }

        if (isUpdate) {
            Tutor updated = tutorService.update(tutor);
            infoAlert("Tutor actualizado", "El tutor '" + updated.getFirstName() + "' se ha actualizado correctamente.");
        } else {
            Tutor saved = tutorService.save(tutor);
            infoAlert("Tutor creado", "El tutor '" + saved.getFirstName() + "' se ha creado correctamente con ID " + saved.getId() + ".");
        }

        clearFields();
        loadTutorDetails();
    }

    @FXML
    private void deleteTutores(ActionEvent event) {
        List<Tutor> selected = tutorTable.getSelectionModel().getSelectedItems();
        if (selected == null || selected.isEmpty()) return;

        boolean tieneFormaciones = false;
        for (Tutor t : selected) {
            try {
                if (t.getFormaciones() != null && !t.getFormaciones().isEmpty()) {
                    tieneFormaciones = true;
                    break;
                }
            } catch (Exception ignore) {
                // Lazy no inicializada: no podemos garantizar que esté libre, abortamos por seguridad.
                tieneFormaciones = true;
                break;
            }
        }
        if (tieneFormaciones) {
            warn("No se puede eliminar un tutor con formaciones asignadas.\n" +
                 "Reasigne o elimine primero las formaciones correspondientes.");
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro de que desea eliminar el/los tutor(es) seleccionado(s)?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.isPresent() && action.get() == ButtonType.OK) {
            tutorService.deleteInBatch(List.copyOf(selected));
            loadTutorDetails();
        }
    }

    private void clearFields() {
        tutorId.setText(null);
        txtNombre.clear();
        txtApellidos.clear();
        txtEmail.clear();
        txtTelefono.clear();
        txtPassword.clear();
        cmbEmpresa.getSelectionModel().clearSelection();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHeader("Gestión de Tutores");
        tutorTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        configureEmpresaCombo();
        setColumnProperties();
        loadEmpresas();
        loadTutorDetails();
    }

    private void configureEmpresaCombo() {
        cmbEmpresa.setItems(empresaList);
        cmbEmpresa.setConverter(new StringConverter<Empresa>() {
            @Override public String toString(Empresa e) { return e == null ? "" : e.getNombre(); }
            @Override public Empresa fromString(String s) { return null; }
        });
    }

    private void setColumnProperties() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmpresa.setCellValueFactory(cd -> {
            try {
                Empresa e = cd.getValue().getEmpresa();
                return new ReadOnlyStringWrapper(e == null ? "" : e.getNombre());
            } catch (Exception ex) {
                return new ReadOnlyStringWrapper("?");
            }
        });
        colEdit.setCellFactory(editCellFactory);
    }

    private final Callback<TableColumn<Tutor, Boolean>, TableCell<Tutor, Boolean>> editCellFactory =
            new Callback<TableColumn<Tutor, Boolean>, TableCell<Tutor, Boolean>>() {
        @Override
        public TableCell<Tutor, Boolean> call(final TableColumn<Tutor, Boolean> param) {
            return new TableCell<Tutor, Boolean>() {
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
                            Tutor t = getTableView().getItems().get(getIndex());
                            populateForm(t);
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

    private void populateForm(Tutor t) {
        tutorId.setText(Long.toString(t.getId()));
        txtNombre.setText(t.getFirstName() != null ? t.getFirstName() : "");
        txtApellidos.setText(t.getLastName() != null ? t.getLastName() : "");
        txtEmail.setText(t.getEmail() != null ? t.getEmail() : "");
        txtTelefono.setText(t.getTelefono() != null ? t.getTelefono() : "");
        txtPassword.clear();
        Empresa emp = null;
        try { emp = t.getEmpresa(); } catch (Exception ignore) { /* lazy */ }
        if (emp != null) {
            for (Empresa e : empresaList) {
                if (e.getIdEmpresa() != null && e.getIdEmpresa().equals(emp.getIdEmpresa())) {
                    cmbEmpresa.getSelectionModel().select(e);
                    return;
                }
            }
        }
        cmbEmpresa.getSelectionModel().clearSelection();
    }

    private void loadEmpresas() {
        empresaList.clear();
        empresaList.addAll(empresaService.findAll());
    }

    private void loadTutorDetails() {
        tutorList.clear();
        tutorList.addAll(tutorService.findAll());
        tutorTable.setItems(tutorList);
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

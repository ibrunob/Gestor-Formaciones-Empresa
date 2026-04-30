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
import dev.brunob.ProyectoBase2025.services.EmpresaService;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * Controlador para la gestión de Empresas colaboradoras.
 */
@Controller
public class EmpresaController extends BaseMenuController implements Initializable {

    @Override
    protected String getPaginaAyuda() { return "empresa.html"; }


    @FXML private Label empresaId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDireccion;

    @FXML private Button reset;
    @FXML private Button saveEmpresa;

    @FXML private TableView<Empresa> empresaTable;
    @FXML private TableColumn<Empresa, Long> colId;
    @FXML private TableColumn<Empresa, String> colNombre;
    @FXML private TableColumn<Empresa, String> colDireccion;
    @FXML private TableColumn<Empresa, String> colTutores;
    @FXML private TableColumn<Empresa, Boolean> colEdit;

    @FXML private MenuItem deleteEmpresas;

    @Lazy @Autowired private StageManager stageManager;

    @Autowired private EmpresaService empresaService;

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
    private void saveEmpresa(ActionEvent event) {
        String nombre = txtNombre.getText() == null ? "" : txtNombre.getText().trim();
        String direccion = txtDireccion.getText() == null ? "" : txtDireccion.getText().trim();

        if (nombre.isEmpty()) {
            warn("Por favor, indique el nombre de la empresa.");
            return;
        }

        boolean isUpdate = empresaId.getText() != null && !empresaId.getText().isEmpty();
        Empresa empresa;
        if (isUpdate) {
            empresa = empresaService.find(Long.parseLong(empresaId.getText()));
            if (empresa == null) {
                warn("La empresa seleccionada ya no existe.");
                clearFields();
                loadEmpresaDetails();
                return;
            }
        } else {
            empresa = new Empresa();
        }

        empresa.setNombre(nombre);
        empresa.setDireccion(direccion);

        if (isUpdate) {
            Empresa updated = empresaService.update(empresa);
            infoAlert("Empresa actualizada", "La empresa '" + updated.getNombre() + "' se ha actualizado correctamente.");
        } else {
            Empresa saved = empresaService.save(empresa);
            infoAlert("Empresa creada", "La empresa '" + saved.getNombre() + "' se ha creado correctamente con ID " + saved.getIdEmpresa() + ".");
        }

        clearFields();
        loadEmpresaDetails();
    }

    @FXML
    private void deleteEmpresas(ActionEvent event) {
        List<Empresa> selected = empresaTable.getSelectionModel().getSelectedItems();
        if (selected == null || selected.isEmpty()) return;

        boolean tieneTutores = false;
        for (Empresa e : selected) {
            if (e.getTutores() != null && !e.getTutores().isEmpty()) {
                tieneTutores = true;
                break;
            }
        }
        if (tieneTutores) {
            warn("No se puede eliminar una empresa que tenga tutores asociados.\n" +
                 "Reasigne o elimine primero los tutores correspondientes.");
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro de que desea eliminar la(s) empresa(s) seleccionada(s)?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.isPresent() && action.get() == ButtonType.OK) {
            empresaService.deleteInBatch(List.copyOf(selected));
            loadEmpresaDetails();
        }
    }

    private void clearFields() {
        empresaId.setText(null);
        txtNombre.clear();
        txtDireccion.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHeader("Gestión de Empresas");
        empresaTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnProperties();
        loadEmpresaDetails();
    }

    private void setColumnProperties() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEmpresa"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colTutores.setCellValueFactory(cd -> {
            int n = 0;
            try {
                if (cd.getValue().getTutores() != null) {
                    n = cd.getValue().getTutores().size();
                }
            } catch (Exception ignore) {
                // Relación lazy no inicializada: mostramos "?" para no romper la tabla.
                return new ReadOnlyStringWrapper("?");
            }
            return new ReadOnlyStringWrapper(Integer.toString(n));
        });
        colEdit.setCellFactory(editCellFactory);
    }

    private final Callback<TableColumn<Empresa, Boolean>, TableCell<Empresa, Boolean>> editCellFactory =
            new Callback<TableColumn<Empresa, Boolean>, TableCell<Empresa, Boolean>>() {
        @Override
        public TableCell<Empresa, Boolean> call(final TableColumn<Empresa, Boolean> param) {
            return new TableCell<Empresa, Boolean>() {
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
                            Empresa emp = getTableView().getItems().get(getIndex());
                            populateForm(emp);
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

    private void populateForm(Empresa e) {
        empresaId.setText(Long.toString(e.getIdEmpresa()));
        txtNombre.setText(e.getNombre() != null ? e.getNombre() : "");
        txtDireccion.setText(e.getDireccion() != null ? e.getDireccion() : "");
    }

    private void loadEmpresaDetails() {
        empresaList.clear();
        empresaList.addAll(empresaService.findAll());
        empresaTable.setItems(empresaList);
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

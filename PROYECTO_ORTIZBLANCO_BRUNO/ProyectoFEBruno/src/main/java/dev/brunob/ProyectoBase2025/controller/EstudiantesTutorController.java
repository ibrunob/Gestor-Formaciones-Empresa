package dev.brunob.ProyectoBase2025.controller;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import dev.brunob.ProyectoBase2025.config.StageManager;
import dev.brunob.ProyectoBase2025.modelo.Estudiante;
import dev.brunob.ProyectoBase2025.modelo.FormacionEmpresa;
import dev.brunob.ProyectoBase2025.modelo.Tutor;
import dev.brunob.ProyectoBase2025.modelo.User;
import dev.brunob.ProyectoBase2025.services.FormacionEmpresaService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Vista de sólo lectura para el Tutor de Empresa: lista los estudiantes que
 * tiene asignados, con datos de su formación en empresa.
 */
@Controller
public class EstudiantesTutorController extends BaseMenuController implements Initializable {

    @Override
    protected String getPaginaAyuda() { return "asignaciones.html"; }

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML private Label lblScope;
    @FXML private TableView<Estudiante> estudiantesTable;
    @FXML private TableColumn<Estudiante, Long> colId;
    @FXML private TableColumn<Estudiante, String> colNombre;
    @FXML private TableColumn<Estudiante, String> colEmail;
    @FXML private TableColumn<Estudiante, String> colCurso;
    @FXML private TableColumn<Estudiante, String> colEstado;
    @FXML private TableColumn<Estudiante, String> colPeriodo;

    @Lazy @Autowired private StageManager stageManager;
    @Autowired private FormacionEmpresaService formacionEmpresaService;

    private final ObservableList<Estudiante> data = FXCollections.observableArrayList();
    private final Map<Long, FormacionEmpresa> feByEstId = new HashMap<>();

    @FXML
    private void volverMenu(ActionEvent event) {
        volverAlMenuPorRol();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHeader("Estudiantes Asignados");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(c -> new ReadOnlyStringWrapper(nombreCompleto(c.getValue())));
        colEmail.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue() != null && c.getValue().getEmail() != null ? c.getValue().getEmail() : ""));
        colCurso.setCellValueFactory(c -> new ReadOnlyStringWrapper(safeNombreCurso(c.getValue())));
        colEstado.setCellValueFactory(c -> {
            FormacionEmpresa fe = feByEstId.get(c.getValue().getId());
            return new ReadOnlyStringWrapper(fe != null && fe.getEstado() != null ? fe.getEstado() : "—");
        });
        colPeriodo.setCellValueFactory(c -> {
            FormacionEmpresa fe = feByEstId.get(c.getValue().getId());
            if (fe == null) return new ReadOnlyStringWrapper("—");
            String ini = fe.getFechaInicio() != null ? fe.getFechaInicio().format(FMT) : "?";
            String fin = fe.getFechaFin() != null ? fe.getFechaFin().format(FMT) : "?";
            return new ReadOnlyStringWrapper(ini + " → " + fin);
        });
        estudiantesTable.setItems(data);
    }

    @Override
    public void setCurrentUser(User user) {
        super.setCurrentUser(user);
        cargar();
    }

    private void cargar() {
        data.clear();
        feByEstId.clear();
        if (!(currentUser instanceof Tutor)) {
            lblScope.setText("Acceso disponible para Tutor de Empresa.");
            return;
        }
        lblScope.setText("Mostrando estudiantes con formación en empresa bajo su tutoría.");
        List<FormacionEmpresa> fes = formacionEmpresaService.findByTutor(currentUser.getId());
        List<Estudiante> lista = new ArrayList<>();
        for (FormacionEmpresa fe : fes) {
            Estudiante e = fe.getEstudiante();
            if (e == null) continue;
            if (!feByEstId.containsKey(e.getId())) {
                feByEstId.put(e.getId(), fe);
                lista.add(e);
            }
        }
        lista.sort(Comparator.comparing(this::nombreCompleto, String.CASE_INSENSITIVE_ORDER));
        data.addAll(lista);
    }

    private String nombreCompleto(User u) {
        if (u == null) return "";
        String fn = u.getFirstName() != null ? u.getFirstName() : "";
        String ln = u.getLastName() != null ? u.getLastName() : "";
        return (fn + " " + ln).trim();
    }

    private String safeNombreCurso(Estudiante e) {
        try {
            return e != null && e.getCurso() != null && e.getCurso().getNombre() != null
                    ? e.getCurso().getNombre() + " (" + e.getCurso().getAnio() + ")"
                    : "—";
        } catch (Exception ex) {
            return "—";
        }
    }
}

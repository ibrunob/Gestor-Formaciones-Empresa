package dev.brunob.ProyectoBase2025.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import dev.brunob.ProyectoBase2025.config.StageManager;
import dev.brunob.ProyectoBase2025.modelo.Administrador;
import dev.brunob.ProyectoBase2025.modelo.Empresa;
import dev.brunob.ProyectoBase2025.modelo.Estudiante;
import dev.brunob.ProyectoBase2025.modelo.FormacionEmpresa;
import dev.brunob.ProyectoBase2025.modelo.Profesor;
import dev.brunob.ProyectoBase2025.modelo.Tutor;
import dev.brunob.ProyectoBase2025.modelo.User;
import dev.brunob.ProyectoBase2025.services.EmpresaService;
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
 * Vista de sólo lectura: lista las empresas en las que el profesor (o cualquier
 * usuario consultor) tiene formaciones en empresa registradas.
 */
@Controller
public class EmpresasProfesorController extends BaseMenuController implements Initializable {

    @Override
    protected String getPaginaAyuda() { return "empresa.html"; }

    @FXML private Label lblScope;
    @FXML private TableView<EmpresaResumen> empresaTable;
    @FXML private TableColumn<EmpresaResumen, Long> colId;
    @FXML private TableColumn<EmpresaResumen, String> colNombre;
    @FXML private TableColumn<EmpresaResumen, String> colDireccion;
    @FXML private TableColumn<EmpresaResumen, String> colTutor;
    @FXML private TableColumn<EmpresaResumen, String> colEstudiantes;

    @Lazy @Autowired private StageManager stageManager;
    @Autowired private FormacionEmpresaService formacionEmpresaService;
    @Autowired private EmpresaService empresaService;

    private final ObservableList<EmpresaResumen> data = FXCollections.observableArrayList();

    @FXML
    private void volverMenu(ActionEvent event) {
        volverAlMenuPorRol();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initHeader("Empresas Asignadas");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().nombre()));
        colDireccion.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().direccion()));
        colTutor.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().tutores()));
        colEstudiantes.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().estudiantes()));
        empresaTable.setItems(data);
    }

    @Override
    public void setCurrentUser(User user) {
        super.setCurrentUser(user);
        cargar();
    }

    private void cargar() {
        data.clear();
        List<FormacionEmpresa> fes;
        if (currentUser instanceof Profesor) {
            fes = formacionEmpresaService.findByProfesor(currentUser.getId());
            lblScope.setText("Mostrando empresas con formaciones bajo su tutoría docente.");
        } else if (currentUser instanceof Administrador) {
            fes = formacionEmpresaService.findAll();
            lblScope.setText("Mostrando todas las empresas con formaciones registradas.");
        } else {
            fes = new ArrayList<>();
            lblScope.setText("Acceso de sólo lectura.");
        }

        // Agrupar por empresa
        Map<Long, List<FormacionEmpresa>> porEmpresa = new LinkedHashMap<>();
        for (FormacionEmpresa fe : fes) {
            Empresa emp = null;
            try {
                if (fe.getTutor() != null) emp = fe.getTutor().getEmpresa();
            } catch (Exception ignore) { /* lazy */ }
            if (emp == null || emp.getIdEmpresa() == null) continue;
            porEmpresa.computeIfAbsent(emp.getIdEmpresa(), k -> new ArrayList<>()).add(fe);
        }

        List<EmpresaResumen> resumenes = new ArrayList<>();
        for (Map.Entry<Long, List<FormacionEmpresa>> e : porEmpresa.entrySet()) {
            Empresa emp = empresaService.find(e.getKey());
            if (emp == null) continue;

            String tutores = e.getValue().stream()
                    .map(FormacionEmpresa::getTutor)
                    .filter(t -> t != null)
                    .map(this::nombreCompleto)
                    .distinct()
                    .reduce((a, b) -> a + ", " + b).orElse("—");

            String estudiantes = e.getValue().stream()
                    .map(FormacionEmpresa::getEstudiante)
                    .filter(es -> es != null)
                    .map(this::nombreCompleto)
                    .distinct()
                    .reduce((a, b) -> a + ", " + b).orElse("—");

            resumenes.add(new EmpresaResumen(
                    emp.getIdEmpresa(),
                    emp.getNombre() != null ? emp.getNombre() : "",
                    emp.getDireccion() != null ? emp.getDireccion() : "",
                    tutores,
                    estudiantes));
        }
        resumenes.sort(Comparator.comparing(EmpresaResumen::nombre, String.CASE_INSENSITIVE_ORDER));
        data.addAll(resumenes);
    }

    private String nombreCompleto(User u) {
        if (u == null) return "";
        String fn = u.getFirstName() != null ? u.getFirstName() : "";
        String ln = u.getLastName() != null ? u.getLastName() : "";
        return (fn + " " + ln).trim();
    }

    private String nombreCompleto(Estudiante e) { return nombreCompleto((User) e); }

    private String nombreCompleto(Tutor t) { return nombreCompleto((User) t); }

    public record EmpresaResumen(Long id, String nombre, String direccion,
                                 String tutores, String estudiantes) {
        public String tutor() { return tutores; }
    }
}

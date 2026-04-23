package dev.brunob.ProyectoBase2025.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import dev.brunob.ProyectoBase2025.config.StageManager;
import dev.brunob.ProyectoBase2025.modelo.Administrador;
import dev.brunob.ProyectoBase2025.modelo.FormacionEmpresa;
import dev.brunob.ProyectoBase2025.modelo.Profesor;
import dev.brunob.ProyectoBase2025.modelo.User;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * Caso de uso: Gestionar Informes.
 *
 * Disponible para administrador y profesor coordinador.
 *  - Administrador: informes de todas las formaciones del departamento.
 *  - Profesor coordinador: solo formaciones bajo su tutela.
 *
 * Filtros: ciclo formativo, estado, rango de fechas.
 * Salida: tabla detallada agrupada por curso académico + resumen estadístico
 *         + exportación CSV.
 */
@Controller
public class InformesController extends BaseMenuController implements Initializable {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String TODOS = "__TODOS__";

    @FXML private Label lblScopeInfo;
    @FXML private ComboBox<String> cbCiclo;
    @FXML private ComboBox<String> cbEstado;
    @FXML private DatePicker dpDesde;
    @FXML private DatePicker dpHasta;
    @FXML private FlowPane resumenPane;

    @FXML private TableView<FormacionEmpresa> informeTable;
    @FXML private TableColumn<FormacionEmpresa, String> colCurso;
    @FXML private TableColumn<FormacionEmpresa, String> colCiclo;
    @FXML private TableColumn<FormacionEmpresa, String> colEstudiante;
    @FXML private TableColumn<FormacionEmpresa, String> colEmpresa;
    @FXML private TableColumn<FormacionEmpresa, String> colTutorEmp;
    @FXML private TableColumn<FormacionEmpresa, String> colProfesor;
    @FXML private TableColumn<FormacionEmpresa, String> colInicio;
    @FXML private TableColumn<FormacionEmpresa, String> colFin;
    @FXML private TableColumn<FormacionEmpresa, String> colEstado;

    @Lazy @Autowired private StageManager stageManager;
    @Autowired private FormacionEmpresaService formacionEmpresaService;

    private final ObservableList<FormacionEmpresa> filas = FXCollections.observableArrayList();
    private List<FormacionEmpresa> dataset = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colCurso.setCellValueFactory(c -> str(nombreCurso(c.getValue())));
        colCiclo.setCellValueFactory(c -> str(nombreCiclo(c.getValue())));
        colEstudiante.setCellValueFactory(c -> str(nombreCompleto(c.getValue().getEstudiante())));
        colEmpresa.setCellValueFactory(c -> str(nombreEmpresa(c.getValue())));
        colTutorEmp.setCellValueFactory(c -> str(nombreCompleto(c.getValue().getTutor())));
        colProfesor.setCellValueFactory(c -> str(nombreCompleto(c.getValue().getProfesor())));
        colInicio.setCellValueFactory(c -> str(c.getValue().getFechaInicio() != null
                ? c.getValue().getFechaInicio().format(FMT) : ""));
        colFin.setCellValueFactory(c -> str(c.getValue().getFechaFin() != null
                ? c.getValue().getFechaFin().format(FMT) : ""));
        colEstado.setCellValueFactory(c -> str(c.getValue().getEstado()));
        informeTable.setItems(filas);
    }

    @Override
    public void setCurrentUser(User user) {
        super.setCurrentUser(user);
        cargarDataset();
        rellenarFiltros();
        aplicarFiltros();
    }

    private void cargarDataset() {
        if (currentUser instanceof Profesor && !(currentUser instanceof Administrador)) {
            dataset = new ArrayList<>(formacionEmpresaService.findByProfesor(currentUser.getId()));
            lblScopeInfo.setText("Profesor coordinador: informes limitados a las formaciones bajo su tutela.");
        } else {
            dataset = new ArrayList<>(formacionEmpresaService.findAll());
            lblScopeInfo.setText("Administrador: informes globales del departamento.");
        }
    }

    private void rellenarFiltros() {
        TreeSet<String> ciclos = new TreeSet<>();
        TreeSet<String> estados = new TreeSet<>();
        for (FormacionEmpresa f : dataset) {
            String c = nombreCiclo(f);
            if (c != null && !c.isEmpty() && !"—".equals(c)) ciclos.add(c);
            if (f.getEstado() != null && !f.getEstado().isEmpty()) estados.add(f.getEstado());
        }
        ObservableList<String> ciclosList = FXCollections.observableArrayList();
        ciclosList.add(TODOS);
        ciclosList.addAll(ciclos);
        cbCiclo.setItems(ciclosList);
        cbCiclo.setConverter(new javafx.util.StringConverter<String>() {
            @Override public String toString(String s) { return TODOS.equals(s) ? "Todos" : (s == null ? "" : s); }
            @Override public String fromString(String s) { return s; }
        });
        cbCiclo.getSelectionModel().select(TODOS);

        ObservableList<String> estadosList = FXCollections.observableArrayList();
        estadosList.add(TODOS);
        estadosList.addAll(estados);
        cbEstado.setItems(estadosList);
        cbEstado.setConverter(new javafx.util.StringConverter<String>() {
            @Override public String toString(String s) { return TODOS.equals(s) ? "Todos" : (s == null ? "" : s); }
            @Override public String fromString(String s) { return s; }
        });
        cbEstado.getSelectionModel().select(TODOS);
    }

    @FXML
    private void generar(ActionEvent event) {
        aplicarFiltros();
    }

    @FXML
    private void limpiar(ActionEvent event) {
        cbCiclo.getSelectionModel().select(TODOS);
        cbEstado.getSelectionModel().select(TODOS);
        dpDesde.setValue(null);
        dpHasta.setValue(null);
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        String ciclo = cbCiclo.getValue();
        String estado = cbEstado.getValue();
        LocalDate desde = dpDesde.getValue();
        LocalDate hasta = dpHasta.getValue();

        List<FormacionEmpresa> filtradas = new ArrayList<>();
        for (FormacionEmpresa f : dataset) {
            if (ciclo != null && !TODOS.equals(ciclo) && !ciclo.equals(nombreCiclo(f))) continue;
            if (estado != null && !TODOS.equals(estado) && !estado.equals(f.getEstado())) continue;
            if (desde != null && (f.getFechaFin() == null || f.getFechaFin().isBefore(desde))) continue;
            if (hasta != null && (f.getFechaInicio() == null || f.getFechaInicio().isAfter(hasta))) continue;
            filtradas.add(f);
        }
        // Ordenadas por curso académico (agrupación visual) y luego por estudiante.
        filtradas.sort(Comparator
                .comparing((FormacionEmpresa f) -> nombreCurso(f), Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(f -> nombreCompleto(f.getEstudiante()), Comparator.nullsLast(Comparator.naturalOrder())));
        filas.setAll(filtradas);
        actualizarResumen(filtradas);
    }

    private void actualizarResumen(List<FormacionEmpresa> lista) {
        resumenPane.getChildren().clear();
        int total = lista.size();
        Map<String, Integer> porEstado = new LinkedHashMap<>();
        Map<String, Integer> porCurso = new LinkedHashMap<>();
        for (FormacionEmpresa f : lista) {
            porEstado.merge(f.getEstado() != null ? f.getEstado() : "Sin estado", 1, Integer::sum);
            porCurso.merge(nombreCurso(f), 1, Integer::sum);
        }

        resumenPane.getChildren().add(card("Total formaciones", String.valueOf(total), "#3498db"));
        for (Map.Entry<String, Integer> e : porEstado.entrySet()) {
            resumenPane.getChildren().add(card("Estado: " + e.getKey(), String.valueOf(e.getValue()), "#27ae60"));
        }
        for (Map.Entry<String, Integer> e : porCurso.entrySet()) {
            if (e.getKey() == null || e.getKey().isEmpty()) continue;
            resumenPane.getChildren().add(card("Curso: " + e.getKey(), String.valueOf(e.getValue()), "#8e44ad"));
        }
    }

    private VBox card(String titulo, String valor, String color) {
        Label tl = new Label(titulo);
        tl.setStyle("-fx-font-size: 11px; -fx-text-fill: white;");
        Label vl = new Label(valor);
        vl.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        VBox box = new VBox(2, tl, vl);
        box.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8px; -fx-padding: 10 16 10 16;");
        return box;
    }

    @FXML
    private void exportar(ActionEvent event) {
        if (filas.isEmpty()) {
            info("No hay datos para exportar. Ajuste los filtros y pulse Generar.");
            return;
        }
        FileChooser fc = new FileChooser();
        fc.setTitle("Exportar informe a CSV");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        fc.setInitialFileName("informe_FE_" + LocalDate.now() + ".csv");
        File destino = fc.showSaveDialog(informeTable.getScene().getWindow());
        if (destino == null) return;
        try (BufferedWriter w = Files.newBufferedWriter(destino.toPath(), StandardCharsets.UTF_8)) {
            w.write("Curso;Ciclo;Estudiante;Empresa;Tutor empresa;Profesor tutor;Inicio;Fin;Estado");
            w.newLine();
            for (FormacionEmpresa f : filas) {
                w.write(String.join(";",
                        csv(nombreCurso(f)),
                        csv(nombreCiclo(f)),
                        csv(nombreCompleto(f.getEstudiante())),
                        csv(nombreEmpresa(f)),
                        csv(nombreCompleto(f.getTutor())),
                        csv(nombreCompleto(f.getProfesor())),
                        csv(f.getFechaInicio() != null ? f.getFechaInicio().format(FMT) : ""),
                        csv(f.getFechaFin() != null ? f.getFechaFin().format(FMT) : ""),
                        csv(f.getEstado())));
                w.newLine();
            }
            // Resumen al final
            w.newLine();
            w.write("Resumen");
            w.newLine();
            w.write("Total;" + filas.size());
            w.newLine();
            Map<String, Integer> porEstado = new HashMap<>();
            for (FormacionEmpresa f : filas) {
                porEstado.merge(f.getEstado() != null ? f.getEstado() : "Sin estado", 1, Integer::sum);
            }
            for (Map.Entry<String, Integer> e : porEstado.entrySet()) {
                w.write("Estado " + csv(e.getKey()) + ";" + e.getValue());
                w.newLine();
            }
            info("Informe exportado correctamente a:\n" + destino.getAbsolutePath());
        } catch (IOException ex) {
            info("Error al exportar: " + ex.getMessage());
        }
    }

    @FXML
    private void volverMenu(ActionEvent event) {
        if (currentUser instanceof Administrador) stageManager.switchScene(FxmlView.MENU_ADMIN);
        else if (currentUser instanceof Profesor) stageManager.switchScene(FxmlView.MENU_PROFESOR);
        else stageManager.switchScene(FxmlView.LOGIN);
    }

    private static String csv(String s) {
        if (s == null) return "";
        String r = s.replace("\"", "\"\"").replace(";", ",");
        return r;
    }

    private static javafx.beans.property.ReadOnlyStringWrapper str(String s) {
        return new ReadOnlyStringWrapper(s != null ? s : "");
    }

    private static String nombreCompleto(User u) {
        if (u == null) return "—";
        String fn = u.getFirstName() != null ? u.getFirstName() : "";
        String ln = u.getLastName() != null ? u.getLastName() : "";
        String r = (fn + " " + ln).trim();
        return r.isEmpty() ? "—" : r;
    }

    private static String nombreCurso(FormacionEmpresa f) {
        try {
            return f.getCurso() != null && f.getCurso().getNombre() != null ? f.getCurso().getNombre() : "—";
        } catch (Exception ex) { return "—"; }
    }

    private static String nombreCiclo(FormacionEmpresa f) {
        try {
            if (f.getCurso() != null && f.getCurso().getCicloFormativo() != null) {
                return f.getCurso().getCicloFormativo().getNombre();
            }
        } catch (Exception ignored) {}
        return "—";
    }

    private static String nombreEmpresa(FormacionEmpresa f) {
        try {
            if (f.getTutor() != null && f.getTutor().getEmpresa() != null) return f.getTutor().getEmpresa().getNombre();
        } catch (Exception ignored) {}
        return "—";
    }

    private void info(String msg) {
        Alert a = new Alert(AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}

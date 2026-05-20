package dev.brunob.ProyectoBase2025.controller;

import java.awt.Color;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeSet;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
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

    @Override
    protected String getPaginaAyuda() { return "informes.html"; }


    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String TODOS = "__TODOS__";
    private static final float PDF_MARGIN = 36f;

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
        initHeader("Informes de Formaciones en Empresa");
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
            Map<String, Integer> porEstado = contarPorEstado(filas);
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
    private void exportarPdf(ActionEvent event) {
        if (filas.isEmpty()) {
            info("No hay datos para exportar. Ajuste los filtros y pulse Generar.");
            return;
        }

        FileChooser fc = new FileChooser();
        fc.setTitle("Exportar informe a PDF");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        fc.setInitialFileName("informe_FE_estadisticas_" + LocalDate.now() + ".pdf");
        File destino = fc.showSaveDialog(informeTable.getScene().getWindow());
        if (destino == null) return;

        try (PDDocument document = new PDDocument()) {
            PdfReportWriter pdf = new PdfReportWriter(document);
            pdf.addTitle("Informe estadístico de Formaciones en Empresa");
            pdf.addParagraph(lblScopeInfo.getText());
            pdf.addSection("Filtros aplicados");
            pdf.addKeyValue("Ciclo formativo", valorFiltro(cbCiclo.getValue()));
            pdf.addKeyValue("Estado", valorFiltro(cbEstado.getValue()));
            pdf.addKeyValue("Desde", dpDesde.getValue() != null ? dpDesde.getValue().format(FMT) : "Sin límite");
            pdf.addKeyValue("Hasta", dpHasta.getValue() != null ? dpHasta.getValue().format(FMT) : "Sin límite");
            pdf.addKeyValue("Fecha de generación", LocalDate.now().format(FMT));

            pdf.addSection("Resumen estadístico");
            pdf.addKeyValue("Total de formaciones", String.valueOf(filas.size()));
            pdf.addSubsection("Por estado");
            for (Map.Entry<String, Integer> entry : contarPorEstado(filas).entrySet()) {
                pdf.addKeyValue(entry.getKey(), entry.getValue() + porcentaje(entry.getValue(), filas.size()));
            }
            pdf.addSubsection("Por curso académico");
            for (Map.Entry<String, Integer> entry : contarPorCurso(filas).entrySet()) {
                pdf.addKeyValue(entry.getKey(), entry.getValue() + porcentaje(entry.getValue(), filas.size()));
            }
            pdf.addSubsection("Por ciclo formativo");
            for (Map.Entry<String, Integer> entry : contarPorCiclo(filas).entrySet()) {
                pdf.addKeyValue(entry.getKey(), entry.getValue() + porcentaje(entry.getValue(), filas.size()));
            }

            pdf.addSection("Detalle de FE");
            pdf.addTable(new String[] {"Curso", "Ciclo", "Estudiante", "Empresa", "Tutor empresa", "Profesor", "Inicio", "Fin", "Estado"},
                    crearFilasDetallePdf());
            pdf.close();
            document.save(destino);
            info("Informe PDF generado correctamente en:\n" + destino.getAbsolutePath());
        } catch (IOException ex) {
            info("Error al generar el PDF: " + ex.getMessage());
        }
    }

    @FXML
    private void volverMenu(ActionEvent event) {
        volverAlMenuPorRol();
    }

    private static String csv(String s) {
        if (s == null) return "";
        String r = s.replace("\"", "\"\"").replace(";", ",");
        return r;
    }

    private static String valorFiltro(String value) {
        return value == null || TODOS.equals(value) ? "Todos" : value;
    }

    private static String porcentaje(int value, int total) {
        if (total <= 0) return "";
        double pct = value * 100.0 / total;
        return String.format(" (%.1f%%)", pct);
    }

    private static Map<String, Integer> contarPorEstado(List<FormacionEmpresa> lista) {
        Map<String, Integer> conteo = new LinkedHashMap<>();
        for (FormacionEmpresa f : lista) {
            conteo.merge(f.getEstado() != null && !f.getEstado().isEmpty() ? f.getEstado() : "Sin estado", 1, Integer::sum);
        }
        return conteo;
    }

    private static Map<String, Integer> contarPorCurso(List<FormacionEmpresa> lista) {
        Map<String, Integer> conteo = new LinkedHashMap<>();
        for (FormacionEmpresa f : lista) {
            conteo.merge(nombreCurso(f), 1, Integer::sum);
        }
        return conteo;
    }

    private static Map<String, Integer> contarPorCiclo(List<FormacionEmpresa> lista) {
        Map<String, Integer> conteo = new LinkedHashMap<>();
        for (FormacionEmpresa f : lista) {
            conteo.merge(nombreCiclo(f), 1, Integer::sum);
        }
        return conteo;
    }

    private List<String[]> crearFilasDetallePdf() {
        List<String[]> rows = new ArrayList<>();
        for (FormacionEmpresa f : filas) {
            rows.add(new String[] {
                    nombreCurso(f),
                    nombreCiclo(f),
                    nombreCompleto(f.getEstudiante()),
                    nombreEmpresa(f),
                    nombreCompleto(f.getTutor()),
                    nombreCompleto(f.getProfesor()),
                    f.getFechaInicio() != null ? f.getFechaInicio().format(FMT) : "",
                    f.getFechaFin() != null ? f.getFechaFin().format(FMT) : "",
                    f.getEstado()
            });
        }
        return rows;
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

    private static class PdfReportWriter {
        private static final float LINE_HEIGHT = 12f;
        private static final float CELL_PADDING = 3f;
        private static final float[] TABLE_WIDTHS = {78f, 86f, 110f, 88f, 88f, 88f, 50f, 50f, 55f};

        private final PDDocument document;
        private final PDRectangle pageSize = new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());
        private PDPageContentStream content;
        private float y;

        PdfReportWriter(PDDocument document) throws IOException {
            this.document = document;
            newPage();
        }

        void addTitle(String title) throws IOException {
            writeLine(title, PDType1Font.HELVETICA_BOLD, 18f, new Color(44, 62, 80));
            y -= 6f;
        }

        void addSection(String title) throws IOException {
            y -= 5f;
            ensureSpace(25f);
            writeLine(title, PDType1Font.HELVETICA_BOLD, 13f, new Color(41, 128, 185));
            y -= 3f;
        }

        void addSubsection(String title) throws IOException {
            y -= 2f;
            ensureSpace(18f);
            writeLine(title, PDType1Font.HELVETICA_BOLD, 10f, new Color(52, 73, 94));
        }

        void addParagraph(String text) throws IOException {
            for (String line : wrap(text, PDType1Font.HELVETICA, 9f, pageSize.getWidth() - PDF_MARGIN * 2)) {
                writeLine(line, PDType1Font.HELVETICA, 9f, Color.DARK_GRAY);
            }
            y -= 3f;
        }

        void addKeyValue(String key, String value) throws IOException {
            String text = key + ": " + (value != null && !value.isEmpty() ? value : "-");
            for (String line : wrap(text, PDType1Font.HELVETICA, 9f, pageSize.getWidth() - PDF_MARGIN * 2)) {
                writeLine(line, PDType1Font.HELVETICA, 9f, Color.BLACK);
            }
        }

        void addTable(String[] headers, List<String[]> rows) throws IOException {
            drawRow(headers, true);
            for (String[] row : rows) {
                drawRow(row, false);
            }
        }

        void close() throws IOException {
            if (content != null) {
                content.close();
                content = null;
            }
        }

        private void newPage() throws IOException {
            if (content != null) {
                content.close();
            }
            PDPage page = new PDPage(pageSize);
            document.addPage(page);
            content = new PDPageContentStream(document, page);
            y = pageSize.getHeight() - PDF_MARGIN;
        }

        private void ensureSpace(float needed) throws IOException {
            if (y - needed < PDF_MARGIN) {
                newPage();
            }
        }

        private void writeLine(String text, PDFont font, float size, Color color) throws IOException {
            ensureSpace(LINE_HEIGHT + 2f);
            content.beginText();
            content.setFont(font, size);
            content.setNonStrokingColor(color);
            content.newLineAtOffset(PDF_MARGIN, y);
            content.showText(pdfText(text));
            content.endText();
            y -= LINE_HEIGHT;
        }

        private void drawRow(String[] values, boolean header) throws IOException {
            PDFont font = header ? PDType1Font.HELVETICA_BOLD : PDType1Font.HELVETICA;
            float size = header ? 8f : 7f;
            List<List<String>> linesByCell = new ArrayList<>();
            int maxLines = 1;
            for (int i = 0; i < values.length; i++) {
                List<String> lines = wrap(values[i], font, size, TABLE_WIDTHS[i] - CELL_PADDING * 2);
                linesByCell.add(lines);
                maxLines = Math.max(maxLines, lines.size());
            }
            float rowHeight = Math.max(18f, maxLines * 9f + CELL_PADDING * 2);
            ensureSpace(rowHeight + 4f);

            float x = PDF_MARGIN;
            Color background = header ? new Color(236, 240, 241) : Color.WHITE;
            content.setNonStrokingColor(background);
            content.addRect(x, y - rowHeight + 4f, totalTableWidth(), rowHeight);
            content.fill();
            content.setStrokingColor(new Color(189, 195, 199));

            for (int i = 0; i < values.length; i++) {
                content.addRect(x, y - rowHeight + 4f, TABLE_WIDTHS[i], rowHeight);
                content.stroke();
                float textY = y - 7f;
                for (String line : linesByCell.get(i)) {
                    content.beginText();
                    content.setFont(font, size);
                    content.setNonStrokingColor(Color.BLACK);
                    content.newLineAtOffset(x + CELL_PADDING, textY);
                    content.showText(pdfText(line));
                    content.endText();
                    textY -= 9f;
                }
                x += TABLE_WIDTHS[i];
            }
            y -= rowHeight;
        }

        private static float totalTableWidth() {
            float total = 0f;
            for (float width : TABLE_WIDTHS) total += width;
            return total;
        }

        private static List<String> wrap(String value, PDFont font, float size, float maxWidth) throws IOException {
            String text = pdfText(value);
            List<String> lines = new ArrayList<>();
            if (text.isEmpty()) {
                lines.add("-");
                return lines;
            }
            StringBuilder current = new StringBuilder();
            for (String word : text.split("\\s+")) {
                String candidate = current.length() == 0 ? word : current + " " + word;
                if (width(candidate, font, size) <= maxWidth) {
                    current.setLength(0);
                    current.append(candidate);
                } else {
                    if (current.length() > 0) {
                        lines.add(current.toString());
                        current.setLength(0);
                    }
                    if (width(word, font, size) <= maxWidth) {
                        current.append(word);
                    } else {
                        splitLongWord(word, font, size, maxWidth, lines, current);
                    }
                }
            }
            if (current.length() > 0) lines.add(current.toString());
            return lines;
        }

        private static void splitLongWord(String word, PDFont font, float size, float maxWidth,
                                          List<String> lines, StringBuilder current) throws IOException {
            StringBuilder part = new StringBuilder();
            for (int i = 0; i < word.length(); i++) {
                String candidate = part.toString() + word.charAt(i);
                if (width(candidate, font, size) <= maxWidth) {
                    part.append(word.charAt(i));
                } else {
                    if (part.length() > 0) lines.add(part.toString());
                    part.setLength(0);
                    part.append(word.charAt(i));
                }
            }
            current.append(part);
        }

        private static float width(String text, PDFont font, float size) throws IOException {
            return font.getStringWidth(pdfText(text)) / 1000f * size;
        }

        private static String pdfText(String text) {
            if (text == null) return "";
            return text.replace('\n', ' ')
                    .replace('\r', ' ')
                    .replace('—', '-')
                    .replace('–', '-')
                    .replace('·', '-')
                    .trim();
        }
    }
}

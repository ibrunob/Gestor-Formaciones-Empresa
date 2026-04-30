# Guía de Estilos — Proyecto FE Bruno

Documento de referencia para mantener una **apariencia visual** y un **estilo de código** coherentes a lo largo del proyecto `ProyectoFEBruno` (JavaFX + Spring Boot).

---

## 1. Información general

| Dato | Valor |
|------|-------|
| Proyecto | ProyectoFEBruno |
| Lenguaje | Java 17+ |
| Framework UI | JavaFX (FXML + CSS) |
| Framework backend | Spring Boot |
| Build | Maven (`mvnw`) |
| Paquete raíz | `dev.brunob.ProyectoBase2025` |
| i18n | `Bundle.properties` |

---

## 2. Estructura de carpetas

```
src/main/java/dev/brunob/ProyectoBase2025/
├── config/         → Configuración Spring + carga FXML
├── controller/     → Controladores JavaFX (uno por vista)
├── modelo/         → Entidades JPA / POJOs
├── repositorios/   → Interfaces Spring Data
├── services/       → Lógica de negocio
└── view/           → Enums / utilidades de vista

src/main/resources/
├── fxml/           → Vistas FXML
├── styles/         → Hojas de estilo CSS (JavaFX)
├── images/         → Recursos gráficos
├── Bundle.properties
└── application.properties
```

**Reglas:**
- Cada vista FXML debe tener **un único controlador** en `controller/` con el mismo nombre (`Login.fxml` → `LoginController.java`).
- Las clases de modelo van en singular (`Empresa`, no `Empresas`).
- Los repositorios terminan en `Repository` y heredan de `JpaRepository`.

---

## 3. Convenciones de nombrado

### 3.1 Java

| Elemento | Convención | Ejemplo |
|----------|------------|---------|
| Clases | `PascalCase` | `EvaluacionController` |
| Métodos / variables | `camelCase` | `cargarEstudiantes()` |
| Constantes | `UPPER_SNAKE_CASE` | `MAX_INTENTOS` |
| Paquetes | `lowercase` | `dev.brunob.proyectobase2025.services` |
| Controladores | sufijo `Controller` | `MenuAdminController` |
| Servicios | sufijo `Service` | `UsuarioService` |
| Repositorios | sufijo `Repository` | `EmpresaRepository` |

### 3.2 FXML

- Nombre del archivo en `PascalCase`: `MenuEstudiante.fxml`.
- Atributo `fx:id` en `camelCase`: `txtUsuario`, `btnLogin`, `tblEstudiantes`.
- Prefijos sugeridos para `fx:id`:
  - `txt` → TextField / TextArea
  - `pwd` → PasswordField
  - `btn` → Button
  - `lbl` → Label
  - `cbo` → ComboBox
  - `tbl` → TableView
  - `col` → TableColumn
  - `lst` → ListView
  - `chk` → CheckBox
  - `rdo` → RadioButton

### 3.3 CSS (JavaFX)

- Clases en `kebab-case`: `.menu-card`, `.btn-logout`.
- Clases utilitarias breves en `camelCase` cuando ya existen (`.btnGreen`, `.btnLogin`) — mantener por compatibilidad.
- Evitar IDs (`#`) en CSS; preferir `styleClass`.

---

## 4. Paleta de colores

Basada en los estilos actuales (`Menu.css`, `Login.css`, `Styles.css`).

### 4.1 Primarios

| Token | Hex | Uso |
|-------|-----|-----|
| Azul principal | `#3498db` | Botones, foco, cabeceras |
| Azul oscuro | `#2980b9` | Hover de botones, gradientes |
| Azul presión | `#2471a3` | Estado `:pressed` |
| Azul marino | `#2c3e50` | Menús, títulos, headers de tabla |
| Azul marino hover | `#34495e` | Hover de menú |

### 4.2 Estados

| Token | Hex | Uso |
|-------|-----|-----|
| Éxito | `#27ae60` / hover `#219a52` | `.button-success`, `.btnGreen` (`#3cbc53`) |
| Peligro | `#e74c3c` / hover `#c0392b` / pressed `#a93226` | `.button-danger`, `.btn-logout` |
| Advertencia | `#f39c12` / hover `#d68910` | `.button-warning` |

### 4.3 Neutros

| Token | Hex | Uso |
|-------|-----|-----|
| Fondo app | `#f5f6fa` | `.root` |
| Fondo tarjeta | `#ffffff` | Cards, inputs |
| Fondo alterno | `#f8f9fa` | Filas impares de tabla, hover de card |
| Borde suave | `#bdc3c7` | Bordes inputs, tablas |
| Borde claro | `#ddd` | Separadores, context menu |
| Texto principal | `#2c3e50` | Títulos |
| Texto secundario | `#7f8c8d` | Subtítulos |
| Texto deshabilitado | `#95a5a6` | Descripciones |
| Footer | `#ecf0f1` | Pie de página |

---

## 5. Tipografía

| Elemento | Tamaño | Peso | Color |
|----------|--------|------|-------|
| `.main-title` | 32 px | Bold | `#2c3e50` |
| `.title-header` | 24 px | Bold | `white` |
| `.subtitle` | 18 px | Normal | `#7f8c8d` |
| `.card-title` | 18 px | Bold | `#2c3e50` |
| `.label` (form) | 14 px | Normal | — |
| `.subtitle-header` | 14 px | Normal | `#ecf0f1` |
| `.card-description` | 13 px | Normal | `#95a5a6` |
| Pie de página | 12 px | Normal | `#7f8c8d` |

> La pantalla de **Login** usa `Comic Sans MS` por diseño. Para el resto de vistas se utiliza la tipografía por defecto de JavaFX (Segoe UI / System).

---

## 6. Espaciado y radios

- **Padding interno** estándar de inputs/botones: `10px` (vertical) × `20px` (horizontal).
- **Padding** de menú-card: `30px`.
- **Border radius**:
  - Inputs y botones: `5px`
  - Botones de Login: `12px`
  - Menú-card: `15px`
  - Avatar/icono circular: `50%`
- **Sombras**: usar `dropshadow(three-pass-box, rgba(0,0,0,0.15), 15, 0, 0, 5)` para tarjetas elevadas.

---

## 7. Componentes UI

### 7.1 Botones

```xml
<Button text="Guardar" styleClass="button" />
<Button text="Eliminar" styleClass="button,button-danger" />
<Button text="Aceptar" styleClass="button,button-success" />
<Button text="Aviso"   styleClass="button,button-warning" />
```

- Estado por defecto: azul `#3498db`.
- Variantes semánticas: `button-success`, `button-danger`, `button-warning`.
- Cursor siempre `hand`.

### 7.2 Campos de formulario

- `TextField`, `PasswordField`, `TextArea`, `ComboBox` comparten:
  - Fondo blanco, borde `#bdc3c7`, radio `5px`.
  - En foco: borde `#3498db` + sombra azul translúcida.

### 7.3 Tablas

- Cabecera azul marino con texto blanco.
- Filas impares con fondo `#f8f9fa`.
- Selección con fondo `#3498db`.

### 7.4 Tarjetas de menú (`.menu-card`)

- Fondo blanco, sombra suave, hover con escalado `1.03` y sombra acentuada.
- Tamaño preferido: 250 × 220 px.

---

## 8. Estilo de código Java

- **Indentación:** 4 espacios, sin tabuladores.
- **Llaves:** misma línea (`if (...) {`).
- **Imports:** sin comodines `*`, ordenados alfabéticamente.
- **Anotaciones Spring:** `@Component`, `@Service`, `@Repository`, `@Autowired` (preferir constructor injection sobre field injection).
- **Logs:** usar `org.slf4j.Logger` (nunca `System.out.println` en producción).
- **Excepciones:** capturar lo más específico posible; nunca `catch (Exception e) {}` vacío.
- **Comentarios:** Javadoc en clases y métodos públicos del modelo y servicios.
- **Idioma:** identificadores y comentarios en **español** (consistente con el dominio del proyecto).

### 8.1 Ejemplo de controlador

```java
@Component
public class LoginController {

    private final UsuarioService usuarioService;

    @FXML private TextField txtUsuario;
    @FXML private PasswordField pwdContrasena;
    @FXML private Button btnLogin;

    @Autowired
    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @FXML
    public void onLogin(ActionEvent event) {
        // ...
    }
}
```

---

## 9. Internacionalización

- Todos los literales visibles van en `Bundle.properties`.
- Claves en `snake_case` agrupadas por vista: `login.titulo`, `login.boton.entrar`, `menu.admin.usuarios`.
- En FXML: `<Label text="%login.titulo"/>`.

---

## 10. Recursos gráficos

- Imágenes en `resources/images/`.
- Formato preferido: **PNG** con transparencia.
- Nombres en `kebab-case`: `logo-app.png`, `icono-usuario.png`.
- No incrustar rutas absolutas; cargar siempre vía `getClass().getResource("/images/...")`.

---

## 11. Git y commits

- Rama principal: `main`.
- Mensajes en español, en imperativo y con prefijo:
  - `feat:` nueva funcionalidad
  - `fix:` corrección de bug
  - `style:` cambios visuales / formato
  - `refactor:` reestructuración sin cambio funcional
  - `docs:` documentación
  - `chore:` tareas auxiliares

Ejemplo: `feat: añadir validación de NIF en formulario empresa`.

---

## 12. Checklist antes de commitear

- [ ] El proyecto compila (`./mvnw -DskipTests compile`).
- [ ] No hay `System.out.println` ni código comentado muerto.
- [ ] Los nuevos literales están en `Bundle.properties`.
- [ ] Las nuevas vistas usan las clases CSS existentes (no estilos inline).
- [ ] Los `fx:id` siguen el prefijo del componente.
- [ ] El controlador asociado al FXML existe y está anotado como `@Component`.

---

_Última actualización: abril 2026._

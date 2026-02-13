package dev.brunob.ProyectoBase2025.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import dev.brunob.ProyectoBase2025.config.StageManager;
import dev.brunob.ProyectoBase2025.modelo.Administrador;
import dev.brunob.ProyectoBase2025.modelo.Estudiante;
import dev.brunob.ProyectoBase2025.modelo.Profesor;
import dev.brunob.ProyectoBase2025.modelo.Role;
import dev.brunob.ProyectoBase2025.modelo.Tutor;
import dev.brunob.ProyectoBase2025.modelo.User;
import dev.brunob.ProyectoBase2025.services.UserService;
import dev.brunob.ProyectoBase2025.view.FxmlView;
import javafx.application.Platform;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * Controlador para la gestión de usuarios del sistema
 * Permite crear, actualizar y eliminar usuarios
 */
@Controller
public class UserController implements Initializable {

	@FXML
	private Label userId;

	@FXML
	private TextField firstName;

	@FXML
	private TextField lastName;

	@FXML
	private DatePicker dob;

	@FXML
	private RadioButton rbMale;

	@FXML
	private ToggleGroup gender;

	@FXML
	private RadioButton rbFemale;

	@FXML
	private ComboBox<String> cbRole;

	@FXML
	private TextField email;

	@FXML
	private PasswordField password;

	@FXML
	private Button reset;

	@FXML
	private Button saveUser;

	@FXML
	private VBox panelProfesor;

	@FXML
	private CheckBox cbCoordinador;

	@FXML
	private VBox panelTutor;

	@FXML
	private TextField txtTelefono;

	@FXML
	private TableView<User> userTable;

	@FXML
	private TableColumn<User, Long> colUserId;

	@FXML
	private TableColumn<User, String> colFirstName;

	@FXML
	private TableColumn<User, String> colLastName;

	@FXML
	private TableColumn<User, LocalDate> colDOB;

	@FXML
	private TableColumn<User, String> colGender;

	@FXML
	private TableColumn<User, String> colRole;

	@FXML
	private TableColumn<User, String> colEmail;

	@FXML
	private TableColumn<User, Boolean> colEdit;

	@FXML
	private MenuItem deleteUsers;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private UserService userService;

	private ObservableList<User> userList = FXCollections.observableArrayList();

	private ObservableList<String> roles = FXCollections.observableArrayList();

	@FXML
	private void exit(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	private void logout(ActionEvent event) throws IOException {
		stageManager.switchScene(FxmlView.LOGIN);
	}

	@FXML
	private void volverMenu(ActionEvent event) throws IOException {
		stageManager.switchScene(FxmlView.MENU_ADMIN);
	}

	@FXML
	private void acercaDe(ActionEvent event) {
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
	private void saveUser(ActionEvent event) {

		if (validate("Nombre", getFirstName(), "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")
				&& validate("Apellidos", getLastName(), "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")
				&& emptyValidation("Fecha de Nacimiento", dob.getEditor().getText().isEmpty())
				&& emptyValidation("Rol", getRole() == null)) {

			if (userId.getText() == null || userId.getText().isEmpty()) {
				// Crear nuevo usuario
				if (validate("Email", getEmail(), "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+")
						&& emptyValidation("Contraseña", getPassword().isEmpty())) {

					User user = createUserByRole(getRole());
					user.setFirstName(getFirstName());
					user.setLastName(getLastName());
					user.setDob(getDob());
					user.setGender(getGender());
					user.setRole(getRole());
					user.setEmail(getEmail());
					user.setPassword(getPassword());

					// Campos especificos del rol
					applyRoleSpecificFields(user);

					User newUser = userService.save(user);
					saveAlert(newUser);
				}
			} else {
				// Actualizar usuario existente
				User user = userService.find(Long.parseLong(userId.getText()));
				user.setFirstName(getFirstName());
				user.setLastName(getLastName());
				user.setDob(getDob());
				user.setGender(getGender());

				applyRoleSpecificFields(user);
				
				User updatedUser = userService.update(user);
				updateAlert(updatedUser);
			}

			clearFields();
			loadUserDetails();
		}
	}

	/**
	 * Crea la instancia correcta de User según el rol seleccionado
	 */
	private User createUserByRole(String roleName) {
		Role role = Role.fromDisplayName(roleName);
		if (role == null) {
			return new User();
		}
		switch (role) {
			case ADMINISTRADOR:
				return new Administrador();
			case PROFESOR_TUTOR:
				return new Profesor();
			case TUTOR_EMPRESA:
				return new Tutor();
			case ESTUDIANTE:
				return new Estudiante();
			default:
				return new User();
		}
	}

	/**
	 * Aplica los campos específicos del rol al usuario.
	 */
	private void applyRoleSpecificFields(User user) {
		if (user instanceof Profesor) {
			Profesor profesor = (Profesor) user;
			profesor.setEsCoordinador(cbCoordinador.isSelected());
		} else if (user instanceof Tutor) {
			Tutor tutor = (Tutor) user;
			tutor.setTelefono(txtTelefono.getText());
		}
	}

	@FXML
	private void deleteUsers(ActionEvent event) {
		List<User> users = userTable.getSelectionModel().getSelectedItems();

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar eliminación");
		alert.setHeaderText(null);
		alert.setContentText("¿Está seguro de que desea eliminar el usuario seleccionado?");
		Optional<ButtonType> action = alert.showAndWait();

		if (action.isPresent() && action.get() == ButtonType.OK) {
			userService.deleteInBatch(users);
		}

		loadUserDetails();
	}

	private void clearFields() {
		userId.setText(null);
		firstName.clear();
		lastName.clear();
		dob.getEditor().clear();
		dob.setValue(null);
		rbMale.setSelected(true);
		rbFemale.setSelected(false);
		cbRole.getSelectionModel().clearSelection();
		email.clear();
		password.clear();
		cbCoordinador.setSelected(false);
		txtTelefono.clear();
		panelProfesor.setVisible(false);
		panelProfesor.setManaged(false);
		panelTutor.setVisible(false);
		panelTutor.setManaged(false);

		cbRole.setDisable(false);
		email.setDisable(false);
		password.setDisable(false);
	}

	private void saveAlert(User user) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Usuario guardado");
		alert.setHeaderText(null);
		alert.setContentText("El usuario " + user.getFirstName() + " " + user.getLastName()
				+ " ha sido creado correctamente con ID " + user.getId() + ".");
		alert.showAndWait();
	}

	private void updateAlert(User user) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Usuario actualizado");
		alert.setHeaderText(null);
		alert.setContentText("El usuario " + user.getFirstName() + " " + user.getLastName()
				+ " ha sido actualizado correctamente.");
		alert.showAndWait();
	}

	public String getFirstName() {
		return firstName.getText();
	}

	public String getLastName() {
		return lastName.getText();
	}

	public LocalDate getDob() {
		return dob.getValue();
	}

	public String getGender() {
		return rbMale.isSelected() ? "Masculino" : "Femenino";
	}

	public String getRole() {
		return cbRole.getSelectionModel().getSelectedItem();
	}

	public String getEmail() {
		return email.getText();
	}

	public String getPassword() {
		return password.getText();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		for (Role role : Role.values()) {
			roles.add(role.getDisplayName());
		}
		cbRole.setItems(roles);

		// Listener para los campos específicos por rol
		cbRole.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			toggleRoleSpecificFields(newVal);
		});

		// Ocultar paneles específicos de rol al inicio
		panelProfesor.setVisible(false);
		panelProfesor.setManaged(false);
		panelTutor.setVisible(false);
		panelTutor.setManaged(false);

		userTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		setColumnProperties();

		loadUserDetails();
	}

	/**
	 * Muestra u oculta los campos específicos según el rol seleccionado.
	 */
	private void toggleRoleSpecificFields(String roleName) {
		boolean isProfesor = Role.PROFESOR_TUTOR.getDisplayName().equals(roleName);
		boolean isTutor = Role.TUTOR_EMPRESA.getDisplayName().equals(roleName);

		panelProfesor.setVisible(isProfesor);
		panelProfesor.setManaged(isProfesor);
		panelTutor.setVisible(isTutor);
		panelTutor.setManaged(isTutor);
	}

	private void setColumnProperties() {
		colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
		colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colEdit.setCellFactory(cellFactory);
	}

	Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>> cellFactory = new Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>>() {
		@Override
		public TableCell<User, Boolean> call(final TableColumn<User, Boolean> param) {
			final TableCell<User, Boolean> cell = new TableCell<User, Boolean>() {
				Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
				final Button btnEdit = new Button();

				@Override
				public void updateItem(Boolean check, boolean empty) {
					super.updateItem(check, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {
						btnEdit.setOnAction(e -> {
							User user = getTableView().getItems().get(getIndex());
							updateUser(user);
						});

						btnEdit.setStyle("-fx-background-color: transparent;");
						ImageView iv = new ImageView();
						iv.setImage(imgEdit);
						iv.setPreserveRatio(true);
						iv.setSmooth(true);
						iv.setCache(true);
						btnEdit.setGraphic(iv);

						setGraphic(btnEdit);
						setAlignment(Pos.CENTER);
						setText(null);
					}
				}

				private void updateUser(User user) {
					userId.setText(Long.toString(user.getId()));
					firstName.setText(user.getFirstName());
					lastName.setText(user.getLastName());
					dob.setValue(user.getDob());
					if ("Masculino".equals(user.getGender())) {
						rbMale.setSelected(true);
					} else {
						rbFemale.setSelected(true);
					}
					cbRole.getSelectionModel().select(user.getRole());
					email.setText(user.getEmail());

					cbRole.setDisable(true);
					email.setDisable(true);
					password.setDisable(true);

					if (user instanceof Profesor) {
						Profesor profesor = (Profesor) user;
						cbCoordinador.setSelected(
								profesor.getEsCoordinador() != null && profesor.getEsCoordinador());
					} else if (user instanceof Tutor) {
						Tutor tutor = (Tutor) user;
						txtTelefono.setText(tutor.getTelefono() != null ? tutor.getTelefono() : "");
					}

					toggleRoleSpecificFields(user.getRole());
				}
			};
			return cell;
		}
	};

	private void loadUserDetails() {
		userList.clear();
		userList.addAll(userService.findAll());
		userTable.setItems(userList);
	}

	/*
	 * Validaciones
	 */
	private boolean validate(String field, String value, String pattern) {
		if (!value.isEmpty()) {
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(value);
			if (m.find() && m.group().equals(value)) {
				return true;
			} else {
				validationAlert(field, false);
				return false;
			}
		} else {
			validationAlert(field, true);
			return false;
		}
	}

	private boolean emptyValidation(String field, boolean empty) {
		if (!empty) {
			return true;
		} else {
			validationAlert(field, true);
			return false;
		}
	}

	private void validationAlert(String field, boolean empty) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error de Validación");
		alert.setHeaderText(null);
		if (field.equals("Rol")) {
			alert.setContentText("Por favor, seleccione un " + field);
		} else {
			if (empty) {
				alert.setContentText("Por favor, introduzca " + field);
			} else {
				alert.setContentText("Por favor, introduzca un " + field + " válido");
			}
		}
		alert.showAndWait();
	}
}

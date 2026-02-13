package dev.brunob.ProyectoBase2025.view;

import java.util.ResourceBundle;

public enum FxmlView {
	USER {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("user.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/User.fxml";
		}
	},
	LOGIN {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("login.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Login.fxml";
		}
	},
	INICIO {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("inicio.title");
		}
		@Override
		public String getFxmlFile() {
			return "/fxml/Inicio.fxml";
		}
	},
	MENU_ADMIN {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("menu.admin.title");
		}
		@Override
		public String getFxmlFile() {
			return "/fxml/MenuAdmin.fxml";
		}
	},
	MENU_PROFESOR {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("menu.profesor.title");
		}
		@Override
		public String getFxmlFile() {
			return "/fxml/MenuProfesor.fxml";
		}
	},
	MENU_TUTOR_EMPRESA {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("menu.tutor.empresa.title");
		}
		@Override
		public String getFxmlFile() {
			return "/fxml/MenuTutorEmpresa.fxml";
		}
	},
	MENU_ESTUDIANTE {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("menu.estudiante.title");
		}
		@Override
		public String getFxmlFile() {
			return "/fxml/MenuEstudiante.fxml";
		}
	};

	public abstract String getTitle();

	public abstract String getFxmlFile();

	String getStringFromResourceBundle(String key) {
		return ResourceBundle.getBundle("Bundle").getString(key);
	}
}

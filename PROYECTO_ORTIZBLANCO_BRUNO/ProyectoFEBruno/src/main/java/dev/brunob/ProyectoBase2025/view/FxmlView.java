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
	},
	FORMACION {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("formacion.title");
		}
		@Override
		public String getFxmlFile() {
			return "/fxml/Formacion.fxml";
		}
	},
	EMPRESA {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("empresa.title");
		}
		@Override
		public String getFxmlFile() {
			return "/fxml/Empresa.fxml";
		}
	},
	ASIGNACIONES {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("asignaciones.title");
		}
		@Override
		public String getFxmlFile() {
			return "/fxml/Asignaciones.fxml";
		}
	},
	EVALUACIONES {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("evaluaciones.title");
		}
		@Override
		public String getFxmlFile() {
			return "/fxml/Evaluaciones.fxml";
		}
	},
	DOCUMENTOS {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("documentos.title");
		}
		@Override
		public String getFxmlFile() {
			return "/fxml/Documentos.fxml";
		}
	},
	MI_INFORMACION {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("mi.informacion.title");
		}
		@Override
		public String getFxmlFile() {
			return "/fxml/MiInformacion.fxml";
		}
	},
	INFORMES {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("informes.title");
		}
		@Override
		public String getFxmlFile() {
			return "/fxml/Informes.fxml";
		}
	};

	public abstract String getTitle();

	public abstract String getFxmlFile();

	String getStringFromResourceBundle(String key) {
		return ResourceBundle.getBundle("Bundle").getString(key);
	}
}

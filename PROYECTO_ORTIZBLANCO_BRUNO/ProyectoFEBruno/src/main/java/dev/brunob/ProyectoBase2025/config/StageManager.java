package dev.brunob.ProyectoBase2025.config;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Objects;

import org.slf4j.Logger;

import dev.brunob.ProyectoBase2025.view.FxmlView;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Manages switching Scenes on the Primary Stage
 */
public class StageManager {

    private static final Logger LOG = getLogger(StageManager.class);
    private final Stage primaryStage;
    private final SpringFXMLLoader springFXMLLoader;

    public StageManager(SpringFXMLLoader springFXMLLoader, Stage stage) {
        this.springFXMLLoader = springFXMLLoader;
        this.primaryStage = stage;
    }

    public void switchScene(final FxmlView view) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
        show(viewRootNodeHierarchy, view.getTitle(), view);
    }
    
    private void show(final Parent rootnode, String title, FxmlView view) {
        Scene scene = prepareScene(rootnode);
        
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        
        if (isMenuView(view)) {
            // Para los menus principales, maximizar la ventana
            primaryStage.setResizable(true);
            primaryStage.setMaximized(true);
        } else if (view == FxmlView.LOGIN) {
            // Para el login, ventana fija centrada
            primaryStage.setMaximized(false);
            primaryStage.setResizable(false);
            primaryStage.setWidth(1000);
            primaryStage.setHeight(600);
            primaryStage.centerOnScreen();
        } else {
            primaryStage.setMaximized(false);
            primaryStage.setResizable(true);
            primaryStage.setWidth(1200);
            primaryStage.setHeight(700);
            primaryStage.centerOnScreen();
        }
        
        try {
            primaryStage.show();
        } catch (Exception exception) {
            logAndExit ("Unable to show scene for title" + title,  exception);
        }
    }
    
    /**
     * Verifica si la vista es uno de los menús principales por rol o pantallas de gestión.
     */
    private boolean isMenuView(FxmlView view) {
        return view == FxmlView.MENU_ADMIN || 
               view == FxmlView.MENU_PROFESOR || 
               view == FxmlView.MENU_TUTOR_EMPRESA || 
               view == FxmlView.MENU_ESTUDIANTE ||
               view == FxmlView.USER;
    }
    
    private Scene prepareScene(Parent rootnode){
        Scene scene = primaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootnode);
        }
        scene.setRoot(rootnode);
        return scene;
    }

    /**
     * Loads the object hierarchy from a FXML document and returns to root node
     * of that hierarchy.
     *
     * @return Parent root node of the FXML document hierarchy
     */
    private Parent loadViewNodeHierarchy(String fxmlFilePath) {
        Parent rootNode = null;
        try {
            rootNode = springFXMLLoader.load(fxmlFilePath);
            Objects.requireNonNull(rootNode, "A Root FXML node must not be null");
        } catch (Exception exception) {
            logAndExit("Unable to load FXML view" + fxmlFilePath, exception);
        }
        return rootNode;
    }
    
    
    private void logAndExit(String errorMsg, Exception exception) {
        LOG.error(errorMsg, exception, exception.getCause());
        Platform.exit();
    }

}

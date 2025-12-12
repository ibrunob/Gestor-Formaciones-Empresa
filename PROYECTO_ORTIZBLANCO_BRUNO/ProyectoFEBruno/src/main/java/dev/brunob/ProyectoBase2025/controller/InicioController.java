package dev.brunob.ProyectoBase2025.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import dev.brunob.ProyectoBase2025.config.StageManager;
import dev.brunob.ProyectoBase2025.view.FxmlView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;

@Controller
public class InicioController implements Initializable {

    @FXML
    private ProgressIndicator progressIndicator;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Thread splashThread = new Thread(() -> {
            try {
                Thread.sleep(5000); //TODO
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            Platform.runLater(() -> {
                stageManager.switchScene(FxmlView.LOGIN);
            });
        });
        
        splashThread.setDaemon(true);
        splashThread.start();
    }
}


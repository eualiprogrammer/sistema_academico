package com.example.sea;

import com.example.sea.gui.ScreenManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        ScreenManager.getInstance().setPrimaryStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("TelaLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("SEA");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }
}

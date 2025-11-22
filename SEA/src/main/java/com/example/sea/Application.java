package com.example.sea;

import com.example.sea.gui.ScreenManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Configura o ScreenManager com o palco principal
        ScreenManager.getInstance().setPrimaryStage(stage);

        // Define o tamanho inicial antes de carregar a tela
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.setResizable(false); // Opcional: trava o redimensionamento

        // Carrega a tela de Login
        ScreenManager.getInstance().carregarTela("TelaLogin.fxml", "Login");
    }

    public static void main(String[] args) {
        launch();
    }
}

package com.example.sea.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ScreenManager {

    private static ScreenManager instance;
    private Stage primaryStage;

    private ScreenManager() {
    }

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void carregarTela(String nomeArquivoFxml, String titulo) {
        try {
            String caminho = "/com/example/sea/" + nomeArquivoFxml;

            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));

            if (loader.getLocation() == null) {
                System.err.println("ERRO FATAL: Não foi possível encontrar o arquivo FXML em: " + caminho);
            }

            Parent root = loader.load();

            Scene scene = new Scene(root);

            if (primaryStage != null) {
                primaryStage.setScene(scene);
                primaryStage.setTitle("SGA - " + titulo);
                primaryStage.setWidth(800);
                primaryStage.setHeight(600);
                primaryStage.centerOnScreen();
                primaryStage.show();
            } else {
                System.err.println("Erro: PrimaryStage não foi definido no App.java!");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar a tela: " + nomeArquivoFxml);
        }
    }

    public void carregarTelaEdicao(String nomeArquivoFxml, String titulo, Object objetoParaEditar) {
        try {
            String caminho = "/com/example/sea/" + nomeArquivoFxml;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));

            Parent root = loader.load();

            Object controller = loader.getController();

            if (controller instanceof TelaCadastroInscricaoController && objetoParaEditar instanceof com.example.sea.model.Inscricao) {
                ((TelaCadastroInscricaoController) controller).setInscricao((com.example.sea.model.Inscricao) objetoParaEditar);
            }
            else if (controller instanceof TelaCadastroSalaController && objetoParaEditar instanceof com.example.sea.model.Sala) {
                ((TelaCadastroSalaController) controller).setSala((com.example.sea.model.Sala) objetoParaEditar);
            }
            else if (controller instanceof TelaCadastroEventoController && objetoParaEditar instanceof com.example.sea.model.Evento) {
                ((TelaCadastroEventoController) controller).setEvento((com.example.sea.model.Evento) objetoParaEditar);
            }

            Scene scene = new Scene(root);

            if (primaryStage != null) {
                primaryStage.setScene(scene);
                primaryStage.setTitle("SGA - " + titulo);
                primaryStage.centerOnScreen();
                primaryStage.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar tela de edição: " + nomeArquivoFxml);
        }
    }
}

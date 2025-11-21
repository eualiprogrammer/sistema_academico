package com.example.sea.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Gerenciador de Telas (Singleton).
 * Responsável por carregar arquivos FXML e mudar a cena principal.
 */
public class ScreenManager {

    private static ScreenManager instance;
    private Stage primaryStage; // O palco principal da aplicação

    // Construtor privado (Padrão Singleton)
    private ScreenManager() {}

    // Método para pegar a única instância do gerenciador
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    // Define o palco principal (chamado lá no App.java)
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Carrega uma nova tela FXML.
     * @param nomeArquivoFxml O nome do arquivo (ex: "TelaCadastroPalestrante.fxml")
     * @param titulo O título da janela
     */
    public void carregarTela(String nomeArquivoFxml, String titulo) {
        try {
            // O caminho para a pasta 'view' dentro de resources 
            FXMLLoader loader = new FXMLLoader(getClass().getResource("src/main/resources/br/com/seuprojeto/sga/view/" + nomeArquivoFxml));
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            
            // Se tivermos um arquivo CSS global, podemos adicioná-lo aqui
            // scene.getStylesheets().add(getClass().getResource("/br/com/seuprojeto/sga/css/estilo.css").toExternalForm());

            if (primaryStage != null) {
                primaryStage.setScene(scene);
                primaryStage.setTitle("SGA - " + titulo);
                // Tamanho padrão (opcional)
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
}
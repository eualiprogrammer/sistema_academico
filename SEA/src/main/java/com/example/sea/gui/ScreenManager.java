package com.example.sea.gui;

import com.example.sea.model.Inscricao;
import com.example.sea.model.Sala;
import com.example.sea.model.Evento;
import com.example.sea.model.Palestra;
import com.example.sea.model.Palestrante;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ScreenManager {

    // --- Definição das Constantes (Isso resolve o erro de LARGURA/ALTURA) ---
    private static final int LARGURA = 1280;
    private static final int ALTURA = 720;

    private static ScreenManager instance;
    private Stage primaryStage;

    // Construtor privado (Singleton)
    private ScreenManager() {}

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Método simples para carregar tela (Menu, Listas, Login)
    public void carregarTela(String nomeArquivoFxml, String titulo) {
        try {
            String caminho = "/com/example/sea/" + nomeArquivoFxml;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));

            if (loader.getLocation() == null) {
                System.err.println("ERRO FATAL: Arquivo não encontrado: " + caminho);
                return;
            }

            // --- AQUI É CRIADA A VARIÁVEL 'root' (Isso resolve o erro 'symbol root') ---
            Parent root = loader.load();

            Scene scene = new Scene(root);

            // Adiciona CSS Global
            String cssPath = getClass().getResource("/com/example/sea/CSS/styles.css").toExternalForm();
            scene.getStylesheets().add(cssPath);

            if (primaryStage != null) {
                primaryStage.setScene(scene);
                primaryStage.setTitle("SGA - " + titulo);

                // Configura tamanho fixo usando as constantes
                primaryStage.setWidth(LARGURA);
                primaryStage.setHeight(ALTURA);
                primaryStage.setResizable(false);

                primaryStage.centerOnScreen();
                primaryStage.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para carregar tela de edição (passando dados)
    public void carregarTelaEdicao(String nomeArquivoFxml, String titulo, Object objetoParaEditar) {
        try {
            String caminho = "/com/example/sea/" + nomeArquivoFxml;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));

            if (loader.getLocation() == null) {
                System.err.println("ERRO FATAL: Arquivo não encontrado: " + caminho);
                return;
            }

            // --- AQUI TAMBÉM PRECISA DO 'root' ---
            Parent root = loader.load();
            Object controller = loader.getController();

            // --- INJEÇÃO DE DADOS ---

            // 1. Inscrição
            if (controller instanceof TelaCadastroInscricaoController && objetoParaEditar instanceof Inscricao) {
                ((TelaCadastroInscricaoController) controller).setInscricao((Inscricao) objetoParaEditar);
            }
            // 2. Sala
            else if (controller instanceof TelaCadastroSalaController && objetoParaEditar instanceof Sala) {
                ((TelaCadastroSalaController) controller).setSala((Sala) objetoParaEditar);
            }
            // 3. Evento
            else if (controller instanceof TelaCadastroEventoController && objetoParaEditar instanceof Evento) {
                ((TelaCadastroEventoController) controller).setEvento((Evento) objetoParaEditar);
            }
            // 4. Palestra
            else if (controller instanceof TelaCadastroPalestraController && objetoParaEditar instanceof Palestra) {
                ((TelaCadastroPalestraController) controller).setPalestra((Palestra) objetoParaEditar);
            }
            // 5. Palestrante
            else if (controller instanceof TelaCadastroPalestranteController && objetoParaEditar instanceof Palestrante) {
                ((TelaCadastroPalestranteController) controller).setPalestrante((Palestrante) objetoParaEditar);
            }
            // 6. Presenças (Chamada)
            else if (controller instanceof TelaPresencasParticipantesController && objetoParaEditar instanceof Palestra) {
                ((TelaPresencasParticipantesController) controller).setPalestra((Palestra) objetoParaEditar);
            }

            // ------------------------

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/example/sea/styles.css").toExternalForm());

            if (primaryStage != null) {
                primaryStage.setScene(scene);
                primaryStage.setTitle("SGA - " + titulo);

                primaryStage.setWidth(LARGURA);
                primaryStage.setHeight(ALTURA);
                primaryStage.setResizable(false);

                primaryStage.centerOnScreen();
                primaryStage.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
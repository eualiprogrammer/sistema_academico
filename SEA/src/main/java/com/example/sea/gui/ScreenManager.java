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
    private ScreenManager() {
    }

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
     *
     * @param nomeArquivoFxml O nome do arquivo
     * @param titulo          O título da janela
     */
    public void carregarTela(String nomeArquivoFxml, String titulo) {
        try {
            // CORREÇÃO AQUI:
            // 1. Usamos "/" para indicar que é um caminho absoluto a partir da raiz do classpath.
            // 2. Apontamos para o pacote correto do seu projeto: com.example.sea
            String caminho = "/com/example/sea/" + nomeArquivoFxml;

            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));

            // Verificação de segurança para ajudar no debug
            if (loader.getLocation() == null) {
                System.err.println("ERRO FATAL: Não foi possível encontrar o arquivo FXML em: " + caminho);
                // Isso evita o "Location is not set" genérico e te mostra o caminho que falhou
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

    /**
     * Carrega a tela de cadastro preenchendo os dados para edição.
     */
    public void carregarTelaEdicao(String nomeArquivoFxml, String titulo, Object objetoParaEditar) {
        try {
            String caminho = "/com/example/sea/" + nomeArquivoFxml;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));

            Parent root = loader.load();

            // --- A MÁGICA ACONTECE AQUI ---
            // Pegamos o controlador da tela que acabou de ser carregada
            Object controller = loader.getController();

            // Se for a tela de inscrição e o objeto for uma inscrição, passamos os dados
            if (controller instanceof TelaCadastroInscricaoController && objetoParaEditar instanceof com.example.sea.model.Inscricao) {
                ((TelaCadastroInscricaoController) controller).setInscricao((com.example.sea.model.Inscricao) objetoParaEditar);
            }
            else if (controller instanceof TelaCadastroSalaController && objetoParaEditar instanceof com.example.sea.model.Sala) {
                ((TelaCadastroSalaController) controller).setSala((com.example.sea.model.Sala) objetoParaEditar);
            }

            // 3. Para EVENTO (Novo)
            else if (controller instanceof TelaCadastroEventoController && objetoParaEditar instanceof com.example.sea.model.Evento) {
                ((TelaCadastroEventoController) controller).setEvento((com.example.sea.model.Evento) objetoParaEditar);
            }
            // ------------------------------

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
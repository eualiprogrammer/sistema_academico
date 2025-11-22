package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Sala;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class TelaCadastroSalaController {

    @FXML private TextField txtNome;
    @FXML private TextField txtCapacidade;
    @FXML private TextField txtProjetores;    // Campo que faltava ler
    @FXML private TextField txtCaixasDeSom;   // Campo que faltava ler

    private Sala salaEmEdicao;

    /**
     * Método chamado pelo ScreenManager para preencher os dados na edição.
     */
    public void setSala(Sala sala) {
        this.salaEmEdicao = sala;
        if (sala != null) {
            txtNome.setText(sala.getNome());
            txtCapacidade.setText(String.valueOf(sala.getCapacidade()));
            txtProjetores.setText(String.valueOf(sala.getNumeroProjetores())); // Preenche projetores
            txtCaixasDeSom.setText(String.valueOf(sala.getNumeroCaixasSom())); // Preenche som
        }
    }

    @FXML
    private void salvar() {
        try {
            // 1. Ler os textos dos campos
            String nome = txtNome.getText();
            String capText = txtCapacidade.getText();
            String projText = txtProjetores.getText();
            String somText = txtCaixasDeSom.getText();

            // Validação simples
            if (nome == null || nome.trim().isEmpty() || capText.isEmpty()) {
                mostrarAlerta(AlertType.WARNING, "Atenção", "Preencha pelo menos Nome e Capacidade.");
                return;
            }

            // 2. Converter para números (com valores padrão 0 se estiver vazio)
            int capacidade = Integer.parseInt(capText);
            int numProjetores = projText.isEmpty() ? 0 : Integer.parseInt(projText);
            int numCaixasSom = somText.isEmpty() ? 0 : Integer.parseInt(somText);

            if (salaEmEdicao != null) {
                // --- MODO EDIÇÃO ---
                salaEmEdicao.setNome(nome);
                salaEmEdicao.setCapacidade(capacidade);
                salaEmEdicao.setNumeroProjetores(numProjetores); // Atualiza projetores
                salaEmEdicao.setNumeroCaixasSom(numCaixasSom);   // Atualiza som

                SistemaSGA.getInstance().getControladorSala().atualizar(salaEmEdicao);
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Sala atualizada com sucesso!");
            } else {
                // --- MODO CADASTRO ---
                // CORREÇÃO: Agora passamos os 4 argumentos exigidos pelo construtor
                Sala novaSala = new Sala(nome, capacidade, numProjetores, numCaixasSom);

                SistemaSGA.getInstance().getControladorSala().cadastrar(novaSala);
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Sala cadastrada com sucesso!");
            }

            cancelar(); // Volta para a lista

        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.ERROR, "Erro", "Capacidade, Projetores e Som devem ser números inteiros.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Erro ao Salvar", e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        ScreenManager.getInstance().carregarTela("TelaListarSalas.fxml", "Salas");
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
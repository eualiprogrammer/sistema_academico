package com.example.sea.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class TelaPrincipalController {

    // --- MÉTODOS DE NAVEGAÇÃO (Botões do Menu) ---

    @FXML
    private void irParaPalestrantes() {
        // Carrega a tela de listagem de palestrantes
        // Certifica-te que o nome do ficheiro FXML está correto na tua pasta view
        ScreenManager.getInstance().carregarTela("TelaListarPalestrantes.fxml", "Gestão de Palestrantes");
    }

    @FXML
    private void irParaSalas() {
        ScreenManager.getInstance().carregarTela("TelaListarSalas.fxml", "Gestão de Salas");
    }

    @FXML
    private void irParaEventos() {
        ScreenManager.getInstance().carregarTela("TelaListarEventos.fxml", "Gestão de Eventos");
    }

    @FXML
    private void irParaPalestras() {
        System.out.println("Navegando para Palestras...");
        ScreenManager.getInstance().carregarTela("TelaListarPalestras.fxml", "Gerenciar Palestras");
    }

    @FXML
    private void irParaInscricoes() {
        ScreenManager.getInstance().carregarTela("TelaListarInscricoes.fxml", "Gestão de Inscrições");
    }

    @FXML
    private void irParaRelatorios() {
        // Futura tela de relatórios
        mostrarAviso("Funcionalidade de Relatórios em breve!");
    }

    // --- LOGOUT ---

    @FXML
    private void sair() {
        // Volta para a tela de Login
        ScreenManager.getInstance().carregarTela("TelaLogin.fxml", "Login");
    }

    // --- Utilitário ---

    private void mostrarAviso(String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

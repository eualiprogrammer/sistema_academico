package com.example.sea.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class TelaPrincipalController {

    @FXML
    private void irParaPalestrantes() {
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
        mostrarAviso("Funcionalidade de Relatórios em breve!");
    }

    @FXML
    private void sair() {
        ScreenManager.getInstance().carregarTela("TelaLogin.fxml", "Login");
    }

    private void mostrarAviso(String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

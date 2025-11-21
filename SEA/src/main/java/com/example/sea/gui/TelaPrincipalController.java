package com.example.sea.gui;

import javafx.fxml.FXML;

public class TelaPrincipalController {

    @FXML
    private void irParaPalestrantes() {
        System.out.println("Navegando para Palestrantes...");
        ScreenManager.getInstance().carregarTela("TelaCadastroPalestrante.fxml", "Gerenciar Palestrantes");
    }

    @FXML
    private void irParaSalas() {
        System.out.println("Navegando para Salas...");
        // ScreenManager.getInstance().carregarTela("TelaCadastroSala.fxml", "Gerenciar Salas");
    }

    @FXML
    private void irParaEventos() {
        System.out.println("Navegando para Eventos...");
        // ScreenManager.getInstance().carregarTela("TelaCadastroEvento.fxml", "Gerenciar Eventos");
    }

    @FXML
    private void sair() {
        System.out.println("Saindo do sistema...");
        System.exit(0);
    }
}
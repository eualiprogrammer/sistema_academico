package com.example.sea.gui;

import com.example.sea.business.SessaoUsuario;
import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Evento;
import com.example.sea.model.Participante;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.util.List;

public class ViewEventosController {

    @FXML private VBox containerEventos;

    @FXML
    public void initialize() {
        carregarEventos();
    }

    private void carregarEventos() {
        List<Evento> eventos = SistemaSGA.getInstance().getControladorEvento().listar();

        containerEventos.getChildren().clear();

        for (Evento evento : eventos) {
            VBox card = new VBox(5);
            card.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: white;");

            Label lblTitulo = new Label(evento.getNome());
            lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label lblDescricao = new Label(evento.getDescricao());

            Button btnVerPalestras = new Button("Ver Palestras");
            btnVerPalestras.getStyleClass().add("btn-acao");
            btnVerPalestras.setOnAction(e -> abrirPalestrasDoEvento(evento));

            card.getChildren().addAll(lblTitulo, lblDescricao, btnVerPalestras);
            containerEventos.getChildren().add(card);
        }
    }

    private void abrirPalestrasDoEvento(Evento evento) {
        System.out.println("Abrindo palestras de: " + evento.getNome());
    }

    @FXML
    private void sair() {
        SessaoUsuario.getInstance().logout();
        ScreenManager.getInstance().carregarTela("TelaLogin.fxml", "Login");
    }
}

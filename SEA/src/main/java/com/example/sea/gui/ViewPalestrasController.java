package com.example.sea.gui;

import com.example.sea.business.SessaoUsuario;
import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Palestra;
import com.example.sea.model.Participante;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewPalestrasController {

    @FXML
    private VBox containerPalestras;

    @FXML
    public void initialize() {
        carregarPalestras();
    }

    private void carregarPalestras() {
        containerPalestras.getChildren().clear();

        List<Palestra> palestras = SistemaSGA.getInstance().getControladorPalestra().listarTodos();
        Participante participanteLogado = SessaoUsuario.getInstance().getParticipanteLogado();

        if (palestras.isEmpty()) {
            containerPalestras.getChildren().add(new Label("Nenhuma palestra cadastrada no momento."));
            return;
        }

        for (Palestra palestra : palestras) {
            VBox card = criarCardPalestra(palestra, participanteLogado);
            containerPalestras.getChildren().add(card);
        }
    }

    private VBox criarCardPalestra(Palestra palestra, Participante participante) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1);");

        Label lblTitulo = new Label(palestra.getTitulo());
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataStr = palestra.getDataHoraInicio() != null ? palestra.getDataHoraInicio().format(fmt) : "Data a definir";

        Label lblInfo = new Label("Palestrante: " + palestra.getPalestrante().getNome() +
                " | Sala: " + palestra.getSala().getNome() +
                " | Data: " + dataStr);
        lblInfo.setWrapText(true);

        Button btnAcao = new Button("Inscrever-se");
        btnAcao.getStyleClass().add("btn-acao");
        btnAcao.setOnAction(e -> realizarInscricao(palestra, btnAcao));

        HBox linhaTopo = new HBox(10, lblTitulo);
        HBox.setHgrow(linhaTopo, Priority.ALWAYS);

        HBox linhaBotoes = new HBox(btnAcao);
        linhaBotoes.setAlignment(Pos.CENTER_RIGHT);

        card.getChildren().addAll(linhaTopo, lblInfo, linhaBotoes);

        return card;
    }

    private void realizarInscricao(Palestra palestra, Button btn) {
        Participante participante = SessaoUsuario.getInstance().getParticipanteLogado();

        if (participante == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Sessão", "Você precisa estar logado.");
            ScreenManager.getInstance().carregarTela("TelaLogin.fxml", "Login");
            return;
        }

        try {
            SistemaSGA.getInstance().getControladorInscricao().inscrever(participante, palestra);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Inscrição realizada com sucesso!");
            btn.setText("Inscrito ✓");
            btn.setDisable(true);
            btn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Não foi possível inscrever", e.getMessage());
        }
    }

    @FXML
    private void voltar() {
        ScreenManager.getInstance().carregarTela("view_eventos.fxml", "Eventos Disponíveis");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

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
        // 1. Limpa a lista visual
        containerPalestras.getChildren().clear();

        // 2. Busca dados do sistema
        List<Palestra> palestras = SistemaSGA.getInstance().getControladorPalestra().listarTodos();
        Participante participanteLogado = SessaoUsuario.getInstance().getParticipanteLogado();

        if (palestras.isEmpty()) {
            containerPalestras.getChildren().add(new Label("Nenhuma palestra cadastrada no momento."));
            return;
        }

        // 3. Cria um card para cada palestra
        for (Palestra palestra : palestras) {
            VBox card = criarCardPalestra(palestra, participanteLogado);
            containerPalestras.getChildren().add(card);
        }
    }

    private VBox criarCardPalestra(Palestra palestra, Participante participante) {
        // Estilo do Card
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1);");

        // Título
        Label lblTitulo = new Label(palestra.getTitulo());
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Informações
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataStr = palestra.getDataHoraInicio() != null ? palestra.getDataHoraInicio().format(fmt) : "Data a definir";

        Label lblInfo = new Label("Palestrante: " + palestra.getPalestrante().getNome() +
                " | Sala: " + palestra.getSala().getNome() +
                " | Data: " + dataStr);
        lblInfo.setWrapText(true);

        // Botão de Ação
        Button btnAcao = new Button("Inscrever-se");
        btnAcao.getStyleClass().add("btn-acao"); // Usa seu estilo CSS

        // Verifica se já está inscrito (lógica visual)
        // Nota: Para ser perfeito, o controlador de inscrição deveria ter um método "isInscrito(participante, palestra)"
        // Aqui vamos tentar inscrever e tratar o erro se já existir, ou verificar na lista do participante se possível.

        btnAcao.setOnAction(e -> realizarInscricao(palestra, btnAcao));

        // Layout Horizontal para separar Texto do Botão
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
            // Chama o controlador de negócio
            SistemaSGA.getInstance().getControladorInscricao().inscrever(participante, palestra);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Inscrição realizada com sucesso!");
            btn.setText("Inscrito ✓");
            btn.setDisable(true); // Evita duplo clique
            btn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;"); // Verde

        } catch (Exception e) {
            // Trata erros como "Já inscrito", "Lotação esgotada", "Conflito de horário"
            mostrarAlerta(Alert.AlertType.WARNING, "Não foi possível inscrever", e.getMessage());
        }
    }

    @FXML
    private void voltar() {
        // Volta para o menu principal do aluno (ViewEventos) ou Login
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
package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Inscricao;
import com.example.sea.model.Palestra;
import com.example.sea.model.Participante;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;

import java.time.LocalDateTime;

public class TelaCadastroInscricaoController {

    @FXML
    private ChoiceBox<Participante> choiceParticipante;

    @FXML
    private ChoiceBox<Palestra> choicePalestra;

    // Variável para controlar se estamos editando
    private Inscricao inscricaoEmEdicao;

    @FXML
    public void initialize() {
        carregarDadosChoiceBox();
    }

    private void carregarDadosChoiceBox() {
        // Carrega as listas de participantes e palestras nos selects
        choiceParticipante.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorParticipante().listarTodos()
        ));

        choicePalestra.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorPalestra().listarTodos()
        ));
    }

    // Método chamado pelo ScreenManager para passar os dados na edição
    public void setInscricao(Inscricao inscricao) {
        this.inscricaoEmEdicao = inscricao;

        if (inscricao != null) {
            // Seleciona os itens correspondentes nos ChoiceBoxes
            choiceParticipante.setValue(inscricao.getParticipante());
            choicePalestra.setValue(inscricao.getPalestra());
        }
    }

    @FXML
    private void salvar() {
        try {
            Participante participante = choiceParticipante.getValue();
            Palestra palestra = choicePalestra.getValue();

            if (participante == null || palestra == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos Obrigatórios", "Selecione um participante e uma palestra.");
                return;
            }

            if (inscricaoEmEdicao != null) {
                // --- MODO EDIÇÃO ---
                inscricaoEmEdicao.setParticipante(participante);
                inscricaoEmEdicao.setPalestra(palestra);
                // Chama o método atualizar (que vamos criar no Passo 2 e 3)
                SistemaSGA.getInstance().getControladorInscricao().atualizar(inscricaoEmEdicao);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Inscrição atualizada com sucesso!");
            } else {
                // --- MODO CRIAÇÃO ---
                // O seu construtor já define a data/hora internamente
                Inscricao novaInscricao = new Inscricao(participante, palestra);
                SistemaSGA.getInstance().getControladorInscricao().cadastrar(novaInscricao);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Inscrição realizada com sucesso!");
            }

            voltar(); // Volta para a lista

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao salvar: " + e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        voltar();
    }

    private void voltar() {
        ScreenManager.getInstance().carregarTela("TelaListarInscricoes.fxml", "Inscrições");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
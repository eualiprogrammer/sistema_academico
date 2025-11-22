package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Inscricao;
import com.example.sea.model.Palestra;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;

public class TelaPresencasParticipantesController {

    @FXML private Label lblTituloPalestra;
    @FXML private TableView<Inscricao> tabelaInscritos;
    @FXML private TableColumn<Inscricao, String> colParticipante;
    @FXML private TableColumn<Inscricao, String> colStatus;

    private Palestra palestraAtual;

    // Método especial para receber a palestra da tela anterior
    // O ScreenManager vai chamar este método.
    // IMPORTANTE: Precisamos adicionar um "case" no ScreenManager para esta classe (veja Passo 3)
    public void setPalestra(Palestra palestra) {
        this.palestraAtual = palestra;
        if (palestra != null) {
            if (lblTituloPalestra != null) lblTituloPalestra.setText(palestra.getTitulo());
            carregarInscritos();
        }
    }

    // Método alternativo caso o ScreenManager use nomes diferentes (gambiarra segura)
    public void setEvento(Object obj) {
        if (obj instanceof Palestra) {
            setPalestra((Palestra) obj);
        }
    }

    @FXML
    public void initialize() {
        configurarColunas();
    }

    private void configurarColunas() {
        colParticipante.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getParticipante().getNome()));

        colStatus.setCellValueFactory(cellData -> {
            boolean presente = cellData.getValue().isPresenca();
            return new SimpleStringProperty(presente ? "PRESENTE ✅" : "Ausente ❌");
        });
    }

    private void carregarInscritos() {
        if (palestraAtual != null) {
            tabelaInscritos.setItems(FXCollections.observableArrayList(
                    SistemaSGA.getInstance().getControladorInscricao().listarPorPalestra(palestraAtual)
            ));
        }
    }

    @FXML
    private void confirmarPresenca() {
        Inscricao inscricaoSelecionada = tabelaInscritos.getSelectionModel().getSelectedItem();

        if (inscricaoSelecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um aluno na lista.");
            return;
        }

        try {
            // Marca presença no sistema
            SistemaSGA.getInstance().getControladorInscricao().marcarPresenca(inscricaoSelecionada);

            // Atualiza a tabela para mostrar o "✅"
            tabelaInscritos.refresh();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Presença confirmada para: " + inscricaoSelecionada.getParticipante().getNome());

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void voltar() {
        ScreenManager.getInstance().carregarTela("admin_presencas_list.fxml", "Gerenciar Presenças");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
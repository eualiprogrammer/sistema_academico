package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Inscricao;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TelaListarInscricoesController {

    // --- Tabela e Colunas ---
    @FXML private TableView<Inscricao> tabelaInscricoes;

    @FXML private TableColumn<Inscricao, String> colParticipante;
    @FXML private TableColumn<Inscricao, String> colPalestra;
    @FXML private TableColumn<Inscricao, String> colConfirmacao;
    @FXML private TableColumn<Inscricao, String> colDataHoraInscricao;

    @FXML
    public void initialize() {
        configurarColunas();
        atualizarTabela();
    }

    private void configurarColunas() {
        // Coluna PARTICIPANTE: Pega o nome de dentro do objeto Participante
        colParticipante.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getParticipante().getNome())
        );

        // Coluna PALESTRA: Pega o título de dentro do objeto Palestra
        colPalestra.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPalestra().getTitulo())
        );

        // Coluna CONFIRMAÇÃO: Liga direto ao atributo statusConfirmacao
        colConfirmacao.setCellValueFactory(new PropertyValueFactory<>("statusConfirmacao"));

        // Coluna DATA/HORA: Formata o LocalDateTime para ficar bonito (dd/MM/yyyy HH:mm)
        colDataHoraInscricao.setCellValueFactory(cellData -> {
            LocalDateTime data = cellData.getValue().getDataHoraInscricao();
            if (data != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                return new SimpleStringProperty(data.format(formatter));
            }
            return new SimpleStringProperty("");
        });
    }

    private void atualizarTabela() {
        // Busca a lista completa do sistema
        tabelaInscricoes.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorInscricao().listarTodos()
        ));
    }

    @FXML
    private void novaInscricao() {
        // Abre a tela de cadastro que já criamos
        ScreenManager.getInstance().carregarTela("TelaCadastroInscricao.fxml", "Nova Inscrição");
    }

    @FXML
    private void excluir() {
        Inscricao inscricaoSelecionada = tabelaInscricoes.getSelectionModel().getSelectedItem();

        if (inscricaoSelecionada == null) {
            mostrarAlerta(AlertType.WARNING, "Atenção", "Selecione uma inscrição para cancelar.");
            return;
        }

        try {
            // Chama o backend para cancelar
            SistemaSGA.getInstance().getControladorInscricao().cancelarInscricao(inscricaoSelecionada);

            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Inscrição cancelada com sucesso!");
            atualizarTabela(); // Atualiza a lista visualmente

        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Erro", "Não foi possível cancelar: " + e.getMessage());
        }
    }

    @FXML
    private void editar() {
        // 1. Pega a inscrição selecionada na tabela
        Inscricao inscricaoSelecionada = tabelaInscricoes.getSelectionModel().getSelectedItem();

        // 2. Verifica se tem algo selecionado
        if (inscricaoSelecionada == null) {
            mostrarAlerta(AlertType.WARNING, "Atenção", "Selecione uma inscrição para editar.");
            return;
        }

        // 3. Chama o ScreenManager passando a inscrição para a tela de cadastro
        // Nota: Vamos criar esse método "carregarTelaEdicao" no próximo passo
        ScreenManager.getInstance().carregarTelaEdicao(
                "TelaCadastroInscricao.fxml",
                "Editar Inscrição",
                inscricaoSelecionada
        );
    }

    @FXML
    private void voltar() {
        ScreenManager.getInstance().carregarTela("TelaPrincipal.fxml", "Menu Principal");
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }


}

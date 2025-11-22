package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Palestra;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TelaListarPresencasController {

    @FXML private TableView<Palestra> tabelaPalestras;
    @FXML private TableColumn<Palestra, String> colNome;
    @FXML private TableColumn<Palestra, String> colPalestrante;
    @FXML private TableColumn<Palestra, String> colSala;
    @FXML private TableColumn<Palestra, String> colData;

    @FXML
    public void initialize() {
        configurarColunas();
        carregarPalestras();
    }

    private void configurarColunas() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        colPalestrante.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPalestrante().getNome()));

        colSala.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSala().getNome()));

        // Formatação simples da data
        colData.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDataHoraInicio().toString()));
    }

    private void carregarPalestras() {
        tabelaPalestras.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorPalestra().listarTodos()
        ));
    }

    @FXML
    private void gerenciarParticipantes() {
        Palestra palestraSelecionada = tabelaPalestras.getSelectionModel().getSelectedItem();

        if (palestraSelecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma palestra para fazer a chamada.");
            return;
        }

        // Reutiliza o método de "edição" para passar a palestra para a próxima tela
        ScreenManager.getInstance().carregarTelaEdicao(
                "admin_presencas_participantes.fxml",
                "Lista de Presença - " + palestraSelecionada.getTitulo(),
                palestraSelecionada
        );
    }

    @FXML
    private void voltar() {
        ScreenManager.getInstance().carregarTela("TelaPrincipal.fxml", "Menu Principal");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
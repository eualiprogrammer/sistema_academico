package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Evento;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class TelaListarEventosController {

    // --- Tabela e Colunas ---
    @FXML private TableView<Evento> tabelaEventos;
    @FXML private TableColumn<Evento, String> colNome;
    @FXML private TableColumn<Evento, String> colDescricao;
    @FXML private TableColumn<Evento, LocalDate> colDataInicio;
    @FXML private TableColumn<Evento, LocalDate> colDataFim;

    // Esta coluna é especial, pois 'atividades' é uma lista, não uma String
    @FXML private TableColumn<Evento, String> colAtividades;

    @FXML
    public void initialize() {
        configurarColunas();
        atualizarTabela();
    }

    private void configurarColunas() {
        // Liga as colunas simples aos atributos da classe Evento
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colDataInicio.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
        colDataFim.setCellValueFactory(new PropertyValueFactory<>("dataFim"));

        // Configuração Especial para a coluna ATIVIDADES
        // Em vez de mostrar a lista bruta, mostra o tamanho dela (ex: "5 atividades")
        colAtividades.setCellValueFactory(cellData -> {
            int qtd = cellData.getValue().getAtividades().size();
            return new SimpleStringProperty(qtd + " atividades");
        });
    }

    private void atualizarTabela() {
        // Busca a lista atualizada do Sistema e coloca na tabela
        tabelaEventos.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorEvento().listar()
        ));
    }

    @FXML
    private void novoEvento() {
        // Navega para a tela de cadastro que criamos antes
        ScreenManager.getInstance().carregarTela("TelaCadastroEvento.fxml", "Novo Evento");
    }

    @FXML
    private void excluir() {
        Evento eventoSelecionado = tabelaEventos.getSelectionModel().getSelectedItem();

        if (eventoSelecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Atenção", "Selecione um evento na tabela para excluir.");
            return;
        }

        try {
            // Chama o backend para remover
            SistemaSGA.getInstance().getControladorEvento().remover(eventoSelecionado.getNome());

            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Evento removido com sucesso!");
            atualizarTabela(); // Atualiza a lista visualmente

        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Erro", "Não foi possível excluir: " + e.getMessage());
        }
    }

    // (Opcional) Se você tiver um botão "Ver Detalhes" ou "Adicionar Palestras"
    @FXML
    private void verDetalhes() {
        Evento eventoSelecionado = tabelaEventos.getSelectionModel().getSelectedItem();
        if (eventoSelecionado != null) {
            // Lógica futura para abrir a tela admin_evento_detail.fxml
            // e passar o eventoSelecionado para ela
            System.out.println("Ver detalhes de: " + eventoSelecionado.getNome());
        }
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

    @FXML
    private void editar() {
        Evento eventoSelecionado = tabelaEventos.getSelectionModel().getSelectedItem();

        if (eventoSelecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atenção");
            alert.setHeaderText(null);
            alert.setContentText("Selecione um evento para editar.");
            alert.showAndWait();
            return;
        }

        ScreenManager.getInstance().carregarTelaEdicao(
                "TelaCadastroEvento.fxml",
                "Editar Evento",
                eventoSelecionado
        );
    }
}

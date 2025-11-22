package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Palestrante;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TelaListarPalestrantesController {

    // --- Tabela e Colunas ---
    @FXML private TableView<Palestrante> tabelaPalestrantes;

    @FXML private TableColumn<Palestrante, String> colNome;
    @FXML private TableColumn<Palestrante, String> colEmail;
    @FXML private TableColumn<Palestrante, String> colEspecializacao;
    @FXML private TableColumn<Palestrante, String> colTelefone;

    @FXML
    public void initialize() {
        configurarColunas();
        atualizarTabela();
    }

    private void configurarColunas() {
        // Liga as colunas aos atributos exatos da classe Palestrante
        // (Cuidado: O nome entre aspas deve ser igual ao nome do atributo na classe Model)
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        // Nota: No teu model o atributo é 'areaEspecializacao', então usamos esse nome aqui
        colEspecializacao.setCellValueFactory(new PropertyValueFactory<>("areaEspecializacao"));
    }

    private void atualizarTabela() {
        // Busca a lista atualizada do Sistema e coloca na tabela
        tabelaPalestrantes.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorPalestrante().listar()
        ));
    }

    @FXML
    private void novoPalestrante() {
        // Navega para a tela de cadastro que já criámos
        ScreenManager.getInstance().carregarTela("TelaCadastroPalestrante.fxml", "Novo Palestrante");
    }

    @FXML
    private void editar() {
        Palestrante selecionado = tabelaPalestrantes.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Atenção", "Selecione um palestrante na tabela para editar.");
            return;
        }

        // No futuro, podes criar um método no 'TelaCadastroPalestranteController'
        // chamado 'setPalestrante(Palestrante p)' para preencher os campos e editar.
        System.out.println("Editar palestrante: " + selecionado.getNome());
        mostrarAlerta(AlertType.INFORMATION, "Em Breve", "A funcionalidade de edição será implementada.");
    }

    @FXML
    private void excluir() {
        Palestrante selecionado = tabelaPalestrantes.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta(AlertType.WARNING, "Atenção", "Selecione um palestrante para excluir.");
            return;
        }

        try {
            // O identificador único do palestrante é o Email (conforme definimos no Repositorio)
            SistemaSGA.getInstance().getControladorPalestrante().remover(selecionado.getEmail());

            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Palestrante removido com sucesso!");
            atualizarTabela(); // Atualiza a lista visualmente

        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Erro", "Não foi possível excluir: " + e.getMessage());
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
}


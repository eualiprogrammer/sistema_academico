package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Evento;
import com.example.sea.model.Workshop;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TelaCadastroWorkshopController {

    @FXML private TextField txtNome;
    @FXML private ChoiceBox<Evento> cbEvento;
    @FXML private TextArea txtDescricao;

    private Workshop workshopEmEdicao;

    @FXML
    public void initialize() {
        // Carrega apenas a lista de Eventos
        cbEvento.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorEvento().listar()
        ));
    }

    public void setWorkshop(Workshop workshop) {
        this.workshopEmEdicao = workshop;
        if (workshop != null) {
            txtNome.setText(workshop.getTitulo());
            txtDescricao.setText(workshop.getDescricao());
            cbEvento.setValue(workshop.getEvento());

            txtNome.setDisable(true); // Se o título for ID, bloqueia na edição
        }
    }

    @FXML
    private void salvar() {
        try {
            // 1. Pegar dados simples
            String titulo = txtNome.getText();
            String descricao = txtDescricao.getText();
            Evento evento = cbEvento.getValue();

            // 2. Validação
            if (titulo == null || titulo.trim().isEmpty()) {
                mostrarAlerta(AlertType.WARNING, "Atenção", "O título é obrigatório.");
                return;
            }
            if (evento == null) {
                mostrarAlerta(AlertType.WARNING, "Atenção", "Selecione um evento.");
                return;
            }

            if (workshopEmEdicao != null) {
                // --- MODO EDIÇÃO ---
                workshopEmEdicao.setDescricao(descricao);
                workshopEmEdicao.setEvento(evento);
                // Titulo geralmente não muda se for chave

                SistemaSGA.getInstance().getControladorWorkshop().atualizar(workshopEmEdicao);
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Workshop atualizado!");

            } else {
                // --- MODO CADASTRO ---
                // Chama o cadastrar simples (que aceita 3 argumentos)
                // Nota: Verifique se o seu ControladorWorkshop.cadastrar aceita (String, String, Evento)
                SistemaSGA.getInstance().getControladorWorkshop().cadastrar(titulo, descricao, evento);

                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Workshop criado!");
            }

            cancelar();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        ScreenManager.getInstance().carregarTela("TelaListarWorkshops.fxml", "Workshops");
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
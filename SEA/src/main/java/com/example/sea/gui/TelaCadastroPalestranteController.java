package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Palestrante;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class TelaCadastroPalestranteController {

    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEspecializacao;

    @FXML
    private void salvar() {
        try {
            String nome = txtNome.getText();
            String email = txtEmail.getText();
            String telefone = txtTelefone.getText();
            String especializacao = txtEspecializacao.getText();

            if (nome == null || nome.trim().isEmpty() ||
                    email == null || email.trim().isEmpty()) {
                mostrarAlerta(AlertType.WARNING, "Atenção", "Nome e Email são obrigatórios.");
                return;
            }

            Palestrante novoPalestrante = new Palestrante(nome, especializacao, telefone, email);

            SistemaSGA.getInstance().getControladorPalestrante().cadastrar(novoPalestrante);

            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Palestrante cadastrado com sucesso!");
            limparCampos();

        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Erro ao Cadastrar", e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        ScreenManager.getInstance().carregarTela("TelaPrincipal.fxml", "Menu Principal");
    }

    private void limparCampos() {
        txtNome.clear();
        txtEmail.clear();
        txtTelefone.clear();
        txtEspecializacao.clear();
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Palestrante;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class TelaCadastroPalestranteController {

    // --- Seus IDs definidos ---
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEspecializacao;

    @FXML
    private void salvar() { // Associe este método ao botão "Salvar" (#salvar)
        try {
            // 1. Pegar os dados da tela
            String nome = txtNome.getText();
            String email = txtEmail.getText();
            String telefone = txtTelefone.getText();
            String especializacao = txtEspecializacao.getText();

            // Validação básica de UI (campos vazios)
            if (nome == null || nome.trim().isEmpty() ||
                    email == null || email.trim().isEmpty()) {
                mostrarAlerta(AlertType.WARNING, "Atenção", "Nome e Email são obrigatórios.");
                return;
            }

            // 2. Criar o objeto (Model)
            // O construtor é: Palestrante(nome, especializacao, telefone, email)
            Palestrante novoPalestrante = new Palestrante(nome, especializacao, telefone, email);

            // 3. Chamar o Sistema (Business)
            // O controlador de palestrante já verifica se o email é duplicado
            SistemaSGA.getInstance().getControladorPalestrante().cadastrar(novoPalestrante);

            // 4. Sucesso!
            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Palestrante cadastrado com sucesso!");
            limparCampos();

        } catch (Exception e) {
            // 5. Erro (Ex: Email duplicado, erro de conexão)
            mostrarAlerta(AlertType.ERROR, "Erro ao Cadastrar", e.getMessage());
        }
    }

    @FXML
    private void cancelar() { // Associe ao botão "Cancelar" (#cancelar)
        // Volta para o Menu Principal
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

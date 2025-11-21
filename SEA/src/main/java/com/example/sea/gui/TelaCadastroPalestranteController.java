package com.example.sea.gui;
import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Palestrante;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class TelaCadastroPalestranteController {

    @FXML private TextField txtNome;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtEspecializacao;

    @FXML
    private void salvar() {
        try {
            // 1. Pegar os dados da tela
            String nome = txtNome.getText();
            String telefone = txtTelefone.getText();
            String email = txtEmail.getText();
            String especializacao = txtEspecializacao.getText();

            // 2. Criar o objeto (Model)
            Palestrante novoPalestrante = new Palestrante(nome, especializacao, telefone, email);

            // 3. Chamar o Sistema (Business)
            SistemaSGA.getInstance().getControladorPalestrante().cadastrar(novoPalestrante);

            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Palestrante cadastrado com sucesso!");
            limparCampos();

        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Erro ao Cadastrar", e.getMessage());
        }
    }

    @FXML
    private void voltar() {
        // Volta para o Menu Principal
        ScreenManager.getInstance().carregarTela("TelaPrincipal.fxml", "Menu Principal");
    }

    private void limparCampos() {
        txtNome.clear();
        txtTelefone.clear();
        txtEmail.clear();
        txtEspecializacao.clear();
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void irParaPalestrantes() {
        // Carrega a tela nova!
        ScreenManager.getInstance().carregarTela("TelaCadastroPalestrante.fxml", "Cadastro de Palestrante");
    }

} 
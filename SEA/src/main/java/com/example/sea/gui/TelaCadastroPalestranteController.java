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

    // Variável para controlar se é edição ou novo
    private Palestrante palestranteEmEdicao;

    // --- MÉTODO QUE FALTAVA (Para o ScreenManager usar) ---
    public void setPalestrante(Palestrante palestrante) {
        this.palestranteEmEdicao = palestrante;
        if (palestrante != null) {
            txtNome.setText(palestrante.getNome());
            txtTelefone.setText(palestrante.getTelefone());
            txtEmail.setText(palestrante.getEmail());
            txtEspecializacao.setText(palestrante.getAreaEspecializacao());

            // Desabilita e-mail na edição se for a chave única
            txtEmail.setDisable(true);
        }
    }

    @FXML
    private void salvar() {
        try {
            String nome = txtNome.getText();
            String telefone = txtTelefone.getText();
            String email = txtEmail.getText();
            String especializacao = txtEspecializacao.getText();

            // Validação básica
            if (nome.isEmpty() || email.isEmpty()) {
                mostrarAlerta(AlertType.WARNING, "Atenção", "Preencha nome e e-mail.");
                return;
            }

            if (palestranteEmEdicao != null) {
                // --- MODO EDIÇÃO ---
                palestranteEmEdicao.setNome(nome);
                palestranteEmEdicao.setTelefone(telefone);
                palestranteEmEdicao.setAreaEspecializacao(especializacao);
                // Email geralmente não muda pois é ID

                SistemaSGA.getInstance().getControladorPalestrante().atualizar(palestranteEmEdicao);
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Palestrante atualizado com sucesso!");

            } else {
                // --- MODO NOVO CADASTRO ---
                Palestrante novoPalestrante = new Palestrante(nome, especializacao, telefone, email);
                SistemaSGA.getInstance().getControladorPalestrante().cadastrar(novoPalestrante);
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Palestrante cadastrado com sucesso!");
            }

            cancelar(); // Volta para a lista

        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Erro ao Salvar", e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        ScreenManager.getInstance().carregarTela("TelaListarPalestrantes.fxml", "Palestrantes");
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
}
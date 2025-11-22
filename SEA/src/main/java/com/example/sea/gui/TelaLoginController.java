package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Participante;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class TelaLoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;

    @FXML
    private void fazerLogin() {
        String email = txtEmail.getText();
        String senha = txtSenha.getText();

        // 1. Validação Básica
        if (email == null || email.isEmpty() || senha == null || senha.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos Vazios", "Por favor, preencha email e senha.");
            return;
        }

        // 2. LÓGICA DE LOGIN DE ADMINISTRADOR (Acesso ao Dashboard)
        // Como não temos banco de usuários com senha, usamos um login fixo para o Admin.
        if (email.equals("admin") && senha.equals("123")) {
            System.out.println("Login de Admin realizado!");

            // Carrega o Dashboard do Administrador
            // (Certifica-te que o nome do ficheiro é exatamente este na pasta view)
            ScreenManager.getInstance().carregarTela("TelaPrincipal.fxml", "Menu Principal");
            return;
        }

        // 3. LÓGICA DE LOGIN DE PARTICIPANTE (Opcional)
        // Aqui podíamos verificar se o email existe no repositório de participantes
        try {
            // Tentamos buscar um participante pelo CPF (se o login fosse CPF) ou teríamos de criar buscaPorEmail
            // Por enquanto, se não for admin, dá erro.
            mostrarAlerta(AlertType.ERROR, "Login Falhou", "Usuário ou senha incorretos.");

        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Erro", "Erro ao validar login.");
        }
    }

    @FXML
    private void irParaCadastro() {
        // MUDANÇA AQUI: Use o nome correto do arquivo que acabamos de criar
        ScreenManager.getInstance().carregarTela("TelaCadastroParticipante.fxml", "Novo Cadastro");
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

package com.example.sea.gui;

import com.example.sea.business.SessaoUsuario;
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

        if (email.equals("admin") && senha.equals("admin")) {
            System.out.println("Login de Admin realizado!");
            SessaoUsuario.getInstance().logout(); // Garante que não há participante logado
            ScreenManager.getInstance().carregarTela("TelaPrincipal.fxml", "Menu Administrativo");
            return;
        }
        try {

            Participante encontrado = null;
            for (Participante p : SistemaSGA.getInstance().getControladorParticipante().listarTodos()) {
                if (p.getEmail().equals(email) && p.getSenha().equals(senha)) {
                    encontrado = p;
                    break;
                }
            }

            if (encontrado != null) {
                // Sucesso! Salva na sessão
                SessaoUsuario.getInstance().login(encontrado);
                System.out.println("Bem-vindo, " + encontrado.getNome());

                // Redireciona para a área do aluno (ex: lista de eventos disponíveis)
                ScreenManager.getInstance().carregarTela("view_eventos.fxml", "Área do Aluno");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "E-mail ou senha incorretos.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Falha no login: " + e.getMessage());
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

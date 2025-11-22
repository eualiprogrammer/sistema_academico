package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Evento;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class TelaCadastroEventoController {

    @FXML private TextField txtNome;
    @FXML private DatePicker dpDataInicio;
    @FXML private DatePicker dpDataFim;
    @FXML private TextArea txtDescricao; // Mudei para TextArea (melhor para descrição), se for TextField no seu FXML, mude aqui.

    // --- CORREÇÃO 1: Declarar a variável que estava faltando ---
    private Evento eventoEmEdicao;

    // Método chamado pelo ScreenManager para receber os dados
    public void setEvento(Evento evento) {
        this.eventoEmEdicao = evento;
        if (evento != null) {
            txtNome.setText(evento.getNome());
            // O modelo usa LocalDate, então passamos direto (sem converter)
            dpDataInicio.setValue(evento.getDataInicio());
            dpDataFim.setValue(evento.getDataFim());
            txtDescricao.setText(evento.getDescricao());
        }
    }

    @FXML
    private void salvar() {
        try {
            // 1. Pegar os dados da tela
            String nome = txtNome.getText();
            String descricao = txtDescricao.getText();

            // Validação simples
            if (dpDataInicio.getValue() == null || dpDataFim.getValue() == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione as datas.");
                return;
            }

            // --- CORREÇÃO 2: Usar LocalDate direto (sem atStartOfDay) ---
            LocalDate dataInicio = dpDataInicio.getValue();
            LocalDate dataFim = dpDataFim.getValue();

            if (eventoEmEdicao != null) {
                // --- MODO EDIÇÃO ---
                eventoEmEdicao.setNome(nome);
                eventoEmEdicao.setDataInicio(dataInicio); // Passa LocalDate direto
                eventoEmEdicao.setDataFim(dataFim);       // Passa LocalDate direto
                eventoEmEdicao.setDescricao(descricao);

                SistemaSGA.getInstance().getControladorEvento().atualizar(eventoEmEdicao);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Evento atualizado!");

            } else {
                // --- MODO CRIAÇÃO ---
                // O construtor espera (String, LocalDate, LocalDate, String)
                Evento novoEvento = new Evento(nome, dataInicio, dataFim, descricao);

                SistemaSGA.getInstance().getControladorEvento().cadastrar(novoEvento);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Evento cadastrado!");
            }

            cancelar(); // Volta para a lista

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        // Volta para a tela de listagem
        ScreenManager.getInstance().carregarTela("TelaListarEventos.fxml", "Eventos");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
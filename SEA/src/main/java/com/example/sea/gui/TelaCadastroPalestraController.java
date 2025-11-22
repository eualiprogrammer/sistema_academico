package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TelaCadastroPalestraController {


    @FXML private TextField txtNome;
    @FXML private ChoiceBox<Evento> cbEvento;
    @FXML private ChoiceBox<Sala> cbSala;
    @FXML private ChoiceBox<Palestrante> cbPalestrante;
    @FXML private DatePicker dpDataInicio;
    @FXML private TextField txtDuracaoHoras;
    @FXML private TextArea txtDescricao;


    @FXML private TextField txtHora;

    @FXML
    public void initialize() {
        carregarChoiceBoxes();
    }

    private void carregarChoiceBoxes() {
        // 1. Carregar Eventos
        cbEvento.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorEvento().listar()
        ));
        // Converter para mostrar apenas o NOME do evento
        cbEvento.setConverter(new StringConverter<>() {
            @Override
            public String toString(Evento e) {
                return (e != null) ? e.getNome() : "";
            }

            @Override
            public Evento fromString(String string) {
                return null;
            }
        });

        // 2. Carregar Salas
        cbSala.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorSala().listar()
        ));
        cbSala.setConverter(new StringConverter<>() {
            @Override
            public String toString(Sala s) {
                return (s != null) ? s.getNome() : "";
            }

            @Override
            public Sala fromString(String string) {
                return null;
            }
        });

        // 3. Carregar Palestrantes
        cbPalestrante.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorPalestrante().listar()
        ));
        cbPalestrante.setConverter(new StringConverter<>() {
            @Override
            public String toString(Palestrante p) {
                return (p != null) ? p.getNome() : "";
            }

            @Override
            public Palestrante fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    private void salvar() { // Associe este método ao botão "Salvar" (On Action: #salvar)
        try {
            // 1. Pegar os dados simples
            String titulo = txtNome.getText();
            String descricao = txtDescricao.getText();
            String duracaoTexto = txtDuracaoHoras.getText();

            // 2. Pegar Data e Hora
            LocalDate data = dpDataInicio.getValue();
            String horaTexto = txtHora.getText(); // Ex: "14:00"

            // Validação básica de UI
            if (data == null || horaTexto == null || horaTexto.isEmpty()) {
                mostrarAlerta(AlertType.WARNING, "Atenção", "Preencha a data e a hora da palestra.");
                return;
            }

            // Montar o LocalDateTime
            LocalTime hora = LocalTime.parse(horaTexto);
            LocalDateTime dataHoraInicio = LocalDateTime.of(data, hora);

            float duracao = Float.parseFloat(duracaoTexto);

            // 3. Pegar os objetos selecionados nos ChoiceBoxes
            Evento eventoSelecionado = cbEvento.getValue();
            Sala salaSelecionada = cbSala.getValue();
            Palestrante palestranteSelecionado = cbPalestrante.getValue();

            if (eventoSelecionado == null) throw new Exception("Selecione um Evento.");
            if (salaSelecionada == null) throw new Exception("Selecione uma Sala.");
            if (palestranteSelecionado == null) throw new Exception("Selecione um Palestrante.");

            // 4. Chamar o Backend
            SistemaSGA.getInstance().getControladorPalestra().cadastrar(
                    titulo, descricao, eventoSelecionado,
                    dataHoraInicio, duracao,
                    salaSelecionada, palestranteSelecionado
            );

            mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Palestra cadastrada com sucesso!");
            limparCampos();

        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void cancelar() { // Associe ao botão "Cancelar"
        ScreenManager.getInstance().carregarTela("TelaPrincipal.fxml", "Menu Principal");
    }

    private void limparCampos() {
        txtNome.clear();
        txtDescricao.clear();
        txtDuracaoHoras.clear();
        txtHora.clear();
        dpDataInicio.setValue(null);
        cbEvento.setValue(null);
        cbSala.setValue(null);
        cbPalestrante.setValue(null);
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String msg) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
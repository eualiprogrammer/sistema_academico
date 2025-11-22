package com.example.sea.gui;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Evento;
import com.example.sea.model.Palestra;
import com.example.sea.model.Palestrante;
import com.example.sea.model.Sala;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class TelaCadastroPalestraController {

    // --- Seus Campos Atualizados ---
    @FXML private TextField txtNome;
    @FXML private ChoiceBox<Evento> cbEvento;
    @FXML private ChoiceBox<Sala> cbSala;
    @FXML private ChoiceBox<Palestrante> cbPalestrante;
    @FXML private DatePicker dpDataInicio;
    @FXML private TextField txtDuracaoHoras;
    @FXML private TextArea txtDescricao;
    @FXML private TextField txtHora; // Campo auxiliar para digitar a hora (ex: 14:00)

    private Palestra palestraEmEdicao;

    @FXML
    public void initialize() {
        carregarListas();
    }

    private void carregarListas() {
        // Preenche os ChoiceBox com dados do banco
        cbEvento.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorEvento().listar()));
        cbSala.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorSala().listar()));
        cbPalestrante.setItems(FXCollections.observableArrayList(
                SistemaSGA.getInstance().getControladorPalestrante().listar()));
    }

    // Método chamado pelo ScreenManager para EDIÇÃO
    public void setPalestra(Palestra palestra) {
        this.palestraEmEdicao = palestra;
        if (palestra != null) {
            txtNome.setText(palestra.getTitulo());
            txtDescricao.setText(palestra.getDescricao());
            txtDuracaoHoras.setText(String.valueOf(palestra.getDuracaoHoras()));

            if (palestra.getDataHoraInicio() != null) {
                dpDataInicio.setValue(palestra.getDataHoraInicio().toLocalDate());
                txtHora.setText(palestra.getDataHoraInicio().toLocalTime().toString());
            }

            cbEvento.setValue(palestra.getEvento());
            cbSala.setValue(palestra.getSala());
            cbPalestrante.setValue(palestra.getPalestrante());

            // Se o título for a chave de identificação, desabilitamos na edição
            txtNome.setDisable(true);
        }
    }

    @FXML
    private void salvar() {
        try {
            // 1. Pegar dados dos campos
            String nome = txtNome.getText();
            String descricao = txtDescricao.getText();
            String duracaoStr = txtDuracaoHoras.getText();
            String horaStr = txtHora.getText();
            LocalDate data = dpDataInicio.getValue();

            Evento evento = cbEvento.getValue();
            Sala sala = cbSala.getValue();
            Palestrante palestrante = cbPalestrante.getValue();

            // 2. Validações Básicas
            if (nome == null || nome.trim().isEmpty()) throw new IllegalArgumentException("O nome da palestra é obrigatório.");
            if (data == null) throw new IllegalArgumentException("Selecione a data de início.");
            if (horaStr == null || horaStr.trim().isEmpty()) throw new IllegalArgumentException("Digite o horário (Ex: 14:00).");
            if (duracaoStr == null || duracaoStr.trim().isEmpty()) throw new IllegalArgumentException("Digite a duração em horas.");
            if (evento == null || sala == null || palestrante == null) throw new IllegalArgumentException("Preencha todos os campos de seleção (Evento, Sala, Palestrante).");

            // 3. Conversões
            float duracao = Float.parseFloat(duracaoStr);
            LocalTime hora = LocalTime.parse(horaStr); // Pode gerar erro se o formato estiver errado
            LocalDateTime dataHoraInicio = LocalDateTime.of(data, hora);

            if (palestraEmEdicao != null) {
                // --- MODO EDIÇÃO ---
                // Atualiza os dados do objeto existente
                palestraEmEdicao.setDescricao(descricao);
                palestraEmEdicao.setDuracaoHoras(duracao);
                palestraEmEdicao.setDataHoraInicio(dataHoraInicio);
                palestraEmEdicao.setEvento(evento);
                palestraEmEdicao.setSala(sala);
                palestraEmEdicao.setPalestrante(palestrante);
                // palestraEmEdicao.setTitulo(nome); // Se permitir mudar o nome, descomente aqui

                SistemaSGA.getInstance().getControladorPalestra().atualizar(palestraEmEdicao);
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Palestra atualizada com sucesso!");

            } else {
                // --- MODO CADASTRO ---
                // Chama o controlador para criar nova palestra
                SistemaSGA.getInstance().getControladorPalestra().cadastrar(
                        nome, descricao, evento, dataHoraInicio, duracao, sala, palestrante
                );
                mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Palestra cadastrada com sucesso!");
            }

            cancelar(); // Volta para a lista

        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.ERROR, "Erro de Formato", "A duração deve ser um número (ex: 1.5 ou 2).");
        } catch (DateTimeParseException e) {
            mostrarAlerta(AlertType.ERROR, "Erro de Data/Hora", "Formato de hora inválido. Use HH:mm (ex: 13:30).");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(AlertType.ERROR, "Erro ao Salvar", e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        ScreenManager.getInstance().carregarTela("TelaListarPalestras.fxml", "Palestras");
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
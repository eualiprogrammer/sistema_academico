package com.example.sea.gui;

import com.example.sea.business.SessaoUsuario;
import com.example.sea.business.SistemaSGA;
import com.example.sea.model.Certificado;
import com.example.sea.model.Participante;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

// Imports do iText para gerar PDF
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewCertificadosController {

    @FXML
    private VBox containerCertificados;

    @FXML
    public void initialize() {
        carregarCertificados();
    }

    private void carregarCertificados() {
        containerCertificados.getChildren().clear();

        Participante logado = SessaoUsuario.getInstance().getParticipanteLogado();
        if (logado == null) {
            ScreenManager.getInstance().carregarTela("TelaLogin.fxml", "Login");
            return;
        }

        List<Certificado> certificados = SistemaSGA.getInstance()
                .getControladorCertificado()
                .listarPorParticipante(logado);

        if (certificados.isEmpty()) {
            Label emptyLabel = new Label("Nenhum certificado disponível ainda.");
            emptyLabel.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 14px;");
            containerCertificados.getChildren().add(emptyLabel);
            return;
        }

        for (Certificado cert : certificados) {
            containerCertificados.getChildren().add(criarCardCertificado(cert));
        }
    }

    private VBox criarCardCertificado(Certificado cert) {
        // --- 1. Configuração Visual (Estilo Dark) ---
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-color: #1E2130; -fx-padding: 20; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 15, 0, 0, 5); -fx-border-color: rgba(255,255,255,0.05); -fx-border-width: 1;");

        // --- 2. Dados ---
        String nomeAtividade = cert.getInscricao().getPalestra().getTitulo();
        String codigo = cert.getCodigoValidacao();
        String dataEvento = "Data: " + cert.getInscricao().getPalestra().getDataHoraInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Título (Dourado para dar ideia de "Premium"/Certificado)
        Label lblTitulo = new Label("🎓 " + nomeAtividade);
        lblTitulo.setStyle("-fx-text-fill: #FACC15; -fx-font-size: 20px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(250, 204, 21, 0.2), 10, 0, 0, 0);");

        // Detalhes
        String detalhesTexto = dataEvento + "\n🔑 Código de Validação: " + codigo;
        Label lblDetalhes = new Label(detalhesTexto);
        lblDetalhes.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 13px; -fx-font-family: 'Consolas', monospace;");
        lblDetalhes.setWrapText(true);

        // --- 3. Botão Gerar PDF ---
        Button btnPdf = new Button("📥 Baixar PDF");
        btnPdf.getStyleClass().add("btn-acao");
        // Estilo específico para o botão de baixar (Azul Ciano)
        btnPdf.setStyle("-fx-background-color: linear-gradient(to right, #06B6D4, #3B82F6); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 30;");

        btnPdf.setOnAction(e -> salvarPdf(cert));

        // --- 4. Layout ---
        VBox infoBox = new VBox(5, lblTitulo, lblDetalhes);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        HBox linhaBtn = new HBox(btnPdf);
        linhaBtn.setAlignment(Pos.CENTER_RIGHT);

        // Organiza horizontalmente: Texto na esquerda, Botão na direita
        HBox linhaPrincipal = new HBox(10);
        linhaPrincipal.setAlignment(Pos.CENTER_LEFT);
        linhaPrincipal.getChildren().addAll(infoBox, linhaBtn);

        card.getChildren().add(linhaPrincipal);
        return card;
    }

    private void salvarPdf(Certificado cert) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Certificado");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Certificado_" + cert.getCodigoValidacao() + ".pdf");

        File file = fileChooser.showSaveDialog(containerCertificados.getScene().getWindow());

        if (file != null) {
            gerarArquivoPdf(cert, file);
        }
    }

    private void gerarArquivoPdf(Certificado cert, File destino) {
        try {
            PdfWriter writer = new PdfWriter(destino);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("CERTIFICADO DE PARTICIPAÇÃO")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(24)
                    .setBold());

            document.add(new Paragraph("\n\n"));

            String texto = "Certificamos que " + cert.getInscricao().getParticipante().getNome().toUpperCase() +
                    " participou da atividade " + cert.getInscricao().getPalestra().getTitulo().toUpperCase() +
                    " realizada em " + cert.getInscricao().getPalestra().getDataHoraInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".";

            document.add(new Paragraph(texto).setFontSize(14).setTextAlignment(TextAlignment.JUSTIFIED));

            document.add(new Paragraph("\n\n\nCódigo de Validação: " + cert.getCodigoValidacao())
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "PDF salvo com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Falha ao gerar PDF: " + e.getMessage());
        }
    }

    @FXML
    private void voltar() {
        ScreenManager.getInstance().carregarTela("view_eventos.fxml", "Área do Aluno");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
package com.example.sea.model;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Inscricao implements Serializable{
    private static final long serialVersionUID = 4L;
    private Participante participante;
    private Palestra palestra;
    private LocalDateTime dataHoraInscricao;
    private boolean presenca; 
    private String statusConfirmacao;
    private Certificado certificado; 
    
    public Inscricao(Participante participante, Palestra palestra) {
        if (participante == null || palestra == null) {
            throw new IllegalArgumentException("Participante e Palestra não podem ser nulos.");
        }
        this.participante = participante;
        this.palestra = palestra;
        this.dataHoraInscricao = LocalDateTime.now();
        this.presenca = false; 
        this.statusConfirmacao = "Confirmada";
        participante.adicionarInscricao(this);
        palestra.adicionarInscricao(this);
    }

    public Certificado getCertificado() {
    return certificado;
    }

    public void setCertificado(Certificado certificado) {
        this.certificado = certificado;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public Palestra getPalestra() {
        return palestra;
    }

    public void setPalestra(Palestra palestra) {
        this.palestra = palestra;
    }

    public LocalDateTime getDataHoraInscricao() {
        return dataHoraInscricao;
    }

    public void setDataHoraInscricao(LocalDateTime dataHoraInscricao) {
        this.dataHoraInscricao = dataHoraInscricao;
    }
    public boolean isPresenca() {
        return presenca;
    }

    public void confirmarPresenca() {
        this.presenca = true;
    }

    public void marcarAusencia() {
        this.presenca = false;
    }

    public String getStatusConfirmacao() {
        return statusConfirmacao;
    }

    public void setStatusConfirmacao(String statusConfirmacao) {
        this.statusConfirmacao = statusConfirmacao;
    }

    public void setPresenca(boolean presenca) {
        this.presenca = presenca;
    }
}
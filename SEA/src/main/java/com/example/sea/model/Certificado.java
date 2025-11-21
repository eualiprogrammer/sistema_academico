package com.example.sea.model;
import java.time.LocalDate;
import java.util.UUID; 
import java.io.Serializable;

public class Certificado implements Serializable{
    private static final long serialVersionUID = 2L;
    private Inscricao inscricao;
    private String codigoValidacao; 
    private LocalDate dataEmissao; 
    private float cargaHorariaRegistrada; 

    public Certificado(Inscricao inscricao) {
        if (inscricao == null) {
            throw new IllegalArgumentException("A inscrição não pode ser nula.");
        }

        if (!inscricao.isPresenca()) {
            throw new RuntimeException("Não é possível gerar certificado: Participante '" + 
                inscricao.getParticipante().getNome() + "' está marcado como ausente.");
        }

        this.inscricao = inscricao;
        this.dataEmissao = LocalDate.now(); 
        this.codigoValidacao = UUID.randomUUID().toString(); 
        this.cargaHorariaRegistrada = inscricao.getPalestra().getDuracaoHoras();
        inscricao.setCertificado(this); 
    }

    public Inscricao getInscricao() {
        return inscricao;
    }

    public String getCodigoValidacao() {
        return codigoValidacao;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public float getCargaHorariaRegistrada() {
        return cargaHorariaRegistrada;
    }
    
    public String getNomeParticipante() {
        return this.inscricao.getParticipante().getNome();
    }
    
    public String getTituloPalestra() {
        return this.inscricao.getPalestra().getTitulo();
    }
    
}
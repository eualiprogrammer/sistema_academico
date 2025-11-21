package com.example.sea.model;
import java.io.Serializable;

public abstract class Atividade implements Serializable{

    private String titulo;
    private String descricao;

    private Evento evento;

    public Atividade (String titulo, String descricao, Evento evento) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.evento = evento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}
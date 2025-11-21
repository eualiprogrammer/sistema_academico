package com.example.sea.model;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Participante implements Serializable{
    private String nome;
    private String email;
    private String instituicao;
    private String cpf; 
    private List<Inscricao> inscricoes;

    public Participante(String nome, String email, String instituicao, String cpf) {
        this.nome = nome;
        this.email = email;
        this.instituicao = instituicao;
        this.cpf = cpf;
        this.inscricoes = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<Inscricao> getInscricoes() {
        return this.inscricoes;
    }

    public void adicionarInscricao(Inscricao inscricao) {
        if (inscricao != null && !this.inscricoes.contains(inscricao)) {
            this.inscricoes.add(inscricao);
        }
    }
}
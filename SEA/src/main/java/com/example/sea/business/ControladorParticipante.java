package com.example.sea.business;

import com.example.sea.data.IRepositorioParticipante;
import com.example.sea.data.RepositorioParticipante; 
import com.example.sea.model.Participante;
import com.example.sea.exceptions.CampoVazioException;
import com.example.sea.exceptions.ParticipanteJaExisteException;
import com.example.sea.exceptions.ParticipanteNaoEncontradoException;

import java.util.List;


 // Esta classe contém as regras de negócio e validações.

public class ControladorParticipante implements IControladorParticipante {

    private IRepositorioParticipante repositorioParticipante;

    public ControladorParticipante() {
        // Instancia a implementação concreta do repositório
        this.repositorioParticipante = new RepositorioParticipante();
    }

    @Override
    public void cadastrar(Participante participante) throws ParticipanteJaExisteException, CampoVazioException {
       
        if (participante == null) {
            throw new IllegalArgumentException("O objeto 'Participante' não pode ser nulo.");
        }
        
        if (participante.getNome() == null || participante.getNome().trim().isEmpty()) {
            throw new CampoVazioException("Nome");
        }
        if (participante.getEmail() == null || participante.getEmail().trim().isEmpty()) {
            throw new CampoVazioException("Email");
        }
        if (participante.getCpf() == null || participante.getCpf().trim().isEmpty()) {
            throw new CampoVazioException("CPF");
        }
        this.repositorioParticipante.salvar(participante);
    }

    @Override
    public Participante buscar(String cpf) throws ParticipanteNaoEncontradoException, CampoVazioException {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new CampoVazioException("CPF");
        }
        return this.repositorioParticipante.buscarPorIdentificador(cpf);
    }

    @Override
    public List<Participante> listar() {
        return this.repositorioParticipante.listarTodos();
    }

    @Override
    public void atualizar(Participante participante) throws ParticipanteNaoEncontradoException, CampoVazioException {
        // Validações (semelhantes ao cadastrar)
        if (participante == null) {
            throw new IllegalArgumentException("O objeto 'Participante' não pode ser nulo.");
        }
        if (participante.getCpf() == null || participante.getCpf().trim().isEmpty()) {
            throw new CampoVazioException("CPF");
        }
        // Se passou nas validações, chama a camada 'data'
        this.repositorioParticipante.atualizar(participante);
    }

    @Override
    public void remover(String cpf) throws ParticipanteNaoEncontradoException, CampoVazioException {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new CampoVazioException("CPF");
        }
        this.repositorioParticipante.deletar(cpf);
    }
}
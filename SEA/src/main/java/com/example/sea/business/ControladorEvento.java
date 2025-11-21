package com.example.sea.business;

import com.example.sea.data.IRepositorioEvento;
import com.example.sea.data.RepositorioEvento; 
import com.example.sea.model.Evento;
import com.example.sea.exceptions.CampoVazioException;
import com.example.sea.exceptions.DataInvalidaException;
import com.example.sea.exceptions.EventoJaExisteException;
import com.example.sea.exceptions.EventoNaoEncontradoException;

import java.util.List;

public class ControladorEvento implements IControladorEvento {

    private IRepositorioEvento repositorioEvento;

    public ControladorEvento() {
        // Instancia a implementação concreta do repositório
        this.repositorioEvento = new RepositorioEvento();
    }

    /**
     * Valida as regras de negócio de um objeto Evento.
     * @param evento O evento a ser validado.
     * @throws CampoVazioException Se campos obrigatórios estiverem vazios.
     * @throws DataInvalidaException Se a data de início for após a data de fim.
     */
    
    private void validarEvento(Evento evento) throws CampoVazioException, DataInvalidaException {
        if (evento == null) {
            throw new IllegalArgumentException("O objeto 'Evento' não pode ser nulo.");
        }
        if (evento.getNome() == null || evento.getNome().trim().isEmpty()) {
            throw new CampoVazioException("Nome");
        }
        if (evento.getDataInicio() == null) {
            throw new CampoVazioException("Data de Início");
        }
        if (evento.getDataFim() == null) {
            throw new CampoVazioException("Data de Fim");
        }

        if (evento.getDataInicio().isAfter(evento.getDataFim())) {
            throw new DataInvalidaException("A data de início não pode ser posterior à data de fim.");
        }
    }


    @Override
    public void cadastrar(Evento evento) throws EventoJaExisteException, CampoVazioException, DataInvalidaException {
        // 1. Valida as regras de negócio
        this.validarEvento(evento);
        
        // 2. Se todas as regras de negócio passaram, chama a camada 'data'
        this.repositorioEvento.salvar(evento);
    }

    @Override
    public Evento buscar(String nome) throws EventoNaoEncontradoException, CampoVazioException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new CampoVazioException("Nome");
        }

        return this.repositorioEvento.buscarPorNome(nome);
    }

    @Override
    public List<Evento> listar() {
        return this.repositorioEvento.listarTodos();
    }

    @Override
    public void atualizar(Evento evento) throws EventoNaoEncontradoException, CampoVazioException, DataInvalidaException {
        this.validarEvento(evento);
        this.repositorioEvento.atualizar(evento);
    }

    @Override
    public void remover(String nome) throws EventoNaoEncontradoException, CampoVazioException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new CampoVazioException("Nome");
        }

        this.repositorioEvento.deletar(nome);
    }
}
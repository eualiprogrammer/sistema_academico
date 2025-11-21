package com.example.sea.business;

import com.example.sea.model.Evento;
import com.example.sea.exceptions.EventoJaExisteException;
import com.example.sea.exceptions.EventoNaoEncontradoException;
import com.example.sea.exceptions.CampoVazioException;
import com.example.sea.exceptions.DataInvalidaException; // <-- IMPORTADO
import java.util.List;

public interface IControladorEvento {

    /**
     * @throws EventoJaExisteException Se o evento já existir.
     * @throws CampoVazioException Se campos obrigatórios estiverem vazios.
     * @throws DataInvalidaException Se a data de início for após a data de fim.
     */
    void cadastrar(Evento evento) throws EventoJaExisteException, CampoVazioException, DataInvalidaException;

    /**
     * @throws EventoNaoEncontradoException Se o evento não for encontrado.
     * @throws CampoVazioException Se 'nome' estiver vazio.
     */
    Evento buscar(String nome) throws EventoNaoEncontradoException, CampoVazioException;

    List<Evento> listar();

    /**
     * @throws EventoNaoEncontradoException Se o evento não for encontrado.
     * @throws CampoVazioException Se 'nome' estiver vazio.
     * @throws DataInvalidaException Se a data de início for após a data de fim.
     */
    void atualizar(Evento evento) throws EventoNaoEncontradoException, CampoVazioException, DataInvalidaException;

    /**
     * @throws EventoNaoEncontradoException Se o evento não for encontrado.
     * @throws CampoVazioException Se 'nome' estiver vazio.
     */
    void remover(String nome) throws EventoNaoEncontradoException, CampoVazioException;
}
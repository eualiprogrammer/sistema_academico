package com.example.sea.data;

import com.example.sea.model.Evento;
import com.example.sea.exceptions.EventoJaExisteException;
import com.example.sea.exceptions.EventoNaoEncontradoException;
import java.util.List;
// Interface (Contrato) para a camada de dados (Repositório) de Evento.

public interface IRepositorioEvento {

    /**
     * Salva um novo evento no repositório.
     * @param evento O objeto Evento a ser salvo.
     * @throws EventoJaExisteException Se um evento com o mesmo nome já existir.
     */
    void salvar(Evento evento) throws EventoJaExisteException;

    /**
     * Busca um evento pelo seu nome (que usaremos como identificador único).
     * @param nome O nome do evento.
     * @return O objeto Evento encontrado.
     * @throws EventoNaoEncontradoException Se o evento não for encontrado.
     */
    Evento buscarPorNome(String nome) throws EventoNaoEncontradoException;

    /**
     * Retorna uma lista com todos os eventos cadastrados.
     * @return Uma List<Evento>.
     */
    List<Evento> listarTodos();

    /**
     * Atualiza os dados de um evento já existente (identificado pelo nome).
     * @param evento O objeto Evento com os dados atualizados.
     * @throws EventoNaoEncontradoException Se o evento a ser atualizado não for encontrado.
     */
    void atualizar(Evento evento) throws EventoNaoEncontradoException;

    /**
     * Remove um evento do repositório.
     * @param nome O nome do evento a ser removido.
     * @throws EventoNaoEncontradoException Se o evento a ser removido não for encontrado.
     */
    void deletar(String nome) throws EventoNaoEncontradoException;
}
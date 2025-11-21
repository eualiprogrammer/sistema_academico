package com.example.sea.exceptions;

/**
 * Exceção lançada quando uma operação (buscar, atualizar, deletar)
 * não encontra o Evento.
 */
public class EventoNaoEncontradoException extends Exception {

    public EventoNaoEncontradoException(String nomeDoEvento) {
        super("Nenhum evento foi encontrado com o nome: " + nomeDoEvento);
    }
}
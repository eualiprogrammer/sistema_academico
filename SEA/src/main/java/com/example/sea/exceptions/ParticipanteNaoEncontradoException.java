package com.example.sea.exceptions;

/**
 * Exceção lançada quando uma operação (buscar, atualizar, deletar)
 * não encontra o Participante.
 */
public class ParticipanteNaoEncontradoException extends Exception {

    public ParticipanteNaoEncontradoException(String identificador) {
        super("Nenhum participante foi encontrado com o identificador: " + identificador);
    }
}
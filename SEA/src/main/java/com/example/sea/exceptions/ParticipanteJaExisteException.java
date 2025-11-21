package com.example.sea.exceptions;

/**
 * Exceção lançada quando se tenta cadastrar um Participante que já existe
 * (ex: com o mesmo email ou CPF).
 */
public class ParticipanteJaExisteException extends Exception {

    public ParticipanteJaExisteException(String identificador) {
        super("Já existe um participante cadastrado com o identificador (email/CPF): " + identificador);
    }
}
package com.example.sea.exceptions;

/**
 * Exceção lançada quando se tenta inscrever um participante
 * que já está inscrito numa palestra.
 */
public class InscricaoJaExisteException extends Exception {

    public InscricaoJaExisteException(String participante, String palestra) {
        super("O participante '" + participante + "' já está inscrito na palestra '" + palestra + "'.");
    }
}
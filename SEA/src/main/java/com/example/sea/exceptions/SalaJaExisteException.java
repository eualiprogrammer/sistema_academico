package com.example.sea.exceptions;

/**
 * Exceção lançada quando se tenta cadastrar uma Sala que já existe
 * (ex: com o mesmo nome).
 */
public class SalaJaExisteException extends Exception {

    public SalaJaExisteException(String nomeDaSala) {
        super("Já existe uma sala cadastrada com o nome: " + nomeDaSala);
    }
}
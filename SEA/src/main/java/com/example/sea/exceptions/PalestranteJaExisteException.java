package com.example.sea.exceptions;

/**
 * Exceção lançada quando se tenta cadastrar um palestrante que já existe.
 */
public class PalestranteJaExisteException extends Exception {
    public PalestranteJaExisteException(String email) {
        super("Já existe um palestrante cadastrado com o email: " + email);
    }
}
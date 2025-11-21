package com.example.sea.exceptions;

/**
 * Exceção lançada quando uma operação (buscar, atualizar, deletar)
 * não encontra a Palestra.
 */
public class PalestraNaoEncontradaException extends Exception {
    
    public PalestraNaoEncontradaException(String identificador) {
        super("Nenhuma palestra foi encontrada com o identificador: " + identificador);
    }
}
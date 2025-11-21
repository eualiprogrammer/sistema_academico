package com.example.sea.exceptions;

/**
 * Exceção lançada quando uma operação (buscar, atualizar, deletar)
 * não encontra a Sala.
 */
public class SalaNaoEncontradaException extends Exception {

    public SalaNaoEncontradaException(String nomeDaSala) {
        super("Nenhuma sala foi encontrada com o nome: " + nomeDaSala);
    }
}
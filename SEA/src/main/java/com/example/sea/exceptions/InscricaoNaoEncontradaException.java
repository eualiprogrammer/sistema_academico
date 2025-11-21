package com.example.sea.exceptions;

 //Exceção lançada quando se tenta buscar/remover uma inscrição que não existe.
public class InscricaoNaoEncontradaException extends Exception {

    public InscricaoNaoEncontradaException() {
        super("A inscrição procurada não foi encontrada.");
    }
}
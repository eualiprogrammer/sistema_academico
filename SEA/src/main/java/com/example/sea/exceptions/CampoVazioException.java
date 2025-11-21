package com.example.sea.exceptions;
/**
 * Exceção lançada pela camada de negócio (business) quando um campo
 * obrigatório não é preenchido.
 */
public class CampoVazioException extends Exception {
    
    public CampoVazioException(String nomeDoCampo) {
        super("O campo '" + nomeDoCampo + "' é obrigatório e não pode estar vazio.");
    }
}
package com.example.sea.exceptions;
/**
 * Exceção lançada pela camada de negócio (business) quando uma regra 
 * de datas é violada (ex: data de início após a data de fim).
 */
public class DataInvalidaException extends Exception {
    
    public DataInvalidaException(String mensagem) {
        super(mensagem);
    }

    public DataInvalidaException(String tipoData, String mensagem) {
        super("Erro na " + tipoData + ": " + mensagem);
    }
}
package com.example.sea.exceptions;
/**
 * Exceção lançada quando se tenta inscrever num evento
 * cujas vagas estão esgotadas (REQ23).
 */
public class LotacaoExcedidaException extends Exception {

    public LotacaoExcedidaException(String nomePalestra) {
        super("Não foi possível realizar a inscrição: As vagas para a palestra '" + nomePalestra + "' estão esgotadas.");
    }
}
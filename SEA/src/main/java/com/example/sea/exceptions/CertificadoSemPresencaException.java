package com.example.sea.exceptions;

/**
 * Exceção lançada quando se tenta gerar um certificado para uma inscrição
 * sem presença confirmada (REQ26).
 */
public class CertificadoSemPresencaException extends Exception {

    public CertificadoSemPresencaException(String nomeParticipante, String nomePalestra) {
        super("Não é possível gerar certificado: O participante '" + nomeParticipante + 
              "' não teve a presença confirmada na palestra '" + nomePalestra + "'.");
    }
}
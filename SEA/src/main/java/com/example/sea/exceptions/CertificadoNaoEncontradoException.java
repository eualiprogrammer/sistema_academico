package com.example.sea.exceptions;

/**
 * Exceção lançada quando não se encontra um certificado procurado.
 */
public class CertificadoNaoEncontradoException extends Exception {
    
    public CertificadoNaoEncontradoException(String codigo) {
        super("Certificado não encontrado com o código: " + codigo);
    }
}
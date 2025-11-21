package com.example.sea.exceptions;

/**
 * Exceção lançada quando se tenta remover uma palestra
 * que já possui participantes inscritos (REQ25).
 */
public class PalestraComInscritosException extends Exception {
    
    public PalestraComInscritosException(String tituloPalestra, int numeroInscritos) {
        super("Não é possível remover a palestra '" + tituloPalestra + 
              "'. Ela já possui " + numeroInscritos + " participante(s) inscrito(s).");
    }
}
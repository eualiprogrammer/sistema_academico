
package com.example.sea.exceptions;

/**
 * Exceção lançada quando há um conflito de horário (REQ24).
 */
public class ConflitoHorarioException extends Exception {

    /**
     * Construtor para mensagens de conflito genéricas (ex: Sala ou Palestrante ocupado).
     * @param mensagem A mensagem de erro específica.
     */
    public ConflitoHorarioException(String mensagem) {
        super(mensagem);
    }

    /**
     * Construtor para conflitos de inscrição de participante.
     * @param palestraExistente A palestra em que o participante já está.
     * @param novaPalestra A palestra que está a tentar inscrever.
     */
    public ConflitoHorarioException(String palestraExistente, String novaPalestra) {
        super("Conflito de Horário: A inscrição na palestra '" + novaPalestra + 
              "' conflita com a palestra '" + palestraExistente + "' em que já está inscrito.");
    }
}
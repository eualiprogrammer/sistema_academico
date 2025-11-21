package com.example.sea.exceptions;

 // Exceção lançada quando se tenta cadastrar um Evento que já existe
public class EventoJaExisteException extends Exception {

    public EventoJaExisteException(String nomeDoEvento) {
        super("Já existe um evento cadastrado com o nome: " + nomeDoEvento);
    }
}
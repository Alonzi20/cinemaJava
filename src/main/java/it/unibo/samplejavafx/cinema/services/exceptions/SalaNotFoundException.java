package it.unibo.samplejavafx.cinema.services.exceptions;

public class SalaNotFoundException extends RuntimeException{
    static final String MESSAGE = "Sala con id %s non trovata";

    public SalaNotFoundException(String id) {
      super(String.format(MESSAGE, id));
    }
}

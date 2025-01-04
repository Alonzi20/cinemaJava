package it.unibo.samplejavafx.cinema.services.exceptions;

public class ProiezioneNotFoundException extends RuntimeException{
    static final String MESSAGE = "Proiezione con id %s non trovata";

    public ProiezioneNotFoundException(String id) {
      super(String.format(MESSAGE, id));
    }
}

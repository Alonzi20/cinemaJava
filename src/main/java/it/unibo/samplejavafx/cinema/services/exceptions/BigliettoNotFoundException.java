package it.unibo.samplejavafx.cinema.services.exceptions;

public class BigliettoNotFoundException extends RuntimeException {
  static final String MESSAGE = "Biglietto con id %s non trovato";

  public BigliettoNotFoundException(String id) {
    super(String.format(MESSAGE, id));
  }
}

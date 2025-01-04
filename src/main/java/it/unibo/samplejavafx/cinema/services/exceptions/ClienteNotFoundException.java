package it.unibo.samplejavafx.cinema.services.exceptions;

public class ClienteNotFoundException extends RuntimeException {
  static final String MESSAGE = "Cliente con id %s non trovato";

  public ClienteNotFoundException(String id) {
    super(String.format(MESSAGE, id));
  }
}

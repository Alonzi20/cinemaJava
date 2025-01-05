package it.unibo.samplejavafx.cinema.services.exceptions;

public class PostoNotFoundException extends RuntimeException {
  static final String MESSAGE = "Posto con id %s non trovato";

  public PostoNotFoundException(String id) {
    super(String.format(MESSAGE, id));
  }
}

package it.unibo.samplejavafx.cinema.services.exceptions;

public class FilmNotFoundException extends RuntimeException {
  static final String MESSAGE = "Film con id %s non trovato";

  public FilmNotFoundException(String id) {
    super(String.format(MESSAGE, id));
  }
}

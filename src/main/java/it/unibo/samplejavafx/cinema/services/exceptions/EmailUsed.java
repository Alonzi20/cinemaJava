package it.unibo.samplejavafx.cinema.services.exceptions;

public class EmailUsed extends RuntimeException {
  static final String MESSAGE = "L'email %s è già stata registrata";

  public EmailUsed(String email) {
    super(String.format(MESSAGE, email));
  }
}

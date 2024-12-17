package it.unibo.samplejavafx.cinema.models;

import lombok.Data;

@Data
public class Biglietto {
  private static final double PREZZO_INTERO = 8.0;
  private static final double PREZZO_RIDOTTO = 5.0;

  int id;
  Proiezione proiezione;
  boolean ridotto;

  // Posto posto;

  public double prezzo() {
    return ridotto ? PREZZO_RIDOTTO : PREZZO_INTERO;
  }

  // TODO Alex: [17/12/2024]
  public void compra() {
    if (proiezione.isPrenotabile()) {}
  }
}

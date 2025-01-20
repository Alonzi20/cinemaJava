package it.unibo.samplejavafx.cinema.application.dto;

import it.unibo.samplejavafx.cinema.application.models.Biglietto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BigliettoBuyDto {
  private Biglietto biglietto;
  private boolean ridotto;
}

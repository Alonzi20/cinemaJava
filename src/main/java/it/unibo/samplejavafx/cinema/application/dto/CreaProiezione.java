package it.unibo.samplejavafx.cinema.application.dto;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreaProiezione {
  Long filmId;
  Long salaId;
  Date data;
  Long orarioProiezioneId;
}

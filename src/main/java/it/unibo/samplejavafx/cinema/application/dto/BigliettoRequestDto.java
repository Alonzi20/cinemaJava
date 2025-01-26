package it.unibo.samplejavafx.cinema.application.dto;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BigliettoRequestDto {
  private long idProiezione;
  private Map<Long, List<String>> posti;
  private boolean ridotto;
}

package it.unibo.samplejavafx.cinema.models;

import java.util.List;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class Proiezione {
  Film film;
  Sala sala;
  String orario;
  List<Posto> postiPrenotati = List.of();

  public int postiLiberi() {
    return sala.getPosti() - postiPrenotati.size();
  }

  public boolean isPrenotabile() {
    return postiLiberi() > 0;
  }

  // TODO Alex: [17/12/2024] implementare il metodo prenota, questo è solo uno scheletro
  public boolean prenota(@NotNull Posto posto) {
    if (isPrenotabile()) {
      this.postiPrenotati.add(posto);
      return true;
    }
    return false;
  }
}

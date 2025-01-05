package it.unibo.samplejavafx.cinema.services.sala;

import it.unibo.samplejavafx.cinema.application.models.Sala;
import java.util.List;

public interface SalaService {
  Sala findSalaById(Long id);

  List<Sala> findAllSale();

  Sala createSala();
}

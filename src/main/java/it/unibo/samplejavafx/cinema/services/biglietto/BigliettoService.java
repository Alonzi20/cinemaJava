package it.unibo.samplejavafx.cinema.services.biglietto;

import it.unibo.samplejavafx.cinema.application.models.Biglietto;

import java.util.List;

public interface BigliettoService {
  Biglietto findBigliettoById(Long id);

  List<Biglietto> findAllBiglietti();

  Biglietto compra(long idProiezione, long idSala, long idPosto, boolean ridotto);
}

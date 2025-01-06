package it.unibo.samplejavafx.cinema.services.biglietto;

import it.unibo.samplejavafx.cinema.application.models.Biglietto;
import java.util.List;

public interface BigliettoService {
  Biglietto findBigliettoById(Long id);

  List<Biglietto> findAllBiglietti();

  List<Biglietto> findAllBigliettiByClienteId(long idCliente);

  Biglietto compra(long idProiezione, long idPosto, boolean ridotto);
}

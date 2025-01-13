package it.unibo.samplejavafx.cinema.services.biglietto;

import it.unibo.samplejavafx.cinema.application.models.Biglietto;
import java.util.List;

public interface BigliettoService {
  Biglietto findBigliettoById(Long id);

  List<Biglietto> findAllBiglietti();

  List<Biglietto> findAllBigliettiByClienteId(long idCliente);

  Double importoBiglietto(boolean ridotto);

  Biglietto compra(long idProiezione, long numero, String fila, boolean ridotto);
}

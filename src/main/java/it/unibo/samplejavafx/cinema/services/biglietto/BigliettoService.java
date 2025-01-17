package it.unibo.samplejavafx.cinema.services.biglietto;

import it.unibo.samplejavafx.cinema.application.models.Biglietto;
import java.util.List;
import java.util.Map;

public interface BigliettoService {
  Biglietto findBigliettoById(Long id);

  List<Biglietto> findAllBiglietti();

  List<Biglietto> findAllBigliettiByClienteId(long idCliente);

  List<Biglietto> createBiglietti(long idProiezione, Map<Long, String> posti, boolean ridotto);

  Double importoBiglietto(boolean ridotto);

  Biglietto compra(long idProiezione, long numero, String fila, boolean ridotto);
}

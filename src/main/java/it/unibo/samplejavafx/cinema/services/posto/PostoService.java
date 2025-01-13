package it.unibo.samplejavafx.cinema.services.posto;

import it.unibo.samplejavafx.cinema.application.models.Posto;
import java.util.List;

public interface PostoService {
  Posto findPostoById(Long id);

  Posto findPostoByIdAndProiezioneId(Long id, Long proiezioneId);

  List<Posto> findAllPosti();

  void savePosto(Posto posto);

  int postiPrenotatiByProiezioneId(Long idProiezione);

  boolean isPostoPrenotabile(long numero, String fila, long idProiezione);
}

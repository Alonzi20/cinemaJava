package it.unibo.samplejavafx.cinema.services.posto;

import it.unibo.samplejavafx.cinema.application.models.Posto;
import java.util.List;

public interface PostoService {
  Posto findPostoById(Long id);

  List<Posto> findAllPosti();

  Posto createPosto();
}

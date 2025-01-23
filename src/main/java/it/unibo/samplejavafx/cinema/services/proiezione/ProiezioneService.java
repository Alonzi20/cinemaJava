package it.unibo.samplejavafx.cinema.services.proiezione;

import it.unibo.samplejavafx.cinema.application.dto.CreaProiezione;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import java.util.List;
import java.util.Map;

public interface ProiezioneService {
  Proiezione findProiezioneById(Long id);

  List<Proiezione> findAllProiezioni();

  List<Proiezione> findAllProiezioniByFilmId(Long idFilm);

  Proiezione createProiezione(CreaProiezione creaProiezione);

  int quantitaPostiLiberi(long idProiezione, long idSala);

  boolean isSalaPrenotabile(long idProiezione, long idSala);

  boolean isPostoPrenotabile(long numero, String fila, long idProiezione, long idSala);

  Map<String, Long> postiLiberi(long idProiezione, long idSala);

  Long prenota(long numero, String fila, long idProiezione, long idSala, long idCliente);

  List<Proiezione> createProiezioniFromApi();
}

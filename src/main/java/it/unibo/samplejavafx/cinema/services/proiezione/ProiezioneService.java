package it.unibo.samplejavafx.cinema.services.proiezione;

import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import java.util.List;
import java.util.Map;

public interface ProiezioneService {
  Proiezione findProiezioneById(Long id);

  List<Proiezione> findAllProiezioni();

  List<Proiezione> findAllProiezioniByFilmId(Long idFilm);

  Proiezione createProiezione();

  int quantitaPostiLiberi(long idProiezione, long idSala);

  boolean isSalaPrenotabile(long idProiezione, long idSala);

  boolean isPostoPrenotabile(long idPosto, long idProiezione, long idSala);

  Map<String, Long> postiLiberi(long idProiezione, long idSala);

  Long prenota(long idPosto, long idProiezione, long idSala);
}

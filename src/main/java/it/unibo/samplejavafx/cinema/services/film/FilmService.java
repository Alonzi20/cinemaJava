package it.unibo.samplejavafx.cinema.services.film;

import it.unibo.samplejavafx.cinema.application.models.Film;
import java.util.List;

public interface FilmService {
  Film findFilmById(Long id);

  List<Film> findAllFilm();

  Film createFilm();
}

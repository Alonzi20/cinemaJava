package it.unibo.samplejavafx.cinema.services.film;

import it.unibo.samplejavafx.cinema.application.models.Film;
import it.unibo.samplejavafx.cinema.repositories.FilmRepository;
import it.unibo.samplejavafx.cinema.services.exceptions.FilmNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class FilmServiceImpl implements FilmService {
  private final FilmRepository filmRepository;

  @Override
  public Film findFilmById(Long id) {
    return filmRepository
        .findById(id)
        .orElseThrow(() -> new FilmNotFoundException(String.valueOf(id)));
  }

  @Override
  public List<Film> findAllFilm() {
    return filmRepository.findAll();
  }

  @Override
  public Film createFilm() {
    return null;
  }
}

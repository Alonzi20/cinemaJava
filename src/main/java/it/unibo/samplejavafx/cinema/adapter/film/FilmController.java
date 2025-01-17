package it.unibo.samplejavafx.cinema.adapter.film;

import it.unibo.samplejavafx.cinema.application.models.Film;
import it.unibo.samplejavafx.cinema.services.film.FilmService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/film", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class FilmController {
  private final FilmService filmService;

  @GetMapping
  public Film findFilm(@RequestParam Long id) {
    try {
      return filmService.findFilmById(id);
    } catch (Exception e) {
      log.error("Errore: {}", e.getMessage());
      return null;
    }
  }

  @GetMapping("/all")
  public List<Film> findAllFilm() {
    return filmService.findAllFilm();
  }

  @PostMapping
  public Film createFilm() {
    return filmService.createFilm();
  }
}

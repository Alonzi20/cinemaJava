package it.unibo.samplejavafx.cinema.services.proiezione;

import it.unibo.samplejavafx.cinema.application.models.Film;
import it.unibo.samplejavafx.cinema.application.models.Posto;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.application.models.Sala;
import it.unibo.samplejavafx.cinema.repositories.FilmRepository;
import it.unibo.samplejavafx.cinema.repositories.PostoRepository;
import it.unibo.samplejavafx.cinema.repositories.ProiezioneRepository;
import it.unibo.samplejavafx.cinema.services.MovieProjections;
import it.unibo.samplejavafx.cinema.services.exceptions.ProiezioneNotFoundException;
import it.unibo.samplejavafx.cinema.services.posto.PostoService;
import it.unibo.samplejavafx.cinema.services.sala.SalaService;
import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ProiezioneServiceImpl implements ProiezioneService {

  private final ProiezioneRepository proiezioneRepository;
  private final PostoRepository postoRepository;
  private final PostoService postoService;
  private final SalaService salaService;
  private final FilmRepository filmRepository;
  private final FilmRepository salaRepository;

  @Override
  public Proiezione findProiezioneById(Long id) {
    return proiezioneRepository
        .findById(id)
        .orElseThrow(() -> new ProiezioneNotFoundException(String.valueOf(id)));
  }

  @Override
  public List<Proiezione> findAllProiezioni() {
    return proiezioneRepository.findAll();
  }

  @Override
  public List<Proiezione> findAllProiezioniByFilmId(Long idFilm) {
    return proiezioneRepository.findAllByFilmId(idFilm);
  }

  @Override
  public Proiezione createProiezione() {
    return null;
  }

  @Override
  public int quantitaPostiLiberi(long idProiezione, long idSala) {
    Sala sala = salaService.findSalaById(idSala);
    return sala.getPosti() - postoService.postiPrenotatiByProiezioneId(idProiezione);
  }

  @Override
  public boolean isSalaPrenotabile(long idProiezione, long idSala) {
    return quantitaPostiLiberi(idProiezione, idSala) > 0;
  }

  // isSalaPrenotabile controlla solo se la quantità di posti liberi era maggiore di 0
  // isPostoPrenotabile controlla anche se il posto che si vuole prenotare è libero
  @Override
  public boolean isPostoPrenotabile(long numero, String fila, long idProiezione, long idSala) {
    if (!isSalaPrenotabile(idProiezione, idSala)) {
      return false;
    }

    return postoService.isPostoPrenotabile(numero, fila, idProiezione);
  }

  // Ritorna una mappa di posti liberi con chiave "fila" e valore "numero"
  @Override
  public Map<String, Long> postiLiberi(long idProiezione, long idSala) {
    if (isSalaPrenotabile(idProiezione, idSala)) {
      var postiLiberiIds = postoRepository.findAllByProiezione_Id(idProiezione);

      var postiLiberi = new HashMap<String, Long>();
      for (var postoId : postiLiberiIds) {
        postoRepository
            .findById(postoId)
            .ifPresent(posto -> postiLiberi.put(posto.getFila(), posto.getNumero()));
      }
      return postiLiberi;
    } else {
      return null;
    }
  }

  // Commento per Luca:
  // ho messo che ritorna idPosto,
  // perché serve avere le info del posto nel biglietto
  // e per aggiornare i dati nel db ricordati di fare la save (r.96)
  @Override
  public Long prenota(long numero, String fila, long idProiezione, long idSala) {
    if (isPostoPrenotabile(numero, fila, idProiezione, idSala)) {
      Posto posto = new Posto();
      posto.setNumero(numero);
      posto.setFila(fila);
      posto.setProiezione(this.findProiezioneById(idProiezione));
      postoRepository.save(posto);

      if (posto.getProiezione() != null && posto.getProiezione().getId() == idProiezione) {
        return posto.getId();
      }

      throw new ProiezioneNotFoundException(String.valueOf(idProiezione));
    } else {
      return null;
    }
  }

  @Override
  public List<Proiezione> createProiezioniFromApi() {
    MovieProjections movieProjections = new MovieProjections();
    List<Film> films = movieProjections.getWeeklyMovies();
    List<Proiezione> nuoveProiezioni = new ArrayList<>();

    // Recupera tutti gli ID delle sale dal database usando lambda invece di method reference
    List<Long> salaIds =
        salaRepository.findAll().stream()
            .map(sala -> sala.getId()) // Using lambda instead of method reference
            .collect(Collectors.toList());

    // Patterns di orari
    Map<String, List<List<String>>> patterns = new HashMap<>();
    patterns.put(
        "WEEKDAY",
        Arrays.asList(
            Arrays.asList("16:30", "19:00", "21:30"),
            Arrays.asList("17:00", "20:00", "22:30"),
            Arrays.asList("15:45", "18:15", "21:00"),
            Arrays.asList("16:15", "18:45", "21:15"),
            Arrays.asList("17:30", "20:30")));
    patterns.put(
        "WEEKEND",
        Arrays.asList(
            Arrays.asList("15:00", "17:30", "20:00", "22:30"),
            Arrays.asList("14:30", "17:00", "19:30", "22:00"),
            Arrays.asList("15:15", "17:45", "20:15", "22:45"),
            Arrays.asList("14:45", "16:45", "18:45", "20:45", "22:45"),
            Arrays.asList("15:30", "18:00", "20:30", "23:00")));
    patterns.put(
        "WEEKEND_MORNING",
        Arrays.asList(
            Arrays.asList("10:30", "12:45"),
            Arrays.asList("11:00", "13:15"),
            Arrays.asList("10:15", "12:30"),
            Arrays.asList("11:30", "13:45")));

    for (Film film : films) {
      Film filmEsistente = filmRepository.findByTitle(film.getTitle()).orElse(null);
      if (filmEsistente == null) {
          filmEsistente = filmRepository.save(film);
      }

      LocalDate releaseDate = LocalDate.parse(filmEsistente.getReleaseDate());
      DayOfWeek dayOfWeek = releaseDate.getDayOfWeek();

      List<List<String>> orari;
      if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
        orari = patterns.get("WEEKEND");
      } else {
        orari = patterns.get("WEEKDAY");
      }

      if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
        orari.addAll(patterns.get("WEEKEND_MORNING"));
      }

      int salaIndex = 0;
      for (List<String> orariGiorno : orari) {
        for (String orario : orariGiorno) {
          Proiezione proiezione = new Proiezione();
          proiezione.setFilmId(filmEsistente.getId());
          proiezione.setData(Date.valueOf(releaseDate));
          proiezione.setOrario(Time.valueOf(orario + ":00"));

          // Assegna dinamicamente l'ID della sala
          proiezione.setSalaId(salaIds.get(salaIndex));
          salaIndex = (salaIndex + 1) % salaIds.size();

          nuoveProiezioni.add(proiezioneRepository.save(proiezione));
        }
      }
    }

    return nuoveProiezioni;
  }
}

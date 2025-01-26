package it.unibo.samplejavafx.cinema.services.proiezione;

import it.unibo.samplejavafx.cinema.application.dto.CreaProiezione;
import it.unibo.samplejavafx.cinema.application.models.Film;
import it.unibo.samplejavafx.cinema.application.models.Posto;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.application.models.Sala;
import it.unibo.samplejavafx.cinema.repositories.*;
import it.unibo.samplejavafx.cinema.services.MovieProjections;
import it.unibo.samplejavafx.cinema.services.exceptions.ProiezioneNotFoundException;
import it.unibo.samplejavafx.cinema.services.posto.PostoService;
import it.unibo.samplejavafx.cinema.services.sala.SalaService;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import it.unibo.samplejavafx.cinema.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unibo.samplejavafx.cinema.application.models.Film;
import it.unibo.samplejavafx.cinema.application.models.Posto;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.application.models.Sala;
import it.unibo.samplejavafx.cinema.application.models.OrariProiezioni;
import it.unibo.samplejavafx.cinema.services.exceptions.ProiezioneNotFoundException;
import it.unibo.samplejavafx.cinema.services.posto.PostoService;
import it.unibo.samplejavafx.cinema.services.sala.SalaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
  private final SalaRepository salaRepository;
  private final OrariProiezioniRepository orariProiezioniRepository;

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
  public Proiezione createProiezione(CreaProiezione creaProiezione) {
    var orarioProiezione =
        orariProiezioniRepository.findById(creaProiezione.getOrarioProiezioneId()).orElse(null);

    var proiezione =
        Proiezione.builder()
            .filmId(creaProiezione.getFilmId())
            .salaId(creaProiezione.getSalaId())
            .data(creaProiezione.getData())
            .orarioProiezione(orarioProiezione)
            .build();
    return proiezioneRepository.save(proiezione);
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
  public Long prenota(long numero, String fila, long idProiezione, long idSala, long idCliente) {
    if (isPostoPrenotabile(numero, fila, idProiezione, idSala)) {
      Posto posto = new Posto();
      posto.setNumero(numero);
      posto.setFila(fila);
      posto.setProiezione(this.findProiezioneById(idProiezione));
      posto.setClienteId(idCliente);
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
  public List<Proiezione> generateProjections() {
    List<Film> films = filmRepository.findAll();
    if (films.isEmpty()) {
      throw new IllegalStateException("Non ci sono film disponibili nel database per creare proiezioni.");
    }

    List<Proiezione> nuoveProiezioni = new ArrayList<>();
    List<Long> salaIds = salaRepository.findAll().stream()
            .map(Sala::getId)
            .collect(Collectors.toList());

    for (Film film : films) {
      LocalDate startDate = LocalDate.now();

      // Genera proiezioni per i prossimi 7 giorni
      for (int i = 0; i < 7; i++) {
        LocalDate currentDate = startDate.plusDays(i);
        Map<Integer, Set<LocalTime>> salaOccupata = new HashMap<>();

        // Determina il tipo di pattern (WEEKDAY o WEEKEND)
        String patternType = currentDate.getDayOfWeek().getValue() >= 6 ? "WEEKEND" : "WEEKDAY";
        List<OrariProiezioni> orari = orariProiezioniRepository.findByPatternType(patternType);

        // Aggiungi gli orari del mattino per i weekend con una probabilità del 40%
        if (patternType.equals("WEEKEND") && Math.random() < 0.4) {
          orari.addAll(orariProiezioniRepository.findByPatternType("WEEKEND_MORNING"));
        }

        // Crea una proiezione per ogni orario disponibile
        for (OrariProiezioni orario : orari) {
          Optional<Long> availableSala = findAvailableSala(salaIds, salaOccupata,
                  LocalTime.parse(orario.getStartTime().toString()), film.getDuration());

          if (availableSala.isPresent()) {
            Proiezione proiezione = new Proiezione();
            proiezione.setFilmId(film.getId());
            proiezione.setSalaId(availableSala.get());
            proiezione.setData(Date.valueOf(currentDate));
            proiezione.setOrarioProiezione(orario);

            // Salva la proiezione
            Proiezione saved = proiezioneRepository.save(proiezione);
            nuoveProiezioni.add(saved);

            // Genera i posti per la proiezione
            generateAndSavePosti(saved);

            // Aggiorna la mappa di occupazione della sala
            salaOccupata.computeIfAbsent(availableSala.get().intValue(), k -> new HashSet<>())
                    .add(LocalTime.parse(orario.getStartTime().toString()));
          }
        }
      }
    }

    return nuoveProiezioni;
  }


  private Optional<Long> findAvailableSala(List<Long> salaIds, Map<Integer, Set<LocalTime>> salaOccupata,
                                           LocalTime startTime, int duration) {
    return salaIds.stream()
            .filter(salaId -> {
              Set<LocalTime> occupiedTimes = salaOccupata.getOrDefault(salaId.intValue(), new HashSet<>());
              return occupiedTimes.stream().noneMatch(occupied ->
                      !(startTime.plusMinutes(duration).isBefore(occupied) ||
                              occupied.plusMinutes(duration).isBefore(startTime)));
            })
            .findFirst();
  }

  private void generateAndSavePosti(Proiezione proiezione) {
    int numeroFile = 10;
    int postiPerFila = 10;

    for (int fila = 1; fila <= numeroFile; fila++) {
      for (int numero = 1; numero <= postiPerFila; numero++) {
        Posto posto = new Posto();
        posto.setNumero((long) numero);
        posto.setFila(String.valueOf((char) ('A' + fila - 1)));
        posto.setProiezione(proiezione);
        postoRepository.save(posto);
      }
    }
  }

}


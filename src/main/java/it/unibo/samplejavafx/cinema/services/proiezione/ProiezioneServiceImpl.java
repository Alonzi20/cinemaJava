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

import jakarta.annotation.PostConstruct;
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

  @PostConstruct
  public void initializeProjections() {
    try {
      generateProjections();
    } catch (Exception e) {
      log.error("Error initializing projections", e);
    }
  }

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
    @Transactional
    public List<Proiezione> generateProjections() {
        //recupero tutti i film che devono essere processati
        List<Film> allFilms = filmRepository.findAll();
        List<Film> films = allFilms.stream()
                .sorted((f1, f2) -> LocalDate.parse(f2.getReleaseDate())
                        .compareTo(LocalDate.parse(f1.getReleaseDate())))
                .limit(10)
                .collect(Collectors.toList());

        //controlla se esistono già proiezioni per i film selezionati
        List<Long> filmIds = films.stream()
                .map(Film::getId)
                .collect(Collectors.toList());

        long countExistingProiezioni = proiezioneRepository.countByFilmIdIn(filmIds);
        if (countExistingProiezioni > 0) {
            log.info("Proiezioni già presenti per questi film. Skip della generazione.");
            return Collections.emptyList();
        }

        log.info("Nessuna proiezione trovata per i film selezionati, generazione in corso...");

        Map<String, List<OrariProiezioni>> orariCache = new HashMap<>();
        orariCache.put("WEEKDAY", orariProiezioniRepository.findByPatternType("WEEKDAY"));
        orariCache.put("WEEKEND", orariProiezioniRepository.findByPatternType("WEEKEND"));
        orariCache.put("WEEKEND_MORNING", orariProiezioniRepository.findByPatternType("WEEKEND_MORNING"));

        List<Long> salaIds = salaRepository.findAll().stream()
                .map(Sala::getId)
                .collect(Collectors.toList());

        log.info("Initialization: {} films, {} weekday times, {} weekend times",
                films.size(),
                orariCache.get("WEEKDAY").size(),
                orariCache.get("WEEKEND").size());

        List<Proiezione> nuoveProiezioni = new ArrayList<>();
        LocalDate startDate = LocalDate.now();

        List<Proiezione> proiezioniToSave = new ArrayList<>();
        List<Posto> postiToSave = new ArrayList<>();

        for (Film film : films) {
            log.info("Processing film: {}", film.getTitle());

            for (int i = 0; i < 7; i++) {
                LocalDate currentDate = startDate.plusDays(i);
                Map<Integer, Set<LocalTime>> salaOccupata = new HashMap<>();
                Set<LocalTime> orariAssegnati = new HashSet<>();

                String patternType = currentDate.getDayOfWeek().getValue() >= 6 ? "WEEKEND" : "WEEKDAY";
                List<OrariProiezioni> orari = new ArrayList<>(orariCache.get(patternType));

                if (patternType.equals("WEEKEND") && Math.random() < 0.4) {
                    orari.addAll(orariCache.get("WEEKEND_MORNING"));
                }


                for (OrariProiezioni orario : orari) {
                    LocalTime startTime = orario.getStartTime().toLocalTime();

                    if (orariAssegnati.contains(startTime)) {
                        continue;
                    }

                    Optional<Long> availableSala = findAvailableSala(salaIds, salaOccupata,
                            currentDate, startTime, film.getDuration());

                    if (availableSala.isPresent()) {
                        Proiezione proiezione = new Proiezione();
                        proiezione.setFilmId(film.getId());
                        proiezione.setSalaId(availableSala.get());
                        proiezione.setData(Date.valueOf(currentDate));
                        proiezione.setOrarioProiezione(orario);

                        proiezioniToSave.add(proiezione);
                        postiToSave.addAll(createPosti(proiezione));

                        salaOccupata.computeIfAbsent(availableSala.get().intValue(), k -> new HashSet<>())
                                .add(startTime);

                        orariAssegnati.add(startTime);

                        if (proiezioniToSave.size() >= 50) {
                            nuoveProiezioni.addAll(proiezioneRepository.saveAll(proiezioniToSave));
                            postoRepository.saveAll(postiToSave);
                            proiezioniToSave.clear();
                            postiToSave.clear();
                        }
                    }
                }
            }
        }

        if (!proiezioniToSave.isEmpty()) {
            nuoveProiezioni.addAll(proiezioneRepository.saveAll(proiezioniToSave));
            postoRepository.saveAll(postiToSave);
        }

        log.info("Completed: Generated {} projections", nuoveProiezioni.size());
        return nuoveProiezioni;
    }



    private Optional<Long> findAvailableSala(List<Long> salaIds, Map<Integer, Set<LocalTime>> salaOccupata,
                                           LocalDate currentDate, LocalTime startTime, int duration) {
    return salaIds.stream()
            .filter(salaId -> {
              // Check in-memory occupied times
              Set<LocalTime> occupiedTimes = salaOccupata.getOrDefault(salaId.intValue(), new HashSet<>());

              // Check database for existing projections
              List<Proiezione> existingProiezioni = proiezioneRepository.findByDataAndSalaId(
                      Date.valueOf(currentDate), salaId
              );

              boolean isTimeSlotFree = existingProiezioni.stream().noneMatch(p -> {
                LocalTime existingStart = p.getOrarioProiezione().getStartTime().toLocalTime();
                int existingDuration = filmRepository.findById(p.getFilmId()).get().getDuration();
                LocalTime existingEnd = existingStart.plusMinutes(existingDuration);

                return !(startTime.plusMinutes(duration).isBefore(existingStart) ||
                        existingEnd.isBefore(startTime));
              });

              return occupiedTimes.isEmpty() && isTimeSlotFree;
            })
            .findFirst();
  }

  private List<Posto> createPosti(Proiezione proiezione) {
    List<Posto> posti = new ArrayList<>();
    int numeroFile = 10;
    int postiPerFila = 10;

    for (int fila = 1; fila <= numeroFile; fila++) {
        for (int numero = 1; numero <= postiPerFila; numero++) {
            Posto posto = new Posto();
            posto.setNumero((long) numero);
            posto.setFila(String.valueOf((char) ('A' + fila - 1)));
            posto.setProiezione(proiezione);
            posti.add(posto);
        }
    }
    return posti;
}

}


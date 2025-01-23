package it.unibo.samplejavafx.cinema.adapter.proiezione;

import it.unibo.samplejavafx.cinema.application.dto.CreaProiezione;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.services.proiezione.ProiezioneService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/proiezione", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ProiezioneController {
  private final ProiezioneService proiezioneService;

  @GetMapping
  public Proiezione findProiezione(@RequestParam Long id) {
    try {
      return proiezioneService.findProiezioneById(id);
    } catch (Exception e) {
      log.error("Errore: {}", e.getMessage());
      return null;
    }
  }

  @GetMapping("/all")
  public List<Proiezione> findAllProiezioni() {
    return proiezioneService.findAllProiezioni();
  }

  @GetMapping("/all/{idFilm}")
  public List<Proiezione> findAllProiezioniByFilmId(@PathVariable Long idFilm) {
    return proiezioneService.findAllProiezioniByFilmId(idFilm);
  }

  @PostMapping
  public Proiezione createProiezione(@RequestBody CreaProiezione creaProiezione) {
    return proiezioneService.createProiezione(creaProiezione);
  }

  @GetMapping("/prenotabile/sala")
  public boolean isSalaPrenotabile(@RequestParam long idProiezione, @RequestParam long idSala) {
    return proiezioneService.isSalaPrenotabile(idProiezione, idSala);
  }

  @GetMapping("/prenotabile/posto")
  public boolean isPostoPrenotabile(
      @RequestParam long numero,
      @RequestParam String fila,
      @RequestParam long idProiezione,
      @RequestParam long idSala) {
    return proiezioneService.isPostoPrenotabile(numero, fila, idProiezione, idSala);
  }

  @GetMapping("/postiLiberi")
  public Map<String, Long> postiLiberi(@RequestParam long idProiezione, @RequestParam long idSala) {
    return proiezioneService.postiLiberi(idProiezione, idSala);
  }

  @PostMapping("/populate")
  public List<Proiezione> populateWeeklyProiezioni() {
    return proiezioneService.createProiezioniFromApi();
  }
}

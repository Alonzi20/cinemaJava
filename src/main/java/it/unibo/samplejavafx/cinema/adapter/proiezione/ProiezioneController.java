package it.unibo.samplejavafx.cinema.adapter.proiezione;

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

  @PostMapping
  public Proiezione createProiezione() {
    return proiezioneService.createProiezione();
  }

  @GetMapping("/prenotabile")
  public boolean isSalaPrenotabile(@RequestParam long idProiezione, @RequestParam long idSala) {
    return proiezioneService.isSalaPrenotabile(idProiezione, idSala);
  }

  @GetMapping("/prenotabile")
  public boolean isPostoPrenotabile(
      @RequestParam long idPosto, @RequestParam long idProiezione, @RequestParam long idSala) {
    return proiezioneService.isPostoPrenotabile(idPosto, idProiezione, idSala);
  }

  @GetMapping("/postiLiberi")
  public Map<String, Long> postiLiberi(@RequestParam long idProiezione, @RequestParam long idSala) {
    return proiezioneService.postiLiberi(idProiezione, idSala);
  }
}

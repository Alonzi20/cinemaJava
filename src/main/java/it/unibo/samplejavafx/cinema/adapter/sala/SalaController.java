package it.unibo.samplejavafx.cinema.adapter.sala;

import it.unibo.samplejavafx.cinema.application.models.Sala;
import it.unibo.samplejavafx.cinema.services.sala.SalaService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/sala", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class SalaController {
  private final SalaService salaService;

  @GetMapping
  public Sala findSala(@RequestParam Long id) {
    try {
      return salaService.findSalaById(id);
    } catch (Exception e) {
      log.error("Errore: {}", e.getMessage());
      return null;
    }
  }

  @GetMapping("/all")
  public List<Sala> findAllSala() {
    return salaService.findAllSale();
  }

  @PostMapping
  public Sala createSala() {
    return salaService.createSala();
  }
}

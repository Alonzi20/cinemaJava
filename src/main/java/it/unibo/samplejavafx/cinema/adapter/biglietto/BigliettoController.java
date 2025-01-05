package it.unibo.samplejavafx.cinema.adapter.biglietto;

import it.unibo.samplejavafx.cinema.application.models.Biglietto;
import it.unibo.samplejavafx.cinema.services.biglietto.BigliettoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/biglietto", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class BigliettoController {
  private final BigliettoService bigliettoService;

  @GetMapping
  public Biglietto findBiglietto(@RequestParam Long id) {
    try {
      return bigliettoService.findBigliettoById(id);
    } catch (Exception e) {
      log.error("Errore: {}", e.getMessage());
      return null;
    }
  }

  @GetMapping("/all")
  public List<Biglietto> findAllBiglietti() {
    return bigliettoService.findAllBiglietti();
  }

  @PostMapping("/compra")
  public Biglietto compra(
      @RequestParam long idProiezione,
      @RequestParam long idSala,
      @RequestParam long idPosto,
      @RequestParam boolean ridotto) {
    return bigliettoService.compra(idProiezione, idSala, idPosto, ridotto);
  }
}

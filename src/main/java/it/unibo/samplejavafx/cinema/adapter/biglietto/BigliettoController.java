package it.unibo.samplejavafx.cinema.adapter.biglietto;

import it.unibo.samplejavafx.cinema.application.dto.BigliettoBuyDto;
import it.unibo.samplejavafx.cinema.application.dto.BigliettoRequestDto;
import it.unibo.samplejavafx.cinema.application.models.Biglietto;
import it.unibo.samplejavafx.cinema.services.biglietto.BigliettoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

  @GetMapping("/all/{idCliente}")
  public List<Biglietto> findAllBigliettiByClienteId(@PathVariable long idCliente) {
    return bigliettoService.findAllBigliettiByClienteId(idCliente);
  }

  @PostMapping("/create")
  public ResponseEntity<List<Biglietto>> createBiglietti(@RequestBody BigliettoRequestDto request) {
    // Esegui la logica per creare i biglietti usando i dati della richiesta
    List<Biglietto> biglietti =
        bigliettoService.createBiglietti(
            request.getIdProiezione(), request.getPosti(), request.isRidotto());
    return ResponseEntity.ok(biglietti);
  }

  @GetMapping("/importo")
  public Double importoBiglietto(@RequestParam boolean ridotto) {
    return bigliettoService.importoBiglietto(ridotto);
  }

  // TODO Alex: [20/01/2025]
  //  Passare in ingresso Biglietto direttamente e clienteId per acquistare
  @PostMapping("/compra")
  public Biglietto compra(@RequestBody BigliettoBuyDto request) {
    return bigliettoService.compra(request.getBiglietto(), request.isRidotto());
  }
}

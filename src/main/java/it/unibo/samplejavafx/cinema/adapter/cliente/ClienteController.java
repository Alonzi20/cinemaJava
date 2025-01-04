package it.unibo.samplejavafx.cinema.adapter.cliente;

import it.unibo.samplejavafx.cinema.application.models.Cliente;
import it.unibo.samplejavafx.cinema.services.cliente.ClienteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/cliente", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ClienteController {
  private final ClienteService clienteService;

  @GetMapping
  public Cliente findCliente(@RequestParam int id) {
    try {
      return clienteService.findClienteById(id);
    } catch (Exception e) {
      log.error("Errore: {}", e.getMessage());
      return null;
    }
  }

  @GetMapping("/all")
  public List<Cliente> findAllClienti() {
    return clienteService.findAllClienti();
  }

  @PostMapping
  public Cliente createCliente(
      @RequestParam String nome, @RequestParam String cognome, @RequestParam String email) {
    return clienteService.createCliente(nome, cognome, email); // TODO Alex: [20/12/2024] tabelle db
  }
}

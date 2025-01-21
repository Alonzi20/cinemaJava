package it.unibo.samplejavafx.cinema.adapter.cliente;

import it.unibo.samplejavafx.cinema.application.models.Cliente;
import it.unibo.samplejavafx.cinema.services.cliente.ClienteService;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/cliente", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ClienteController {
  private final ClienteService clienteService;
  private final HttpSession httpSession;

  @GetMapping
  public Cliente findCliente(@RequestParam Long id) {
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

  @GetMapping("/login")
  public ResponseEntity<?> logInCliente(@RequestParam String email, @RequestParam String password) {
    try{
      Cliente cliente = clienteService.logInCliente(email, password);
      httpSession.setAttribute("token", cliente);
      return ResponseEntity.ok(cliente);
    }catch(RuntimeException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/signin")
  public ResponseEntity<?> createCliente(@RequestParam String nome, @RequestParam String cognome, @RequestParam String email, @RequestParam String password) {
    try{
      Cliente cliente = clienteService.createCliente(nome, cognome, email, password);
      httpSession.setAttribute("token", cliente);
      return ResponseEntity.ok(cliente);
    }catch(RuntimeException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/logout")
  public ResponseEntity<?> logOutCliente(@RequestParam HttpSession session) {
    clienteService.logOutCliente(session);
    return ResponseEntity.ok("Log Out effettuato con successo!");
  }
  
}
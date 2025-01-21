package it.unibo.samplejavafx.cinema.services.cliente;

import it.unibo.samplejavafx.cinema.application.models.Cliente;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface ClienteService {
  Cliente findClienteById(Long id);

  Cliente findClienteByEmail(String email);

  List<Cliente> findAllClienti();

  Cliente createCliente(String nome, String cognome, String email, String password);

  Cliente logInCliente(String email, String password);

  void logOutCliente(HttpSession session);
}

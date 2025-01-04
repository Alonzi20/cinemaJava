package it.unibo.samplejavafx.cinema.services.cliente;

import it.unibo.samplejavafx.cinema.application.models.Cliente;
import java.util.List;

public interface ClienteService {
  Cliente findClienteById(int id);

  List<Cliente> findAllClienti();

  Cliente createCliente(String nome, String cognome, String email);
}

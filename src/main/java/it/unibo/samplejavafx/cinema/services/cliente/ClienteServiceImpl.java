package it.unibo.samplejavafx.cinema.services.cliente;

import it.unibo.samplejavafx.cinema.application.models.Cliente;
import it.unibo.samplejavafx.cinema.repositories.ClienteRepository;
import it.unibo.samplejavafx.cinema.services.exceptions.ClienteNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

  private final ClienteRepository clienteRepository;

  @Override
  public Cliente findClienteById(Long id) {
    return clienteRepository
        .findById(id)
        .orElseThrow(() -> new ClienteNotFoundException(String.valueOf(id)));
  }

  @Override
  public List<Cliente> findAllClienti() {
    return clienteRepository.findAll();
  }

  @Override
  public Cliente createCliente(String nome, String cognome, String email) {
    Cliente cliente = new Cliente();
    cliente.setNome(nome);
    cliente.setCognome(cognome);
    cliente.setEmail(email);
    return clienteRepository.save(cliente);
  }
}

package it.unibo.samplejavafx.cinema.services.cliente;

import it.unibo.samplejavafx.cinema.application.models.Cliente;
import it.unibo.samplejavafx.cinema.repositories.ClienteRepository;
import it.unibo.samplejavafx.cinema.services.exceptions.ClienteNotFoundException;
import it.unibo.samplejavafx.cinema.services.exceptions.EmailUsed;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

  private final ClienteRepository clienteRepository;
  private final PasswordEncoder pswEncoder;

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
  public Cliente createCliente(String nome, String cognome, String email, String password) {
    if(clienteRepository.findClienteByEmail(email) != null){
      throw new EmailUsed(email);
    }
    Cliente cliente = new Cliente();
    cliente.setNome(nome);
    cliente.setCognome(cognome);
    cliente.setEmail(email);
    cliente.setPassword(pswEncoder.encode(password));
    return clienteRepository.save(cliente);
  }

  @Override
  public Cliente logInCliente(String email, String password){
    Cliente cliente = clienteRepository.findClienteByEmail(email);  

    if(cliente == null || !pswEncoder.matches(password, cliente.getPassword())){
      throw new RuntimeException("Email o password errati");
    }

    return cliente;
  }

  @Override
  public Cliente findClienteByEmail(String email){
    List<Cliente> clienti = clienteRepository.findAll();
    for(Cliente cliente : clienti){
      if(email == cliente.getEmail()){
        return cliente;
      }
    }
    return null;
  }

  @Override
  public void logOutCliente(HttpSession session){
    if(session != null){
      session.invalidate();
    }
  }
}

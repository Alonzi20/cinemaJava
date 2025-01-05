package it.unibo.samplejavafx.cinema.services.sala;

import it.unibo.samplejavafx.cinema.application.models.Sala;
import it.unibo.samplejavafx.cinema.repositories.SalaRepository;
import java.util.List;

import it.unibo.samplejavafx.cinema.services.exceptions.SalaNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class SalaServiceImpl implements SalaService {

  private final SalaRepository salaRepository;

  @Override
  public Sala findSalaById(Long id) {
    return salaRepository
        .findById(id)
        .orElseThrow(() -> new SalaNotFoundException(String.valueOf(id)));
  }

  @Override
  public List<Sala> findAllSale() {
    return salaRepository.findAll();
  }

  @Override
  public Sala createSala() {
    Sala sala = new Sala();
    // TODO Alex: [05/01/2025]
    return salaRepository.save(sala);
  }
}

package it.unibo.samplejavafx.cinema.services.posto;

import it.unibo.samplejavafx.cinema.application.models.Posto;
import it.unibo.samplejavafx.cinema.repositories.PostoRepository;
import it.unibo.samplejavafx.cinema.services.exceptions.PostoNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PostoServiceImpl implements PostoService {

  private final PostoRepository postoRepository;

  @Override
  public Posto findPostoById(Long id) {
    return postoRepository
        .findById(id)
        .orElseThrow(() -> new PostoNotFoundException(String.valueOf(id)));
  }

  @Override
  public List<Posto> findAllPosti() {
    return postoRepository.findAll();
  }

  @Override
  public Posto createPosto() {
    Posto posto = new Posto();
    // TODO Alex: [05/01/2025]
    return postoRepository.save(posto);
  }
}

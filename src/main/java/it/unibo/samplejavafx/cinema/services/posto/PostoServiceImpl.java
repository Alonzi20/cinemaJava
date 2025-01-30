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
  public Posto findPostoByIdAndProiezioneId(Long id, Long proiezioneId) {
    return postoRepository.findByIdAndProiezione_Id(id, proiezioneId);
  }

  @Override
  public List<Posto> findAllPosti() {
    return postoRepository.findAll();
  }

  @Override
  public void savePosto(Posto posto) {
    postoRepository.save(posto);
  }

  @Override
  public int postiPrenotatiByProiezioneId(Long idProiezione) {
    return postoRepository.countByPrenotatoAndProiezione_Id(true, idProiezione);
  }

  @Override
  public boolean isPostoPrenotabile(long numero, String fila, long idProiezione) {
    return postoRepository
        .findAllByNumeroAndFilaAndPrenotatoAndProiezione_Id(numero, fila, true, idProiezione)
        .isEmpty();
  }
}

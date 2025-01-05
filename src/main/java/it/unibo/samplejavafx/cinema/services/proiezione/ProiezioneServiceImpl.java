package it.unibo.samplejavafx.cinema.services.proiezione;

import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.application.models.Sala;
import it.unibo.samplejavafx.cinema.repositories.PostoRepository;
import it.unibo.samplejavafx.cinema.repositories.ProiezioneRepository;
import it.unibo.samplejavafx.cinema.services.exceptions.ProiezioneNotFoundException;
import it.unibo.samplejavafx.cinema.services.sala.SalaService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ProiezioneServiceImpl implements ProiezioneService {

  private final ProiezioneRepository proiezioneRepository;
  private final PostoRepository postoRepository;
  private final SalaService salaService;

  @Override
  public Proiezione findProiezioneById(Long id) {
    return proiezioneRepository
            .findById(id)
            .orElseThrow(() -> new ProiezioneNotFoundException(String.valueOf(id)));
  }

  @Override
  public List<Proiezione> findAllProiezioni() {
    return proiezioneRepository.findAll();
  }

  @Override
  public Proiezione createProiezione() {
    return null;
  }

  @Override
  public int quantitaPostiLiberi(long idProiezione, long idSala) {
    Proiezione proiezione = findProiezioneById(idProiezione);
    Sala sala = salaService.findSalaById(idSala);
    return sala.getPosti() - proiezione.getPostiPrenotatiIds().size();
  }

  @Override
  public boolean isSalaPrenotabile(long idProiezione, long idSala) {
    return quantitaPostiLiberi(idProiezione, idSala) > 0;
  }

  // isSalaPrenotabile controlla solo se la quantità di posti liberi era maggiore di 0
  // isPostoPrenotabile controlla anche se il posto che si vuole prenotare è libero
  @Override
  public boolean isPostoPrenotabile(long idPosto, long idProiezione, long idSala) {
    if (!isSalaPrenotabile(idProiezione, idSala)) {
      return false;
    }

    var proiezione = findProiezioneById(idProiezione);
    return !proiezione.getPostiPrenotatiIds().contains(idPosto);
  }

  // Ritorna una mappa di posti liberi con chiave "fila" e valore "numero"
  @Override
  public Map<String, Long> postiLiberi(long idProiezione, long idSala) {
    if (isSalaPrenotabile(idProiezione, idSala)) {
      var postiLiberiIds = postoRepository.findAllBySalaId(idSala);

      var postiLiberi = new HashMap<String, Long>();
      for (var postoId : postiLiberiIds) {
        postoRepository
            .findById(postoId)
            .ifPresent(posto -> postiLiberi.put(posto.getFila(), posto.getNumero()));
      }
      return postiLiberi;
    } else {
      return null;
    }
  }

  // Commento per Luca:
  // ho messo che ritorna idPosto,
  // perché serve avere le info del posto nel biglietto
  // e per aggiornare i dati nel db ricordati di fare la save (r.96)
  @Override
  public Long prenota(long idPosto, long idProiezione, long idSala) {
    if (isPostoPrenotabile(idPosto, idProiezione, idSala)) {
      Proiezione proiezione = findProiezioneById(idProiezione);
      proiezione.getPostiPrenotatiIds().add(idPosto);
      proiezioneRepository.save(proiezione);

      return idPosto;
    } else {
      return null;
    }
  }
}

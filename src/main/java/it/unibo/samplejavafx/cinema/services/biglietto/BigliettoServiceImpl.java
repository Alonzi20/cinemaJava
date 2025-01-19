package it.unibo.samplejavafx.cinema.services.biglietto;

import it.unibo.samplejavafx.cinema.application.models.Biglietto;
import it.unibo.samplejavafx.cinema.repositories.BigliettoRepository;
import it.unibo.samplejavafx.cinema.services.exceptions.BigliettoNotFoundException;
import it.unibo.samplejavafx.cinema.services.posto.PostoService;
import it.unibo.samplejavafx.cinema.services.proiezione.ProiezioneService;
import java.util.ArrayList;
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
public class BigliettoServiceImpl implements BigliettoService {

  private final BigliettoRepository bigliettoRepository;
  private final PostoService postoService;
  private final ProiezioneService proiezioneService;

  @Override
  public Biglietto findBigliettoById(Long id) {
    return bigliettoRepository
        .findById(id)
        .orElseThrow(() -> new BigliettoNotFoundException(String.valueOf(id)));
  }

  @Override
  public List<Biglietto> findAllBiglietti() {
    return bigliettoRepository.findAll();
  }

  @Override
  public List<Biglietto> findAllBigliettiByClienteId(long idCliente) {
    return bigliettoRepository.findAllByClienteId(idCliente);
  }

  @Override
  public List<Biglietto> createBiglietti(
      long idProiezione, Map<Long, String> posti, boolean ridotto) {
    var biglietti = new ArrayList<Biglietto>();

    for (var entry : posti.entrySet()) {
      var numero = entry.getKey();
      var fila = entry.getValue();
      var biglietto = new Biglietto();
      biglietto.setProiezioneId(idProiezione);
      biglietto.setNumero(numero);
      biglietto.setFila(fila);
      biglietto.setPrezzo(importoBiglietto(ridotto));
      biglietti.add(biglietto);
    }

    return biglietti;
  }

  @Override
  public Double importoBiglietto(boolean ridotto) {
    return ridotto ? Biglietto.PREZZO_RIDOTTO : Biglietto.PREZZO_INTERO;
  }

  // Commento per Luca:
  // non chiedo salaId perché lo recupero dalla Proiezione
  // Per Sala e Posto non serve il controllo che sia null
  // perché è fatto già dentro al metodo di recupero ById.
  // Li recupero per avere le info
  @Override
  public Biglietto compra(long idProiezione, long numero, String fila, boolean ridotto) {
    var proiezione = proiezioneService.findProiezioneById(idProiezione);

    var idPostoPrenotato =
        proiezioneService.prenota(numero, fila, idProiezione, proiezione.getSalaId());
    if (idPostoPrenotato != null) {
      Biglietto biglietto = new Biglietto();
      biglietto.setProiezioneId(idProiezione);
      biglietto.setRidotto(ridotto);
      biglietto.setPrezzo(ridotto ? Biglietto.PREZZO_RIDOTTO : Biglietto.PREZZO_INTERO);

      // INFO POSTO
      var posto = postoService.findPostoById(idPostoPrenotato);
      biglietto.setNumero(posto.getNumero());
      biglietto.setFila(posto.getFila());

      return bigliettoRepository.save(biglietto);
    } else {
      throw new RuntimeException("Biglietto non comprato, la proiezione non è prenotabile");
    }
  }
}

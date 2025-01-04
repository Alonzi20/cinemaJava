package it.unibo.samplejavafx.cinema.services.biglietto;

import it.unibo.samplejavafx.cinema.application.models.Biglietto;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.application.models.Sala;
import it.unibo.samplejavafx.cinema.repositories.BigliettoRepository;
import it.unibo.samplejavafx.cinema.repositories.ProiezioneRepository;
import it.unibo.samplejavafx.cinema.repositories.SalaRepository;
import it.unibo.samplejavafx.cinema.services.proiezione.ProiezioneService;
import it.unibo.samplejavafx.cinema.services.exceptions.ProiezioneNotFoundException;
import it.unibo.samplejavafx.cinema.services.exceptions.SalaNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class BigliettoServiceImpl implements BigliettoService{

    private final BigliettoRepository bigliettoRepository;
    private final SalaRepository salaRepository;
    private final ProiezioneRepository proiezioneRepository;
    private final ProiezioneService proiezioneService;

    @Override
    public Biglietto compra(long idProiezione, long idSala, long idPosto, boolean ridotto){
        Proiezione proiezione = proiezioneRepository
            .findById(idProiezione)
            .orElseThrow(() -> new ProiezioneNotFoundException(String.valueOf(idProiezione)));
        Sala sala = salaRepository
            .findById(idSala)
            .orElseThrow(() -> new SalaNotFoundException(String.valueOf(idSala)));
        if(proiezioneService.prenota(idPosto, proiezione.getId(), sala.getId())){
            Biglietto biglietto = new Biglietto();
            biglietto.setProiezioneId(proiezione.getId());
            biglietto.setRidotto(ridotto);
            return bigliettoRepository.save(biglietto);
        }else{
            throw new RuntimeException("Biglietto non comprato, la proiezione non è prenotabile");
        }
        
    }
    
}

package it.unibo.samplejavafx.cinema.services.proiezione;

import it.unibo.samplejavafx.cinema.application.models.Sala;
import it.unibo.samplejavafx.cinema.application.models.Posto;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.repositories.ProiezioneRepository;
import it.unibo.samplejavafx.cinema.repositories.SalaRepository;
import it.unibo.samplejavafx.cinema.services.exceptions.ProiezioneNotFoundException;
import it.unibo.samplejavafx.cinema.services.exceptions.SalaNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ProiezioneServiceImpl implements ProiezioneService{

    private final ProiezioneRepository proiezioneRepository;
    private final SalaRepository salaRepository;

    @Override
    public int postiLiberi(long idProiezione, long idSala){
        Proiezione proiezione = proiezioneRepository
            .findById(idProiezione)
            .orElseThrow(() -> new ProiezioneNotFoundException(String.valueOf(idProiezione)));
        Sala sala = salaRepository
            .findById(idSala)
            .orElseThrow(() -> new SalaNotFoundException(String.valueOf(idSala)));
        return sala.getPosti() - proiezione.getPostiPrenotatiIds().size();
    }

    @Override
    public boolean isPrenotabile(long idProiezione, long idSala){
        return postiLiberi(idProiezione, idSala) > 0;
    }

    @Override
    public boolean prenota(long idPosto, long idProiezione, long idSala){
        if (isPrenotabile(idProiezione, idSala)){
            Proiezione proiezione = proiezioneRepository
                .findById(idProiezione)
                .orElseThrow(() -> new ProiezioneNotFoundException(String.valueOf(idProiezione)));
            proiezione.getPostiPrenotatiIds().add(idPosto);
            return true;
        }else{
            return false;
        }
    }
    
}

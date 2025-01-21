package it.unibo.samplejavafx.cinema.services.orari_proiezioni;

import it.unibo.samplejavafx.cinema.application.models.OrariProiezioni;
import it.unibo.samplejavafx.cinema.repositories.OrariProiezioniRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class OrariProiezioniServiceImpl implements OrariProiezioniService {
    
    private final OrariProiezioniRepository orariProiezioniRepository;

    @Override
    public List<OrariProiezioni> findAllOrari() {
        return orariProiezioniRepository.findAll();
    }

    @Override
    public List<OrariProiezioni> findOrariByPatternType(String patternType) {
        return orariProiezioniRepository.findByPatternType(patternType);
    }

    @Override
    public OrariProiezioni findOrariById(Long id) {
        return orariProiezioniRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Orario proiezione non trovato con id: " + id));
    }
}

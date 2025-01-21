package it.unibo.samplejavafx.cinema.services.orari_proiezioni;

import it.unibo.samplejavafx.cinema.application.models.OrariProiezioni;
import java.util.List;

public interface OrariProiezioniService {
    List<OrariProiezioni> findAllOrari();
    List<OrariProiezioni> findOrariByPatternType(String patternType);
    OrariProiezioni findOrariById(Long id);
}
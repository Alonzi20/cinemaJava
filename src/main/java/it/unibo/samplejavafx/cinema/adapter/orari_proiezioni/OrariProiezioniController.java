package it.unibo.samplejavafx.cinema.adapter.orari_proiezioni;

import it.unibo.samplejavafx.cinema.application.models.OrariProiezioni;
import it.unibo.samplejavafx.cinema.services.orari_proiezioni.OrariProiezioniService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/orari-proiezioni", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class OrariProiezioniController {

    private final OrariProiezioniService orariProiezioniService;

    @GetMapping
    public List<OrariProiezioni> findAllOrari() {
        return orariProiezioniService.findAllOrari();
    }

    @GetMapping("/pattern/{type}")
    public List<OrariProiezioni> findOrariByPattern(@PathVariable String type) {
        return orariProiezioniService.findOrariByPatternType(type);
    }

    @GetMapping("/{id}")
    public OrariProiezioni findOrariById(@PathVariable Long id) {
        return orariProiezioniService.findOrariById(id);
    }
}
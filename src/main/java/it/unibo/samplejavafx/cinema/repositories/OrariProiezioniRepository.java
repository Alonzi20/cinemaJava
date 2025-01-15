package it.unibo.samplejavafx.cinema.repositories;

import it.unibo.samplejavafx.cinema.application.models.OrariProiezioni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrariProiezioniRepository extends JpaRepository<OrariProiezioni, Long> {
    List<OrariProiezioni> findByPatternType(String patternType);
}
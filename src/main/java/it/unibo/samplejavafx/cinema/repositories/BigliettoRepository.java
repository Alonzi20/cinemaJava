package it.unibo.samplejavafx.cinema.repositories;

import it.unibo.samplejavafx.cinema.application.models.Biglietto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BigliettoRepository extends JpaRepository<Biglietto, Long> {}

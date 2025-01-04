package it.unibo.samplejavafx.cinema.repositories;

import it.unibo.samplejavafx.cinema.application.models.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Integer> {}
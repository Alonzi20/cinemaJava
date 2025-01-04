package it.unibo.samplejavafx.cinema.repositories;

import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProiezioneRepository extends JpaRepository<Proiezione, Integer> {}
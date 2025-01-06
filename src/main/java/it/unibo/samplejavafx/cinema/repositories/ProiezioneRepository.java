package it.unibo.samplejavafx.cinema.repositories;

import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProiezioneRepository extends JpaRepository<Proiezione, Long> {
  List<Proiezione> findAllByFilmId(Long idFilm);
}

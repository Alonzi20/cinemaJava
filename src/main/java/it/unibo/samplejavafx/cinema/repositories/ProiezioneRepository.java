package it.unibo.samplejavafx.cinema.repositories;

import it.unibo.samplejavafx.cinema.application.models.Proiezione;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProiezioneRepository extends JpaRepository<Proiezione, Long> {
  List<Proiezione> findAllByFilmId(Long idFilm);

  boolean existsByDataAndSalaIdAndOrarioProiezione_Id(Date data, Long salaId, Long orarioProiezioneId);

  List<Proiezione> findAllByDataAndOrarioProiezione_StartTime(Date date, Time startTime);

  List<Proiezione> findByDataAndSalaId(Date date, Long salaId);

  long countByFilmIdIn(List<Long> filmIds);

}

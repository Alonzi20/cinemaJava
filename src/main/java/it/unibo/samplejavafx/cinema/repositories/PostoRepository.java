package it.unibo.samplejavafx.cinema.repositories;

import it.unibo.samplejavafx.cinema.application.models.Posto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostoRepository extends JpaRepository<Posto, Long> {

  Posto findByIdAndProiezione_Id(Long id, Long proiezioneId);

  List<Long> findAllByProiezione_Id(Long proiezioneId);

  int countByProiezione_Id(Long proiezioneId);

  List<Posto> findAllByNumeroAndFilaAndProiezione_Id(Long numero, String fila, Long proiezioneId);
}

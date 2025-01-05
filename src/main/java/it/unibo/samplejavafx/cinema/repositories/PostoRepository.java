package it.unibo.samplejavafx.cinema.repositories;

import it.unibo.samplejavafx.cinema.application.models.Posto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostoRepository extends JpaRepository<Posto, Long> {

  List<Long> findAllBySalaId(Long salaId);
}

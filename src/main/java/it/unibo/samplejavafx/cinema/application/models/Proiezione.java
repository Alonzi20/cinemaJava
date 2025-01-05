package it.unibo.samplejavafx.cinema.application.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import lombok.*;

@Entity
@DomainAggregate
@Data
@Builder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Proiezione {
  @Id
  @DomainAggregateId
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  Long filmId;
  Long salaId;
  Date data;
  Time orario;

  @Builder.Default List<Long> postiPrenotatiIds = List.of();
}

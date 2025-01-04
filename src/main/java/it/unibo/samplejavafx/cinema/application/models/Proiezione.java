package it.unibo.samplejavafx.cinema.application.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
  String orario;

  @Builder.Default
  List<Long> postiPrenotatiIds = List.of();

  /*public int postiLiberi() {
    return sala.getPosti() - postiPrenotati.size();
  }

  public boolean isPrenotabile() {
    return postiLiberi() > 0;
  }

  // TODO Alex: [17/12/2024] implementare il metodo prenota, questo è solo uno scheletro
  public boolean prenota(@NotNull Posto posto) {
    if (isPrenotabile()) {
      this.postiPrenotati.add(posto);
      return true;
    }
    return false;
  }*/
}

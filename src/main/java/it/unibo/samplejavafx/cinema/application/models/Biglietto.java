package it.unibo.samplejavafx.cinema.application.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Biglietto {
  private static final double PREZZO_INTERO = 8.0;
  private static final double PREZZO_RIDOTTO = 5.0;

  @Id
  @DomainAggregateId
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  Long proiezioneId;
  @Builder.Default boolean ridotto = false;

  // Posto posto;

  public double prezzo() {
    return ridotto ? PREZZO_RIDOTTO : PREZZO_INTERO;
  }

  // TODO Alex: [17/12/2024]
  /*public void compra() {
    if (proiezione.isPrenotabile()) {}
  }*/
}

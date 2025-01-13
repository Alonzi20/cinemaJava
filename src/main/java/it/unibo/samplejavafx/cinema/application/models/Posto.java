package it.unibo.samplejavafx.cinema.application.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Posto {
  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  Long numero;
  String fila;

  @ManyToOne
  @JoinColumn(name = "proiezione_id", nullable = false) // Collega il posto a una proiezione
  private Proiezione proiezione;
}

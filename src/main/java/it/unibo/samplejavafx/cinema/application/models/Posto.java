package it.unibo.samplejavafx.cinema.application.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
  @JsonIgnoreProperties("posti") // Ignora il lato opposto della relazione, per evitare cicli
  private Proiezione proiezione;

  Long clienteId; // Id del cliente che ha prenotato il posto

  @Builder.Default
  boolean prenotato = false; // Nuovo campo per distinguere i posti prenotati da quelli liberi
}

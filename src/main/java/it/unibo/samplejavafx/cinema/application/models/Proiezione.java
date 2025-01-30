package it.unibo.samplejavafx.cinema.application.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.sql.Date;
import java.util.List;
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
public class Proiezione {
  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  Long filmId;
  Long salaId;
  Date data;

  @OneToMany(mappedBy = "proiezione", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Posto> posti;

  @ManyToOne(
      fetch = FetchType.EAGER) // Per caricare l'entità OrariProiezioni, sennò non la risolveva
  @JoinColumn(name = "orario_proiezione_id")
  @JsonIgnoreProperties("proiezioni") // Ignora il lato opposto della relazione, per evitare cicli
  private OrariProiezioni orarioProiezione;
}

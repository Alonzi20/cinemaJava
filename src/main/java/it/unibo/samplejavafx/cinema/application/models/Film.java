package it.unibo.samplejavafx.cinema.application.models;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Data
@Builder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of", access = AccessLevel.PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Film {
  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  String title;
  String overview;
  String releaseDate;
  String posterPath;
  List<String> genres;
  int duration;
  List<String> cast;
  String director;
  boolean adult;
}

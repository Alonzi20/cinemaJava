package it.unibo.samplejavafx.cinema.application.models;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Time;
import java.util.List;

@Entity
@Data
@Builder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrariProiezioni {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String patternType;

    @Column(nullable = false)
    private Time startTime;

    @OneToMany(mappedBy = "orarioProiezione")
    private List<Proiezione> proiezioni;
}
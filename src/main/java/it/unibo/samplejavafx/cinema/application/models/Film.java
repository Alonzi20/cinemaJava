package it.unibo.samplejavafx.cinema.application.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Film {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String overview;
    private String releaseDate;
    private String posterPath;
    
    @Column(columnDefinition = "TEXT")
    private String genres;
    
    private int duration;
    
    @Column(columnDefinition = "TEXT")
    private String cast;
    
    private String director;
    private boolean adult;

    @Transient
    public List<String> getGenresList() {
        if (this.genres == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(this.genres.split(","));
    }

    @Transient
    public void setGenresList(List<String> genresList) {
        if (genresList != null) {
            this.genres = String.join(",", genresList);
        } else {
            this.genres = "";
        }
    }

    @Transient
    public List<String> getCastList() {
        if (this.cast == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(this.cast.split(","));
    }

    @Transient
    public void setCastList(List<String> castList) {
        if (castList != null) {
            this.cast = String.join(",", castList);
        } else {
            this.cast = "";
        }
    }
}
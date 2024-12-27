package it.unibo.samplejavafx.cinema.models;

import java.util.List;

import lombok.Value;

@Value
public class Film {
    int id;
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
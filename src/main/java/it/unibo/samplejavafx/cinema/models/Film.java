package it.unibo.samplejavafx.cinema.models;
import java.util.List;

public class Film {
    private int id;
    private String title;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private List<String> genres;  
    private int duration;         
    private boolean adult;

    public Film(int id, String title, String overview, String releaseDate, 
                String posterPath, List<String> genres, int duration, boolean adult) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.genres = genres;
        this.duration = duration;
        this.adult = adult;
    }

    public List<String> getGenres() { return genres; }
    public int getDuration() { return duration; }
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getOverview() { return overview; }
    public String getReleaseDate() { return releaseDate; }
    public String getPosterPath() { return posterPath; }
    public boolean getAdult() { return adult; }
}

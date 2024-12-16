package it.unibo.samplejavafx;

import java.util.List;

import it.unibo.samplejavafx.cinema.models.Film;
import it.unibo.samplejavafx.cinema.services.MovieProjections;

public class App {
    public static void main(String[] args) {
        MovieProjections projections = new MovieProjections();
        
        List<Film> weeklyMovies = projections.getWeeklyMovies();
        
        System.out.println("Film della settimana:");
        System.out.println("Numero totale di film: " + weeklyMovies.size());
        
        for (Film movie : weeklyMovies) {
            System.out.println("Titolo: " + movie.getTitle());
            System.out.println("Descrizione: " + movie.getOverview());
            System.out.println("Generi: " + movie.getGenres());
            System.out.println("Durata: " + movie.getDuration() + " minuti");
            System.out.println("Vietato ai minori: " + movie.getAdult());
            System.out.println("Poster: " + movie.getPosterPath());
            System.out.println("----------------------------");
        }
    }
}
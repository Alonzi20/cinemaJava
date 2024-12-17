package it.unibo.samplejavafx;

import it.unibo.samplejavafx.cinema.services.MovieProjections;

public class App {
  public static void main(String[] args) {
    var projections = new MovieProjections();

    var weeklyMovies = projections.getWeeklyMovies();

    System.out.println("Film della settimana:");
    System.out.println("Numero totale di film: " + weeklyMovies.size());

    for (var movie : weeklyMovies) {
      System.out.println("Titolo: " + movie.getTitle());
      System.out.println("Descrizione: " + movie.getOverview());
      System.out.println("Generi: " + movie.getGenres());
      System.out.println("Durata: " + movie.getDuration() + " minuti");
      System.out.println("Vietato ai minori: " + movie.isAdult());
      System.out.println("Poster: " + movie.getPosterPath());
      System.out.println("----------------------------");
    }
  }
}

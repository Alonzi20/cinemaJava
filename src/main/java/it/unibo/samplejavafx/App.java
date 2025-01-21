package it.unibo.samplejavafx;

import it.unibo.samplejavafx.ui.CinemaSchedule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConfigurationProperties({CinemaConfigurationProperties.class})
public class App {
  // public static void main(String[] args) {
  // var projections = new MovieProjections();

  // var weeklyMovies = projections.getWeeklyMovies();

  // System.out.println("Film della settimana:");
  // System.out.println("Numero totale di film: " + weeklyMovies.size());

  // for (var movie : weeklyMovies) {
  // System.out.println("Titolo: " + movie.getTitle());
  // System.out.println("Descrizione: " + movie.getOverview());
  // System.out.println("Generi: " + movie.getGenres());
  // System.out.println("Durata: " + movie.getDuration() + " minuti");
  // System.out.println("Vietato ai minori: " + movie.isAdult());
  // System.out.println("Poster: " + movie.getPosterPath());
  // System.out.println("----------------------------");
  // }
  // }

  public static void main(String[] args) {
      ApplicationContext context = new SpringApplicationBuilder(App.class)
          .headless(false)
          .run(args);
      
      JavaFXApplication.launch(context);
  }
}

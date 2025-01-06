package it.unibo.samplejavafx;

import it.unibo.samplejavafx.ui.CinemaSchedule;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableAsync
@SpringBootApplication(
    scanBasePackages = "it.unibo.samplejavafx.cinema",
    exclude = {DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({CinemaConfigurationProperties.class})
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableJpaRepositories(basePackages = "it.unibo.samplejavafx.cinema.repositories")
@EntityScan(basePackages = "it.unibo.samplejavafx.cinema.application.models")
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
    //SpringApplication.run(App.class, args);

    CinemaSchedule.main(args);
  }
}

package it.unibo.samplejavafx;

import it.unibo.samplejavafx.cinema.services.MovieProjections;
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
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);

    var projections = new MovieProjections();

    var weeklyMovies = projections.getWeeklyMovies();

    log.info("Film della settimana:");
    log.info("Numero totale di film: {}", weeklyMovies.size());

    for (var movie : weeklyMovies) {
      log.info("Titolo: {}", movie.getTitle());
      log.info("Descrizione: {}", movie.getOverview());
      log.info("Generi: {}", movie.getGenres());
      log.info("Durata: {} minuti", movie.getDuration());
      log.info("Vietato ai minori: {}", movie.isAdult());
      log.info("Poster: {}", movie.getPosterPath());
      log.info("----------------------------");
    }
  }
}

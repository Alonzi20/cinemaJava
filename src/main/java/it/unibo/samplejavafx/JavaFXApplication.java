package it.unibo.samplejavafx;

import it.unibo.samplejavafx.cinema.repositories.FilmRepository;
import it.unibo.samplejavafx.cinema.services.orari_proiezioni.OrariProiezioniService;
import it.unibo.samplejavafx.ui.CinemaSchedule;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class JavaFXApplication extends Application {
    
    private static ApplicationContext applicationContext;
    private static OrariProiezioniService orariProiezioniService;
    private static FilmRepository filmRepository;

    public static void launch(ApplicationContext context) {
        JavaFXApplication.applicationContext = context;
        JavaFXApplication.orariProiezioniService = context.getBean(OrariProiezioniService.class);
        JavaFXApplication.filmRepository = context.getBean(FilmRepository.class); // Aggiunta questa riga
        Application.launch(JavaFXApplication.class);
    }

    @Override
    public void start(Stage primaryStage) {
        CinemaSchedule cinemaSchedule = new CinemaSchedule(orariProiezioniService, filmRepository);
        cinemaSchedule.start(primaryStage);
    }

    @Override
    public void stop() {
        Platform.exit();
    }
}
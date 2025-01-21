package it.unibo.samplejavafx;

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

    public static void launch(ApplicationContext context) {
        JavaFXApplication.applicationContext = context;
        JavaFXApplication.orariProiezioniService = context.getBean(OrariProiezioniService.class);
        Application.launch(JavaFXApplication.class);
    }

    @Override
    public void start(Stage primaryStage) {
        CinemaSchedule cinemaSchedule = new CinemaSchedule(orariProiezioniService);
        cinemaSchedule.start(primaryStage);
    }

    @Override
    public void stop() {
        Platform.exit();
    }
}
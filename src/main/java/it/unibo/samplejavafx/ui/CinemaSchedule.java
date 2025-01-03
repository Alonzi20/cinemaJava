package it.unibo.samplejavafx.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import it.unibo.samplejavafx.cinema.models.Film;
import it.unibo.samplejavafx.cinema.services.MovieProjections;
import java.util.List;

public class CinemaSchedule extends Application {
    private static final String[] DAYS = {"GIOVEDÌ", "VENERDÌ", "SABATO", "DOMENICA", "LUNEDÌ", "MARTEDÌ", "MERCOLEDÌ"};
    private final MovieProjections movieService = new MovieProjections();
    private VBox container;

    @Override
    public void start(Stage primaryStage) {
        ScrollPane root = new ScrollPane();
        root.setFitToWidth(true); 
        root.setFitToHeight(true); 
        container = new VBox(10);
        container.setPadding(new Insets(20));
        container.getStyleClass().add("container");
        container.setPrefWidth(Region.USE_COMPUTED_SIZE); 
        container.setMaxWidth(Double.MAX_VALUE);          

        // for (Film movie : movieService.getWeeklyMovies()) {
        //     HBox movieBox = createMovieBox(movie);
        //     movieBox.setMaxWidth(Double.MAX_VALUE); 
        //     movieBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        //     container.getChildren().add(movieBox);
        // }

        MovieSearchInterface searchInterface = new MovieSearchInterface(
            movieService.getWeeklyMovies(),
            this::visualizzaFilmFiltrati
        );
        container.getChildren().add(searchInterface);

        visualizzaFilmFiltrati(movieService.getWeeklyMovies());

        root.setContent(container);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        primaryStage.setMaximized(true); 
        primaryStage.setTitle("Programmazione Cinema");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void visualizzaFilmFiltrati(List<Film> films) {
        // Rimossi tutti gli elementi tranne i componenti di ricerca
        container.getChildren().removeIf(node -> !(node instanceof MovieSearchInterface));
    
        // Controllo se la lista è vuota
        if (films.isEmpty()) {
            Label nessunRisultato = new Label("Nessun risultato");
            nessunRisultato.setId("nessunRisultatoLabel"); // Imposta un ID univoco
            nessunRisultato.getStyleClass().add("nessun-risultato-label");
            
            container.getChildren().removeIf(node -> node.getId() != null && node.getId().equals("nessunRisultatoLabel"));
            container.getChildren().add(nessunRisultato);
        } else {
            // Rimossa vecchia etichetta "Nessun risultato" se presente
            container.getChildren().removeIf(node -> node.getId() != null && node.getId().equals("nessunRisultatoLabel"));
            
            // Aggiunti film trovati
            for (Film film : films) {
                HBox boxFilm = createMovieBox(film);
                boxFilm.setMaxWidth(Double.MAX_VALUE);
                container.getChildren().add(boxFilm);
            }
        }
    }
    
    

    private HBox createMovieBox(Film movie) {
        HBox box = new HBox(15);
        box.setPadding(new Insets(10));
        box.setMaxWidth(Double.MAX_VALUE); 

        // Container per poster e bottone
        VBox posterContainer = new VBox(5);
        ImageView poster = new ImageView(new Image("https://image.tmdb.org/t/p/w154" + movie.getPosterPath()));
        poster.setFitWidth(100);
        poster.setFitHeight(150);

        // Bottone per accedere ai dettagli del film
        Button detailButton = new Button("SCHEDA FILM");
        detailButton.getStyleClass().add("detail-button");
        detailButton.setOnAction(e -> openMovieDetail(movie));

        posterContainer.getChildren().addAll(poster, detailButton);
        // Informazioni film
        VBox info = new VBox(5);
        Label title = new Label(movie.getTitle());
        title.getStyleClass().add("movie-title");
        Label director = new Label("Regia: " + movie.getDirector()); 
        director.getStyleClass().add("movie-info");
        Label genre = new Label("Genere: " + String.join(", ", movie.getGenres()));
        genre.getStyleClass().add("movie-info");
        Label duration = new Label("Durata: " + movie.getDuration() + "'");
        duration.getStyleClass().add("movie-info");
        Label cast = new Label("Cast: " + String.join(", ", movie.getCast()));
        cast.getStyleClass().add("movie-info");
        // Griglia orari
        GridPane schedule = createScheduleGrid(movie);

        info.getChildren().addAll(title, director, genre, duration, cast, schedule);
        box.getChildren().addAll(posterContainer,info);

        return box;
    }

    private GridPane createScheduleGrid(Film movie) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);

        for (int i = 0; i < DAYS.length; i++) {
            Label day = new Label(DAYS[i]);
            day.getStyleClass().add("schedule-grid-label");
            grid.add(day, 0, i);

            // TODO: Aggiungere la logica degli orari
            Label time = new Label("n.d.");
            time.getStyleClass().add("schedule-grid-label");
            grid.add(time, 1, i);
        }

        return grid;
    }

    private void openMovieDetail(Film movie) {
        Stage detailStage = new Stage();
        MovieDetail movieDetail = new MovieDetail(movie);
        movieDetail.start(detailStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

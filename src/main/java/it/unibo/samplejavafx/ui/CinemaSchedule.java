package it.unibo.samplejavafx.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
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

public class CinemaSchedule extends Application {
    private static final String[] DAYS = {"GIOVEDÌ", "VENERDÌ", "SABATO", "DOMENICA", "LUNEDÌ", "MARTEDÌ", "MERCOLEDÌ"};
    private final MovieProjections movieService = new MovieProjections();

    @Override
    public void start(Stage primaryStage) {
        ScrollPane root = new ScrollPane();
        root.setFitToWidth(true); 
        root.setFitToHeight(true); 
        VBox container = new VBox(10);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: #00008B;");
        container.setPrefWidth(Region.USE_COMPUTED_SIZE); 
        container.setMaxWidth(Double.MAX_VALUE);          

        for (Film movie : movieService.getWeeklyMovies()) {
            HBox movieBox = createMovieBox(movie);
            movieBox.setMaxWidth(Double.MAX_VALUE); 
            movieBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
            container.getChildren().add(movieBox);
        }

        root.setContent(container);
        Scene scene = new Scene(root);
        primaryStage.setMaximized(true); 
        primaryStage.setTitle("Programmazione Cinema");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createMovieBox(Film movie) {
        HBox box = new HBox(15);
        box.setPadding(new Insets(10));
        box.setMaxWidth(Double.MAX_VALUE); 

        // Poster del film
        ImageView poster = new ImageView(new Image("https://image.tmdb.org/t/p/w154" + movie.getPosterPath()));
        poster.setFitWidth(100);
        poster.setFitHeight(150);

        // Informazioni film
        VBox info = new VBox(5);
        Label title = new Label(movie.getTitle());
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        Label director = new Label("Regia: " + movie.getDirector()); 
        director.setStyle("-fx-text-fill: white;");
        Label genre = new Label("Genere: " + String.join(", ", movie.getGenres()));
        genre.setStyle("-fx-text-fill: white;");
        Label duration = new Label("Durata: " + movie.getDuration() + "'");
        duration.setStyle("-fx-text-fill: white;");
        Label cast = new Label("Cast: " + String.join(", ", movie.getCast()));
        cast.setStyle("-fx-text-fill: white;");

        // Griglia orari
        GridPane schedule = createScheduleGrid(movie);

        info.getChildren().addAll(title, director, genre, duration, cast, schedule);
        box.getChildren().addAll(poster, info);

        return box;
    }

    private GridPane createScheduleGrid(Film movie) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);

        for (int i = 0; i < DAYS.length; i++) {
            Label day = new Label(DAYS[i]);
            day.setStyle("-fx-text-fill: white;");
            grid.add(day, 0, i);

            // TODO: Aggiungere la logica degli orari
            Label time = new Label("n.d.");
            time.setStyle("-fx-text-fill: white;");
            grid.add(time, 1, i);
        }

        return grid;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package it.unibo.samplejavafx.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import it.unibo.samplejavafx.cinema.models.Film;

public class MovieDetail extends Application {
    private final Film movie;
    
    public MovieDetail(Film movie) {
        this.movie = movie;
    }
    
    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #00008B;");

        Button backButton = new Button("Indietro");
        backButton.setOnAction(e -> stage.close());

        ImageView poster = new ImageView(new Image("https://image.tmdb.org/t/p/w300" + movie.getPosterPath()));
        poster.setFitWidth(200);
        poster.setFitHeight(300);

        Label adultLabel = createLabel("Vietato ai minori: " + (movie.isAdult() ? "Si" : "No")); 
        Label tramaLabel = createLabel("Trama: " + movie.getOverview());
            tramaLabel.setWrapText(true);
            tramaLabel.setMaxWidth(600);   

        root.getChildren().addAll(
            backButton,
            poster,
            createLabel(movie.getTitle(), "-fx-font-size: 24; -fx-font-weight: bold;"),
            createLabel("Regia: " + movie.getDirector()),
            createLabel("Durata: " + movie.getDuration() + "'"),
            createLabel("Genere: " + String.join(", ", movie.getGenres())),
            createLabel("Cast: " + String.join(", ", movie.getCast())),
            createLabel("Anno: " + movie.getReleaseDate()), // TODO: Valutare formato data
            adultLabel,
            tramaLabel
        );

        Scene scene = new Scene(root);
        stage.setTitle("Dettagli Film");
        stage.setScene(scene);
        stage.show();
    }

    private Label createLabel(String text, String... styles) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white;" + String.join(";", styles));
        return label;
    }
}
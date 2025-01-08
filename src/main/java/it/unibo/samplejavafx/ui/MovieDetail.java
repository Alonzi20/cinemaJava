package it.unibo.samplejavafx.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import it.unibo.samplejavafx.ScheduleManager;
import it.unibo.samplejavafx.cinema.models.Film;
import it.unibo.samplejavafx.cinema.services.MovieProjections;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Map;

public class MovieDetail extends Application {
    private final Film movie;
    private final MovieProjections movieService = new MovieProjections();
    private final Map<String, List<String>> scheduleCache;

    public MovieDetail(Film movie) {
        this.movie = movie;
        this.scheduleCache = ScheduleManager.getInstance().getScheduleForMovie(
            movie, 
            movieService.getWeeklyMovies()
        );
    }
    
    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("detail-root");

        Button backButton = new Button("Indietro");
        backButton.setOnAction(e -> stage.close());

        ImageView poster = new ImageView(new Image("https://image.tmdb.org/t/p/w300" + movie.getPosterPath()));
        poster.setFitWidth(200);
        poster.setFitHeight(300);

        Label adultLabel = createLabel("Vietato ai minori: " + (movie.isAdult() ? "Si" : "No"), "detail-label"); 
        Label tramaLabel = createLabel("Trama: " + movie.getOverview(), "detail-label");
        tramaLabel.setWrapText(true);
        tramaLabel.setMaxWidth(600);   

        VBox ticketSection = createTicketPurchaseSection();
        ticketSection.getStyleClass().add("ticket-section");

        root.getChildren().addAll(
            backButton,
            poster,
            createLabel(movie.getTitle(), "detail-title"),
            createLabel("Regia: " + movie.getDirector(), "detail-label"),
            createLabel("Durata: " + movie.getDuration() + "'", "detail-label"),
            createLabel("Genere: " + String.join(", ", movie.getGenres()), "detail-label"),
            createLabel("Cast: " + String.join(", ", movie.getCast()), "detail-label"),
            createLabel("Anno: " + movie.getReleaseDate(), "detail-label"),
            adultLabel,
            tramaLabel,
            ticketSection
        );

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setTitle("Dettagli Film");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createTicketPurchaseSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(20, 0, 0, 0));
        section.setMaxWidth(400);
        section.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Label titleLabel = createLabel("Acquista biglietto", "ticket-section-title");
        
        ComboBox<String> daySelector = new ComboBox<>();
        daySelector.setPromptText("Scegli il GIORNO");
        daySelector.setMaxWidth(300);
        populateDaySelector(daySelector);
        
        ComboBox<String> timeSelector = new ComboBox<>();
        timeSelector.setPromptText("Scegli l'ORA");
        timeSelector.setMaxWidth(300);
        
        daySelector.setOnAction(e -> {
            String selectedDay = daySelector.getValue();
            if (selectedDay != null) {
                updateTimeSelector(timeSelector, selectedDay);
            }
        });
        
        Button purchaseButton = new Button("SCEGLI IL POSTO");
        purchaseButton.getStyleClass().add("purchase-button");
        purchaseButton.setDisable(true);
        
        timeSelector.setOnAction(e -> {
            purchaseButton.setDisable(daySelector.getValue() == null || timeSelector.getValue() == null);
        });
        
        HBox selectors = new HBox(10);
        selectors.getChildren().addAll(daySelector, timeSelector);
        
        section.getChildren().addAll(titleLabel, selectors, purchaseButton);
        return section;
    }
    
    private void populateDaySelector(ComboBox<String> daySelector) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd/MM", Locale.forLanguageTag("it-IT"));
        
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            String formattedDate = date.format(formatter).toUpperCase();
            daySelector.getItems().add(formattedDate);
        }
    }
    
    private void updateTimeSelector(ComboBox<String> timeSelector, String selectedDay) {
        timeSelector.getItems().clear();
        
        // Estrai il giorno della settimana dal formato "GIORNO dd/MM"
        String dayOfWeek = selectedDay.split(" ")[0];
        
        // Usa gli orari dalla cache
        List<String> times = scheduleCache.getOrDefault(dayOfWeek, new ArrayList<>());
        timeSelector.getItems().addAll(times);
    }
    private Label createLabel(String text, String... styleClasses) {
        Label label = new Label(text);
        label.getStyleClass().addAll(styleClasses);
        return label;
    }
}
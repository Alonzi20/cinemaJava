package it.unibo.samplejavafx.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import it.unibo.samplejavafx.ScheduleManager;
import it.unibo.samplejavafx.cinema.models.Film;
import it.unibo.samplejavafx.cinema.services.MovieProjections;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
// import java.util.Random;
// import com.fasterxml.jackson.databind.ObjectMapper;


public class CinemaSchedule extends Application {
    // private static final String[] DAYS = {"GIOVEDÌ", "VENERDÌ", "SABATO", "DOMENICA", "LUNEDÌ", "MARTEDÌ", "MERCOLEDÌ"};
    private final MovieProjections movieService = new MovieProjections();
    private VBox container;
    private final ScheduleManager scheduleManager = ScheduleManager.getInstance();
    // private ScheduleData scheduleData;

    // private static class ScheduleData {
    //     public List<String> DAYS;
    //     public List<List<String>> WEEKDAY_PATTERNS;
    //     public List<List<String>> WEEKEND_PATTERNS;
    //     public List<List<String>> WEEKEND_MORNING_PATTERNS;
    // }

    // private void loadScheduleData() {
    //     try {
    //         ObjectMapper mapper = new ObjectMapper();
    //         scheduleData = mapper.readValue(
    //             getClass().getResourceAsStream("/schedulePatterns.json"), 
    //             ScheduleData.class
    //         );
    //     } catch (Exception e) {
    //         throw new RuntimeException("Errore durante il caricamento dei pattern di programmazione: " + e.getMessage(), e);
    //     }
    // }


    //   // Pattern di orari per giorni feriali
    // private final String[][] WEEKDAY_PATTERNS = {
    //     {"16:30", "19:00", "21:30"},
    //     {"17:00", "20:00", "22:30"},
    //     {"15:45", "18:15", "21:00"},
    //     {"16:15", "18:45", "21:15"},
    //     {"17:30", "20:30"},
    //     {"16:00", "19:30", "22:00"},
    //     {"15:30", "18:00", "20:30", "22:45"},
    //     {"17:15", "19:45", "22:15"}
    // };

    // // Pattern di orari per il weekend (più spettacoli)
    // private final String[][] WEEKEND_PATTERNS = {
    //     {"15:00", "17:30", "20:00", "22:30"},
    //     {"14:30", "17:00", "19:30", "22:00", "00:30"},
    //     {"15:15", "17:45", "20:15", "22:45"},
    //     {"14:45", "16:45", "18:45", "20:45", "22:45"},
    //     {"15:30", "18:00", "20:30", "23:00"},
    //     {"14:15", "16:30", "19:00", "21:30", "00:00"},
    //     {"15:45", "18:15", "20:45", "23:15"}
    // };

    // // Pattern mattutini per il weekend
    // private final String[][] WEEKEND_MORNING_PATTERNS = {
    //     {"10:30", "12:45"},
    //     {"11:00", "13:15"},
    //     {"10:15", "12:30"},
    //     {"11:30", "13:45"}
    // };

    // private Map<String, List<String>> generateScheduleForMovie(int movieIndex) {
    //     Map<String, List<String>> schedule = new HashMap<>();
        
    //     // Genera un seed basato sull'indice del film per mantenere consistenza
    //     Random movieRandom = new Random(movieIndex * 31L);
        
    //     for (int i = 0; i < scheduleData.DAYS.size(); i++) {
    //         String day = scheduleData.DAYS.get(i);
    //         List<String> times = new ArrayList<>();
            
    //         if (day.equals("SABATO") || day.equals("DOMENICA")) {
    //             // 40% di probabilità di aggiungere spettacoli mattutini
    //             if (movieRandom.nextDouble() < 0.4) {
    //                 List<String> morningPattern = scheduleData.WEEKEND_MORNING_PATTERNS.get(movieRandom.nextInt(scheduleData.WEEKEND_MORNING_PATTERNS.size()));
    //                 times.addAll(morningPattern);
    //             }
                
    //             List<String> mainPattern = scheduleData.WEEKEND_PATTERNS.get(movieRandom.nextInt(scheduleData.WEEKEND_PATTERNS.size()));
    //             times.addAll(mainPattern);
    //         }
           
    //         else if (day.equals("VENERDÌ")) {
    //             List<String> pattern = scheduleData.WEEKDAY_PATTERNS.get(movieRandom.nextInt(scheduleData.WEEKDAY_PATTERNS.size()));
    //             times.addAll(pattern);
    //             // 30% di probabilità di aggiungere uno spettacolo notturno
    //             if (movieRandom.nextDouble() < 0.3) {
    //                 times.add("00:15");
    //             }
    //         }
         
    //         else {
    //             List<String> pattern = scheduleData.WEEKDAY_PATTERNS.get(movieRandom.nextInt(scheduleData.WEEKDAY_PATTERNS.size()));
    //             times.addAll(pattern);
    //         }
            
    //         // Ordina gli orari
    //         Collections.sort(times);
    //         schedule.put(day, times);
    //     }
        
    //     return schedule;
    // }

    @Override
    public void start(Stage primaryStage) {
        // loadScheduleData();
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
        
        HBox quickPurchaseSection = createQuickPurchaseSection();
        container.getChildren().add(quickPurchaseSection);

        visualizzaFilmFiltrati(movieService.getWeeklyMovies());

        root.setContent(container);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        primaryStage.setMaximized(true); 
        primaryStage.setTitle("Programmazione Cinema");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createQuickPurchaseSection() {
        HBox purchaseSection = new HBox(10);
        purchaseSection.setPadding(new Insets(20, 0, 0, 0));
        purchaseSection.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        purchaseSection.getStyleClass().add("quick-purchase-section");
        
        Label titleLabel = new Label("ACQUISTA IL BIGLIETTO!");
        titleLabel.getStyleClass().add("quick-purchase-title");
        
        ComboBox<Film> movieSelector = new ComboBox<>();
        movieSelector.setPromptText("Scegli il FILM");
        movieSelector.setPrefWidth(300);
        
        ComboBox<String> daySelector = new ComboBox<>();
        daySelector.setPromptText("Scegli il GIORNO");
        daySelector.setPrefWidth(200);
        daySelector.setDisable(true);
        
        ComboBox<String> timeSelector = new ComboBox<>();
        timeSelector.setPromptText("Scegli l'ORA");
        timeSelector.setPrefWidth(150);
        timeSelector.setDisable(true);
        
        movieSelector.getItems().addAll(movieService.getWeeklyMovies());
        movieSelector.setConverter(new StringConverter<Film>() {
            @Override
            public String toString(Film film) {
                return film != null ? film.getTitle() : "";
            }
            
            @Override
            public Film fromString(String string) {
                return null;
            }
        });
        
        movieSelector.setOnAction(e -> {
            Film selectedMovie = movieSelector.getValue();
            daySelector.getItems().clear();
            timeSelector.getItems().clear();
            
            if (selectedMovie != null) {
                daySelector.setDisable(false);
                populateDaySelector(daySelector);
            } else {
                daySelector.setDisable(true);
                timeSelector.setDisable(true);
            }
        });
        
        daySelector.setOnAction(e -> {
            String selectedDay = daySelector.getValue();
            timeSelector.getItems().clear();
            
            if (selectedDay != null && movieSelector.getValue() != null) {
                timeSelector.setDisable(false);
                updateTimeSelector(timeSelector, selectedDay, movieSelector.getValue());
            } else {
                timeSelector.setDisable(true);
            }
        });
        
        Button purchaseButton = new Button("SCEGLI IL POSTO");
        purchaseButton.getStyleClass().add("purchase-button");
        purchaseButton.setDisable(true);
        
        timeSelector.setOnAction(e -> {
            purchaseButton.setDisable(movieSelector.getValue() == null || 
                                    daySelector.getValue() == null || 
                                    timeSelector.getValue() == null);
        });
        
        purchaseSection.getChildren().addAll(titleLabel, movieSelector, daySelector, 
                                           timeSelector, purchaseButton);
        
        return purchaseSection;
    }

    private void populateDaySelector(ComboBox<String> daySelector) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd/MM", 
                                    Locale.forLanguageTag("it-IT"));
        
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            String formattedDate = date.format(formatter).toUpperCase();
            daySelector.getItems().add(formattedDate);
        }
    }

    private void updateTimeSelector(ComboBox<String> timeSelector, String selectedDay, Film movie) {
        timeSelector.getItems().clear();
        
        String dayOfWeek = selectedDay.split(" ")[0];
        Map<String, List<String>> movieSchedule = scheduleManager.getScheduleForMovie(
            movie, 
            movieService.getWeeklyMovies()
        );
        
        List<String> times = movieSchedule.getOrDefault(dayOfWeek, new ArrayList<>());
        timeSelector.getItems().addAll(times);
    }

    private void visualizzaFilmFiltrati(List<Film> films) {
        // Rimuovi tutti gli elementi tranne l'interfaccia di ricerca e la sezione di acquisto rapido
        container.getChildren().removeIf(node -> 
            !(node instanceof MovieSearchInterface) && 
            !(node instanceof HBox && ((HBox)node).getChildren().size() > 0 && 
              ((HBox)node).getChildren().get(0) instanceof Label && 
              ((Label)((HBox)node).getChildren().get(0)).getText().equals("ACQUISTA IL BIGLIETTO!"))
        );
    
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
        box.getChildren().addAll(posterContainer, info);

        return box;
    }

    private GridPane createScheduleGrid(Film movie) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);

        Map<String, List<String>> movieSchedule = scheduleManager.getScheduleForMovie(
            movie, 
            movieService.getWeeklyMovies()
        );

        List<String> days = scheduleManager.getDays();
        for (int i = 0; i < days.size(); i++) {
            String day = days.get(i);
            Label dayLabel = new Label(day);
            dayLabel.getStyleClass().add("schedule-grid-label");
            grid.add(dayLabel, 0, i);

            String times = String.join(" ", movieSchedule.get(day));
            Label timeLabel = new Label(times);
            timeLabel.getStyleClass().add("schedule-grid-label");
            grid.add(timeLabel, 1, i);
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

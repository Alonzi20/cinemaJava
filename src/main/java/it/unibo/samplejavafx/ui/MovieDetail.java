package it.unibo.samplejavafx.ui;

import it.unibo.samplejavafx.ScheduleManager;
import it.unibo.samplejavafx.cinema.application.models.Film;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.repositories.FilmRepository;
import it.unibo.samplejavafx.cinema.services.BffService;
import it.unibo.samplejavafx.cinema.services.MovieProjections;
import it.unibo.samplejavafx.cinema.services.orari_proiezioni.OrariProiezioniService;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MovieDetail extends Application {
  private final Film movie;
  //private final Map<String, List<String>> scheduleCache;
  private final BffService bffService;
  private final OrariProiezioniService orariProiezioniService;
  //private final MovieProjections movieService;

  public MovieDetail(Film movie, OrariProiezioniService orariProiezioniService, FilmRepository filmRepository) {
    this.movie = movie;
    this.orariProiezioniService = orariProiezioniService;
    this.bffService = new BffService();
  }
  @Override
  public void start(Stage stage) {
    VBox root = new VBox(10);
    root.setPadding(new Insets(20));
    root.getStyleClass().add("detail-root");

      Button backButton = new Button("← Indietro");
      backButton.getStyleClass().add("detail-button");
    backButton.setOnAction(e -> stage.close());

    ImageView poster =
        new ImageView(new Image("https://image.tmdb.org/t/p/w300" + movie.getPosterPath()));
    poster.setFitWidth(200);
    poster.setFitHeight(300);

    Label adultLabel =
        createLabel("Vietato ai minori: " + (movie.isAdult() ? "Si" : "No"), "detail-label");
    Label tramaLabel = createLabel("Trama: " + movie.getOverview(), "detail-label");
    tramaLabel.setWrapText(true);
    tramaLabel.setMaxWidth(600);

    VBox ticketSection = createTicketPurchaseSection();
    ticketSection.getStyleClass().add("ticket-section");

    root.getChildren()
        .addAll(
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
            ticketSection);

    Scene scene = new Scene(root);
    scene
        .getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
    stage.setTitle("Dettagli Film");
    stage.setScene(scene);
    stage.show();
  }

  private VBox createTicketPurchaseSection() {
    VBox section = new VBox(10);
    section.setPadding(new Insets(20, 0, 0, 0));
    section.setMaxWidth(400);
    section.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
    section.getStyleClass().add("detail-ticket-section");

    Label titleLabel = new Label("ACQUISTA IL BIGLIETTO!");
    titleLabel.getStyleClass().add("detail-ticket-title");

    ComboBox<String> daySelector = new ComboBox<>();
    daySelector.setPromptText("Scegli il GIORNO");
    daySelector.setMaxWidth(300);
    daySelector.getStyleClass().add("quick-purchase-combo-box");
    populateDaySelector(daySelector);

    ComboBox<String> timeSelector = new ComboBox<>();
    timeSelector.setPromptText("Scegli l'ORA");
    timeSelector.setMaxWidth(300);
    timeSelector.getStyleClass().add("quick-purchase-combo-box");

    daySelector.setOnAction(
        e -> {
          String selectedDay = daySelector.getValue();
          if (selectedDay != null) {
            updateTimeSelector(timeSelector, selectedDay);
          }
        });

    Button purchaseButton = new Button("SCEGLI IL POSTO");
    purchaseButton.getStyleClass().add("quick-purchase-button");
    purchaseButton.setDisable(true);

    timeSelector.setOnAction(
        e ->
            purchaseButton.setDisable(
                daySelector.getValue() == null || timeSelector.getValue() == null));

    purchaseButton.setOnAction(e -> {
      try {
         
          String selectedDay = daySelector.getValue();
          String selectedTime = timeSelector.getValue();
  
         
          String[] parts = selectedDay.split(" ")[1].split("/");
          int day = Integer.parseInt(parts[0]);
          int month = Integer.parseInt(parts[1]);
          int year = LocalDate.now().getYear();
  
          LocalDate selectedDate = LocalDate.of(year, month, day);
          LocalTime time = LocalTime.parse(selectedTime);
          
          List<Proiezione> proiezioni = bffService.findAllProiezioniByFilmId(movie.getId());
          
          Proiezione proiezione = proiezioni.stream()
              .filter(p -> {
                  LocalDate proiezioneDate = p.getData().toLocalDate();
                  LocalTime proiezioneTime = p.getOrarioProiezione().getStartTime().toLocalTime();
                  return proiezioneDate.equals(selectedDate) && proiezioneTime.equals(time);
              })
              .findFirst()
              .orElseThrow(() -> new RuntimeException("Nessuna proiezione trovata per la data e ora selezionate"));
              
          new BuyTicket(proiezione).start(new Stage());
          
      } catch (Exception ex) {
          ex.printStackTrace();
      }
  });

    HBox selectors = new HBox(10);
    selectors.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
    selectors.getChildren().addAll(daySelector, timeSelector);

    section.getChildren().addAll(titleLabel, selectors, purchaseButton);
    return section;
  }

  private void populateDaySelector(ComboBox<String> daySelector) {
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("EEEE dd/MM", Locale.forLanguageTag("it-IT"));

    for (int i = 0; i < 7; i++) {
      LocalDate date = today.plusDays(i);
      String formattedDate = date.format(formatter).toUpperCase();
      daySelector.getItems().add(formattedDate);
    }
  }

  private void updateTimeSelector(ComboBox<String> timeSelector, String selectedDay) {
    timeSelector.getItems().clear();

    try {
        // Estrai la data dal formato "GIORNO dd/MM"
        String[] parts = selectedDay.split(" ")[1].split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = LocalDate.now().getYear();
        LocalDate selectedDate = LocalDate.of(year, month, day);

        // Ottieni le proiezioni per il film e la data selezionata
        List<Proiezione> proiezioni = bffService.findAllProiezioniByFilmId(movie.getId());
        List<String> orari = proiezioni.stream()
            .filter(p -> p.getData().toLocalDate().equals(selectedDate))
            .map(p -> p.getOrarioProiezione().getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
            .sorted()
            .collect(Collectors.toList());

        timeSelector.getItems().addAll(orari);
    } catch (Exception e) {
        System.err.println("Errore nel recupero delle proiezioni: " + e.getMessage());
        // Opzionalmente, mostra un messaggio all'utente
        timeSelector.setPromptText("Orari non disponibili");
    }
}

  private Label createLabel(String text, String... styleClasses) {
    Label label = new Label(text);
    label.getStyleClass().addAll(styleClasses);
    return label;
  }
}

package it.unibo.samplejavafx.ui;

import static it.unibo.samplejavafx.cinema.application.models.Biglietto.PREZZO_INTERO;
import static it.unibo.samplejavafx.cinema.application.models.Biglietto.PREZZO_RIDOTTO;

import it.unibo.samplejavafx.cinema.application.models.Biglietto;
import it.unibo.samplejavafx.cinema.application.models.Film;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.application.models.Sala;
import it.unibo.samplejavafx.cinema.services.BffService;
import java.util.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuyTicket extends Application {
  private static final String INTERO = "Intero";
  private static final String RIDOTTO = "Ridotto";
  private static final Logger log = LoggerFactory.getLogger(BuyTicket.class);

  private final BffService bffService = new BffService();
  private final Proiezione proiezione;
  private final Film movie;
  private final Sala sala;
  private final List<Biglietto> biglietti = new ArrayList<>();
  private final Label totalLabel = new Label("Totale: 0€");
  private final SeatSelection seatSelection;

  public BuyTicket(Proiezione proiezione) {
    this.proiezione = proiezione;
    this.seatSelection = new SeatSelection(proiezione.getId(), proiezione.getSalaId());
    try {
      this.movie = bffService.findByFilmId(proiezione.getFilmId());
      this.sala = bffService.findBySalaId(proiezione.getSalaId());
    } catch (Exception e) {
      log.error("Errore durante il recupero dei dati: {}", e.getMessage());
      throw new RuntimeException("Errore durante il recupero dei dati");
    }
  }

  @Override
  public void start(Stage stage) {
    VBox root = new VBox(20); // Aumentato lo spacing tra gli elementi
    root.setPadding(new Insets(20));
    root.getStyleClass().add("detail-root");

    // Header section con bottone indietro
    Button backButton = new Button("← Indietro");
    backButton.getStyleClass().add("detail-button");
    backButton.setOnAction(e -> stage.close());

    // Movie info section
    VBox movieInfoSection = new VBox(10);
    movieInfoSection.getStyleClass().add("movie-info-section");
    movieInfoSection.setPadding(new Insets(15));

    // Titolo principale
    Label titleLabel = createLabel(movie.getTitle(), "detail-title");

    // Container per info film in griglia
    GridPane movieDetailsGrid = new GridPane();
    movieDetailsGrid.setHgap(20);
    movieDetailsGrid.setVgap(10);
    movieDetailsGrid.setPadding(new Insets(15, 0, 15, 0));

    // Aggiunta info film con etichette più descrittive
    movieDetailsGrid.add(createLabel("Genere:", "detail-label", "label-header"), 0, 0);
    movieDetailsGrid.add(createLabel(movie.getGenres(), "detail-label", "label-content"), 1, 0);

    movieDetailsGrid.add(createLabel("Sala:", "detail-label", "label-header"), 0, 1);
    movieDetailsGrid.add(createLabel(String.valueOf(sala.getNumero()), "detail-label", "label-content"), 1, 1);

    movieDetailsGrid.add(createLabel("Restrizioni:", "detail-label", "label-header"), 0, 2);
    movieDetailsGrid.add(createLabel(movie.isAdult() ? "Vietato ai minori" : "Per tutti", "detail-label", "label-content"), 1, 2);

    // Assembla la sezione info film
    movieInfoSection.getChildren().addAll(titleLabel, movieDetailsGrid);

    // Poster section (se lo vuoi includere)
    if (movie.getPosterPath() != null && !movie.getPosterPath().isEmpty()) {
      ImageView poster = new ImageView(new Image("https://image.tmdb.org/t/p/w300" + movie.getPosterPath()));
      poster.setFitWidth(100);
      poster.setFitHeight(150);
      poster.getStyleClass().add("detail-poster");
      movieInfoSection.getChildren().add(0, poster); // Aggiungi il poster all'inizio della sezione
    }

    // Seat selection section
    VBox seatSelectionPane = seatSelection.createSeatSelectionPane();
    seatSelectionPane.getStyleClass().add("seat-selection-section");

    // Dynamic content for tickets
    VBox dynamicContent = new VBox(10);
    dynamicContent.getStyleClass().add("tickets-section");
    seatSelection.setOnConfirm(() -> this.handleSeatSelection(dynamicContent));

    // Assembla tutto
    root.getChildren().addAll(
            backButton,
            movieInfoSection,
            seatSelectionPane,
            dynamicContent
    );

    // ScrollPane setup
    ScrollPane scrollPane = new ScrollPane(root);
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefHeight(javafx.stage.Screen.getPrimary().getVisualBounds().getHeight() - 35);
    scrollPane.setFitToHeight(false);
    scrollPane.getStyleClass().add("detail-scroll-pane");

    Scene scene = new Scene(scrollPane);
    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
    stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icontheme.png")));
    stage.setTitle("Acquista Biglietti");
    stage.setScene(scene);
    stage.show();
  }
  private void handleSeatSelection(VBox dynamicContent) {
    Map<Long, String> posti = seatSelection.getSelectedSeats();
    try {
      dynamicContent.getChildren().clear();

      Map<Long, List<String>> postiConListe = new HashMap<>();

      for (Map.Entry<Long, String> entry : posti.entrySet()) {
        Long colonna = entry.getKey();
        String righeSelezionate = entry.getValue();
        List<String> righe = List.of(righeSelezionate.split(","));
        postiConListe.put(colonna, righe);
      }

      List<Biglietto> createdBiglietti =
              bffService.createBiglietti(proiezione.getId(), postiConListe, false);
      biglietti.clear();
      biglietti.addAll(createdBiglietti);

      if (!biglietti.isEmpty()) {
        Label bigliettiLabel = createLabel("Biglietti");
        dynamicContent.getChildren().add(bigliettiLabel);

        for (Biglietto biglietto : biglietti) {
          dynamicContent.getChildren().add(createSelezione(biglietto));
        }

        Button buyButton = new Button("COMPRA");
        buyButton.getStyleClass().add("quick-purchase-button");
        buyButton.setOnAction(e -> buyTickets());
        dynamicContent.getChildren().add(totalLabel);
        dynamicContent.getChildren().add(buyButton);

        // Aggiungi questo codice per scrollare automaticamente
        Platform.runLater(() -> {
          ScrollPane scrollPane = (ScrollPane) dynamicContent.getScene().getRoot();
          scrollPane.setVvalue(1.0); // Scrolla alla fine
        });
      }
    } catch (Exception e) {
      new Alert(
              Alert.AlertType.ERROR,
              "Errore durante la creazione dei biglietti: " + e.getMessage(),
              ButtonType.OK)
              .showAndWait();
    }
  }
  private Label createLabel(String text, String... styleClasses) {
    Label label = new Label(text);
    label.getStyleClass().addAll(styleClasses);
    return label;
  }

  private VBox createSelezione(Biglietto biglietto) {
    VBox tipoBiglietto = new VBox();
    if (!movie.isAdult() && biglietto != null) {
      Label bigliettoLabel =
          createLabel("Fila: " + biglietto.getFila() + " Numero: " + biglietto.getNumero());
      ComboBox<String> comboBox = new ComboBox<>();
      comboBox.getItems().addAll(INTERO, RIDOTTO);

      // imposta "Intero" come default
      comboBox.getSelectionModel().select(biglietto.isRidotto() ? RIDOTTO : INTERO);

      comboBox.setOnAction(
          event -> {
            String selezione = comboBox.getSelectionModel().getSelectedItem();
            if (Objects.equals(selezione, INTERO)) {
              biglietto.setRidotto(false);
              biglietto.setPrezzo(PREZZO_INTERO);
            } else if (Objects.equals(selezione, RIDOTTO)) {
              biglietto.setRidotto(true);
              biglietto.setPrezzo(PREZZO_RIDOTTO);
            }
            updateTotal();
          });

      updateTotal();

      tipoBiglietto = new VBox(5);
      tipoBiglietto.getChildren().addAll(bigliettoLabel, comboBox);
    }

    return tipoBiglietto;
  }

  private void updateTotal() {
    double totale = 0;
    for (Biglietto biglietto : biglietti) {
      totale = totale + (biglietto.getPrezzo() != null ? biglietto.getPrezzo() : 0);
    }
    totalLabel.setText("Totale: " + totale + "€");
  }

  private void buyTickets() {
    try {
      if (biglietti.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText("Nessun biglietto selezionato");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("custom-alert");
        dialogPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        alert.showAndWait();
        return;
      }

      for (Biglietto biglietto : biglietti) {
        // TODO Alex: [23/01/2025] settare idCliente al biglietto per l'acquisto
        biglietto.setClienteId(1L);
        bffService.compraBiglietto(biglietto, biglietto.isRidotto());
      }

      Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
      successAlert.setTitle("Successo");
      successAlert.setHeaderText(null);
      successAlert.setContentText("Biglietti acquistati con successo");

      DialogPane successPane = successAlert.getDialogPane();
      successPane.getStyleClass().add("custom-alert");
      successPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

      successAlert.showAndWait();
      closePage();
    } catch (Exception e) {
      Alert errorAlert = new Alert(Alert.AlertType.ERROR);
      errorAlert.setTitle("Errore");
      errorAlert.setHeaderText(null);
      errorAlert.setContentText("Errore durante l'acquisto dei biglietti: " + e.getMessage());


      DialogPane errorPane = errorAlert.getDialogPane();
      errorPane.getStyleClass().add("custom-alert");
      errorPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

      errorAlert.showAndWait();
    }
  }

  private void closePage() {
    // Questo chiuderà la finestra attuale (acquisto biglietti)
    Stage stage = (Stage) totalLabel.getScene().getWindow();
    stage.close();
  }
}

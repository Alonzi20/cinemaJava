package it.unibo.samplejavafx.ui;

import it.unibo.samplejavafx.cinema.application.models.Biglietto;
import it.unibo.samplejavafx.cinema.application.models.Film;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.application.models.Sala;
import it.unibo.samplejavafx.cinema.services.BffService;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BuyTicket extends Application {
  private static final String INTERO = "Intero";
  private static final String RIDOTTO = "Ridotto";

  private final BffService bffService = new BffService();
  private final Proiezione proiezione;
  private final Film movie;
  private final Sala sala;
  private List<Biglietto> biglietti;
  private final Label totalLabel = new Label("Totale: 0€");
  private final SeatSelection seatSelection;

  public BuyTicket(Proiezione proiezione) {
    this.proiezione = proiezione;
    this.seatSelection = new SeatSelection(proiezione.getId(), proiezione.getSalaId());
    try {
      this.movie = bffService.findByFilmId(proiezione.getFilmId());
      this.sala = bffService.findBySalaId(proiezione.getSalaId());
    } catch (Exception e) {
      throw new RuntimeException("Errore durante il recupero dei dati");
    }
  }

  @Override
  public void start(Stage stage) {
    VBox root = new VBox(10);
    root.setPadding(new Insets(20));

    Button backButton = new Button("Indietro");
    backButton.setOnAction(e -> stage.close());

    ImageView poster =
        new ImageView(new Image("https://image.tmdb.org/t/p/w300" + movie.getPosterPath()));
    poster.setFitWidth(200);
    poster.setFitHeight(300);

    Label adultLabel = createLabel("Vietato ai minori: " + (movie.isAdult() ? "Si" : "No"));

    // Aggiungi il pannello di selezione posti
    VBox seatSelectionPane = seatSelection.createSeatSelectionPane();
    seatSelection.setOnConfirm(() -> handleSeatSelection());

    root.getChildren()
        .addAll(
            backButton,
            poster,
            createLabel("Film: " + movie.getTitle()),
            createLabel("Genere: " + movie.getGenres()),
            createLabel("Sala " + sala.getNumero()),
            adultLabel,
            seatSelectionPane // Aggiunge scelta posti
            );

    root.getChildren().add(createLabel("Biglietti"));

    for (Biglietto biglietto : biglietti) {
      root.getChildren().add(createSelezione(biglietto));
    }

    root.getChildren().add(totalLabel);

    Scene scene = new Scene(root);
    scene
        .getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
    stage.setTitle("Acquista Biglietti");
    stage.setScene(scene);
    stage.show();
  }

  private void handleSeatSelection() {
    Map<Long, String> posti = seatSelection.getSelectedSeats();
    try {
      List<Biglietto> createdBiglietti =
          bffService.createBiglietti(proiezione.getId(), posti, false);
      biglietti.addAll(createdBiglietti);
      updateTotal();
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
    Label bigliettoLabel = createLabel(biglietto.getFila() + biglietto.getNumero());
    ComboBox<String> comboBox = new ComboBox<>();
    comboBox.getItems().addAll(INTERO, RIDOTTO);

    // imposta "Intero" come default
    comboBox.getSelectionModel().select(INTERO);

    comboBox.setOnAction(
        event -> {
          String selezione = comboBox.getSelectionModel().getSelectedItem();
          if (Objects.equals(selezione, INTERO)) {
            biglietto.setRidotto(false);
          } else if (Objects.equals(selezione, RIDOTTO)) {
            biglietto.setRidotto(true);
          }
          updateTotal();
        });

    VBox tipoBiglietto = new VBox(5);
    tipoBiglietto.getChildren().addAll(bigliettoLabel, comboBox);
    return tipoBiglietto;
  }

  private void updateTotal() {
    double totale = 0;
    for (Biglietto biglietto : biglietti) {
      totale = totale + (biglietto.getPrezzo() != null ? biglietto.getPrezzo() : 0);
    }
    totalLabel.setText("Totale: " + totale + "€");
  }
}

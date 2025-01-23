package it.unibo.samplejavafx.ui;

import static it.unibo.samplejavafx.cinema.application.models.Biglietto.PREZZO_INTERO;
import static it.unibo.samplejavafx.cinema.application.models.Biglietto.PREZZO_RIDOTTO;

import it.unibo.samplejavafx.cinema.application.models.Biglietto;
import it.unibo.samplejavafx.cinema.application.models.Film;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.application.models.Sala;
import it.unibo.samplejavafx.cinema.services.BffService;
import java.util.ArrayList;
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
    VBox dynamicContent = new VBox(10); // Contenitore per i biglietti
    seatSelection.setOnConfirm(() -> this.handleSeatSelection(dynamicContent));

    root.getChildren()
        .addAll(
            backButton,
            /*poster,*/
            createLabel("Film: " + movie.getTitle()),
            createLabel("Genere: " + movie.getGenres()),
            createLabel("Sala " + sala.getNumero()),
            adultLabel,
            seatSelectionPane // Aggiunge scelta posti
            );

    root.getChildren().add(createLabel("Biglietti"));

    if (!biglietti.isEmpty()) {
      for (Biglietto biglietto : biglietti) {
        root.getChildren().add(createSelezione(biglietto));
      }
    }

    root.getChildren().add(totalLabel);
    root.getChildren().add(dynamicContent);

    // Inserisci VBox in un ScrollPane
    ScrollPane scrollPane = new ScrollPane(root);
    scrollPane.setFitToWidth(true); // Permette al contenuto di adattarsi alla larghezza
    scrollPane.setFitToHeight(true); // Lascia che l'altezza venga determinata dal contenuto

    Scene scene = new Scene(scrollPane);
    scene
        .getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
    stage.setTitle("Acquista Biglietti");
    stage.setScene(scene);
    stage.show();
  }

  private void handleSeatSelection(VBox dynamicContent) {
    Map<Long, String> posti = seatSelection.getSelectedSeats();
    try {
      dynamicContent.getChildren().clear();

      List<Biglietto> createdBiglietti =
          bffService.createBiglietti(proiezione.getId(), posti, false);
      // Rimuovi i biglietti precedenti per evitare duplicati
      biglietti.clear();
      biglietti.addAll(createdBiglietti);

      // Creazione selezione ridotto
      if (!biglietti.isEmpty()) {
        for (Biglietto biglietto : biglietti) {
          dynamicContent.getChildren().add(createSelezione(biglietto));
        }
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
    Button buyButton = new Button("COMPRA");
    buyButton.getStyleClass().add("quick-purchase-button");
    buyButton.setOnAction(e -> buyTicket());

    tipoBiglietto.getChildren().add(buyButton);
    return tipoBiglietto;
  }

  private void updateTotal() {
    double totale = 0;
    for (Biglietto biglietto : biglietti) {
      totale = totale + (biglietto.getPrezzo() != null ? biglietto.getPrezzo() : 0);
    }
    totalLabel.setText("Totale: " + totale + "€");
  }

  private void buyTicket() {
    try {
      for (Biglietto biglietto : biglietti) {
        // TODO Alex: [23/01/2025] settare idCliente al biglietto per l'acquisto
        biglietto.setClienteId(1L);
        bffService.compraBiglietto(biglietto, biglietto.isRidotto());
      }
      new Alert(Alert.AlertType.INFORMATION, "Biglietti acquistati con successo", ButtonType.OK)
          .showAndWait();
    } catch (Exception e) {
      new Alert(
              Alert.AlertType.ERROR,
              "Errore durante l'acquisto dei biglietti: " + e.getMessage(),
              ButtonType.OK)
          .showAndWait();
    }
  }
}

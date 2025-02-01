package it.unibo.samplejavafx.ui;

import it.unibo.samplejavafx.cinema.services.BffService;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lombok.Setter;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;


public class SeatSelection {

  private static final int COLUMNS = 5; // Numero di colonne
  private static final String[] ROWS = {"A", "B", "C", "D"}; // Righe rappresentate da lettere
  private final ToggleButton[][] seatButtons = new ToggleButton[ROWS.length][COLUMNS];
  private final ObservableSet<ToggleButton> selectedSeats = FXCollections.observableSet();
  private final ObservableSet<ToggleButton> confirmedSeats =
      FXCollections.observableSet(); // Copia dei posti confermati
  private final ExecutorService executor =
      Executors.newCachedThreadPool(); // Per chiamate asincrone
  private final BffService bffService = new BffService(); // Istanza del servizio BFF
  private final Long proiezioneId;
  private final Long salaId;
  @Setter private Runnable onConfirm;

  public SeatSelection(Long proiezioneId, Long salaId) {
    this.proiezioneId = proiezioneId;
    this.salaId = salaId;
  }

  public VBox createSeatSelectionPane() {
    VBox container = new VBox(10);
    container.setAlignment(Pos.CENTER);
    container.setPadding(new Insets(20));
    container.getStyleClass().add("detail-root");

    // Creazione griglia dei posti
    GridPane seatGrid = createSeatGrid();
    seatGrid.setPadding(new Insets(10));
    seatGrid.setHgap(5);
    seatGrid.setVgap(5);

    // Pulsante di conferma
    Button confirmButton = new Button("Conferma Selezione");
    confirmButton.setDisable(true); // Disabilita il pulsante all'avvio
    confirmButton.setOnAction(
        e -> {
          if (onConfirm != null) {
            onConfirm.run();
            confirmedSeats.clear();
            confirmedSeats.addAll(selectedSeats); // Aggiorna i posti confermati
            confirmButton.setDisable(true); // Disabilita il pulsante dopo la conferma
          }
        });

    // Listener per abilitare/disabilitare il pulsante
    selectedSeats.addListener(
        (SetChangeListener<ToggleButton>)
            change -> {
              boolean isSelectionChanged =
                  !selectedSeats.equals(confirmedSeats); // Confronta con la selezione confermata
              confirmButton.setDisable(!isSelectionChanged); // Disabilita se non ci sono modifiche
            });

    // Abilita il pulsante quando ci sono posti selezionati
    // confirmButton.disableProperty().bind(javafx.beans.binding.Bindings.isEmpty(selectedSeats));

    container.getChildren().addAll(seatGrid, confirmButton);
    return container;
  }

  public GridPane createSeatGrid() {
    GridPane gridPane = new GridPane();
    gridPane.setPrefWidth(350);
    gridPane.setPrefHeight(300);
    gridPane.setHgap(5);
    gridPane.setVgap(5);
    gridPane.getStyleClass().add("detail-root");

    // Aggiunge le lettere delle file (A, B, C, D) con stile bianco
    for (int row = 0; row < ROWS.length; row++) {
      Label rowLabel = new Label(ROWS[row]);
      rowLabel.setPrefWidth(30);
      rowLabel.setAlignment(Pos.CENTER);
      rowLabel.getStyleClass().add("row-label");
      gridPane.add(rowLabel, 0, row);
    }

    // Creazione dei pulsanti dei posti
    for (int row = 0; row < ROWS.length; row++) {
      for (int col = 0; col < COLUMNS; col++) {

        Label seatNumber = new Label(String.valueOf(col + 1));
        seatNumber.setAlignment(Pos.CENTER);
        seatNumber.getStyleClass().add("seat-number");
        seatNumber.setMouseTransparent(true);


        ToggleButton seatButton = new ToggleButton();
        seatButton.setPrefSize(50, 50);
        seatButton.getStyleClass().add("seat-button");

        //aggiunta icona posti presi
        ImageView personIcon = new ImageView(new Image(getClass().getResource("/images/seattaken.png").toExternalForm()));
        personIcon.setFitWidth(30);
        personIcon.setFitHeight(30);
        personIcon.setVisible(false);

        //crea uno StackPane per sovrapporre tutti gli elementi
        StackPane seatStack = new StackPane();
        seatStack.getChildren().addAll(seatButton, seatNumber, personIcon);

        // Verifica se il posto è occupato
        int finalRow = row;
        int finalCol = col;
        executor.execute(() -> {
          boolean isPrenotabile;
          try {
            isPrenotabile = bffService.isPostoPrenotabile(
                    finalCol + 1L, ROWS[finalRow], proiezioneId, salaId);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }

          //aggiorna ui
          Platform.runLater(() -> {
            if (!isPrenotabile) {
              seatButton.setDisable(true);
              personIcon.setVisible(true);
              seatNumber.setVisible(false);
              seatButton.setOpacity(0.7);
            }
          });
        });

        //gestione della selezione del posto
        seatButton.setOnAction(e -> {
          if (seatButton.isSelected()) {
            selectedSeats.add(seatButton);
            seatButton.setStyle("-fx-background-color: #00FF00; -fx-text-fill: white;");
          } else {
            selectedSeats.remove(seatButton);
            seatButton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: black;");
          }
        });

        seatButtons[row][col] = seatButton;
        gridPane.add(seatStack, col + 1, row);
      }
    }
    return gridPane;
  }


  public Map<Long, String> getSelectedSeats() {
    Map<Long, String> selectedSeatsMap = new HashMap<>();
    for (ToggleButton seat : selectedSeats) {
      int row = -1;
      int col = -1;

      // Trova la posizione del pulsante nella matrice seatButtons
      for (int i = 0; i < ROWS.length; i++) {
        for (int j = 0; j < COLUMNS; j++) {
          if (seatButtons[i][j] == seat) {
            row = i;
            col = j;
            break;
          }
        }
        if (row != -1) {
          break;
        }
      }

      /*if (row != -1 && col != -1) {
        // Aggiungi la riga e la colonna selezionata alla mappa
        selectedSeatsMap.put((long) (col + 1), ROWS[row]);
      }*/

      if (row != -1 && col != -1) {
        // Concatenare le righe selezionate per ogni colonna
        String existingRows = selectedSeatsMap.getOrDefault((long) (col + 1), "");
        String newRow = ROWS[row];
        if (!existingRows.isEmpty()) {
          existingRows += ","; // Separatore tra le righe
        }
        existingRows += newRow;

        selectedSeatsMap.put((long) (col + 1), existingRows);
      }
    }
    return selectedSeatsMap;
  }
}

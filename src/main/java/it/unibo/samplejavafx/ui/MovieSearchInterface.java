package it.unibo.samplejavafx.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import it.unibo.samplejavafx.cinema.application.models.Film;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class MovieSearchInterface extends HBox {
  private final TextField campoRicerca;
  private final ComboBox<String> filtroGenere;
  private final FilteredList<Film> filmFiltrati;
  private final Consumer<List<Film>> onSearchUpdated;

    public MovieSearchInterface(List<Film> films, Consumer<List<Film>> onSearchUpdated) {
        super(10);
        this.getStyleClass().add("search-section");
        this.onSearchUpdated = onSearchUpdated;
        this.filmFiltrati = new FilteredList<>(FXCollections.observableArrayList(films));
        
        Label titleLabel = new Label("CERCA FILM");
        titleLabel.getStyleClass().add("search-title");
        
        campoRicerca = new TextField();
        campoRicerca.setPromptText("Cerca film...");
        campoRicerca.getStyleClass().add("search-text-field");
        campoRicerca.setPrefWidth(300); 
        campoRicerca.textProperty().addListener((obs, old, newValue) -> applicaFiltri());
        
        Set<String> tuttiGeneri = films.stream()
        .flatMap(m -> m.getGenresList().stream())
        .collect(Collectors.toSet());
        filtroGenere = new ComboBox<>(FXCollections.observableArrayList(tuttiGeneri));
        filtroGenere.setPromptText("Filtra per genere");
        filtroGenere.getStyleClass().add("search-combo-box");
        filtroGenere.setPrefWidth(200); 
        filtroGenere.valueProperty().addListener((obs, old, newValue) -> applicaFiltri());
        
        // Bottone per resettare i filtri
        Button resetButton = new Button("Cancella filtri");
        resetButton.getStyleClass().add("search-button");
        resetButton.setOnAction(e -> resetFiltri());

        this.getChildren().addAll(titleLabel, campoRicerca, filtroGenere, resetButton);
    }

    private void applicaFiltri() {
      filmFiltrati.setPredicate(
          film -> {
            boolean corrispondenzaRicerca =
                campoRicerca.getText() == null
                    || campoRicerca.getText().isEmpty()
                    || film.getTitle().toLowerCase().contains(campoRicerca.getText().toLowerCase());
  
            boolean corrispondenzaGenere =
                filtroGenere.getValue() == null || film.getGenresList().contains(filtroGenere.getValue());
  
            return corrispondenzaRicerca && corrispondenzaGenere;
          });
  
      onSearchUpdated.accept(new ArrayList<>(filmFiltrati));
  }

  public void resetFiltri() {
    campoRicerca.clear();
    filtroGenere.setValue(null);
  }
}

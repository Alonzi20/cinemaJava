package it.unibo.samplejavafx.ui;

import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import it.unibo.samplejavafx.cinema.models.Film;
import javafx.collections.transformation.FilteredList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.function.Consumer;

public class MovieSearchInterface extends HBox {
    private final TextField campoRicerca;
    private final ComboBox<String> filtroGenere;
    private final FilteredList<Film> filmFiltrati;
    private final Consumer<List<Film>> onSearchUpdated;

    public MovieSearchInterface(List<Film> films, Consumer<List<Film>> onSearchUpdated) {
        super(10);
        this.getStyleClass().add("movie-search-interface");
        this.onSearchUpdated = onSearchUpdated;
        this.filmFiltrati = new FilteredList<>(FXCollections.observableArrayList(films));
        
        campoRicerca = new TextField();
        campoRicerca.setPromptText("Cerca film...");
        campoRicerca.getStyleClass().add("text-field");
        campoRicerca.textProperty().addListener((obs, old, newValue) -> applicaFiltri());
        
        Set<String> tuttiGeneri = films.stream()
            .flatMap(m -> m.getGenres().stream())
            .collect(Collectors.toSet());
        filtroGenere = new ComboBox<>(FXCollections.observableArrayList(tuttiGeneri));
        filtroGenere.setPromptText("Filtra per genere");
        filtroGenere.getStyleClass().add("combo-box");
        filtroGenere.valueProperty().addListener((obs, old, newValue) -> applicaFiltri());
        
        // Bottone per resettare i filtri
        Button resetButton = new Button("Cancella filtri");
        resetButton.getStyleClass().add("reset-button");
        resetButton.setOnAction(e -> resetFiltri());

        this.getChildren().addAll(campoRicerca, filtroGenere, resetButton);
    }

    private void applicaFiltri() {
        filmFiltrati.setPredicate(film -> {
            boolean corrispondenzaRicerca = campoRicerca.getText() == null || 
                campoRicerca.getText().isEmpty() ||
                film.getTitle().toLowerCase().contains(campoRicerca.getText().toLowerCase());
                
            boolean corrispondenzaGenere = filtroGenere.getValue() == null ||
                film.getGenres().contains(filtroGenere.getValue());
                
            return corrispondenzaRicerca && corrispondenzaGenere;
        });
        
        onSearchUpdated.accept(filmFiltrati.stream().collect(Collectors.toList()));
    }

    public void resetFiltri() {
        campoRicerca.clear();
        filtroGenere.setValue(null);
    }
}
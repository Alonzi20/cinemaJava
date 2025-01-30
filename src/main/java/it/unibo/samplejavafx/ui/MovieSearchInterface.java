package it.unibo.samplejavafx.ui;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import it.unibo.samplejavafx.cinema.application.models.Film;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.services.BffService;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class MovieSearchInterface extends HBox {
    private TextField campoRicerca;
    private ComboBox<String> filtroGenere;
    private final FilteredList<Film> filmFiltrati;
    private final Consumer<List<Film>> onSearchUpdated;
    private final BffService bffService;
    private final List<Film> filmsConProiezioni;
    private final Map<Long, List<Proiezione>> proiezioniCache;

    public MovieSearchInterface(List<Film> films, Consumer<List<Film>> onSearchUpdated) {
        super(10);
        this.getStyleClass().add("search-section");
        this.onSearchUpdated = onSearchUpdated;
        this.bffService = new BffService();
        this.proiezioniCache = new HashMap<>();

        // Filtra i film con proiezioni e popola la cache
        this.filmsConProiezioni = films.stream()
                .filter(film -> film.getId() != null)
                .filter(film -> {
                    try {
                        List<Proiezione> proiezioni = bffService.findAllProiezioniByFilmId(film.getId());
                        if (!proiezioni.isEmpty()) {
                            proiezioniCache.put(film.getId(), proiezioni);
                            return true;
                        }
                        return false;
                    } catch (Exception e) {
                        System.err.println("Errore nel recupero delle proiezioni per il film " +
                                film.getTitle() + ": " + e.getMessage());
                        return false;
                    }
                })
                .collect(Collectors.toList());

        this.filmFiltrati = new FilteredList<>(FXCollections.observableArrayList(filmsConProiezioni));

        setupUI();
    }

    public List<Proiezione> getProiezioniForFilm(Long filmId) {
        return proiezioniCache.getOrDefault(filmId, Collections.emptyList());
    }

    private void setupUI() {
        Label titleLabel = new Label("CERCA FILM");
        titleLabel.getStyleClass().add("search-title");

        campoRicerca = new TextField();
        campoRicerca.setPromptText("Cerca film...");
        campoRicerca.getStyleClass().add("search-text-field");
        campoRicerca.setPrefWidth(300);
        campoRicerca.textProperty().addListener((obs, old, newValue) -> applicaFiltri());

        Set<String> tuttiGeneri = filmsConProiezioni.stream()
                .flatMap(m -> m.getGenresList().stream())
                .collect(Collectors.toSet());

        filtroGenere = new ComboBox<>(FXCollections.observableArrayList(tuttiGeneri));
        filtroGenere.setPromptText("Filtra per genere");
        filtroGenere.getStyleClass().add("search-combo-box");
        filtroGenere.setPrefWidth(200);
        filtroGenere.valueProperty().addListener((obs, old, newValue) -> applicaFiltri());

        Button resetButton = new Button("Cancella filtri");
        resetButton.getStyleClass().add("search-button");
        resetButton.setOnAction(e -> resetFiltri());

        this.getChildren().addAll(titleLabel, campoRicerca, filtroGenere, resetButton);
    }

    private void applicaFiltri() {
        filmFiltrati.setPredicate(film -> {
            boolean corrispondenzaRicerca = campoRicerca.getText() == null
                    || campoRicerca.getText().isEmpty()
                    || film.getTitle().toLowerCase().contains(campoRicerca.getText().toLowerCase());

            boolean corrispondenzaGenere = filtroGenere.getValue() == null
                    || film.getGenresList().contains(filtroGenere.getValue());

            return corrispondenzaRicerca && corrispondenzaGenere;
        });

        onSearchUpdated.accept(new ArrayList<>(filmFiltrati));
    }

    public void resetFiltri() {
        campoRicerca.clear();
        filtroGenere.setValue(null);
        filmFiltrati.setPredicate(film -> true);
        onSearchUpdated.accept(new ArrayList<>(filmFiltrati));
    }

    public List<Film> getFilmsConProiezioni() {
        return new ArrayList<>(filmsConProiezioni);
    }
}
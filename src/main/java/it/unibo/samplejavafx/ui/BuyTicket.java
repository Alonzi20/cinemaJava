package it.unibo.samplejavafx.ui;

import it.unibo.samplejavafx.cinema.application.models.Biglietto;
import it.unibo.samplejavafx.cinema.application.models.Film;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.application.models.Sala;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import it.unibo.samplejavafx.cinema.services.BffService;

public class BuyTicket extends Application{
    private final Proiezione proiezione;
    private Film movie;
    private Sala sala;
    private List<Biglietto> biglietti;
    private final Label totalLabel = new Label("Totale: 0€");
    private BffService bffService;

    public BuyTicket(Proiezione proiezione){
        this.proiezione = proiezione;
        try{
            this.movie = bffService.findByFilmId(proiezione.getFilmId());
            this.sala = bffService.findBySalaId(proiezione.getSalaId());
        }catch(Exception e){
            throw new RuntimeException("Errore durante il recupero dei dati");
        }
        
    }

    @Override
    public void start(Stage stage){
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Button backButton = new Button("Indietro");
        backButton.setOnAction(e -> stage.close());

        ImageView poster = new ImageView(new Image("https://image.tmdb.org/t/p/w300" + movie.getPosterPath()));
        poster.setFitWidth(200);
        poster.setFitHeight(300);

        Label adultLabel = createLabel("Vietato ai minori: " + (movie.isAdult() ? "Si" : "No")); 

        root.getChildren().addAll(
            backButton,
            poster,
            createLabel("Film: " + movie.getTitle()),
            createLabel("Genere: " + movie.getGenres()),
            createLabel("Sala " + sala.getNumero()),
            adultLabel
        );

        // TODO Alex:
        // inserire la scelta dei posti qua

        root.getChildren().add(
            createLabel("Biglietti")

        );

        for(Biglietto biglietto : biglietti){
            root.getChildren().add(createSelezione(biglietto));
        }

        root.getChildren().add(totalLabel);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setTitle("Acquista Biglietti");
        stage.setScene(scene);
        stage.show();
    }

    private Label createLabel(String text, String... styleClasses) {
        Label label = new Label(text);
        label.getStyleClass().addAll(styleClasses);
        return label;
    }

    private VBox createSelezione(Biglietto biglietto){
        Label bigliettoLabel = createLabel(biglietto.getFila()+biglietto.getNumero());
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Intero", "Ridotto");

        //imposta "Intero" come default
        comboBox.getSelectionModel().select("Intero");

        comboBox.setOnAction(event -> {
            String selezione = comboBox.getSelectionModel().getSelectedItem();
            if (selezione == "Intero"){
                biglietto.setRidotto(false);
            }else if (selezione == "Ridotto"){
                biglietto.setRidotto(true);
            }
            updateTotal();
        }); 

        VBox tipoBiglietto = new VBox(5);
        tipoBiglietto.getChildren().addAll(bigliettoLabel, comboBox);
        return tipoBiglietto;
    }

    private void updateTotal(){
        double totale=0;
        for(Biglietto biglietto : biglietti){
            totale =+ biglietto.getPrezzo();
        }
        totalLabel.setText("Totale: " + totale + "€");
    }
}

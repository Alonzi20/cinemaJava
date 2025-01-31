package it.unibo.samplejavafx.ui;

import it.unibo.samplejavafx.cinema.services.BffService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SignupPage extends Application {

  private final BffService bffService;

  public SignupPage() {
    bffService = new BffService();
  }

  @Override
  public void start(Stage stage) {
    stage.setTitle("Sing Up Page");

    // Bottone per tornare indietro
    Button backButton = new Button("← Indietro");
    backButton.getStyleClass().add("detail-button");
    backButton.setOnAction(e -> stage.close());

    // Creazione del layout
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 25, 25, 25));

    // Etichetta e campo nome
    Label nomeLabel = new Label("Nome:");
    grid.add(nomeLabel, 0, 0);
    TextField nomeField = new TextField();
    grid.add(nomeField, 1, 0);

    // Etichetta e campo cognome
    Label cognomeLabel = new Label("Cognome:");
    grid.add(cognomeLabel, 0, 1);
    TextField cognomeField = new TextField();
    grid.add(cognomeField, 1, 1);

    // Etichetta e campo email
    Label emailLabel = new Label("Email:");
    grid.add(emailLabel, 0, 2);
    TextField emailField = new TextField();
    grid.add(emailField, 1, 2);

    // Etichetta e campo password
    Label passwordLabel = new Label("Password:");
    grid.add(passwordLabel, 0, 3);
    TextField passwordField = new PasswordField();
    grid.add(passwordField, 1, 3);

    // Bottone per registrarsi
    Button singupButton = new Button("Registrati");
    grid.add(singupButton, 1, 4);

    // Azione bottone
    singupButton.setOnAction(
        e -> {
          String nome = nomeField.getText();
          String cognome = cognomeField.getText();
          String email = emailField.getText();
          String password = passwordField.getText();
          try {
            bffService.createCliente(nome, cognome, email, password);
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Successo");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Utente creato con successo");

            DialogPane successPane = successAlert.getDialogPane();
            successPane.getStyleClass().add("custom-alert");
            successPane
                .getStylesheets()
                .add(getClass().getResource("/css/style.css").toExternalForm());

            successAlert.showAndWait();
          } catch (Exception ex) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Errore");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText(
                "Errore durante la creazione dell'utente: " + ex.getMessage());

            DialogPane errorPane = errorAlert.getDialogPane();
            errorPane.getStyleClass().add("custom-alert");
            errorPane
                .getStylesheets()
                .add(getClass().getResource("/css/style.css").toExternalForm());

            errorAlert.showAndWait();
          }
        });

    Scene scene = new Scene(grid, 300, 200);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

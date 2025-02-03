package it.unibo.samplejavafx.ui;

import it.unibo.samplejavafx.cinema.application.models.Cliente;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.services.BffService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignupPage extends Application {

  private final BffService bffService;
  private final Proiezione proiezione;

  public SignupPage(Proiezione proiezione) {
    bffService = new BffService();
    this.proiezione = proiezione;
  }

  @Override
  public void start(Stage stage) {
    VBox root = new VBox(10);
    root.setPadding(new Insets(20));
    root.getStyleClass().add("detail-root");

    // Bottone per tornare indietro
    Button backButton = new Button("← Indietro");
    backButton.getStyleClass().add("detail-button");
    backButton.setOnAction(e -> {
      Stage loginStage = new Stage();
      LoginPage loginPage = new LoginPage(proiezione);
      stage.close();
      loginPage.start(loginStage);
    });

    // Creazione del layout
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 25, 25, 25));

    // Etichetta e campo nome
    Label nomeLabel = new Label("Nome:");
    nomeLabel.getStyleClass().add("detail-label");
    grid.add(nomeLabel, 0, 0);
    TextField nomeField = new TextField();
    grid.add(nomeField, 1, 0);

    // Etichetta e campo cognome
    Label cognomeLabel = new Label("Cognome:");
    cognomeLabel.getStyleClass().add("detail-label");
    grid.add(cognomeLabel, 0, 1);
    TextField cognomeField = new TextField();
    grid.add(cognomeField, 1, 1);

    // Etichetta e campo email
    Label emailLabel = new Label("Email:");
    emailLabel.getStyleClass().add("detail-label");
    grid.add(emailLabel, 0, 2);
    TextField emailField = new TextField();
    grid.add(emailField, 1, 2);

    // Etichetta e campo password
    Label passwordLabel = new Label("Password:");
    passwordLabel.getStyleClass().add("detail-label");
    grid.add(passwordLabel, 0, 3);
    TextField passwordField = new PasswordField();
    grid.add(passwordField, 1, 3);

    // Bottone per registrarsi
    Button singupButton = new Button("Registrati");
    singupButton.getStyleClass().add("detail-button");
    grid.add(singupButton, 1, 4);

    // Azione bottone
    singupButton.setOnAction(
        e -> {
          String nome = nomeField.getText();
          String cognome = cognomeField.getText();
          String email = emailField.getText();
          String password = passwordField.getText();
          try {
            Cliente cliente = bffService.createCliente(nome, cognome, email, password);
            stage.close();
            new BuyTicket(proiezione, cliente).start(new Stage());
          } catch (Exception ex) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Errore");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText(
                ex.getMessage());

            DialogPane errorPane = errorAlert.getDialogPane();
            errorPane.getStyleClass().add("custom-alert");
            errorPane
                .getStylesheets()
                .add(getClass().getResource("/css/style.css").toExternalForm());

            errorAlert.showAndWait();
          }
        }
      );

    root.getChildren()
    .addAll(
      backButton,
      grid
    );

    Scene scene = new Scene(root, 300, 300);
    stage.setTitle("Sing Up Page");
    scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

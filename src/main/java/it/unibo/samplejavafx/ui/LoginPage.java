package it.unibo.samplejavafx.ui;

import it.unibo.samplejavafx.cinema.application.models.Cliente;
import it.unibo.samplejavafx.cinema.application.models.Proiezione;
import it.unibo.samplejavafx.cinema.services.BffService;
import jakarta.servlet.http.HttpSession;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPage extends Application {

  private final BffService bffService;
  private final Proiezione proiezione;

  public LoginPage(Proiezione proiezione) {
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
    backButton.setOnAction(e -> stage.close());

    // Creazione del layout
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 25, 25, 25));

    // Etichetta e campo email
    Label emailLabel = new Label("Email:");
    emailLabel.getStyleClass().add("detail-label");
    grid.add(emailLabel, 0, 0);
    TextField emailField = new TextField();
    grid.add(emailField, 1, 0);

    // Etichetta e campo password
    Label passwordLabel = new Label("Password:");
    passwordLabel.getStyleClass().add("detail-label");
    grid.add(passwordLabel, 0, 1);
    PasswordField passwordField = new PasswordField();
    grid.add(passwordField, 1, 1);

    // Pulsante di login
    Button loginButton = new Button("Accedi");
    loginButton.getStyleClass().add("detail-button");

    // Pulsante per registrarsi
    Button signupButton = new Button("Registrati");
    signupButton.getStyleClass().add("detail-button");
    grid.add(signupButton, 0, 3);

    HBox loginContainer = new HBox();
    loginContainer.setAlignment(Pos.CENTER);
    loginContainer.getChildren().add(loginButton);

    HBox signupContainer = new HBox();
    signupContainer.setAlignment(Pos.CENTER);
    signupContainer.getChildren().add(signupButton);

    grid.add(loginContainer, 0,2,2,1);
    grid.add(signupContainer, 0, 3, 2, 1);

    // Azione del pulsante per registrarsi
    signupButton.setOnAction(
        e -> {
          Stage signupStage = new Stage();
          SignupPage signupPage = new SignupPage(proiezione);
          stage.close();
          signupPage.start(signupStage);
        });

    // Azione del pulsante di accesso
    loginButton.setOnAction(
        e -> {
          String email = emailField.getText();
          String password = passwordField.getText();
          try {
            Cliente cliente = bffService.logInCliente(email, password);
            stage.close();
            new BuyTicket(proiezione, cliente).start(new Stage());

          } catch (Exception ex) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Errore");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText(ex.getMessage());

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
    stage.setTitle("Login Page");
    scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

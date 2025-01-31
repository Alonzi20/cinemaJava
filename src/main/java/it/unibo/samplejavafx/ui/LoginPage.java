package it.unibo.samplejavafx.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import it.unibo.samplejavafx.cinema.services.BffService;

public class LoginPage extends Application {

    private final BffService bffService = new BffService();
    
    @Override
    public void start(Stage stage) {
        stage.setTitle("Login Page");

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
        grid.add(emailLabel, 0, 0);
        TextField emailField = new TextField();
        grid.add(emailField, 1, 0);
        
        // Etichetta e campo password
        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 1);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 1);

        //Pulsante per registrarsi
        Button singupButton = new Button("Registrati");
        grid.add(singupButton, 0,2);
        
        // Pulsante di login
        Button loginButton = new Button("Accedi");
        grid.add(loginButton, 1, 2);
        
        // Messaggio di output
        Label messageLabel = new Label();
        grid.add(messageLabel, 1, 3);

        //Azione del pulsante per registrarsi
        singupButton.setOnAction(e ->{
            Stage singupStage = new Stage();
            SingupPage singupPage = new SingupPage();
            singupPage.start(singupStage);
        });
        
        // Azione del pulsante di accesso
        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            try{
                bffService.logInCliente(email, password);
            }catch (Exception ex) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Errore");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Errore durante l'accesso dell'utente: " + ex.getMessage());
                   
                DialogPane errorPane = errorAlert.getDialogPane();
                errorPane.getStyleClass().add("custom-alert");
                errorPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
          
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

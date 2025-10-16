package controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Usuario;
import util.Session;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class PerfilController {
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblEmail;

    @FXML
    public void initialize() {
        Usuario actual = Session.getCurrentUser();
        if (actual != null) {
            lblNombre.setText("Nombre: " + actual.getNombre());
            lblEmail.setText("Email: " + actual.getEmail());
        }
    }

    @FXML
    private void volverChat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainChat.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) lblNombre.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
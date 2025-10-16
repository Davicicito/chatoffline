package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Usuario;
import service.XMLUsuariosService;
import util.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MainChatController {

    @FXML
    private Label lblUsuario;

    @FXML
    private ListView<String> listaContactos;

    private final XMLUsuariosService servicioUsuarios = new XMLUsuariosService();

    @FXML
    public void initialize() {
        // Mostrar nombre del usuario activo
        Usuario actual = Session.getCurrentUser();
        if (actual != null) {
            lblUsuario.setText(actual.getNombre());
        }

        // Cargar lista de contactos (sin el propio usuario)
        List<Usuario> todos = servicioUsuarios.cargarUsuarios();
        List<String> nombres = todos.stream()
                .filter(u -> !u.getEmail().equalsIgnoreCase(actual.getEmail()))
                .map(Usuario::getNombre)
                .collect(Collectors.toList());
        listaContactos.getItems().addAll(nombres);
    }

    @FXML
    private void abrirPerfil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/perfil.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) lblUsuario.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

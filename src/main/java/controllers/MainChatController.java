package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Mensaje;
import model.Usuario;
import service.XMLMensajesService;
import service.XMLUsuariosService;
import util.Session;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MainChatController {

    @FXML private Label lblUsuario;
    @FXML private ListView<String> listaUsuarios;
    @FXML private TextArea areaChat;
    @FXML private TextField txtMensaje;

    private final XMLUsuariosService servicioUsuarios = new XMLUsuariosService();
    private final XMLMensajesService servicioMensajes = new XMLMensajesService();
    private Usuario usuarioActual;
    private String contactoSeleccionado;

    @FXML
    public void initialize() {
        usuarioActual = Session.getCurrentUser();
        if (usuarioActual == null) {
            lblUsuario.setText("Error de sesión");
            return;
        }
        lblUsuario.setText("Usuario: " + usuarioActual.getNombre());

        // Cargar la lista de contactos (todos los usuarios menos el actual)
        List<Usuario> todos = servicioUsuarios.cargarUsuarios();
        List<String> nombres = todos.stream()
                .filter(u -> !u.getNombre().equalsIgnoreCase(usuarioActual.getNombre()))
                .map(Usuario::getNombre)
                .collect(Collectors.toList());
        listaUsuarios.getItems().addAll(nombres);
    }

    @FXML
    private void abrirChat(MouseEvent event) {
        contactoSeleccionado = listaUsuarios.getSelectionModel().getSelectedItem();
        if (contactoSeleccionado == null) {
            return; // No hay nadie seleccionado
        }

        areaChat.clear();
        // Cargar la conversación entre el usuario actual y el contacto seleccionado
        List<Mensaje> conversacion = servicioMensajes.obtenerConversacion(usuarioActual.getNombre(), contactoSeleccionado);

        for (Mensaje msg : conversacion) {
            areaChat.appendText(msg.toString() + "\n");
        }
    }

    @FXML
    private void enviarMensaje(ActionEvent event) {
        String contenido = txtMensaje.getText().trim();
        if (contenido.isEmpty() || contactoSeleccionado == null) {
            return; // No enviar mensajes vacíos o sin tener un chat abierto
        }

        // Crear el nuevo mensaje
        Mensaje nuevoMensaje = new Mensaje(usuarioActual.getNombre(), contactoSeleccionado, contenido, null);
        servicioMensajes.enviarMensaje(nuevoMensaje);

        // Mostrar el mensaje enviado en el área de chat y limpiar el campo de texto
        areaChat.appendText(nuevoMensaje.toString() + "\n");
        txtMensaje.clear();
    }

    @FXML
    private void abrirPerfil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/perfil.fxml")); // Ruta corregida
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) lblUsuario.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

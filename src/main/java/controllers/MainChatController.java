package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Adjunto;
import model.Mensaje;
import model.Usuario;
import service.AdjuntoService;
import service.XMLMensajesService;
import service.XMLUsuariosService;
import util.Session;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MainChatController {

    @FXML private Label lblUsuario;
    @FXML private ListView<String> listaUsuarios; // Vuelve a ser una lista de Strings
    @FXML private ListView<Mensaje> chatListView;
    @FXML private TextField txtMensaje;
    @FXML private Label lblAdjunto;

    private final XMLUsuariosService servicioUsuarios = new XMLUsuariosService();
    private final XMLMensajesService servicioMensajes = new XMLMensajesService();
    private final AdjuntoService servicioAdjuntos = new AdjuntoService();

    private Usuario usuarioActual;
    private String contactoSeleccionado; // Vuelve a ser un String
    private Adjunto adjuntoActual = null;
    private final ObservableList<Mensaje> mensajesMostrados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        usuarioActual = Session.getCurrentUser();
        if (usuarioActual == null) {
            lblUsuario.setText("Error de sesión");
            return;
        }
        lblUsuario.setText("Usuario: " + usuarioActual.getNombre());

        // Cargar la lista de contactos con nombres (como antes)
        List<String> nombres = servicioUsuarios.cargarUsuarios().stream()
                .map(Usuario::getNombre)
                .filter(nombre -> !nombre.equalsIgnoreCase(usuarioActual.getNombre()))
                .collect(Collectors.toList());
        listaUsuarios.getItems().addAll(nombres);

        // Configurar el área de chat para usar las nuevas "burbujas"
        chatListView.setItems(mensajesMostrados);
        chatListView.setCellFactory(listView -> new MessageCellController());
    }

    @FXML
    private void abrirChat(MouseEvent event) {
        contactoSeleccionado = listaUsuarios.getSelectionModel().getSelectedItem(); // Ahora es un String
        if (contactoSeleccionado == null) return;

        limpiarAdjunto();
        mensajesMostrados.clear();

        List<Mensaje> conversacion = servicioMensajes.obtenerConversacion(usuarioActual.getNombre(), contactoSeleccionado);
        mensajesMostrados.addAll(conversacion);
    }

    @FXML
    private void adjuntarArchivo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo para adjuntar");
        File archivoSeleccionado = fileChooser.showOpenDialog(lblUsuario.getScene().getWindow());

        if (archivoSeleccionado != null) {
            adjuntoActual = servicioAdjuntos.guardarAdjunto(archivoSeleccionado.getAbsolutePath());
            if (adjuntoActual != null) {
                lblAdjunto.setText("Adjunto: " + adjuntoActual.getNombre());
            }
        }
    }

    @FXML
    private void enviarMensaje(ActionEvent event) {
        String contenido = txtMensaje.getText().trim();
        if ((contenido.isEmpty() && adjuntoActual == null) || contactoSeleccionado == null) {
            return;
        }

        Mensaje nuevoMensaje = new Mensaje(usuarioActual.getNombre(), contactoSeleccionado, contenido, adjuntoActual);
        servicioMensajes.enviarMensaje(nuevoMensaje);

        mensajesMostrados.add(nuevoMensaje);
        chatListView.scrollTo(mensajesMostrados.size() - 1);
        txtMensaje.clear();
        limpiarAdjunto();
    }

    private void limpiarAdjunto() {
        adjuntoActual = null;
        lblAdjunto.setText("");
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

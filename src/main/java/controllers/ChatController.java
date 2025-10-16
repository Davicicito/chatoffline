package controllers;

import javafx.fxml.FXML;
import model.Mensaje;
import service.XMLMensajesService;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {
    @FXML
    private TextField txtRemitente, txtDestinatario, txtMensaje;

    @FXML
    private TextArea areaChat;

    private final XMLMensajesService servicioMensajes = new XMLMensajesService();

    @FXML
    private void enviarMensaje() {
        String remitente = txtRemitente.getText();
        String destinatario = txtDestinatario.getText();
        String contenido = txtMensaje.getText();

        if (remitente.isEmpty() || destinatario.isEmpty() || contenido.isEmpty()) {
            areaChat.appendText("⚠️ Rellena todos los campos antes de enviar.\n");
            return;
        }

        Mensaje mensaje = new Mensaje(remitente, destinatario, contenido, null);
        servicioMensajes.enviarMensaje(mensaje);
        areaChat.appendText(mensaje + "\n");
        txtMensaje.setText("");
    }
}

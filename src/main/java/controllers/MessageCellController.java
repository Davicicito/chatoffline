package controllers;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.Adjunto;
import model.Mensaje;
import util.Session;

import java.io.File;
import java.io.IOException;

public class MessageCellController extends ListCell<Mensaje> {

    @FXML private VBox bubble;
    @FXML private Label senderLabel;
    @FXML private ImageView imageView;
    @FXML private Label messageLabel;
    @FXML private Hyperlink fileLink;

    private FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(Mensaje mensaje, boolean empty) {
        super.updateItem(mensaje, empty);

        if (empty || mensaje == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/message_cell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Configurar la alineación de la burbuja
            boolean esMio = mensaje.getRemitente().equalsIgnoreCase(Session.getCurrentUser().getNombre());
            if (esMio) {
                bubble.setAlignment(Pos.CENTER_RIGHT);
                bubble.setStyle("-fx-background-color: #DCF8C6; -fx-background-radius: 10;");
            } else {
                bubble.setAlignment(Pos.CENTER_LEFT);
                bubble.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10;");
            }

            senderLabel.setText(mensaje.getRemitente());

            // Gestionar visibilidad del contenido
            messageLabel.setVisible(!mensaje.getContenido().isEmpty());
            messageLabel.setManaged(!mensaje.getContenido().isEmpty());
            if (!mensaje.getContenido().isEmpty()) {
                messageLabel.setText(mensaje.getContenido());
            }

            Adjunto adjunto = mensaje.getAdjunto();
            imageView.setVisible(adjunto != null && "imagen".equals(adjunto.getTipo()));
            imageView.setManaged(adjunto != null && "imagen".equals(adjunto.getTipo()));
            fileLink.setVisible(adjunto != null && !"imagen".equals(adjunto.getTipo()));
            fileLink.setManaged(adjunto != null && !"imagen".equals(adjunto.getTipo()));

            if (adjunto != null) {
                if ("imagen".equals(adjunto.getTipo())) {
                    try {
                        Image image = new Image(adjunto.getRuta(), true); // Carga en segundo plano
                        imageView.setImage(image);
                    } catch (Exception e) {
                        messageLabel.setText(messageLabel.getText() + " [Imagen no encontrada]");
                        messageLabel.setVisible(true);
                        messageLabel.setManaged(true);
                    }
                } else {
                    fileLink.setText("Adjunto: " + adjunto.getNombre());
                    fileLink.setUserData(adjunto.getRuta()); // Guardar la ruta para usarla al hacer clic
                }
            }

            setGraphic(bubble);
        }
    }

    @FXML
    private void abrirArchivo() {
        String uri = (String) fileLink.getUserData();
        if (uri != null) {
            // Usar HostServices para abrir el archivo de forma segura
            HostServices hostServices = (HostServices) getScene().getWindow().getProperties().get("hostServices");
            if (hostServices != null) {
                hostServices.showDocument(uri);
            } else {
                System.err.println("HostServices no disponible para abrir el archivo.");
            }
        }
    }
}

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Scene scene = new Scene(loader.load());

        // --- AÑADIR ICONO DE LA APLICACIÓN ---
        try (InputStream iconStream = getClass().getResourceAsStream("/icons/logo.jpg")) {
            if (iconStream != null) {
                stage.getIcons().add(new Image(iconStream));
            } else {
                System.out.println("Icono no encontrado en: /icons/logo.jpg");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el icono: " + e.getMessage());
        }

        // --- PASAR HOSTSERVICES PARA PODER ABRIR ARCHIVOS ---
        stage.getProperties().put("hostServices", getHostServices());

        stage.setTitle("Chat Offline XML");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

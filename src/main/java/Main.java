import model.Adjunto;
import model.Mensaje;
import service.*;

public class Main {
    public static void main(String[] args) {
       /* XMLUsuariosService servicio = new XMLUsuariosService();
        servicio.registrarUsuario(new Usuario("Antonio", "anto12@gmail.com"));
        servicio.registrarUsuario(new Usuario("Maria", "maria@gmail.com"));

        servicio.registrarUsuario(new Usuario("Maria", "maria@gmail.com"));*/
//
//        XMLMensajesService chat= new XMLMensajesService();
//        chat.enviarMensaje(new Mensaje("Antonio","Maria","Hola Maria",null));
//        chat.enviarMensaje(new Mensaje("Maria","Antonio","Hola Antonio",null));
//        chat.enviarMensaje(new Mensaje("Antonio","Maria","Como estas?",null));
//
//        chat.mostrarConversacion("Antonio","Maria");
//
//        chat.mostrarTodasLasConversaciones();
//        StreamService resumen = new StreamService();
//        resumen.mostrarConversacion("Antonio","Maria");

//        ExportService exportar = new ExportService();
//        exportar.exportarConversacion("Antonio", "Maria");

//        XMLMensajesService chat = new XMLMensajesService();
//        AdjuntoService gestorAdjuntos = new AdjuntoService();
//
//        // Ruta de un archivo que tengas en tu ordenador (por ejemplo, una imagen o pdf)
//        String rutaArchivo = "C:\\Users\\david\\Pictures\\GdBvJr0WwAA5U9l.png";
//
//        // Guardar el adjunto en /media/ y crear el objeto
//        Adjunto adjunto = gestorAdjuntos.guardarAdjunto(rutaArchivo);
//        // Enviar mensaje con adjunto
//        chat.enviarMensaje(new Mensaje("Antonio", "Maria", "Te envío la foto.", adjunto));


        // nombre que quieras para el ZIP (sin extensión)
//        String nombreZip = "chat_Antonio_Maria";
//
//        // rutas donde están tus archivos exportados y adjuntos
//        String rutaExport = "exported/conversacion_Antonio_Maria.txt";
//        String rutaMedia = "media";
//
//        ZipService zip = new ZipService();
//        zip.crearZip(nombreZip, rutaExport, rutaMedia);
    }
}

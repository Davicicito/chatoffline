import Service.XMLMensajesService;
import Service.XMLUsuariosService;
import model.Mensaje;
import model.Usuario;

public class Main {
    public static void main(String[] args) {
       /* XMLUsuariosService servicio = new XMLUsuariosService();
        servicio.registrarUsuario(new Usuario("Antonio", "anto12@gmail.com"));
        servicio.registrarUsuario(new Usuario("Maria", "maria@gmail.com"));

        servicio.registrarUsuario(new Usuario("Maria", "maria@gmail.com"));*/

        XMLMensajesService chat= new XMLMensajesService();
        chat.enviarMensaje(new Mensaje("Antonio","Maria","Hola Maria",null));
        chat.enviarMensaje(new Mensaje("Maria","Antonio","Hola Antonio",null));
        chat.enviarMensaje(new Mensaje("Antonio","Maria","Como estas?",null));


        chat.mostrarConversacion("Antonio","Maria");

    }
}


package clases;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Chat_Cliente {
    
    private Socket socketChat = puertoServidor();
    private String mensajeRecibido;
    
    public Chat_Cliente() {
        
        socketChat.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override

            public void call(Object... os) {

                System.out.println("Conectado..");

            }
        }).on("reciboMensaje", new Emitter.Listener(){ 
            @Override

            public void call(Object... os) {
                mensajeRecibido = (String) os[0];
                System.out.println("mensaje recibido: " + mensajeRecibido);
            }
        });
        socketChat.connect();
    }
    
    private Socket puertoServidor(){
        
        Socket socket = null;
        try {
            socket = IO.socket("http://localhost:4000");
        } catch (URISyntaxException ex) {
            Logger.getLogger(Chat_Cliente.class.getName())
                    .log(Level.SEVERE, null, ex);
        } finally {
            return socket;
        }
    }

    public String getMensajeRecibido() {
        return mensajeRecibido;
    }

    public void setMensajeRecibido(String mensajeRecibido) {
        this.mensajeRecibido = mensajeRecibido;
    }

    public String envioMensaje(String mensajeEnviado, String usuarioEnviado){
        socketChat.emit("envioMensaje", usuarioEnviado + ": "+ mensajeEnviado);//este método "emite" la función
        //"envioMensaje" al servidor, para que lo reenvía a todos los conectados al puerto
        setMensajeRecibido(mensajeEnviado);//regresa el mensaje enviado por el servidor
        return mensajeRecibido;
    }
}
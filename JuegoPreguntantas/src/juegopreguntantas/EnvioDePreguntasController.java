package juegopreguntantas;

import clasesutilidad.PreguntaEnvio;
import clasesutilidad.RespuestaEnvio;
import com.google.gson.Gson;
import entity.Cuentausuario;
import entity.Pregunta;
import entity.Respuesta;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import persistencia.PersistenciaPregunta;
import javax.xml.bind.DatatypeConverter;

/****************************************************************** 
 * @version 1.0                                                   * 
 * @author Eduardo Rosas Rivera                                   * 
 * @since 12/12/2018                                              *
 * Nombre de la clase EnvioDePreguntasController                  *
 *****************************************************************/ 
public class EnvioDePreguntasController implements Initializable {

    @FXML
    private ProgressBar pbProcesoEnvio;
    @FXML
    private Button btnIniciar;
    
    private final Socket socket;
    private Cuentausuario usuario;
    private String idioma;
    private List<Pregunta> preguntas;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //No es necesario hacer algo
    }    
    
    /**
     * Constructor de la clase.
     * @throws URISyntaxException 
     */
    public EnvioDePreguntasController() throws URISyntaxException {
        
        ResourceBundle propiedadesCliente = ResourceBundle.getBundle(
                "utilidades.conexiones");
        String ipServidor = propiedadesCliente.getString(
                "key.ipServidor");
        socket  = IO.socket("http://" + ipServidor + ":4000");
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... os) {
                //No es necesario hacer algo.
            }
        });
        socket.connect();
    }
    
    /**
     * Este metodo envia las preguntas de la partida a todos los jugadores.
     * @param event Clic en el boton iniciar.
     */
    @FXML
    private void iniciar(ActionEvent event) {
        
        abrirVentanaResponderPregunta();
        socket.emit("iniciarPartida"); 
        socket.disconnect();
        btnIniciar.getScene().getWindow().hide();
    }
    
    /**
     * Este metodo recibe informacion de otras ventanas que instancian a esta.
     * @param usuario Cuentausuario que inicio la partida.
     * @param idioma idioma en que se abrira la ventana.
     * @param idSetPregunta id del set de preguntas a recuperar.
     */
    public void recibirParametros(Cuentausuario usuario, String idioma, 
            int idSetPregunta) {
        
        this.usuario = usuario;
        this.idioma = idioma;
        recuperarPreguntas(idSetPregunta);
        enviarPreguntas();
    }
    
    /**
     * Este metodo recupera todas la preguntas de un set de preguntas.
     * @param idSetPregunta id del set de preguntas.
     */
    private void recuperarPreguntas(int idSetPregunta) {
        
        PersistenciaPregunta recuperadorPreguntas = new PersistenciaPregunta();
        List<Pregunta> preguntasDelSet = 
                recuperadorPreguntas.recuperarPreguntaConRespuestas(
                        idSetPregunta);
        for(int i = 0; i < preguntasDelSet.size(); i++) {
            
            if(preguntasDelSet.get(i).getTipoPregunta() == 2) {
                
                preguntasDelSet.get(i).crearArregloImagen();
            }
            for(int j = 0; j < 4; j++) {
                
                if(preguntasDelSet.get(i).getRespuesta(j).getTipoRespuesta() == 2){
                    
                    preguntasDelSet.get(i).getRespuesta(j).crearArregloImagen();
                }
            }
        }
        preguntas = preguntasDelSet;
    }
    
    /**
     * Este metodo sirve para enviar una pregunta a los jugadores.
     *
     * @param pregunta pregunta a enviar.
     */
    public void enviarPreguntaCompleta(Pregunta pregunta) {

        Gson preguntaGSON = new Gson();
        PreguntaEnvio preguntaAEnviar = convertirPregunta(pregunta);
        List<RespuestaEnvio> respuestasDePregunta
                = convertirRespuesta(pregunta.getRespuestas());
        preguntaAEnviar.setRespuestas(respuestasDePregunta);
        String out = preguntaGSON.toJson(preguntaAEnviar);
        socket.emit("envioPregunta", out);
    }
    
    /**
     * Este metodo convierte la pregunta que va a ser enviada a un objeto de
     * tipo JSONObject.
     *
     * @param pregunta Pregunta a ser enviada.
     * @return Pregunta convertida en tipo JSONObject.
     */
    private PreguntaEnvio convertirPregunta(Pregunta pregunta) {

        PreguntaEnvio pregEnvio = new PreguntaEnvio();
        pregEnvio.setPregunta(pregunta.getPregunta());
        pregEnvio.setTipoPregunta(pregunta.getTipoPregunta());
        if (pregunta.getTipoPregunta() == 2) {

            byte[] imagen = crearArregloImagen(pregunta.getPregunta());
            String imagenCodificada = DatatypeConverter.printBase64Binary(imagen);
            pregEnvio.setImagen(imagenCodificada);
        } else {
            pregEnvio.setImagen(null);
        }
        return pregEnvio;
    }
    
    /**
     * Este metodo convierte una lista de Respuestas a un arreglo de tipo 
     * JSONArray.
     * @param respuestas lista de respuestas a enviar.
     * @return Arreglo de respuestas de tipoJSON.
     */
    private ArrayList<RespuestaEnvio> convertirRespuesta(List<Respuesta> 
            respuestas) {
        
        ArrayList<RespuestaEnvio> respuestasAEnviar = new ArrayList<>();
        for(int i = 0; i < respuestas.size(); i++) {
            
            RespuestaEnvio nuevaRespuestaEnvio1 = new RespuestaEnvio();
            
            nuevaRespuestaEnvio1.setRespuesta(respuestas.get(i).getRespuesta());
            nuevaRespuestaEnvio1.setPuntaje(respuestas.get(i).getPuntaje());
            nuevaRespuestaEnvio1.setTipoRespuesta(respuestas.get(i)
                    .getTipoRespuesta());
            if (respuestas.get(i).getTipoRespuesta() == 2) {
                
                byte[] imagen = 
                        crearArregloImagen(respuestas.get(i).getRespuesta());
                String imagenCodificada = 
                        DatatypeConverter.printBase64Binary(imagen);
                nuevaRespuestaEnvio1.setImagen(imagenCodificada);
            }
            respuestasAEnviar.add(nuevaRespuestaEnvio1);
        }
        
        return respuestasAEnviar;
    }
    
    /**
     * Este metodo crea un arreglo de bytes para poder ser enviado por nodeJs
     * @param ruta ruta del archivo.
     * @return imagen convertida en un arreglo de bytes.
     */
    public byte[] crearArregloImagen(String ruta) {
        
        byte[] imagen = null;
        try {
            Path path = Paths.get(ruta);
            byte[] contenido = Files.readAllBytes(path);
            imagen = contenido;
        } catch (IOException ex) {
            
            Logger.getLogger(EnvioDePreguntasController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return imagen;
    }
    
    /**
     * Este metodo sirve para abrir la ventana Responder Pregunta.
     */
    private void abrirVentanaResponderPregunta() {
        
        try {
            
            Locale.setDefault(new Locale(idioma));
            ResourceBundle resourceBundle
                    = ResourceBundle.getBundle("juegopreguntantas.lang/lang");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(
                    "ResponderPregunta.fxml"));
            loader.setResources(resourceBundle);

            Parent responder = loader.load();
            ResponderPreguntaController controller = loader.getController();
            controller.recibirParametros(usuario, idioma, true);

            Scene scene = new Scene(responder);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

        } catch (Exception ex) {

            Logger.getLogger(EnvioDePreguntasController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Este metodo envia todas las preguntas por nodejs.
     */
    private void enviarPreguntas() {
        
        int proceso = 0;
        for(int i = 0; i < preguntas.size(); i++) {
            
            enviarPreguntaCompleta(preguntas.get(i));
            proceso++;
        }
        
        if(proceso == preguntas.size()) {
            
            btnIniciar.setVisible(true);
            btnIniciar.setDisable(false);
        }
    }
    
}

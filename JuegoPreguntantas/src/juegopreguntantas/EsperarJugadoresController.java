package juegopreguntantas;

import clasesutilidad.JugadorConectadoEnvio;
import entity.Cuentainvitado;
import entity.Cuentausuario;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import persistencia.PersistenciaPartida;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          */ 
/* @since 06/11/2018                                              */
/* Nombre de la clase EsperarJugadoresController                  */
/******************************************************************/ 
public class EsperarJugadoresController implements Initializable {
    
    @FXML
    private ListView<JugadorConectadoEnvio> listJugadores;
    @FXML
    private Button btnIniciar;
    @FXML
    private Button btnCancelar;
    
    private final Socket socket;    
    private Object cuenta;
    private Cuentausuario usuario;
    private Cuentainvitado invitado;
    private String idioma;
    private JugadorConectadoEnvio conectado;
    private ObservableList<JugadorConectadoEnvio> conectados = 
            FXCollections.observableArrayList();
    ObservableList<JugadorConectadoEnvio> conectadosCopia = 
            FXCollections.observableArrayList();
    private int entrada;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        listJugadores.setItems(conectados);
        socket.emit("recuperarPreguntones");
    }
    
    /**
     * Constructor de la clase, se mantiene a la espera de llamadas.
     * @throws URISyntaxException 
     */
    public EsperarJugadoresController() throws URISyntaxException {

        ResourceBundle propiedadesCliente = ResourceBundle.getBundle(
                "utilidades.conexiones");
        String ipServidor = propiedadesCliente.getString(
                "key.ipServidor");
        socket = IO.socket("http://" + ipServidor + ":5000");
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... os) {

                System.out.println("Conectado y Esperando...");

            }
        }).on("recuperarPreguntones", new Emitter.Listener() {

            @Override
            public void call(Object... os) {

                JSONArray arregloRecuperado = (JSONArray) os[0];
                conectadosCopia = conectados;
                for (int i = 0; i < conectadosCopia.size(); i++) {

                    conectadosCopia.get(i).getNombre();
                }

                Platform.runLater(() -> {

                    conectados.removeAll(conectadosCopia);
                });

                for (int i = 0; i < arregloRecuperado.length(); i++) {

                    try {
                        
                        JSONObject jugador = 
                                (JSONObject) arregloRecuperado.get(i);
                        JugadorConectadoEnvio conectado = 
                                new JugadorConectadoEnvio();
                        String nombre = jugador.getString("nombre");
                        conectado.setNombre(nombre);
                        Platform.runLater(() -> {

                            agregarConectado(conectado);

                        });

                    } catch (JSONException ex) {

                        Logger.getLogger(EsperarJugadoresController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
                for (int i = 0; i < conectados.size(); i++) {

                    System.out.println(conectados.get(i).getNombre());
                }
            }
        }).on("inicioPartida", new Emitter.Listener() {

            @Override
            public void call(Object... os) {
                if (entrada == -1) {
                    Platform.runLater(() -> {

                        abrirVentanaResponderPregunta();
                    });
                }
            }
        });

        socket.connect();
    }

    /**
     * Este metodo es para cancelar el inicio de la partida y regresar a la
     * ventana anterior de Inicio de partida
     *
     * @param event del click del mouse
     */
    @FXML
    private void cancelar(ActionEvent event) {

        try {

            Locale.setDefault(new Locale(idioma));
            ResourceBundle resourceBundle = ResourceBundle
                    .getBundle("juegopreguntantas.lang/lang");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass()
                    .getResource("InicioPartida.fxml"));
            loader.setResources(resourceBundle);
            Parent esperaJugadores = loader.load();
            InicioPartidaController controller = loader.getController();
            controller.recibirParametros(usuario, idioma);
            Scene scene = new Scene(esperaJugadores);
            Stage stage = new Stage();
            stage.setTitle("Inicio de partida");
            stage.setScene(scene);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {

            Logger.getLogger(EsperarJugadoresController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

    /**
     * Este metodo es para cancelar iniciar la partida para todos los jugadores
     * @param event del click del mouse
     */
    @FXML
    private void iniciar(ActionEvent event) {
        
        socket.emit("iniciarPartida");
        try {

            Locale.setDefault(new Locale(idioma));
            ResourceBundle resourceBundle = ResourceBundle
                    .getBundle("juegopreguntantas.lang/lang");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass()
                    .getResource("EnvioDePreguntas.fxml"));
            loader.setResources(resourceBundle);
            Parent esperaJugadores = loader.load();
            EnvioDePreguntasController controller = loader.getController();
            controller.recibirParametros(usuario, idioma, entrada, conectados);
            Scene scene = new Scene(esperaJugadores);
            Stage stage = new Stage();
            stage.setTitle("Inicio de partida");
            stage.setScene(scene);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            
            Logger.getLogger(EsperarJugadoresController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }
    
    /**
     * Metodo que recibe el objeto de cuenta de usuario o invitado del 
     * Controlador de la pantalla que la invocó
     * @param cuenta objeto de la cuenta.
     * @param idioma Idioma del properties
     * @param entrada si es 1 entra como jugador, si es 2 entra como monitor.
     */
    public void recibirParametros(Object cuenta, String idioma, int entrada){
        
        Locale.setDefault(new Locale(idioma));
        this.idioma = idioma;
        if(cuenta instanceof Cuentausuario) {
            
            this.usuario = (Cuentausuario) cuenta;
        } else {
            
            this.invitado = (Cuentainvitado) cuenta;
        }
        
        this.cuenta = cuenta;
        this.entrada = entrada;
        if(entrada == -1) {
            
            btnCancelar.setDisable(true);
            btnCancelar.setVisible(false);
            btnIniciar.setDisable(true);
            btnIniciar.setVisible(false);
        }
    }
    
    /**
     * Este metodo verifica que no exista el nuevo jugador en la lista.
     * @param nuevo jugador para el envio y recibo.
     */
    private void agregarConectado(JugadorConectadoEnvio nuevo) {
        
        if(!conectados.contains(nuevo)) {
            conectados.add(nuevo);
        }
    }
    
    /**
     * Este metodo convierte aun jugador en un objeto tipo JSONObject y lo emite
     * para que aparezca en la lista de jugadores conectados en todas las
     * pantallas.
     */
    private void enviarConectado() {
        
        try {
            
            conectado = new JugadorConectadoEnvio();
            if(cuenta instanceof Cuentausuario) {
                
                conectado.setNombre(usuario.getNombreusuario());
            } else {
                
                conectado.setNombre(invitado.getNombre());
            }
            
            JSONObject nuevoConectado = new JSONObject();
            nuevoConectado.put("idSocket", socket.id());
            nuevoConectado.put("nombre", conectado.getNombre());
            socket.emit("registrarPregunton", nuevoConectado);
            conectados.add(conectado);
        } catch (JSONException ex) {
            
            Logger.getLogger(EsperarJugadoresController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Este metodo elimina la partida iniciada de la base de datos para que deje
     * de aparecer como disponibles para otros usuarios.
     */
    private void eliminarPartida() {
        
        PersistenciaPartida partida = new PersistenciaPartida();
    }
    
    private void abrirVentanaResponderPregunta() {
        
        try {

            Locale.setDefault(new Locale(idioma));
            ResourceBundle resourceBundle = ResourceBundle
                    .getBundle("juegopreguntantas.lang/lang");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass()
                    .getResource("ResponderPregunta.fxml"));
            loader.setResources(resourceBundle);
            Parent esperaJugadores = loader.load();
            ResponderPreguntaController controller = loader.getController();
            controller.recibirParametros(usuario, idioma);
            Scene scene = new Scene(esperaJugadores);
            Stage stage = new Stage();
            stage.setTitle("ResponderPregunta");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            
            Logger.getLogger(EsperarJugadoresController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }
}

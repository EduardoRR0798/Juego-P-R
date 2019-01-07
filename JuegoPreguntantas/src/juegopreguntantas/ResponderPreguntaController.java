package juegopreguntantas;

import clasesutilidad.JugadorConectadoEnvio;
import entity.Cuentainvitado;
import entity.Cuentausuario;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import clasesutilidad.PreguntaEnvio;
import clasesutilidad.PuntajeEnvio;
import clasesutilidad.RespuestaEnvio;
import com.google.gson.Gson;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javax.xml.bind.DatatypeConverter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utilidades.UtilidadCadenas;

/******************************************************************
 * @version 1.0                                                   *
 * @author Eduardo Rosas Rivera                                   * 
 * @since 28/11/2018                                              *
 * Nombre de la clase ResponderPreguntaController                 *
 *****************************************************************/
public class ResponderPreguntaController implements Initializable {

    @FXML
    private TextField tfMensaje;
    @FXML
    private Button btnEnviar;
    @FXML
    private TextArea txtChat;
    @FXML
    private ImageView imgPregunta;
    @FXML
    private ImageView imgA;
    @FXML
    private ImageView imgB;
    @FXML
    private ImageView imgC;
    @FXML
    private ImageView imgD;
    @FXML
    private Label lblPuntaje;
    @FXML
    private Label lblTiempo;
    @FXML
    private Label lbPuntajeNum;
    @FXML
    private Label lblPreguntaNo;
    @FXML
    private Label lblCategoria;
    @FXML
    private Label lblPregunta;
    @FXML
    private CheckBox chbOpcionA;
    @FXML
    private CheckBox chbOpcionB;
    @FXML
    private CheckBox chbOpcionC;
    @FXML
    private CheckBox chbOpcionD;
    @FXML
    private Pane pPanelGrafica;
    @FXML
    private Pane pPanelGanador;
    @FXML
    private Label lblAnunciador;
    @FXML
    private Label lblNumeroPregunta;
    @FXML
    private Label lblGanador;
    @FXML
    private Label lblBuenJuego;
    @FXML
    private BarChart<?, ?> bcGraficaPuntaje;
    @FXML
    private ProgressBar pbRestante;
    @FXML
    private Button btnVolver;
    @FXML
    private Pane pnlEspera;
    @FXML
    private ProgressBar pbEspera;
    
    private static final Integer TIEMPORESPUESTA = 15;
    private static final Integer TIEMPOGRAFICA = 10;
    private final ArrayList<String> mensajesChat = new ArrayList<>();
    private final Socket socket;
    private Object cuenta;
    private String idioma;
    private Cuentausuario usuario;
    private Cuentainvitado invitado;
    private int tiempoRestante;
    private int tiempoTotalGrafica;
    private List<PreguntaEnvio> preguntas = new ArrayList<>();
    private List<RespuestaEnvio> respuestas = new ArrayList<>();
    private Timer timer;
    private Timer timerGraph;
    private PreguntaEnvio preguntaActual;
    private boolean activacion = true;
    private final ObservableList<JugadorConectadoEnvio> participantes = 
            FXCollections.observableArrayList();
    private int contador = 0;
    private boolean creador;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        socket.emit("recuperarPreguntones");
        activarPanelGanador(false);
        activarRespuestaEscrita(false);
        UtilidadCadenas cadena = new UtilidadCadenas();
        cadena.limitarCampos(tfMensaje, 70);
    }    
    
    /**
     * Constructor de la clase.
     * @throws URISyntaxException 
     */
    public ResponderPreguntaController() throws URISyntaxException {
        
        ResourceBundle propiedadesCliente = ResourceBundle.getBundle(
                "utilidades.conexiones");
        String ipServidor = propiedadesCliente.getString("key.ipServidor");
        socket  = IO.socket("http://" + ipServidor + ":4000");
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            
            @Override
            public void call(Object... os) { 
                //No es necesario hacer algo.
            }
        }).on("reciboMensaje", new Emitter.Listener(){
            @Override

            public void call(Object... os) {
                
                String mensajeRecibido = (String) os[0];
                mostrarMensaje(mensajeRecibido);
            }
        }).on("reciboPregunta", new Emitter.Listener() {

            @Override
            public void call(Object... os) {

                String preguntaRecibida = (String) os[0];
                Platform.runLater(() -> recibirPregunta(preguntaRecibida));
            }

        }).on("inicioPartida", new Emitter.Listener() {

            @Override
            public void call(Object... os) {
                if(!creador) {

                    Platform.runLater(() -> {

                        activacion = true;
                        desactivarPanelEspera(activacion);
                        verificarPregunta();
                    });
                }
                
            }
        }).on("recuperoPreguntones", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                
                JSONArray recuperados = (JSONArray) os[0];
                Platform.runLater(() -> recuperarPreguntones(recuperados));
            }
        }).on("recibirRespuesta", new Emitter.Listener() {
            
            @Override
            public void call(Object... os) {
                
                String respuesta = (String) os[0];
                Platform.runLater(() -> recibirRespuesta(respuesta));
            }
        }).on("finalizoPartida", new Emitter.Listener() {
            
            @Override
            public void call(Object... os) {
                
                if(creador) {
                    
                    Platform.runLater(() -> {
                        
                        activarPanelGanador(true);
                        creador = false;
                    });
                }
            }
        });
        socket.connect();
    }
    
    /**
     * Este metodo es para disminuir la lista de preguntas
     */
    private void disminuirPreguntas() {
        
        preguntas.remove(0);
        verificarPregunta();
    }
    
    /**
     * Este metodo verifica que la pregunta tiene algo, de ser nula termina el 
     * juego.
     */
    private void verificarPregunta() {
        
        if(preguntas.isEmpty()) {
            
            socket.emit("finalizarPartida");
            activarPanelGanador(true);
        } else {
            
            preguntaActual = preguntas.get(0);
            Platform.runLater(() -> lblNumeroPregunta.setText(Integer.toString(
                    Integer.parseInt(lblNumeroPregunta.getText()) + 1)));
            respuestas = preguntaActual.getRespuestas();
            definirTipoPregunta();
        }
    }
    
    /**
     * Este metood sirve para definir si la pregunta recibida es texto(1) o es
     * imagen(2).
     */
    private void definirTipoPregunta() {

        if (preguntaActual.getTipoPregunta() == 1) {

            Platform.runLater(() -> 
                    lblPregunta.setText(preguntaActual.getPregunta()));
            imgPregunta.setVisible(false);
            imgPregunta.setDisable(true);
        } else {

            try {
                
                String ruta = "PartidaActual\\";
                String imagenPregunta;
                File filePregunta = new File(preguntaActual.getPregunta());
                imagenPregunta = ruta + filePregunta.getName();
                crearImagen(imagenPregunta, preguntaActual.getImagenByte());
                filePregunta = new File(imagenPregunta);
                Image imagePregunta = new Image(filePregunta.toURI()
                        .toString());
                imgPregunta.setImage(imagePregunta);
            } catch (IOException ex) {
                
                Logger.getLogger(ResponderPreguntaController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        
        definirTipoRespuesta();
    }
    
    /**
     * Este metodo verifica si la primera respuesta es de tipo imagen, de ser 
     * asi activa los imageView, de otra forma, activa los checkBox.
     */
    private void definirTipoRespuesta() {

        if(respuestas.get(0).getTipoRespuesta() == 2) {

            activarRespuestaImagen(true);
            activarRespuestaEscrita(false);
            fijarImagenes();
        } else {
            
            activarRespuestaEscrita(true);
            activarRespuestaImagen(false);
            fijarTextos();
        } 
    }
        
    /**
     * Este metodo fija las imagenes en los imageView en caso deque las
     * respuestas sean imagen.
     */
    private void fijarImagenes() {

        try {
            String ruta = "PartidaActual\\";
            String imagenA;
            String imagenB;
            String imagenC;
            String imagenD;
            
            File file1 = new File(respuestas.get(0).getRespuesta());
            File file2 = new File(respuestas.get(1).getRespuesta());
            File file3 = new File(respuestas.get(2).getRespuesta());
            File file4 = new File(respuestas.get(3).getRespuesta());
            
            imagenA = ruta + file1.getName();
            crearImagen(imagenA, respuestas.get(0).getImagenByte());
            file1 = new File(imagenA);
            
            imagenB = ruta + file2.getName();
            crearImagen(imagenB, respuestas.get(1).getImagenByte());
            file2 = new File(imagenB);
            
            imagenC = ruta + file3.getName();
            crearImagen(imagenC, respuestas.get(2).getImagenByte());
            file3 = new File(imagenC);
            
            imagenD = ruta + file4.getName();
            crearImagen(imagenD, respuestas.get(3).getImagenByte());
            file4 = new File(imagenD);
            
            Image imageA = new Image(file1.toURI().toString());
            Image imageB = new Image(file2.toURI().toString());
            Image imageC = new Image(file3.toURI().toString());
            Image imageD = new Image(file4.toURI().toString());
            
            imgA.setImage(imageA);
            imgB.setImage(imageB);
            imgC.setImage(imageC);
            imgD.setImage(imageD);
            contarRegresivamente();
        } catch (IOException ex) {
            
            Logger.getLogger(ResponderPreguntaController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Este metodo habilita los imageView cuando se trata de una pregunta con
     * una respuesta de imagenes.
     * @param permiso true si deben ser visibles y activos, false si no.
     */
    private void activarRespuestaImagen(boolean permiso) {

        imgA.setVisible(permiso);
        imgA.setDisable(!permiso);
        imgB.setVisible(permiso);
        imgB.setDisable(!permiso);
        imgC.setVisible(permiso);
        imgC.setDisable(!permiso);
        imgD.setVisible(permiso);
        imgD.setDisable(!permiso);
    }

    /**
     * Este metodo fija el texto de la pregunta y las respuestas.
     */
    private void fijarTextos() {

        chbOpcionA.setSelected(false);
        chbOpcionB.setSelected(false);
        chbOpcionC.setSelected(false);
        chbOpcionD.setSelected(false);
        Platform.runLater(() -> chbOpcionA.setText(respuestas.get(0).
                getRespuesta()));
        Platform.runLater(() -> chbOpcionB.setText(respuestas.get(1).
                getRespuesta()));
        Platform.runLater(() -> chbOpcionC.setText(respuestas.get(2).
                getRespuesta()));
        Platform.runLater(() -> chbOpcionD.setText(respuestas.get(3).
                getRespuesta()));
        contarRegresivamente();
    }

    /**
     * Este metodo habilita los checkBox cuando se trata de una pregunta con
     * respuestas escritas.
     * @param permiso true si deben ser visibles y activos, false si no.
     */
    private void activarRespuestaEscrita(boolean permiso) {

        chbOpcionA.setDisable(!permiso);
        chbOpcionA.setVisible(permiso);
        chbOpcionB.setDisable(!permiso);
        chbOpcionB.setVisible(permiso);
        chbOpcionC.setDisable(!permiso);
        chbOpcionC.setVisible(permiso);
        chbOpcionD.setDisable(!permiso);
        chbOpcionD.setVisible(permiso);

    }

    /**
     * Este metodo sirve para enviar un mensaje en el chat.
     * @param event Clic en el boton Enviar.
     */
    @FXML
    private void enviar(ActionEvent event) {

        String nombre;
        String mensaje = tfMensaje.getText().trim();
        if (cuenta instanceof Cuentausuario) {

            nombre = usuario.getNombreusuario();
        } else {

            nombre = invitado.getNombre();
        }
        tfMensaje.clear();
        if (!Objects.equals(mensaje, "")) {

            String mensajeEnviado = nombre + ": " + mensaje;
            socket.emit("envioMensaje", mensajeEnviado);
            mostrarMensaje(mensajeEnviado);
        }

    }
    
    /**
     * Este metodo sirve para imprimir en el TextArea del chat todos lo mensajes
     * escritos.
     * @param mensaje mensaje a enviar.
     */
    private void mostrarMensaje(String mensaje) {
        
        mensajesChat.add(mensaje);
        String chatContenido;
        StringBuilder sb = new StringBuilder();
        
        for(int i = 0; i < mensajesChat.size(); i++){
            
            sb.append(mensajesChat.get(i)).append("\n");
        }
        chatContenido = sb.toString();
        txtChat.setText(chatContenido);
        
    }
    
    @FXML
    private void seleccionarA(MouseEvent event) {
    
        bloquearRespuestas();
        asignarPuntaje(respuestas.get(0));
    }

    @FXML
    private void seleccionarB(MouseEvent event) {
    
        bloquearRespuestas();
        asignarPuntaje(respuestas.get(1));
    }

    @FXML
    private void seleccionarC(MouseEvent event) {
    
        bloquearRespuestas();
        asignarPuntaje(respuestas.get(2));
    }

    @FXML
    private void seleccionarD(MouseEvent event) {
    
        bloquearRespuestas();
        asignarPuntaje(respuestas.get(3));
    }
    
    /**
     * Este metodo desabilita el panel que muestra al ganador.
     * @param visibilidad true si seran visibles los componentes, false si no.
     * @param activacion true si estaran desactivados, false si no.
     */
    private void activarPanelGanador(boolean visibilidad) {
        
        pPanelGanador.setVisible(visibilidad);
        pPanelGanador.setDisable(!visibilidad);
        lblGanador.setDisable(!visibilidad);
        lblAnunciador.setDisable(!visibilidad);
        lblBuenJuego.setDisable(!visibilidad);
        lblGanador.setVisible(visibilidad);
        lblAnunciador.setVisible(visibilidad);
        lblBuenJuego.setVisible(visibilidad);
        if(visibilidad) {
            
            mostrarNombreGanador();
        }
    }
    
    /**
     * Este metodo muestra el nombre del jugador que tuvo el mayor puntaje y es
     * el ganador
     */
    public void mostrarNombreGanador() {
        
        ArrayList<Integer> puntajes = new ArrayList<>();
        for(int i = 0; i < participantes.size(); i++) {
            
            puntajes.add(participantes.get(i).getPuntaje());
        }
        List<String> ganador = new ArrayList<>();
        Collections.max(puntajes);
        for(int j = 0; j < participantes.size(); j++) {
                
            if(puntajes.get(puntajes.size() - 1) == participantes.get(j).getPuntaje()) {
                
                ganador.add(participantes.get(j).getNombre());
            }
        }
        StringBuilder sb = new StringBuilder();
        String winner;
        if(ganador.size() > 1) {
            
            for(int e = 0; e < ganador.size(); e++) {    
                
                sb.append(ganador.get(e)).append(", ");
            }
            winner = sb.toString();
        } else {
            winner = ganador.get(0);
        }
        final String ganadorText = winner;
        Platform.runLater(() -> lblGanador.setText(ganadorText));
    }
    
    /**
     * Este metodo deshabilita el panel que muestra la grafica de los jugadores.
     * @param visibilidad true si seran visibles los componentes, false si no.
     * @param activacion true si estaran desactivados, false si no.
     */
    private void activarPanelGrafica(boolean visibilidad) {

        pPanelGrafica.setVisible(visibilidad);
        bcGraficaPuntaje.setVisible(visibilidad);
        pPanelGrafica.setDisable(!visibilidad);
        bcGraficaPuntaje.setDisable(!visibilidad);
        if (visibilidad) {
            
            crearGrafica();
        }
        
    }
    
    /**
     * Metodo que bloquea los botones de respuestas una vez que ha respondido 
     * para que no pueda volver a responder
     */
    public void bloquearRespuestas() {
        
        imgA.setDisable(true);
        imgB.setDisable(true);
        imgC.setDisable(true);
        imgD.setDisable(true);
        chbOpcionA.setDisable(true);
        chbOpcionB.setDisable(true);
        chbOpcionC.setDisable(true);
        chbOpcionD.setDisable(true);
    }
    
    /**
     * Este metodo es para asignar el puntaje segun la respuesta elegida
     * @param respuestaSeleccionada respuesta seleccionada.
     */
    public void asignarPuntaje(RespuestaEnvio respuestaSeleccionada){
        
        PuntajeEnvio puntajeE = new PuntajeEnvio();
        puntajeE.setPuntaje(respuestaSeleccionada.getPuntaje());
        String nombre;
        if(cuenta instanceof Cuentausuario) {
            
            nombre = usuario.getNombreusuario();
        } else {
            
            nombre = invitado.getNombre();
        }
        puntajeE.setUsuario(nombre);
        Gson puntajeEnvio = new Gson();
        String out = puntajeEnvio.toJson(puntajeE);
        socket.emit("enviarRespuesta", out);
        timer.cancel();
    }
    
    /**
     * Este metodo es para crear la gráfica con las puntuaciones hasta el 
     * momento de llamar la gráfica
     */
    private void crearGrafica() {

        XYChart.Series datosGrafica = new XYChart.Series();
        for(int i = 0; i < participantes.size(); i++) {
            
            datosGrafica.getData().add(new XYChart.Data(
                    participantes.get(i).getNombre(), 
                    participantes.get(i).getPuntaje()));
        }
        bcGraficaPuntaje.getData().setAll(datosGrafica);
    }
    
    /**
     * Este metodo lleva la cuenta regresiva para responder una pregunta y anima
     * el label.
     * @return el segundo en que el usuario respondio la pregunta.
     */
    private void contarRegresivamente() {
        
        timer = new Timer();
        tiempoRestante = TIEMPORESPUESTA;
        timer.schedule(new TimerTask() {
            
            @Override
            public void run() {

                if (tiempoRestante > 0) {

                    Platform.runLater(() -> lblTiempo.setText(
                            Integer.toString(tiempoRestante)));
                    tiempoRestante--;
                } else {
                    
                    timer.cancel();
                    RespuestaEnvio sinSeleccion = new RespuestaEnvio();
                    sinSeleccion.setPuntaje(-1);
                    asignarPuntaje(sinSeleccion);
                }
            }
        }, 1000, 1000); 
    }
    
    /**
     * Este metodo lleva la cuenta regresiva de mostrar la gráfica.
     */
    private void contarRegresivamenteGrafica() {
        
        timerGraph = new Timer();
        tiempoTotalGrafica = TIEMPOGRAFICA;
        timerGraph.schedule(new TimerTask() {
            
            @Override
            public void run() {

                if (tiempoTotalGrafica > 0) {

                    Platform.runLater(() -> activarPanelGrafica(true));
                    tiempoTotalGrafica--;
                } else {
                    
                    timerGraph.cancel();
                    activarPanelGrafica(false);
                    disminuirPreguntas();
                }
            }
        }, 1000, 1000);
    }
    
    @FXML
    private void volverAMenu(ActionEvent event) {
            
        try {
            
            Locale.setDefault(new Locale(idioma));
            ResourceBundle resourceBundle = ResourceBundle.getBundle(
                    "juegopreguntantas.lang/lang");          
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MenuPrincipal.fxml"));
            loader.setResources(resourceBundle);
            Parent menu = loader.load();
            
            MenuPrincipalController controller = loader.getController();
            controller.recibirParametros(cuenta, idioma);
            
            Scene scene = new Scene(menu);
            Stage stage = new Stage();
            
            stage.setScene(scene);
            stage.show();
            socket.disconnect();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException ex) {
            
            Logger.getLogger(ResponderPreguntaController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Este metodo crea la imagen en la ruta especificada.
     * @param ruta ruta de donde se creara la imagen.
     * @param imagen arreglo de bytes que compone la imagen.
     * @throws IOException 
     */
    private void crearImagen(String ruta, byte[] imagen) throws IOException {

        try(OutputStream out = 
                new BufferedOutputStream(new FileOutputStream(ruta))) {
            
            out.write(imagen);
        } catch (IOException ex) {
            
            Logger.getLogger(ResponderPreguntaController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Este metodo desactiva el panel de espera del envio de las preguntas.
     */
    private boolean desactivarPanelEspera(boolean activacion) {
        
        pnlEspera.setVisible(!activacion);
        pnlEspera.setDisable(activacion);
        pbEspera.setVisible(!activacion);
        pnlEspera.setDisable(activacion);
        return activacion;
    }
    
    /**
     * Este metodo sirve para cambiar el puntaje del label.
     */
    private void cambiarPuntaje() {
        
        String nombreUsuario;
        if(cuenta instanceof Cuentausuario) {
            
            nombreUsuario = usuario.getNombreusuario();
        } else {
            
            nombreUsuario = invitado.getNombre();
        }
        for(int i = 0; i < participantes.size(); i++) {
            
            if(nombreUsuario.equals(participantes.get(i).getNombre())) {
                
                String puntajeNuevo = Integer.toString(participantes.get(i)
                        .getPuntaje());
                final String puntajeFinal = puntajeNuevo;
                Platform.runLater(() -> lbPuntajeNum.setText(puntajeFinal));
            }
        }
    }
    /**
     * Este metodo recibe una pregunta del objeto JSON.
     * @param preguntaRecibida Pregunta recibida en string
     */
    private void recibirPregunta(String preguntaRecibida) {

        Gson preguntaGson = new Gson();

        PreguntaEnvio nueva
                = preguntaGson.fromJson(preguntaRecibida,
                        PreguntaEnvio.class);
        if (nueva.getTipoPregunta() == 2) {

            nueva.setImagenByte(DatatypeConverter.parseBase64Binary(nueva.getImagen()));
        }
        for (int i = 0; i < nueva.getRespuestas().size(); i++) {

            if (nueva.getRespuestas().get(i).getTipoRespuesta() == 2) {

                nueva.getRespuestas().get(i).setImagenByte(
                        DatatypeConverter.parseBase64Binary(
                                nueva.getRespuestas().get(i).getImagen()));
            }
        }
        preguntas.add(nueva);
    }
    
    /**
     * Este metodo recupera la lista de juegadores del sevidor.
     * @param recuperados JSONArray recuperado.
     */
    private void recuperarPreguntones(JSONArray recuperados) {

        for (int i = 0; i < recuperados.length(); i++) {

            try {

                JSONObject jugador = (JSONObject) recuperados.get(i);
                String nombre = jugador.getString("nombre");
                JugadorConectadoEnvio nuevo
                        = new JugadorConectadoEnvio();
                nuevo.setNombre(nombre);
                nuevo.setPuntaje(0);

                Platform.runLater(() -> participantes.add(nuevo));

            } catch (JSONException ex) {

                Logger.getLogger(ResponderPreguntaController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Este metodo sirve para recibir una respuesta y anadirla a la lista.
     * @param respuesta respuesta recuperada en string.
     */
    private void recibirRespuesta(String respuesta) {

        Gson recibida = new Gson();
        PuntajeEnvio nuevoPuntaje
                = recibida.fromJson(respuesta, PuntajeEnvio.class);
        int indice = 0;
        for (int i = 0; i < participantes.size(); i++) {

            if (participantes.get(i).getNombre().equals(nuevoPuntaje.getUsuario())) {

                indice = i;
            }
        }
        if (nuevoPuntaje.getPuntaje() == 1) {

            if (contador == 0) {

                participantes.get(indice).sumarPuntos(2);
            } else {

                participantes.get(indice).sumarPuntos(1);
            }
        } else {

            participantes.get(indice).sumarPuntos(nuevoPuntaje.getPuntaje());
        }
        cambiarPuntaje();
        contador++;
        if (contador == participantes.size()) {

            if (!creador) {

                contarRegresivamenteGrafica();
            } else {

                crearGrafica();
            }
            contador = 0;
        }
    }
    /**
     * Metodo que recibe el objeto de cuenta de usuario o invitado del
     * Controlador de la pantalla de Login.
     * @param cuenta Cuenta de invitado o usuario registrado.
     * @param idioma idioma del properties.
     * @param creador metodo que valida si el jugador es el creador o no.
     */
    public void recibirParametros(Object cuenta, String idioma, boolean creador) {
        
        Locale.setDefault(new Locale(idioma));
        this.idioma = idioma;
        this.cuenta = cuenta;
        this.creador = creador;
        if (creador) {

            activacion = false;
            desactivarPanelEspera(true);
            activarPanelGrafica(true);
            socket.emit("vaciarLista");
        }
        
        if (cuenta instanceof Cuentausuario) {

            this.usuario = (Cuentausuario) cuenta;
        } else {

            this.invitado = (Cuentainvitado) cuenta;
        }
    }
}
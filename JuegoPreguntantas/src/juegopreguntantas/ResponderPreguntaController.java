/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegopreguntantas;

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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javax.xml.bind.DatatypeConverter;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Eduardo Rosas Rivera                                   */ 
/* @since 28/11/2018                                              */
/* Nombre de la clase ResponderPreguntaController                 */
/******************************************************************/
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
    private static final Integer TIEMPOGBARRAS = 5;
    private final PuntajeEnvio puntaje = new PuntajeEnvio(0);
    private final PuntajeEnvio puntajeTotal = new PuntajeEnvio(0);
    private final ArrayList<String> mensajesChat = new ArrayList<>();
    private final CategoryAxis caJugadores = new CategoryAxis();
    private final NumberAxis caPuntos = new NumberAxis();
    private final ObservableList<String> jugadores = 
            FXCollections.observableArrayList();
    private final Socket socket;
    private final List<PuntajeEnvio> puntosTotalContrario = new ArrayList<>();
    private final XYChart.Series barraPropia = new XYChart.Series<>();
    private final XYChart.Series barraContrarios = new XYChart.Series<>();
    private Object cuenta;
    private String idioma;
    private Cuentausuario usuario;
    private Cuentainvitado invitado;
    private int tiempoRestante;
    private int tiempoGrafica;
    private List<PreguntaEnvio> preguntas = new ArrayList<>();
    private List<RespuestaEnvio> respuestas = new ArrayList<>();
    private PuntajeEnvio puntajeContrario = new PuntajeEnvio();
    private List<PuntajeEnvio> puntosContrario = new ArrayList<>();
    private List<Integer> tiempoRespuestas = new ArrayList<>();
    private int noJugadores = 0;
    private int noJugadoresRespuesta = 0;
    private Timer timer;
    private Timer timerGraph;
    private PreguntaEnvio preguntaActual;
    private boolean activacion = true;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        caJugadores.setLabel("Jugadores");
        caPuntos.setLabel("Puntos");
        barraPropia.setName("Tu");
        barraContrarios.setName("Los otros");
        bcGraficaPuntaje.setTitle("tabla puntos");
        activarPanelGanador(false);
        activarRespuestaEscrita(false);
        limitarCampos(tfMensaje, 70);
    }    
    
    /**
     * Constructor de la clase.
     * @throws URISyntaxException 
     */
    public ResponderPreguntaController() throws URISyntaxException {
        
        ResourceBundle propiedadesCliente = ResourceBundle.getBundle(
                "utilidades.conexiones");
        String ipServidor = propiedadesCliente.getString(
                "key.ipServidor");
        socket  = IO.socket("http://" + ipServidor + ":4000");
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override

            public void call(Object... os) {
                noJugadores++;
                socket.emit("nuevoUsuario");
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
                Gson preguntaGson = new Gson();

                PreguntaEnvio nueva
                        = preguntaGson.fromJson(preguntaRecibida,
                                PreguntaEnvio.class);
                for (int i = 0; i < nueva.getRespuestas().size(); i++) {

                    if (nueva.getRespuestas().get(i).getTipoRespuesta() == 2) {

                        nueva.getRespuestas().get(i).setImagenByte(
                                DatatypeConverter.parseBase64Binary(
                                    nueva.getRespuestas().get(i).getImagen()));
                    }
                }
                preguntas.add(nueva);
            }

        }).on("tiempoRespuesta", new Emitter.Listener() {
            @Override

            public void call(Object... os) {
                int tiempoCompetidor = (int) os[0];
                tiempoRespuestas.add(tiempoCompetidor);
            }
        }).on("agregarUsuario", new Emitter.Listener(){
            @Override

            public void call(Object... os) {
                noJugadores++;
                socket.emit("agregarMiUsuario");
            }
        }).on("agregarSuUsuario", new Emitter.Listener(){
            @Override

            public void call(Object... os) {
                noJugadores++;
            }
        }).on("aumentarNoResp", new Emitter.Listener(){
            @Override

            public void call(Object... os) {
                noJugadoresRespuesta++;
            }
        }).on("calcularPrimeroResp", new Emitter.Listener(){
            @Override

            public void call(Object... os) {
                determinarPrimerRespuesta();
            }
        }).on("puntajeContrario", new Emitter.Listener(){
            @Override

            public void call(Object... os) {
                puntajeContrario.setPuntaje((int) os[0]);
                puntajeContrario.setUsuario((String) os[1]);
                puntosContrario.add(puntajeContrario);
            }
        }).on("agregarTotalContrario", new Emitter.Listener(){
            @Override

            public void call(Object... os) {
                PuntajeEnvio puntajeTotalContrario = new PuntajeEnvio();
                puntajeTotalContrario.setPuntaje((int) os[0]);
                puntajeTotalContrario.setUsuario((String) os[1]);
                agregarTotalContrarios(puntajeTotalContrario);
                finalizarPregunta();
            }
        }).on("nuevoPuntaje", new Emitter.Listener(){
            @Override

            public void call(Object... os) {
                puntajeContrario.setUsuario((String) os[0]);
                puntajeContrario.setPuntaje(puntajeContrario.
                                getPuntaje() + 1);
                
            }
        }).on("agregarPuntajeTotal", new Emitter.Listener(){
            @Override

            public void call(Object... os) {
                agregarPuntajeTotal();
            }
        }).on("inicioPartida", new Emitter.Listener() {

            @Override
            public void call(Object... os) {

                Platform.runLater(() -> {
                    
                    activacion = true;
                    desactivarPanelEspera(activacion);
                    verificarPregunta();
                });
            }
        });
        
        socket.connect();
    }
    
    /**
     * Este metodo es para disminuir la lista de preguntas
     */
    private void disminuirPreguntas() {

        preguntas.remove(0);
        vaciarBarras();
        verificarPregunta();
    }
    
    /**
     * Este metodo verifica que la pregunta tiene algo, de ser nula termina el 
     * juego.
     */
    private void verificarPregunta() {
        
        if(preguntas.isEmpty()) {
            
            activarPanelGanador(true);
        } else {
            
            preguntaActual = preguntas.get(0);
            Platform.runLater(() -> lblNumeroPregunta.setText(Integer.toString(
                    Integer.parseInt(lblNumeroPregunta.getText()) + 1)));
            respuestas = (List<RespuestaEnvio>) preguntaActual.getRespuestas();
            definirTipoPregunta();
        }
    }
    
    /**
     * Este metood sirve para definir si la pregunta recibida es texto(1) o es
     * imagen(2).
     */
    private void definirTipoPregunta() {

        if (preguntaActual.getTipoPregunta() == 1) {

            Platform.runLater(() -> lblPregunta.setText(preguntaActual.
                    getPregunta()));
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
            Logger.getLogger(ResponderPreguntaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Este metodo habilita los imageView cuando se trata de una pregunta con
     * una respuesta de imagenes.
     *
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
     *
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
     *
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
        String chatContenido = "";
        for(int i = 0; i < mensajesChat.size(); i++){
            
            chatContenido = chatContenido + mensajesChat.get(i) + "\n";
        }
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
     * Metodo que recibe el objeto de cuenta de usuario o invitado del
     * Controlador de la pantalla de Login.
     * @param cuenta Cuenta de invitado o usuario registrado.
     * @param idioma idioma del properties.
     */
    public void recibirParametros(Object cuenta, String idioma) {
        
        Locale.setDefault(new Locale(idioma));
        this.idioma = idioma;
        this.cuenta = cuenta;
        if(cuenta instanceof Cuentausuario) {
            
            this.usuario = (Cuentausuario) cuenta;
        } else {
            
            this.invitado = (Cuentainvitado) cuenta; 
        }
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
        if(visibilidad){
            
            mostrarNombreGanador();
        }
    }
    
    /**
     * Este metodo muestra el nombre del jugador que tuvo el mayor puntaje y es
     * el ganador
     */
    public void mostrarNombreGanador() {
        
        String nombre = puntajeTotal.getUsuario();
        for(int i = 0; i < puntosTotalContrario.size(); i++){
            
            if (puntajeTotal.getPuntaje() < puntosTotalContrario.get(i).getPuntaje()) {
                
                nombre = puntosTotalContrario.get(i).getUsuario();
            } else if (puntajeTotal.getPuntaje() == puntosTotalContrario.get(i).getPuntaje()){
                
                nombre = nombre + " y " + puntosTotalContrario.get(i).getUsuario();
            }
        }
        String nombreGanador = nombre;
        Platform.runLater(() -> lblGanador.setText(nombreGanador));
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
     * @param respuestaSel La respuesta elegida.
     */
    public void asignarPuntaje(RespuestaEnvio respuestaSel){//resultados esperados para el que primero respondio
        puntaje.setPuntaje(respuestaSel.getPuntaje());
        String nombre;
        if(cuenta instanceof Cuentausuario) {
            
            nombre = usuario.getNombreusuario();
        } else {
            
            nombre = invitado.getNombre();
        }
        puntaje.setUsuario(nombre);
        socket.emit("tiempoRespCorrecto", tiempoRestante);
        noJugadoresRespuesta++;
        socket.emit("jugadorResp");
        socket.emit("puntajeObtenido", puntaje.getPuntaje(), puntaje.getUsuario());
        if (noJugadoresRespuesta == noJugadores) {
            
            socket.emit("determinarPrimero");
            determinarPrimerRespuesta();
            socket.emit("agregarPuntosTotal");
            agregarPuntajeTotal();
        }
    }
    
    /**
     * Este metodo es para determinar quien obitene el punto extra por responder
     * correctamente primero
     */
    public void determinarPrimerRespuesta() {
        
        if (puntaje.getPuntaje() > 0) {

            boolean primero = false;
            for (int i = 0; i < tiempoRespuestas.size(); i++) {

                if (puntosContrario.get(i).getPuntaje() > 0) {

                    primero = tiempoRestante > tiempoRespuestas.get(i);
                }

            }

            if (primero == true) {
                
                puntaje.setPuntaje(puntaje.getPuntaje() + 1);
                socket.emit("actualizarPuntaje", puntaje.getUsuario());
            }
        }
        
    }

    /**
     * Este metodo es para parar el tiempo de responder de la pregunta y 
     * continuar a la gráfica
     */
    private void finalizarPregunta() {
        
        Platform.runLater(() -> lbPuntajeNum.setText(Integer.toString(
                puntajeTotal.getPuntaje())));
        puntosContrario.clear();
        tiempoRespuestas.clear();
        timer.cancel();
        activarPanelGrafica(true);
    }
    
    /**
     * Este metodo es para agregar el puntaje de la respuesta a el puntaje en
     * general
     */
    private void agregarPuntajeTotal() {
        
        puntajeTotal.setPuntaje(puntajeTotal.getPuntaje() + 
                puntaje.getPuntaje());
        puntajeTotal.setUsuario(puntaje.getUsuario());
        socket.emit("puntosTotalContrario", puntajeTotal.getPuntaje(), puntajeTotal.getUsuario());
    }
    
    public void agregarTotalContrarios(PuntajeEnvio totalPuntos) {//checar totalPuntos, puntajeTotal, puntosTotalContrario, si ya imprimio el Entro aqui----
        
        if (!puntosTotalContrario.isEmpty()) {
            
            for (int i = 0; i < puntosTotalContrario.size(); i++) {

                if ((totalPuntos.getUsuario()).
                        equals(puntosTotalContrario.get(i).getUsuario())) {

                    puntosTotalContrario.get(i).setPuntaje(
                            totalPuntos.getPuntaje());
                }

            }
            
        } else {
            
            puntosTotalContrario.add(totalPuntos);
        }
    }
    
    /**
     * Este metodo es para crear la gráfica con las puntuaciones hasta el 
     * momento de llamar la gráfica
     */
    private void crearGrafica() {
        
        contarRegresGrafica();
        barraPropia.getData().add(new XYChart.Data(puntaje.getUsuario(), 
                puntajeTotal.getPuntaje()));
        for(int i= 0; i < puntosTotalContrario.size(); i++){
            
            barraContrarios.getData().add(new XYChart.Data(puntosTotalContrario.
                    get(i).getUsuario(), 
                    puntosTotalContrario.get(i).getPuntaje()));///7777777777777777777777777777777777777777777
        }
        
        if(noJugadores < 2){
            
            Platform.runLater(() -> bcGraficaPuntaje.getData().
                    addAll(barraPropia));
        } else {
            
            Platform.runLater(() -> bcGraficaPuntaje.getData().
                    addAll(barraPropia, barraContrarios));
        }
        
        noJugadoresRespuesta = 0;
    }
    
    /**
     * Este metodo es para reestablecer las barras de la gráfica
     */
    private void vaciarBarras() {
        
        Platform.runLater(() -> bcGraficaPuntaje.getData().clear());
        Platform.runLater(() -> barraPropia.getData().clear());
        if(!barraContrarios.getData().isEmpty()){
            
            Platform.runLater(() -> barraContrarios.getData().remove(0));
        }
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

                    Platform.runLater(() -> {
                        lblTiempo.setText(
                                Integer.toString(tiempoRestante));
                    });
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
    private void contarRegresGrafica() {
        
        timerGraph = new Timer();
        tiempoGrafica = TIEMPOGBARRAS;
        timerGraph.schedule(new TimerTask() {
            
            @Override
            public void run() {

                if (tiempoGrafica > 0) {
                    
                    tiempoGrafica--;
                } else {

                    timerGraph.cancel();
                    activarPanelGrafica(false);
                    disminuirPreguntas();
                }
                
            }
        }, 1000, 1000); 
    }

    private void inicializarPregunta() {
        
        /*PersistenciaPregunta preguntaBD = new PersistenciaPregunta();
        PersistenciaRespuesta respuestaBD = new PersistenciaRespuesta();
        preguntas = preguntaBD.recuperarPregunta(idSetPregunta);
        respuestas = respuestaBD.recuperarRespuesta(preguntas);
        preguntaActual = preguntas.get(0);*/
    }
    
    /**
     * Este metodo sirve para enviar el puntaje obtenido por cada partida.
     * @param puntaje 
     */
    private void enviarPuntaje(int puntaje) {
        
        PuntajeEnvio penvio = new PuntajeEnvio();
        socket.emit("envioPuntaje", penvio);
    }
    
    /**
     * Este metodo impide que el campo de texto sea mayor a un numero de 
     * caracteres fijo.
     * @param tf textField a limitar
     * @param maximo numero maximo de caracteres permitidos.
     */
    private void limitarCampos(javafx.scene.control.TextField tf, int maximo) {
        tf.textProperty().addListener((final ObservableValue<? 
                extends String> ov, final String oldValue, 
                final String newValue) -> {
            if (tf.getText().length() > maximo) {
                String s = tf.getText().substring(0, maximo);
                tf.setText(s);
            }
        });
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

        OutputStream out = null;

        try {
            
            out = new BufferedOutputStream(new FileOutputStream(ruta));
            out.write(imagen);
        } catch (FileNotFoundException ex) {
            
            Logger.getLogger(ResponderPreguntaController.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            
            Logger.getLogger(ResponderPreguntaController.class.getName())
                    .log(Level.SEVERE, null, ex);
        } finally {
            
            if (out != null) {
                out.close();
            }
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
}

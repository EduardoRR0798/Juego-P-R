/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegopreguntantas;

import entity.Cuentainvitado;
import entity.Cuentausuario;
import entity.Pregunta;
import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
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
import clases.Chat_Cliente;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.util.Objects;

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
    private Label lblGanador;
    @FXML
    private Label lblBuenJuego;
    @FXML
    private BarChart<String, Number> bcGraficaPuntaje;
    private CategoryAxis caJugadores;
    private NumberAxis caPuntos;
    @FXML
    private ProgressBar pbRestante;
    
    private static final Integer TIEMPORESPUESTA = 15;
    private Object cuenta;
    private String idioma;
    private Cuentausuario usuario;
    private Cuentainvitado invitado;
    private int tiempoRestante = 15;
    private int sobra = 16;
    private Chat_Cliente mensajero;
    private ArrayList<String> mensajesChat = new ArrayList<>();
    private ObservableList<String> jugadores = 
            FXCollections.observableArrayList();
    private Pregunta preguntaActual;
    private Socket socketChat = puertoServidor();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        activarPanelGanador(false);
        activarPanelGrafica(false);
        activarRespuestaEscrita(false);
        setTimer();
        fijarImagenes();
        
        socketChat.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override

            public void call(Object... os) {
                System.out.println("Conectado..");
                
            }
        }).on("reciboMensaje", new Emitter.Listener(){
            
            @Override
            public void call(Object... os) {
                String mensajeRecibido = (String) os[0];
                System.out.println(mensajeRecibido+ "1");
                mostrarMensaje(mensajeRecibido);
                System.out.println(mensajeRecibido);
            }
        });
        
        socketChat.connect();
    }    
    
    /**
     * Este metodo inicializa el socket para el chat.
     * @return el Socket.
     */
    private Socket puertoServidor(){
        
        Socket socket = null;
        try {
            socket = IO.socket("http://192.168.43.91:4000");
        } catch (URISyntaxException ex) {
            Logger.getLogger(Chat_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return socket;
        }
    }
    
    /**
     * Este metodo sirve para envisar un mensaje en el chat.
     * @param event Clic en el boton Enviar.
     */
    @FXML
    private void enviar(ActionEvent event) {
        
        String nombre = "";
        String mensaje = tfMensaje.getText().trim();
        if(cuenta instanceof Cuentausuario) {
            
            nombre = usuario.getNombreusuario();
        } else {
            
            nombre = invitado.getNombre();
        }
        tfMensaje.clear();
        if(!Objects.equals(mensaje, "")) {
            
            String mensajeEnviado = nombre + ": " + mensaje;
            socketChat.emit("envioMensaje", mensajeEnviado);
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
        
        activarPanelGrafica(true);
    }

    @FXML
    private void seleccionarB(MouseEvent event) {
        
        activarPanelGrafica(true);
    }

    @FXML
    private void seleccionarC(MouseEvent event) {
        
        activarPanelGrafica(true);
    }

    @FXML
    private void seleccionarD(MouseEvent event) {
        
        activarPanelGrafica(true);
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
        
        boolean activacion;
        if(visibilidad) {
            
            activacion = false;
        } else {
            
            activacion = true;
        }
        
        pPanelGanador.setVisible(visibilidad);
        pPanelGanador.setDisable(visibilidad);
        lblGanador.setDisable(visibilidad);
        lblAnunciador.setDisable(visibilidad);
        lblBuenJuego.setDisable(visibilidad);
        lblGanador.setVisible(visibilidad);
        lblAnunciador.setVisible(visibilidad);
        lblBuenJuego.setVisible(visibilidad);
    }
    
    /**
     * Este metodo deshabilita el panel que muestra la grafica de los jugadores.
     * @param visibilidad true si seran visibles los componentes, false si no.
     * @param activacion true si estaran desactivados, false si no.
     */
    private void activarPanelGrafica(boolean visibilidad) {
        
        boolean activacion;
        if(visibilidad) {
            
            activacion = false;
        } else {
            
            activacion = true;
        }
        
        pPanelGrafica.setVisible(visibilidad);
        bcGraficaPuntaje.setVisible(visibilidad);
        pPanelGrafica.setDisable(activacion);
        bcGraficaPuntaje.setDisable(activacion);
    }
    
    /**
     * Este metodo habilita los checkBox cuando se trata de una pregunta con 
     * respuestas escritas.
     * @param permiso true si deben ser visibles y activos, false si no.
     */
    private void activarRespuestaEscrita(boolean permiso) {
        
        boolean activacion;
        
        if(permiso) {
            activacion = false;
        } else {
            activacion = true;
        }
        
        chbOpcionA.setDisable(activacion);
        chbOpcionA.setVisible(permiso);
        chbOpcionB.setDisable(activacion);
        chbOpcionB.setVisible(permiso);
        chbOpcionC.setDisable(activacion);
        chbOpcionC.setVisible(permiso);
        chbOpcionD.setDisable(activacion);
        chbOpcionD.setVisible(permiso);
    }
    
    /**
     * Este metodo habilita los imageView cuando se trata de una pregunta con 
     * una respuesta de imagenes.
     * @param permiso true si deben ser visibles y activos, false si no.
     */
    private void activarRespuestaImagen(boolean permiso) {
        
        boolean activacion;
        
        if(permiso) {
            activacion = true;
        } else {
            activacion = false;
        }
        
        imgA.setVisible(permiso);
        imgA.setDisable(permiso);
    }
    
    /**
     * Este metodo lleva la cuenta regresiva para responder una pregunta y anima
     * el label.
     * @return el segundo en que el usuario respondio la pregunta.
     */
    private int setTimer() {
        
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            
            public void run() {
                
                if (tiempoRestante > 0) {
                    
                    Platform.runLater(() -> lblTiempo.setText(
                            Integer.toString(tiempoRestante)));
                    sobra = tiempoRestante;
                    tiempoRestante--;
                } else {
                    
                    timer.cancel();
                    activarPanelGrafica(true);
                }
                
            }
        }, 1000, 1000); 
        return sobra;
    }
    
    private void fijarImagenes() {
        
        Pregunta pregunta = new Pregunta();
        String ubicacion = "C:\\Users\\Eduar\\Desktop\\Tecnologias\\Juego-P-R-master\\JuegoPreguntantas\\PartidaActual";
        try {
            
            /*File file1 = new File(pregunta.getRespuesta(0).getRespuesta());
            System.out.println(file1.getName());
            File file2 = new File(pregunta.getRespuesta(1).getRespuesta());
            File file3 = new File(pregunta.getRespuesta(2).getRespuesta());
            File file4 = new File(pregunta.getRespuesta(3).getRespuesta());*/
            File file1 = new File("C:\\Users\\Eduar\\Desktop\\Tecnologias\\Juego-P-R-master\\JuegoPreguntantas\\PartidaActual\\1.png");
            File file2 = new File("C:\\Users\\Eduar\\Desktop\\Tecnologias\\Juego-P-R-master\\JuegoPreguntantas\\PartidaActual\\2.png");
            File file3 = new File("C:\\Users\\Eduar\\Desktop\\Tecnologias\\Juego-P-R-master\\JuegoPreguntantas\\PartidaActual\\3.png");
            File file4 = new File("C:\\Users\\Eduar\\Desktop\\Tecnologias\\Juego-P-R-master\\JuegoPreguntantas\\PartidaActual\\4.png");
            
            Image imageA = new Image(file1.toURI().toString());
            Image imageB = new Image(file2.toURI().toString());
            Image imageC = new Image(file3.toURI().toString());
            Image imageD = new Image(file4.toURI().toString());
            
            imgA.setImage(imageA);
            imgB.setImage(imageB);
            imgC.setImage(imageC);
            imgD.setImage(imageD);
            
            lblPregunta.setText("La pregunta es...");
            System.out.println(InetAddress.getLocalHost().getHostAddress());
            System.out.println(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException ex) {
            
            Logger.getLogger(ResponderPreguntaController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
}

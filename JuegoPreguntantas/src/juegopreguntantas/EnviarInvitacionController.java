package juegopreguntantas;

import entity.Cuentainvitado;
import entity.Cuentausuario;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import persistencia.PersistenciaCuentaInvitado;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez                                 */ 
/* @since 07/11/2018                                              */
/* Nombre de la clase EnviarInvitacionController                  */
/******************************************************************/
public class EnviarInvitacionController implements Initializable {

    @FXML
    private TextField txtCorreoElectronico;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnEnviarInvitacion;
    
    private Cuentausuario cuenta;
    private String idioma;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    /**
     * Este metodo es para cancelar el inicio de la partida y regresar a la 
     * ventana anterior de Inicio de partida
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
                    .getResource("MenuPrincipal.fxml"));
            loader.setResources(resourceBundle);
            Parent esperaJugadores = loader.load();
            MenuPrincipalController controller = loader.getController();
            controller.recibirParametros(cuenta, idioma);
            Scene scene = new Scene(esperaJugadores);
            Stage stage = new Stage();
            stage.setTitle("Menu principal");
            stage.setScene(scene);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {

            Logger.getLogger(EnviarInvitacionController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

    /**
     * Este metodo es para enviar una invitacion por correo electronico para
     * unirse a una partida, cuando es exitoso envía un mensaje de confirmacion
     * @param event del click del mouse
     */
    @FXML
    private void enviarInvitacion(ActionEvent event) throws MessagingException {

        if (!txtCorreoElectronico.getText().isEmpty()) {
            
            try {
                
                PersistenciaCuentaInvitado invitadoBD = new PersistenciaCuentaInvitado();
                if (invitadoBD.comprobarCorreo(txtCorreoElectronico.getText())) {
                    mostrarCorreoRepetido();
                } else {

                    Cuentainvitado nuevoInvitado = new Cuentainvitado();
                    nuevoInvitado.setNombre(invitadoBD.crearNombre());
                    nuevoInvitado.setCorreoelectronico(txtCorreoElectronico.getText());
                    nuevoInvitado.setCodigo(invitadoBD.crearCodigo());
                    String deCorreo = "juego.preguntantas@gmail.com";

                    final String contrasenia = "pr3gunt0n";
                    Properties properties = crearProperties();
                    Authenticator auth = new Authenticator() {
                        
                        @Override
                        public PasswordAuthentication getPasswordAuthentication() {

                            return new PasswordAuthentication(deCorreo
                                    , contrasenia);
                        }
                    };
                    Session sesion = Session.getInstance(properties, auth);
                    Message mensaje = crearContenidoInvitacion(sesion
                            , nuevoInvitado);
                    mostrarInvitadoExito(mensaje, nuevoInvitado);
                }
            } finally {
                
                txtCorreoElectronico.clear();
            }
        }
    }
    
    /**
     * Este metodo es para mostrar una ventana de error si intenta el usuario 
     * volver a invitar al mismo usuario
     */
    private void mostrarCorreoRepetido() {
        
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Ya se había invitado a este correo");
        alert.showAndWait();
    }
    
    /**
     * Este metodo es para hacer todos los put que necesitan las properties
     * @return El el properties para la sesion
     */
    private Properties crearProperties() {
        
        Properties properties = new Properties();
        String host = "smtp.gmail.com";
        String puerto = "587";
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", puerto);
        properties.put("mail.smtp.debug", "true");
        return properties;
    }
    
    /**
     * Este metodo es para crear el mensaje que se va a enviar por correo
     * @param sesion Session para enviar mensaje
     * @param nuevoInvitado Cuenta de invitado con los datos para el mensaje
     * @return El mensaje que se enviara por correo
     */    
    private Message crearContenidoInvitacion(Session sesion, 
            Cuentainvitado nuevoInvitado) {
        
        Message mensaje = new MimeMessage(sesion);
        try {
            
            InternetAddress[] address 
                    = {new InternetAddress(nuevoInvitado.getCorreoelectronico())};
            mensaje.setRecipients(Message.RecipientType.TO, address);
            mensaje.setSubject("Invitacion a jugar Preguntantas");
            String saludo = "Hola futuro pregunton\n\n";
            String cuerpo = "Haz sido invitado a jugar Preguntantas\n\n";
            String despedida = "¡Ingresa con usuario: " 
                    + nuevoInvitado.getNombre() + " y contraseña " 
                    + nuevoInvitado.getCodigo() + " !";
            String contenidoCorreo = saludo + cuerpo + despedida;
            mensaje.setSentDate(new Date());
            mensaje.setText(contenidoCorreo);
        } catch (MessagingException ex) {
            
            Logger.getLogger(EnviarInvitacionController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        return mensaje;
    }
    
    /**
     * Este metodo es para mostrar una ventana en caso de exito
     * @param mensaje Message que se va a enviar por correo
     * @param nuevoInvitado Cuenta de invitado que se guardara en BD
     */ 
    private void mostrarInvitadoExito(Message mensaje, Cuentainvitado nuevoInvitado) {
        
        PersistenciaCuentaInvitado invitadoBD = new PersistenciaCuentaInvitado();
        if (invitadoBD.crearInvitado(nuevoInvitado)) {

            try {
                
                Transport.send(mensaje);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Invitacion exitosa");
                alert.setHeaderText(null);
                alert.setContentText("Invitación enviada");
                alert.showAndWait();
            } catch (MessagingException ex) {
                
                Logger.getLogger(EnviarInvitacionController.class.getName())
                        .log(Level.SEVERE, null, ex);
                mostrarInvitadoFracaso();
                invitadoBD.eliminarInvitado(nuevoInvitado);
            }
        } else {
            mostrarInvitadoFracaso();
        }
    }
    
    /**
     * Este metodo es para mostrar una ventana en caso de fracaso
     */ 
    private void mostrarInvitadoFracaso() {
        
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Se ha perdido conexión con el servidor"
                + ", prueba de nuevo");
        alert.showAndWait();
    }
    
    /**
     * Metodo que recibe el objeto de cuenta de usuario o invitado del 
     * Controlador de la pantalla que la invocó
     * @param usuario Cuenta de usuario registrado
     * @param idioma Idioma del properties
     */
    public void recibirParametros(Object usuario, String idioma){
        
        this.idioma = idioma;
        this.cuenta = (Cuentausuario)usuario;
    }
}

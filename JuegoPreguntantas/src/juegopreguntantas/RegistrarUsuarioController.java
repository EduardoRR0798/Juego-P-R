/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegopreguntantas;

import entity.Cuentainvitado;
import persistencia.PersistenciaCuentaUsuario;
import entity.Cuentausuario;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import persistencia.PersistenciaCuentaInvitado;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Eduardo Rosas Rivera                                   */ 
/* @since 05/11/2018                                              */
/* Nombre de la clase RegistrarUsuarioController                  */
/******************************************************************/

public class RegistrarUsuarioController implements Initializable {

    @FXML
    private TextField txtNombreUsuario;
    @FXML
    private TextField txtCorreoElectronico;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Label lMensaje;
    
    private String idioma;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
  
    /**
     * Este metodo regresa a la pantalla de login.
     * @param event Clic en cancelar.
     */
    @FXML
    private void cancelar(ActionEvent event) {
        Locale.setDefault(new Locale(idioma));
        ResourceBundle resourceBundle = 
                ResourceBundle.getBundle("juegopreguntantas.lang/lang");
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("VentanaLogIn.fxml"));
        loader.setResources(resourceBundle);
        try {
            Parent logIn = loader.load();
            VentanaLogInController controller = loader.getController();
            controller.setIdioma(idioma);
            
            Scene scene = new Scene(logIn);
            Stage stage = new Stage();
            
            stage.setScene(scene);
            stage.show();
            
            ((Node) event.getSource()).getScene().getWindow().hide();
        } catch (IOException ex) {
            Logger.getLogger(VentanaLogInController.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }
  
    /**
     * Este metodo registra a un usuario en la base de datos.
     * @param event Clic en el boton Registrar.
     */
    @FXML
    private void registrar(ActionEvent event) {
        PersistenciaCuentaUsuario persistencia = new PersistenciaCuentaUsuario();
        String nombre = txtNombreUsuario.getText().trim();
        String contrasenia = pfPassword.getText().trim();
        String email = txtCorreoElectronico.getText().trim();
        
        if(validarCampos() == true){
            
            if(verificarRegistroUsuario() == true) {
                
                try {
                    
                    Cuentausuario usuario = new Cuentausuario();
                    usuario.setNombreusuario(nombre);
                    usuario.setContrasenia(contrasenia);
                    usuario.setCorreoelectronico(email);
                    persistencia.registrarCuentaUsuario(usuario);
                    enviarConfirmacion();
                    txtNombreUsuario.clear();
                    pfPassword.clear();
                } catch (MessagingException ex) {
                    
                    Logger.getLogger(RegistrarUsuarioController.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            } 
            
        } else {
            
            lMensaje.setText("Llene todos los datos.");
        }
      
    }
    
    /**
     * Este metodo fija el idioma que enviara de retorno en caso de presionar el
     * boton cancelar.
     * @param idioma idioma fijado en la pantalla LogIn.
     */
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
    
    /**
     * Este metodo valida que no exista algun campo vacio.
     * @return 
     */
    private boolean validarCampos() {
        boolean permiso = true;
        String nombre = txtNombreUsuario.getText().trim();
        String contrasenia = pfPassword.getText().trim();
        String email = txtCorreoElectronico.getText().trim();
        
        if(nombre.length() == 0) {
            permiso = false;
        }
        if(contrasenia.length() == 0) {
            permiso = false;
        }
        if(email.length() == 0) {
            permiso = false;
        }
        return permiso;
    }
    

    /**
     * Este metodo verifica que no exista ese nombre en la base de datos.
     * @return true si no existe, false si existe.
     */
    private boolean verificarRegistrosNombre() {
        boolean permiso = false;
        String user = txtNombreUsuario.getText().trim();
        PersistenciaCuentaUsuario persistencia = new PersistenciaCuentaUsuario();
        Cuentausuario usuario = persistencia.getCuentaUsuarioNombre(
                user.toUpperCase());
        
        if(Objects.equals(usuario, null)) {
            permiso = true;
        }
        return permiso;
    }
    

    /**
     * Este metodo verifica que el correo electronico no exista en la 
     * base de datos.
     * @return true si no existe, false si existe.
     */
    private boolean verificarRegistrosEmail() {
        boolean permiso = false;
        String email = txtCorreoElectronico.getText().trim();
        PersistenciaCuentaUsuario persistencia = new PersistenciaCuentaUsuario();
        Cuentausuario usuario = 
                persistencia.getCuentaUsuarioEmail(email.toUpperCase());
        
        if(Objects.equals(usuario, null)) {
            permiso = true;
        }
        
        return permiso;
    }
    
    /**
     * Este metodo verifica si alguno de los dos metodos verificadores 
     * obtienen true.
     * @return true si el usuario se puede registrar, false si no puede.
     */
    private boolean verificarRegistroUsuario() {
        boolean permiso = true;
        
        if(verificarRegistrosNombre() != true) {
            lMensaje.setText("Nombre ya existente.");
            permiso = false;
        }
        
        if(verificarRegistrosEmail() != true) {
            lMensaje.setText("Email ya registrado.");
            permiso = false;
        }
        return permiso;
    }
    
    /**
     * Este metodo es para enviar una confirmacion por correo electronico del 
     * usuario que se registro.
     */
    private void enviarConfirmacion() throws MessagingException {

        if (!txtCorreoElectronico.getText().isEmpty()) {
            
            try {
                
                String correo = 
                        txtCorreoElectronico.getText().replaceAll("\\S", "");
                PersistenciaCuentaInvitado invitadoBD =
                        new PersistenciaCuentaInvitado();
                if (invitadoBD.comprobarCorreo(correo.toUpperCase())) {
                    
                    lMensaje.setText("Email existente, pruebe otro.");
                } else {

                    Cuentainvitado nuevoInvitado = new Cuentainvitado();
                    nuevoInvitado.setNombre(txtNombreUsuario.getText());
                    nuevoInvitado.setCorreoelectronico(
                            txtCorreoElectronico.getText());
                    nuevoInvitado.setCodigo(pfPassword.getText());
                    String deCorreo = "juego.preguntantas@gmail.com";

                    final String contrasenia = "pr3gunt0n";
                    Properties properties = crearProperties();
                    Authenticator auth = new Authenticator() {
                        public PasswordAuthentication 
                            getPasswordAuthentication() {

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
            InternetAddress[] address = {new InternetAddress(
                    nuevoInvitado.getCorreoelectronico())};
            mensaje.setRecipients(Message.RecipientType.TO, address);
            mensaje.setSubject("Registro a PREGUNTANTAS!");
            String saludo = "Hola " + txtNombreUsuario.getText() + "\n\n";
            String cuerpo = "Tu registro a preguntantas ha sido realizado de"
                    + " forma exitosa!\n\n";
            String contenidoCorreo = saludo + cuerpo;
            mensaje.setSentDate(new Date());
            mensaje.setText(contenidoCorreo);
        } catch (AddressException ex) {
            Logger.getLogger(EnviarInvitacionController.class.getName())
                    .log(Level.SEVERE, null, ex);
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
    private void mostrarInvitadoExito(Message mensaje, 
            Cuentainvitado nuevoInvitado) {
        
        PersistenciaCuentaInvitado invitadoBD = 
                new PersistenciaCuentaInvitado();
        if (invitadoBD.crearInvitado(nuevoInvitado)) {

            try {
                
                Transport.send(mensaje);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registro Exitoso");
                alert.setHeaderText(null);
                alert.setContentText("Se ha enviado un correo electronico a su "
                        + "correo");
                alert.showAndWait();
            } catch (MessagingException ex) {
                
                Logger.getLogger(EnviarInvitacionController.class.getName())
                        .log(Level.SEVERE, null, ex);
                invitadoBD.eliminarInvitado(nuevoInvitado);
            }
        } else {
            lMensaje.setText("No se ha podido registrar su cuenta");
        }
    }
    
    private void emitirMensaje(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

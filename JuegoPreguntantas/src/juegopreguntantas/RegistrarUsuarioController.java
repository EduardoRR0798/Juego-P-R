package juegopreguntantas;

import persistencia.PersistenciaCuentaUsuario;
import entity.Cuentausuario;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        limitarCampos(txtNombreUsuario, 26);
        limitarCampos(pfPassword, 26);
        limitarCampos(txtCorreoElectronico, 60);
        excluirEspacios();
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

        PersistenciaCuentaUsuario persistencia
                = new PersistenciaCuentaUsuario();
        String nombre = txtNombreUsuario.getText().trim();
        String contraseniaNueva = 
                hacerHashAContrasenia(pfPassword.getText().trim());
        String email = txtCorreoElectronico.getText().trim();
        if (validarCampos()) {

            if (verificarRegistroUsuario()) {

                Cuentausuario usuario = new Cuentausuario();
                usuario.setNombreusuario(nombre);
                usuario.setContrasenia(contraseniaNueva);
                usuario.setCorreoelectronico(email);
                txtNombreUsuario.clear();
                pfPassword.clear();
                try {

                    String deCorreo = "juego.preguntantas@gmail.com";
                    final String contrasenia = "pr3gunt0n";
                    Properties properties = crearProperties();
                    Authenticator auth = new Authenticator() {

                        @Override
                        public PasswordAuthentication 
                            getPasswordAuthentication() {

                            return new PasswordAuthentication(deCorreo,
                                    contrasenia);
                        }
                    };
                    Session sesion = Session.getInstance(properties, auth);
                    Message mensaje = crearContenidoRegistro(sesion,
                            usuario);
                    mostrarRegistroExito(mensaje, usuario);
                    volverALogin();
                } finally {

                    txtCorreoElectronico.clear();
                }
            }

        } else {

            lMensaje.setText(java.util.ResourceBundle.getBundle(
                    "juegopreguntantas/lang/lang")
                    .getString("string_llenarCampos"));
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
     * Este metodo verifica si alguno de los dos metodos verificadores 
     * obtienen true.
     * @return true si el usuario se puede registrar, false si no puede.
     */
    private boolean verificarRegistroUsuario() {
        
        boolean permiso = true;
        if(!verificarRegistrosNombre()) {
            
            lMensaje.setText(java.util.ResourceBundle.getBundle(
                    "juegopreguntantas/lang/lang")
                    .getString("string_NombreExistente"));
            permiso = false;
        }
        if(!verificarRegistrosEmail()) {
            
            lMensaje.setText(java.util.ResourceBundle.getBundle(
                    "juegopreguntantas/lang/lang")
                    .getString("string_emailExistente"));
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
        PersistenciaCuentaUsuario persistencia = 
                new PersistenciaCuentaUsuario();
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
        PersistenciaCuentaUsuario persistencia = 
                new PersistenciaCuentaUsuario();
        Cuentausuario usuario = 
                persistencia.getCuentaUsuarioEmail(email.toUpperCase());
        if(Objects.equals(usuario, null)) {
            
            permiso = true;
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
                        txtCorreoElectronico.getText().trim();
                PersistenciaCuentaInvitado invitadoBD =
                        new PersistenciaCuentaInvitado();
                if (invitadoBD.comprobarCorreo(correo.toUpperCase())) {
                    
                    lMensaje.setText(java.util.ResourceBundle.getBundle(
                            "juegopreguntantas/lang/lang")
                            .getString("string_emailExistente"));
                } else {

                    Cuentausuario nuevoUsuario = new Cuentausuario();
                    nuevoUsuario.setNombreusuario(txtNombreUsuario.getText());
                    nuevoUsuario.setCorreoelectronico(
                            txtCorreoElectronico.getText());
                    nuevoUsuario.setContrasenia(pfPassword.getText());
                    
                    String deCorreo = "juego.preguntantas@gmail.com";
                    final String contrasenia = "pr3gunt0n";
                    Properties properties = crearProperties();
                    Authenticator auth = new Authenticator() {
                        
                        @Override
                        public PasswordAuthentication 
                            getPasswordAuthentication() {

                            return new PasswordAuthentication(deCorreo
                                    , contrasenia);
                        }
                    };
                    Session sesion = Session.getInstance(properties, auth);
                    Message mensaje = crearContenidoRegistro(sesion
                            ,nuevoUsuario);
                    mostrarRegistroExito(mensaje, nuevoUsuario);
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
    private Message crearContenidoRegistro(Session sesion, 
            Cuentausuario nuevousuario) {
        
        Message mensaje = new MimeMessage(sesion);
        try {
            
            InternetAddress[] address = {
                new InternetAddress(nuevousuario.getCorreoelectronico())};
            mensaje.setRecipients(Message.RecipientType.TO, address);
            mensaje.setSubject(java.util.ResourceBundle.getBundle(
                    "juegopreguntantas/lang/lang")
                    .getString("string_registro"));
            
            String saludo = java.text.MessageFormat.format(
                    java.util.ResourceBundle.getBundle(
                            "juegopreguntantas/lang/lang")
                            .getString("string_holaUsuario")
                    , new Object[] {
                        txtNombreUsuario.getText()});
            
            String cuerpo = java.util.ResourceBundle.getBundle(
                    "juegopreguntantas/lang/lang")
                    .getString("string_registroExitoso")
                    + java.util.ResourceBundle.getBundle(
                            "juegopreguntantas/lang/lang")
                            .getString("string_formaExitosa");
            
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
    private void mostrarRegistroExito(Message mensaje, 
            Cuentausuario nuevoUsuario) {
        
        PersistenciaCuentaUsuario usuarioBD = 
                new PersistenciaCuentaUsuario();
        if (usuarioBD.crearUsuario(nuevoUsuario)) {

            try {
                
                Transport.send(mensaje);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(java.util.ResourceBundle.getBundle(
                        "juegopreguntantas/lang/lang")
                        .getString("string_tituloExito"));
                alert.setHeaderText(null);
                alert.setContentText(java.util.ResourceBundle.getBundle(
                        "juegopreguntantas/lang/lang")
                        .getString("string_confirmacionEnvio")
                        + java.util.ResourceBundle.getBundle(
                                "juegopreguntantas/lang/lang")
                                .getString("string_email"));
                alert.showAndWait();
            } catch (MessagingException ex) {
                
                Logger.getLogger(EnviarInvitacionController.class.getName())
                        .log(Level.SEVERE, null, ex);
                usuarioBD.eliminarUsuario(nuevoUsuario);
                mostrarRegistroFracaso();
            }
        } else {
            
            mostrarRegistroFracaso();
        }
    }
    
    /**
     * Este metodo es para mostrar una ventana en caso de fracaso al registrar.
     */ 
    private void mostrarRegistroFracaso() {
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Se ha perdido conexión con el servidor"
                + ", prueba de nuevo");
        alert.showAndWait();
    }
    
    /**
     * Este metodo impide que el campo de texto sea mayor a un numero de 
     * caracteres fijo.
     * @param tf textField a limitar
     * @param maximo numero maximo de caracteres permitidos.
     */
    private void limitarCampos(javafx.scene.control.TextField tf, int maximo) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, 
                    final String oldValue, final String newValue) {
                if (tf.getText().length() > maximo) {
                    String s = tf.getText().substring(0, maximo);
                    tf.setText(s);
                }
            }
        });
    }
    
    /**
     * Este metodo sirve para volver a la pantalla de Login.
     */
    private void volverALogin() {
            
        Locale.setDefault(new Locale(idioma));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("juegopreguntantas.lang/lang");
        
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

            btnRegistrar.getScene().getWindow().hide();
        } catch (IOException ex) {
            
            Logger.getLogger(ResponderPreguntaController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Este metodo limita niega la posibilidad de escribir espacios en un 
     * TextField.
     */
    private void excluirEspacios() {
        txtNombreUsuario.textProperty().addListener(
                (observable, old_value, new_value) -> {
                    if (new_value.contains(" ")) {
                        txtNombreUsuario.setText(old_value);
                    }
                });
        txtCorreoElectronico.textProperty().addListener(
                (observable, old_value, new_value) -> {
                    if (new_value.contains(" ")) {
                        txtCorreoElectronico.setText(old_value);
                    }
                });
        pfPassword.textProperty().addListener(
                (observable, old_value, new_value) -> {
                    if (new_value.contains(" ")) {
                        pfPassword.setText(old_value);
                    }
                });
    }
    
    /**
     * Este metodo convierte la contrasenia ingresada por el usuario en una 
     * cadena hash.
     * @return el codigo hash de la contrasenia.
     */
    private String hacerHashAContrasenia(String contrasenia) {
        
        String contraseniaHash = null;
        try {
            
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(contrasenia.getBytes());
            BigInteger codigo = new BigInteger(1, messageDigest);
            contraseniaHash = codigo.toString(16);
            while (contraseniaHash.length() < 32) { 
                
                contraseniaHash = "0" + contraseniaHash; 
            }
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RegistrarUsuarioController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return contraseniaHash;
    }
    
}

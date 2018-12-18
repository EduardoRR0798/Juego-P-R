package juegopreguntantas;

import persistencia.PersistenciaCuentaUsuario;
import entity.Cuentainvitado;
import entity.Cuentausuario;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import persistencia.PersistenciaCuentaInvitado;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Eduardo Rosas Rivera                                   */ 
/* @since 28/11/2018                                              */
/* Nombre de la clase VentanaLogInController                      */
/******************************************************************/
public class VentanaLogInController implements Initializable {

    @FXML
    private ImageView imgLogin;
    @FXML
    private TextField tfUser;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Label lbUser;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnCerrar;
    @FXML
    private Label lTittle;
    @FXML
    private CheckBox chbInvitado;
    @FXML
    private ComboBox<String> cbCambiarIdioma;
    @FXML
    private Label lMensaje;
    
    private final ObservableList idiomas = 
            FXCollections.observableArrayList("Español", "English");
    private Object cuenta; 
    private String idioma = "es";
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
           
        cbCambiarIdioma.setItems(idiomas);
        limitarCampos(tfUser, 26);
        limitarCampos(pfPassword,26);
        excluirEspacios();     

    }
    
    /**
     * Este metodo lleva a la ventana del menu principal.
     * @param event clic en el boton Ingresar.
     * @throws IOException error al abrir ventana.
     */
    @FXML
    private void ingresar(ActionEvent event) throws IOException {
        System.out.println("Hola");
        if (validarCampos() && validarAcceso()) {
            System.out.println("Accesando");
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
        }
    }
    
    
    /**
     * Metodo que invoca la pantalla de registro.
     * @param event Clic en el boton Registrar.
     */
    @FXML
    private void registrar(ActionEvent event) {
        
        Locale.setDefault(new Locale(idioma));
        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "juegopreguntantas.lang/lang");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("RegistrarUsuario.fxml"));
        loader.setResources(resourceBundle);
        Parent registro;
        try {
            
            registro = loader.load();
            RegistrarUsuarioController controller = loader.getController();
            controller.setIdioma(idioma);
            
            Scene scene = new Scene(registro);
            Stage stage = new Stage();
            
            stage.setScene(scene);
            stage.show();

            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException ex) {
            
            Logger.getLogger(VentanaLogInController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Metodo que cierra el programa.
     * @param event Clic en cerrar.
     */
    @FXML
    private void cerrar(ActionEvent event) {
        
        System.exit(0);
    }
    
    /**
     * Metodo que valida si ambos campos fueron fueron llenados.
     * @return true si ambos estan llenos, false si alguno esta vacio.
     */
    private boolean validarCampos() {
        
        boolean validador = true;
        if(Objects.equals(tfUser.getText().trim(), "")) {
            
            validador = false;
            lMensaje.setText(java.util.ResourceBundle.getBundle(
                    "juegopreguntantas/lang/lang").getString(
                            "string_llenarCampos"));
        }
        if(Objects.equals(pfPassword.getText().trim(), "")) {
            
            validador = false;
            lMensaje.setText(java.util.ResourceBundle.getBundle(
                    "juegopreguntantas/lang/lang").getString(
                            "string_llenarCampos"));
        }
        return validador;
    }
    
    /**
     * Metodo que valida si el nombre de usuario y la contrase;a introducidos 
     * por el usuario concuerdan con alguno registrado en la base de datos.
     * @return true si los datos son validos, false si no coinciden.
     */
    private boolean validarIngresoUsuario() {
        
        boolean ingresoExitoso = false;
        String usuarioR = tfUser.getText().trim();
        String contrasenia = hacerHashAContrasenia(pfPassword.getText().trim());
        PersistenciaCuentaUsuario usuarioBD = new PersistenciaCuentaUsuario();
        try {
            
            Cuentausuario usuario = usuarioBD.getCuentaUsuarioNombre(usuarioR
                    .toUpperCase());
            if (usuario != null) {

                if (contrasenia.equals(usuario.getContrasenia())) {

                    cuenta = usuario;
                    ingresoExitoso = true;
                    lMensaje.setText(java.util.ResourceBundle.getBundle(
                            "juegopreguntantas/lang/lang")
                            .getString("string_iniciandoSesion"));
                } else {

                    lMensaje.setText(java.util.ResourceBundle.getBundle(
                            "juegopreguntantas/lang/lang")
                            .getString("string_incorrectos"));
                }
            } else {

                lMensaje.setText(java.util.ResourceBundle.getBundle(
                        "juegopreguntantas/lang/lang")
                        .getString("string_noEncontrado"));
            }
        } catch (Exception ex) {
            
            mostrarMensajeError();
            Logger.getLogger(VentanaLogInController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return ingresoExitoso;   
    }
    
    /**
     * Metodo que valida si el nombre de usuario y la contrase;a 
     * introducidos por el usuario
     * consuerdan con alguno de invitado registrado en la base de datos 
     * @return true si los datos son validos, false si no coinciden.
     */
    private boolean validarIngresoInvitado() {
        
        boolean ingresoExitoso = false;
        String usuario = tfUser.getText().trim();
        String contrasenia = pfPassword.getText().trim();
        try {
            PersistenciaCuentaInvitado persistencia
                    = new PersistenciaCuentaInvitado();
            Cuentainvitado invitado
                    = persistencia.getCuentaInvitado(usuario.toUpperCase());

            if (invitado != null) {

                if (contrasenia.equals(invitado.getCodigo())) {

                    ingresoExitoso = true;
                    cuenta = invitado;
                    lMensaje.setText(java.util.ResourceBundle.getBundle(
                            "juegopreguntantas/lang/lang")
                            .getString("string_iniciandoSesion"));
                } else {

                    lMensaje.setText(java.util.ResourceBundle.getBundle(
                            "juegopreguntantas/lang/lang")
                            .getString("string_incorrectos"));
                }
            } else {

                lMensaje.setText(java.util.ResourceBundle.getBundle(
                        "juegopreguntantas/lang/lang")
                        .getString("string_noEncontrado"));
            }
        } catch (Exception ex) {
            
            Logger.getLogger(VentanaLogInController.class.getName())
                    .log(Level.SEVERE, null, ex);
            mostrarMensajeError();
        }
        return ingresoExitoso;
    }
    
    /**
     * Metodo que valida si el usuario se trata de una cuenta invitada o 
     * una cuenta registrada.
     * @return true si el usuario esta en la base de datos.
     */
    private boolean validarAcceso() {
        
        boolean ingresoExitoso = false;
        
        if(chbInvitado.isSelected()) {
            
            if(validarIngresoInvitado()) {
                
                ingresoExitoso = true;
            }
        
        } else {
            
            if(validarIngresoUsuario()) {
                
                ingresoExitoso = true;
            }
            
        }
        return ingresoExitoso;
    }
    
    /**
     * Este metodo sirve para cambiar el idioma de la ventana.
     * @param event Clic en una opcion del cdIdioma.
     */
    @FXML
    private void cambiarIdioma(ActionEvent event) {
        
        String idiomaLogin;
        if(cbCambiarIdioma.getSelectionModel().getSelectedItem()
                .equals("English")) {
            
            idiomaLogin = "en";
        } else {
            
            idiomaLogin = "es";
        }
        abrirLogin(idiomaLogin, event);
    }
    
    /**
     * Este metodo sirve para abrir la ventana con un nuevo idioma.
     * @param idioma idioma con el que se abrira la ventana.
     * @param event Clic en cdIdioma.
     */
    private void abrirLogin(String idioma, ActionEvent event) {
        
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
            
            Logger.getLogger(VentanaLogInController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Este metodo sirve para fijar solo el idioma con el que se abrira la 
     * ventana.
     * @param idioma idioma con el que iniciara la ventana.
     */
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
    
    /**
     * Este metodo muestra un mensaje de error en caso de fallo.
     */
    private void mostrarMensajeError() {
        
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setTitle("Erro de conexion.");
        alert.setContentText("Se ha perdido la conexion con la base de datos.");
    }
    
    /**
     * Este metodo sirve para que los textFiel nieguen la entrada a espacios.
     */
    private void excluirEspacios() {
        
        tfUser.textProperty().addListener(
                (observable, old_value, new_value) -> {
                    
                    if (new_value.contains(" ")) {
                        
                        tfUser.setText(old_value);
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
            Logger.getLogger(VentanaLogInController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return contraseniaHash;
    }
    
}
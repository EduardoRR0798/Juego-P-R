/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegopreguntantas;

import clases.IndicadorDeIP;
import clases.RecibirIP;
import persistencia.PersistenciaCuentaUsuario;
import entity.Cuentainvitado;
import entity.Cuentausuario;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    private Object cuenta; 
    private ObservableList idiomas = 
            FXCollections.observableArrayList("Español", "English");
    private String idioma = "es";
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
            /*IndicadorDeIP indicador = new IndicadorDeIP();
            indicador.emitoIP();
            RecibirIP recibidor = new RecibirIP();
            recibidor.emitoIP();*/
            cbCambiarIdioma.setItems(idiomas);
        
    }    
    
    @FXML
    private void ingresar(ActionEvent event) throws IOException {
        
        if (validarCampos() == true && validarAcceso() == true) {
            
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
        if(tfUser.getText().equals(null)) {
            
            validador = false;
            lMensaje.setText("Llene todos los campos.");
        }
        if(pfPassword.getText().equals(null)) {
            
            validador = false;
            lMensaje.setText("Llene todos los campos.");
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
        String usuarioR = tfUser.getText().replaceAll("\\s", "");
        String contrasenia = pfPassword.getText().replaceAll("\\s", "");
        PersistenciaCuentaUsuario persistencia = 
                new PersistenciaCuentaUsuario();
        Cuentausuario usuario = persistencia.getCuentaUsuarioNombre(usuarioR
                .toUpperCase());
        if (usuario != null) {
            
            if (contrasenia.equals(usuario.getContrasenia())) {
                
                cuenta = usuario;
                ingresoExitoso = true;
                lMensaje.setText("Iniciando Sesion...");
            } else {
                
                lMensaje.setText("Usuario o Contraseña incorrectos.");
            }
        } else {
            
            lMensaje.setText("Usuario no encontrado");
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
        String usuario = tfUser.getText().replaceAll("\\s", "");
        String contrasenia = pfPassword.getText().replaceAll("\\s", "");
        PersistenciaCuentaInvitado persistencia = 
                new PersistenciaCuentaInvitado();
        Cuentainvitado invitado = 
                persistencia.getCuentaInvitado(usuario.toUpperCase());
        
        if(invitado != null) {
            
            if (contrasenia.equals(invitado.getCodigo())) {
                ingresoExitoso = true;
                cuenta = invitado;
                lMensaje.setText("Iniciando Sesion...");
            } else {
                lMensaje.setText("Usuario o Contraseña incorrectos.");
            }
            
        } else {
            lMensaje.setText("Usuario no encontrado");
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
            
            if(validarIngresoInvitado() == true) {
                ingresoExitoso = true;
            }
        
        } else {
            
            if(validarIngresoUsuario() == true) {
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
        
        String idioma;
        if(cbCambiarIdioma.getSelectionModel().getSelectedItem()
                .equals("English")) {
            
            idioma = "en";
        } else {
            
            idioma = "es";
        }
        abrirLogin(idioma, event);
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
}
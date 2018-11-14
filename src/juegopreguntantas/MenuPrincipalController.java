/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegopreguntantas;

import entity.Cuentainvitado;
import entity.Cuentausuario;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
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
import javafx.scene.control.Label;
import javafx.stage.Stage;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Eduardo Rosas Rivera                                   */ 
/* @since 29/10/2018                                              */
/* Nombre de la clase MenuPrincipalController                     */
/******************************************************************/
public class MenuPrincipalController implements Initializable {

    @FXML
    private Label lUser;
    @FXML
    private Button btnCerrar;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnUnirse;
    @FXML
    private Button btnIniciar;
    @FXML
    private Button btnInvitar;
    private Object cuenta;
    private Cuentausuario usuario;
    private Cuentainvitado invitado;
    private String idioma;
    
    /**
     * Initializes the controller class.
     */
    
    public MenuPrincipalController() {
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    /**
     * Este metodo es para ir a la ventana de registrar pregunta
     * @param event del click del mouse
     */
    @FXML
    private void registrarPregunta(ActionEvent event) {
        
        try {
            Locale.setDefault(new Locale(idioma));
            ResourceBundle resourceBundle = ResourceBundle
                    .getBundle("juegopreguntantas.lang/lang");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass()
                    .getResource("RegistrarPregunta.fxml"));
            loader.setResources(resourceBundle);
            Parent esperaJugadores = loader.load();
            RegistrarPreguntaController controller = loader.getController();
            controller.recibirParametros(cuenta, idioma);
            Scene scene = new Scene(esperaJugadores);
            Stage stage = new Stage();
            stage.setTitle("Preguntas");
            stage.setScene(scene);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException ex) {
            Logger.getLogger(MenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Este metodo es para ir a la ventana para unirse a una partida disponible
     * @param event del click del mouse
     */
    @FXML
    private void uniseAPartida(ActionEvent event) {
        
        try {

            Locale.setDefault(new Locale(idioma));
            ResourceBundle resourceBundle = ResourceBundle
                    .getBundle("juegopreguntantas.lang/lang");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass()
                    .getResource("EsperarPartida.fxml"));
            loader.setResources(resourceBundle);
            Parent esperaJugadores = loader.load();
            EsperarPartidaController controller = loader.getController();
            controller.recibirParametros(cuenta, idioma);
            Scene scene = new Scene(esperaJugadores);
            Stage stage = new Stage();
            stage.setTitle("Espera de inicio de partida");
            stage.setScene(scene);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }

    /**
     * Este metodo es para ir a la ventana para iniciar una partida
     * @param event del click del mouse
     */ 
    @FXML
    private void iniciarPartida(ActionEvent event) {
        
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
            controller.recibirParametros(cuenta, idioma);
            Scene scene = new Scene(esperaJugadores);
            Stage stage = new Stage();
            stage.setTitle("Iniciar partida");
            stage.setScene(scene);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }
    
    /**
     * Este metodo invoca a la pantalla EnviarInvitacion y le pasa como parametro
     * el objeto y el idioma.
     * @param event Clic en el boton invitar.
     */
    @FXML
    private void invitar(ActionEvent event) {
        Locale.setDefault(new Locale(idioma));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("juegopreguntantas.lang/lang");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("EnviarInvitacion.fxml"));
        loader.setResources(resourceBundle);
        Parent registro;
        try {
            registro = loader.load();
            EnviarInvitacionController controller = loader.getController();
            controller.recibirParametros(cuenta, idioma);
            
            Scene scene = new Scene(registro);
            Stage stage = new Stage();
            
            stage.setScene(scene);
            stage.show();

            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException ex) {
            Logger.getLogger(VentanaLogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Metodo que cierra la sesion del usuario, si se trata de un usuario 
     * invitado invoca borrarCuentaInvitado para elimianr su cuenta de la BD.
     * @param event Clic en el boton Cerrar Sesion.
     */
    @FXML
    private void cerrarSesion(ActionEvent event) {
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
            
            ((Node) event.getSource()).getScene().getWindow().hide();
        } catch (IOException ex) {
            Logger.getLogger(MenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            lUser.setText(usuario.getNombreusuario());
        } else {
            this.invitado = (Cuentainvitado) cuenta;
            btnIniciar.setDisable(true);
            btnRegistrar.setDisable(true);
            btnInvitar.setDisable(true);
            lUser.setText(invitado.getNombre());
        }
    }
    
    
    
}

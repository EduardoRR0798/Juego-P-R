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
import java.util.Objects;
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
import persistencia.PersistenciaCuentaInvitado;

/**
 * FXML Controller class
 *
 * @author Eduar
 */
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
    
    private Cuentausuario usuario;
    private Cuentainvitado invitado;
    private Locale locale;
    
    /**
     * Initializes the controller class.
     */
    
    public MenuPrincipalController() {
        //cuenta = (Cuentausuario) usuario;
        //System.out.println(usuario.getNombreusuario());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //lUser.setText(usuario.getNombreusuario());
    }    

    @FXML
    private void registrarPregunta(ActionEvent event) {
        
    }

    @FXML
    private void uniseAPartida(ActionEvent event) {
        
    }

    @FXML
    private void iniciarPartida(ActionEvent event) {
    }

    @FXML
    private void invitar(ActionEvent event) {
    }
    
    /**
     * Metodo que cierra la sesion del usuario, si se trata de un usuario 
     * invitado invoca borrarCuentaInvitado para elimianr su cuenta de la BD.
     * @param event Clic en el boton Cerrar Sesion.
     */
    @FXML
    private void cerrarSesion(ActionEvent event) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("juegopreguntantas.lang/lang");
            try {
                Parent root = FXMLLoader.load(getClass().getResource("VentanaLogIn.fxml"), resourceBundle);
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Inicio de sesion");
                stage.setScene(scene);
                stage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
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
        System.out.println(cuenta.getClass());
        if(cuenta instanceof Cuentausuario) {
            usuario = (Cuentausuario) cuenta;
            lUser.setText(usuario.getNombreusuario());
        } else {
            invitado = (Cuentainvitado) cuenta;
            lUser.setText(invitado.getNombre());
        }
    }
    
    /**
     * Metodo que elimina la cuenta de un usuario invitado una vez que cerro sesion.
     */
    public void borrarcuentainvitado() {
        PersistenciaCuentaInvitado persistenciainvitado = new PersistenciaCuentaInvitado();
        
    }
}
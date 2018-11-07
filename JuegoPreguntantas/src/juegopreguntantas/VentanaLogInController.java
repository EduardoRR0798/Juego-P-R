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

/**
 * FXML Controller class
 *
 * @author Eduardo Rosas Rivera
 */
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
    
    private ResourceBundle bundle;
    private Locale locale;
    private ObservableList idiomas = FXCollections.observableArrayList("Español", "English");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbCambiarIdioma.setItems(idiomas);
    }    
    
    public VentanaLogInController() {}
    
    @FXML
    private void ingresar(ActionEvent event) {
        if(validarAcceso() == true && validarCampos() == true) {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("juegopreguntantas.lang/lang");
            try {
                Parent root = FXMLLoader.load(getClass().getResource("MenuPrincipal.fxml"), resourceBundle);
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Menu principal");
                stage.setScene(scene);
                stage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } catch (IOException ex) {
                Logger.getLogger(VentanaLogInController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }

    @FXML
    private void registrar(ActionEvent event) {
        
    }

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
        }
        if(pfPassword.getText().equals(null)) {
            validador = false;
        }
        return validador;
    }
    
    /**
     * Metodo que valida si el nombre de usuario y la contrase;a introducidos por el usuario
     * concuerdan con alguno registrado en la base de datos.
     * @return true si los datos son validos, false si no coinciden.
     */
    private boolean validarIngresoUsuario() {
        boolean ingresoExitoso = false;
        Persistencia persistencia = new Persistencia();
        Cuentausuario cuenta = persistencia.getCuentaUsuarioNombre(tfUser.getText());
        if (cuenta != null) {
            if (pfPassword.getText().equals(cuenta.getContrasenia())
                    && tfUser.getText().equals(cuenta.getNombreusuario())) {
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
     * Metodo que valida si el nombre de usuario y la contrase;a introducidos por el usuario
     * consuerdan con alguno de invitado registrado en la base de datos 
     * @return true si los datos son validos, false si no coinciden.
     */
    private boolean validarIngresoInvitado() {
        boolean ingresoExitoso = false;
        Persistencia persistencia = new Persistencia();
        Cuentainvitado invitado = persistencia.getCuentaInvitado(tfUser.getText());
        if(invitado != null) {
            if (pfPassword.getText().equals(invitado.getCodigo())
                    && tfUser.getText().equals(invitado.getNombre())) {
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
     * Metodo que valida si el usuario se trata de una cuenta invitada o una cuenta registrada.
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
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegopreguntantas;

import persistencia.PersistenciaCuentaUsuario;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Eduar
 */
public class RegistrarUsuarioController implements Initializable {

    @FXML
    private TextField txtNombreUsuario;
    @FXML
    private TextField txtCorreoElectronico;
    @FXML
    private TextField txtContrasenia;
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
            Logger.getLogger(VentanaLogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    /**
     * Este metodo registra a un usuario en la base de datos.
     * @param event Clic en el boton Registrar.
     */
    @FXML
    private void registrar(ActionEvent event) {
        PersistenciaCuentaUsuario persistencia = new PersistenciaCuentaUsuario();
        if(validarCampos() == true){
            if(verificarRegistroUsuario() == true) {
                Cuentausuario usuario = new Cuentausuario();
                usuario.setNombreusuario(txtNombreUsuario.getText());
                usuario.setContrasenia(txtContrasenia.getText());
                usuario.setCorreoelectronico(txtCorreoElectronico.getText());
                persistencia.registrarCuentaUsuario(usuario);
            } else {
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
        String contrasenia = txtContrasenia.getText().trim();
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
        PersistenciaCuentaUsuario persistencia = new PersistenciaCuentaUsuario();
        Cuentausuario usuario = persistencia.getCuentaUsuarioNombre(txtNombreUsuario.getText().toLowerCase());
        if(Objects.equals(usuario, null)) {
            permiso = true;
        }
        return permiso;
    }
    

    /**
     * Este metodo verifica que el correo electronico no exista en la base de datos.
     * @return true si no existe, false si existe.
     */
    private boolean verificarRegistrosEmail() {
        boolean permiso = false;
        PersistenciaCuentaUsuario persistencia = new PersistenciaCuentaUsuario();
        Cuentausuario usuario = persistencia.getCuentaUsuarioNombre(txtCorreoElectronico.getText().toLowerCase());
        if(Objects.equals(usuario, null)) {
            permiso = true;
        }
        return permiso;
    }
    
    /**
     * Este metodo verifica si alguno de los dos metodos verificadores obtienen true.
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
}

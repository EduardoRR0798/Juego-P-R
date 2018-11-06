/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegopreguntantas;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Eduar
 */
public class VentanaLogInController implements Initializable {

    @FXML
    private ImageView imgLogin;
    @FXML
    private TextField tfUser;
    @FXML
    private TextField tfPassword;
    @FXML
    private Label lbUser;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnCerrar;
    @FXML
    private Label lTittle;
    @FXML
    private CheckBox cbInvitado;
    @FXML
    private Button btnCambiarIdioma;
    private ResourceBundle bundle;
    private Locale locale;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnLogin.setDisable(true);
    }    

    @FXML
    private void ingresar(ActionEvent event) {
        if (validarCampos() == true) {
            //btnLogin.setDisable(false);
        }
        
    }

    @FXML
    private void cancelar(ActionEvent event) {
        tfUser.clear();
        tfPassword.clear();
    }

    @FXML
    private void cerrar(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void entrarComoInvitado(ActionEvent event) {
        
    }

    @FXML
    private void cambiarIdioma(ActionEvent event) {
        
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
        if(tfPassword.getText().equals(null)) {
            validador = false;
        }
        return validador;
    }
}

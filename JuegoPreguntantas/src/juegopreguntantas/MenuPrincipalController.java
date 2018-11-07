/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegopreguntantas;

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
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
    
    /*public MenuPrincipalController(Object object) {
        this.usuario = usuario;
    }*/
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
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
}

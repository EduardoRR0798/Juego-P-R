/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegopreguntantas;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Eduar
 */
public class MenuPrincipalController implements Initializable {

    @FXML
    private Label lUser;
    @FXML
    private ComboBox<?> cbCerrar;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnUnirse;
    @FXML
    private Button btnIniciar;
    @FXML
    private Button btnInvitar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
    
}

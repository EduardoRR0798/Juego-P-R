/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegopreguntantas;

import entity.Cuentausuario;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Samsung RV415
 */
public class RegistrarPreguntaController implements Initializable {

    @FXML
    private TextField txtPregunta;
    @FXML
    private Button btnRespuesta2;
    @FXML
    private Button btnRespuesta1;
    @FXML
    private Button btnRespuesta4;
    @FXML
    private Button btnRespuesta3;
    @FXML
    private ChoiceBox<?> cbCategoriaSet;
    @FXML
    private ChoiceBox<?> cbRespuestaCorrecta;
    @FXML
    private Button btnPregunta;
    @FXML
    private TextField txtRespuesta1;
    @FXML
    private TextField txtRespuesta2;
    @FXML
    private TextField txtRespuesta3;
    @FXML
    private TextField txtRespuesta4;
    @FXML
    private ChoiceBox<?> cbNoPregunta;
    @FXML
    private Button btnAgregarPregunta;
    @FXML
    private Button btnEliminarPregunta;
    @FXML
    private Button btnEliminarSet;
    @FXML
    private ChoiceBox<?> cbNoSet;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnCancelar;
    
    private Cuentausuario cuenta;
    private String idioma;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    /**
     * Metodo que recibe el objeto de cuenta de usuario o invitado del 
     * Controlador de la pantalla que la invocó
     * @param usuario Cuenta de usuario registrado
     * @param idioma Idioma del properties
     */
    public void recibirParametros(Object usuario, String idioma){
        
        Locale.setDefault(new Locale(idioma));
        this.idioma = idioma;
        this.cuenta = (Cuentausuario)usuario;
    }
}

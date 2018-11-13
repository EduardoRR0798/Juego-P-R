package juegopreguntantas;

import entity.Cuentausuario;
import entity.Partida;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import persistencia.PersistenciaCuentaUsuario;
import persistencia.PersistenciaPartida;
import persistencia.PersistenciaSetpregunta;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez                                 */ 
/* @since 10/11/2018                                              */
/* Nombre de la clase IniciarPartidaController                    */
/******************************************************************/ 
public class InicioPartidaController implements Initializable {

    @FXML
    private TextField txtNombrePartida;
    @FXML
    private ComboBox<String> cbCategoria;
    @FXML
    private ComboBox<String> cbModoJuego;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnSiguiente;
    
    private Cuentausuario cuenta;
    private String idioma;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        PersistenciaSetpregunta setPreguntaBD = new PersistenciaSetpregunta();
        cbCategoria.getItems().addAll(setPreguntaBD.recuperarCategoria(cuenta));
    }    
    
    /**
     * Este metodo es para cancelar el inicio de una partida
     * @param event del click del mouse
     */
    @FXML
    private void cancelar(ActionEvent event) {
        
        try {

            Locale.setDefault(new Locale(idioma));
            ResourceBundle resourceBundle = ResourceBundle
                    .getBundle("juegopreguntantas.lang/lang");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass()
                    .getResource("MenuPrincipal.fxml"));
            loader.setResources(resourceBundle);
            Parent esperaJugadores = loader.load();
            MenuPrincipalController controller = loader.getController();
            controller.recibirParametros(cuenta, idioma);
            Scene scene = new Scene(esperaJugadores);
            Stage stage = new Stage();
            stage.setTitle("Espera de jugadores");
            stage.setScene(scene);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }
    
    /**
     * Este metodo es para continuar a la ventana de espera de jugadores para
     * iniciar la partida, la cual se registra antes
     * @param event del click del mouse
     */
    @FXML
    private void seguirEsperarJugadores(ActionEvent event) {
        
        if(!txtNombrePartida.getText().isEmpty() 
                && !cbModoJuego.getSelectionModel().isEmpty() 
                && !cbCategoria.getSelectionModel().isEmpty()){
            
            PersistenciaPartida partidaBD = new PersistenciaPartida();
            PersistenciaSetpregunta setPreguntaBD = new PersistenciaSetpregunta();
            Partida nuevaPartida = new Partida();
            nuevaPartida.setNombre(txtNombrePartida.getText());
            nuevaPartida.setModojuego(cbModoJuego.getValue());
            nuevaPartida.setIdsetpregunta(setPreguntaBD
                    .recuperarSetPregunta(cuenta));
            if (partidaBD.crearPartida(nuevaPartida)) {

                try {
                    
                    Locale.setDefault(new Locale(idioma));
                    ResourceBundle resourceBundle = ResourceBundle
                            .getBundle("juegopreguntantas.lang/lang");
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass()
                            .getResource("EsperarJugadores.fxml"));
                    loader.setResources(resourceBundle);
                    Parent esperaJugadores = loader.load();
                    EsperarJugadoresController controller = loader.getController();
                    controller.recibirParametros(cuenta, idioma);
                    Scene scene = new Scene(esperaJugadores);
                    Stage stage = new Stage();
                    stage.setTitle("Espera de jugadores");
                    stage.setScene(scene);
                    stage.show();
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                
            }
            
        }
        
    }
    
    /**
     * Metodo que recibe el objeto de cuenta de usuario o invitado del 
     * Controlador de la pantalla que la invocó
     * @param usuario Cuenta de usuario registrado
     * @param idioma Idioma del properties
     */
    public void recibirParametros(Object usuario, String idioma){
        
        Locale.setDefault(new Locale(idioma));
        cuenta = (Cuentausuario)usuario;
    }
}

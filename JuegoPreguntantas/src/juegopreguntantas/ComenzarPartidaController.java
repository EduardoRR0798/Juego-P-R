package juegopreguntantas;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez                                 */ 
/* @since 06/11/2018                                              */
/* Nombre de la clase ComenzarPartidaController                   */
/******************************************************************/ 

public class ComenzarPartidaController implements Initializable {
    @FXML
    private ListView<?> listJugadores;
    @FXML
    private Button btnIniciar;
    @FXML
    private Button btnCancelar;

    /**
     * Este metodo es para cancelar el inicio de la partida y regresar a la 
     * ventana anterior de Inicio de partida
     * @param event del click del mouse
     */
    @FXML
    private void cancelar(ActionEvent event) {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle
                ("juegopreguntantas.lang/lang");
            Parent root = FXMLLoader.load(getClass().getResource
                ("InicioPartida.fxml"), resourceBundle);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Inicio de partida");
            stage.setScene(scene);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Este metodo es para iniciar la partida con los jugadores unidos 
     * @param event del click del mouse
     */
    @FXML
    private void iniciar(ActionEvent event) {
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}

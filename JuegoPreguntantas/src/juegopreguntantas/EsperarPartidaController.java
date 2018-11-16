package juegopreguntantas;

import entity.Cuentainvitado;
import entity.Cuentausuario;
import entity.Setpregunta;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import persistencia.PersistenciaPartida;
import persistencia.PersistenciaSetpregunta;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez                                 */ 
/* @since 07/11/2018                                              */
/* Nombre de la clase EnviarInvitacionController                  */
/******************************************************************/
public class EsperarPartidaController implements Initializable {

    @FXML
    private ComboBox<String> cbCategorias;
    @FXML
    private ComboBox<String> cbPartidas;
    @FXML
    private Button btnCancelar;
    
    private Object cuenta;
    private String idioma;    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        PersistenciaSetpregunta setPreguntaBD = new PersistenciaSetpregunta();
        List<String> categorias = setPreguntaBD
                .recuperarCategoria((Cuentausuario) cuenta);
        cbCategorias.getItems().addAll(categorias);
        if (cbCategorias.getItems().isEmpty()) {

            cbCategorias.setDisable(true);
            cbPartidas.setDisable(true);
        }
        
    }

    /**
     * Este metodo es para cancelar el inicio de la partida y regresar a la 
     * ventana anterior de Inicio de partida
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
            stage.setTitle("Menu principal");
            stage.setScene(scene);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            
            Logger.getLogger(EnviarInvitacionController.class.getName()).log(Level.SEVERE, null, e);
        }
    }    
    
    /**
     * Este metodo es para mostrar las partidas del set de preguntas 
     * seleccionado anteriormente
     * @param event del click del mouse
     */
    @FXML
    private void mostrarPartidas(ActionEvent event) {

        PersistenciaPartida partidaBD = new PersistenciaPartida();
        PersistenciaSetpregunta setPreguntaBD = new PersistenciaSetpregunta();
        Setpregunta setpregunta = setPreguntaBD
                .recuperarSetPregunta(cbCategorias.getValue());
        cbPartidas.getItems().addAll(partidaBD.recuperarNombre(setpregunta));
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
        this.cuenta = usuario;
        Locale.setDefault(new Locale(idioma));
    }
    
}

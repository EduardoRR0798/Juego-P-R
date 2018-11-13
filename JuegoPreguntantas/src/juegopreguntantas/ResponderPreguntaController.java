package juegopreguntantas;

import entity.Cuentausuario;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Samsung RV415
 */
public class ResponderPreguntaController implements Initializable {

    @FXML
    private TextField tfMensaje;
    @FXML
    private Button btnEnviar;
    @FXML
    private TextArea txtChat;
    @FXML
    private ImageView imgA;
    @FXML
    private ImageView imgB;
    @FXML
    private ImageView imgC;
    @FXML
    private ImageView imgD;
    @FXML
    private Label lblPuntaje;
    @FXML
    private Label lblTiempo;
    @FXML
    private Label lbPuntajeNum;
    @FXML
    private Label lblPreguntaNo;
    @FXML
    private Label lblCategoria;
    @FXML
    private Label lblPregunta;
    
    private Cuentausuario cuenta;
    private String idioma;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void enviar(ActionEvent event) {
    }

    @FXML
    private void seleccionarA(MouseEvent event) {
    }

    @FXML
    private void seleccionarB(MouseEvent event) {
    }

    @FXML
    private void seleccionarC(MouseEvent event) {
    }

    @FXML
    private void seleccionarD(MouseEvent event) {
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

package juegopreguntantas;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Eduardo Rosas Rivera                                   */ 
/* @since 28/11/2018                                              */
/* Nombre de la clase JuegoPreguntantas                           */
/******************************************************************/
public class JuegoPreguntantas extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Locale.setDefault(new Locale("es"));
        ResourceBundle resourceBundle = 
                ResourceBundle.getBundle("juegopreguntantas.lang/lang");
        Parent root = 
                FXMLLoader.load(getClass().getResource("VentanaLogIn.fxml"),
                        resourceBundle);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

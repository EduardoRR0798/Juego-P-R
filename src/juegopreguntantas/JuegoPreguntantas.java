/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegopreguntantas;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Eduar
 */
public class JuegoPreguntantas extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Locale.setDefault(new Locale("es"));

        ResourceBundle resourceBundle = ResourceBundle.getBundle("juegopreguntantas.lang/lang");
        Parent root = FXMLLoader.load(getClass().getResource("VentanaLogIn.fxml"), resourceBundle);
        
        Scene scene = new Scene(root);
        VentanaLogInController vlc = new VentanaLogInController();
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

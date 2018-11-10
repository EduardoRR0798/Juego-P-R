package juegopreguntantas;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Date;
import java.util.Properties;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import persistencia.PersistenciaCuentaInvitado;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez                                 */ 
/* @since 07/11/2018                                              */
/* Nombre de la clase EnviarInvitacionController                  */
/******************************************************************/
public class EnviarInvitacionController implements Initializable {

    @FXML
    private TextField txtCorreoElectronico;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnEnviarInvitacion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
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
                ("MenuPrincipal.fxml"), resourceBundle);
            Scene scene = new Scene(root);
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
     * Este metodo es para enviar una invitacion por correo electronico para 
     * unirse a una partida, cuando el enviado es exitoso envía un mensaje de
     * confirmacion!!!!!!!!!!!!!!!
     * @param event del click del mouse
     */
    @FXML
    private void enviarInvitacion(ActionEvent event) throws MessagingException {
        
        PersistenciaCuentaInvitado consultaBD = new PersistenciaCuentaInvitado();
        int datoRepetido = 0;//consultaBD.comprobarInvitado
            //(txtCorreoElectronico.getText(), "x0o");
        if(datoRepetido == 1){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Ya se había invitado a este correo");
            alert.showAndWait();
        } else if (datoRepetido == 2){
            //tendré que separar los métods por que si el código está repetido se tiene que volver a sacar random y volver a checar si no se repite
        } else {
            //enviar la invitación
        }
        try {
            
            String to = "baoasamiya2002@gmail.com";
            String from = "baoasamiya2002@gmail.com";
            String host = "smtp.gmail.com";
            String port = "587";
            final String password = "******";
            Properties properties = new Properties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", port);
            properties.put("mail.smtp.debug", "true");
            Authenticator auth = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            };
            
            Session session = Session.getInstance(properties, auth);
            Message message = new MimeMessage(session);
            InternetAddress[] address = {new InternetAddress(to)};
            message.setRecipients(Message.RecipientType.TO, address);
            message.setSubject("This is the Subject Line!");
            message.setSentDate(new Date());
            message.setText("This is actual message");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (AddressException ex) {
            
            Logger.getLogger(EnviarInvitacionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException mex) {
            
            System.out.println("Error: unable to send message....");
            Logger.getLogger(EnviarInvitacionController.class.getName()).log(Level.SEVERE, null, mex);
        }
        /*Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("I have a great message for you!");
        alert.showAndWait();*/
    }
        
    private void comprobarInvitado() {
        
    }
}

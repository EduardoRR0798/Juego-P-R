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
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez                                 */ 
/* @since 07/11/2018                                              */
/* Nombre de la clase EnviarInvitacionController                  */
/******************************************************************/
public class EsperarPartidaController implements Initializable {

    @FXML
    private ComboBox<?> cbCategorias;
    @FXML
    private ComboBox<?> cbPartidas;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //ESTO DEBE ESTAR EN LO DE LA DAO
        /*
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.user", "root");
        properties.put("javax.persistence.jdbc.password", "puxkas");

        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("JuegoPreguntantasPU", properties);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            List<Employees> results = em.createNamedQuery("Employees.findByHireDate").setParameter("hireDate", date1).getResultList();
            System.out.println(results.get(0).getEmpNo());
        } catch (ParseException ex) {
            Logger.getLogger(Empleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }    
    
}

package juegopreguntantas;

import entity.Cuentausuario;
import entity.Setpregunta;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import persistencia.PersistenciaPartida;
import persistencia.PersistenciaSetpregunta;
import utilidades.UtilidadCadenas;

/****************************************************************** 
 * @version 1.0                                                   * 
 * @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          * 
 * @since 10/11/2018                                              *
 * Nombre de la clase InicioPartidaController                     *
 *****************************************************************/
public class InicioPartidaController implements Initializable {

    @FXML
    private TextField txtNombrePartida;
    @FXML
    private ComboBox<String> cbCategoria;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnSiguiente;

    private Cuentausuario usuario;
    private String idioma;
    private Setpregunta setPregunta;
    private static final String RECURSO = "juegopreguntantas.lang/lang";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        UtilidadCadenas cadena = new UtilidadCadenas();
        cadena.limitarCampos(txtNombrePartida, 30);
        excluirEspacios();
    }    
    
    /**
     * Constructor de la clase.
     */
    public InicioPartidaController() {
        //Vacio para instanciacion
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
                    .getBundle(RECURSO);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass()
                    .getResource("MenuPrincipal.fxml"));
            loader.setResources(resourceBundle);
            Parent esperaJugadores = loader.load();
            MenuPrincipalController controller = loader.getController();
            controller.recibirParametros(usuario, idioma);
            Scene scene = new Scene(esperaJugadores);
            Stage stage = new Stage();
            stage.setTitle("Menu Principal");
            stage.setScene(scene);
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            
            Logger.getLogger(InicioPartidaController.class.getName())
                        .log(Level.SEVERE, null, e);
        }
    }
    
    /**
     * Este metodo es para continuar a la ventana de espera de jugadores para
     * iniciar la partida, la cual se registra antes
     * @param event del click del mouse
     */
    @FXML
    private void seguirEsperarJugadores(ActionEvent event) {

        if (validarCampos() && registrarPartida()) {

            try {

                Locale.setDefault(new Locale(idioma));
                ResourceBundle resourceBundle = ResourceBundle
                        .getBundle(RECURSO);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass()
                        .getResource("EsperarJugadores.fxml"));
                loader.setResources(resourceBundle);
                Parent esperaJugadores = loader.load();
                EsperarJugadoresController controller
                        = loader.getController();
                controller.recibirParametros(usuario, idioma,
                        setPregunta.getIdsetpregunta());
                Scene scene = new Scene(esperaJugadores);
                Stage stage = new Stage();
                stage.setTitle("Espera de jugadores");
                stage.setScene(scene);
                stage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {

                Logger.getLogger(InicioPartidaController.class
                        .getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    /**
     * Este metodo valida que el usuario haya seleccionado correctamente los
     * campos.
     * @return true si los campos necesarios estan seleccionados, false si no lo
     * son.
     */
    private boolean validarCampos() {
        
        boolean permiso = false;
         if(!txtNombrePartida.getText().isEmpty() && 
                 !cbCategoria.getSelectionModel().isEmpty()) {
             
             permiso = true;
        }
        return permiso; 
    }
    /**
     * Metodo que para mostrar las categorias de los set de pregunta que ha 
     * hecho el usuario
     */
    public void mostrarCategorias() {
        
        PersistenciaSetpregunta setPreguntaBD = new PersistenciaSetpregunta();
        List<String> categorias = setPreguntaBD.recuperarCategorias(usuario);
        Platform.runLater(() -> cbCategoria.getItems().addAll(categorias));
        
        if (categorias.isEmpty()) {

            cbCategoria.setDisable(true);
        }
    }
    
    /**
     * Metodo que para registrar una partida
     * @return 
     */
    private boolean registrarPartida() {

        PersistenciaPartida partidaBD = new PersistenciaPartida();
        PersistenciaSetpregunta setPreguntaBD = new PersistenciaSetpregunta();
        List<Setpregunta> sets = setPreguntaBD.recuperarSetPregunta(usuario);
        List<String> categorias = setPreguntaBD.recuperarCategorias(usuario);
        setPregunta = sets.get(categorias.indexOf(cbCategoria.getValue()));
        return partidaBD.crearPartida(txtNombrePartida.getText(), setPregunta);
    }
    
    /**
     * Este metodo recibe los parametros de la ventana anterior y los fija
     * como atributo.
     * @param usuario usuario registrado.
     * @param idioma idioma del properties.
     */
    public void recibirParametros(Cuentausuario usuario, String idioma) {
        
        Locale.setDefault(new Locale(idioma));
        this.idioma = idioma;
        this.usuario = usuario;
        mostrarCategorias();
    }
    
     /**
     * Este metodo sirve para que los textField nieguen la entrada a espacios.
     */
    private void excluirEspacios() {

        txtNombrePartida.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    if (newValue.contains(" ")) {

                        txtNombrePartida.setText(oldValue);
                    }
                });
    }
    
}

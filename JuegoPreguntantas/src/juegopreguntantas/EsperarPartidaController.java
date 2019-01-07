package juegopreguntantas;

import entity.Partida;
import entity.Setpregunta;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import persistencia.PersistenciaCategoria;
import persistencia.PersistenciaPartida;
import persistencia.PersistenciaSetpregunta;

/****************************************************************** 
 * @version 1.0                                                   * 
 * @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          * 
 * @since 06/11/2018                                              *
 * Nombre de la clase EsperarPartidaController                    *
 *****************************************************************/
public class EsperarPartidaController implements Initializable {

    @FXML
    private ComboBox<String> cbCategorias;
    @FXML
    private ComboBox<Partida> cbPartidas;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnUnirse;
    
    private Object cuenta;
    private String idioma;    
    private static final String RECURSO = "juegopreguntantas.lang/lang";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //No es necesario hacer algo.
    }
    
    /**
     * Constructor de la clase.
     * @throws URISyntaxException 
     */
    public EsperarPartidaController() throws URISyntaxException {
        //Vacio para instanciacion.
    }
    
    /**
     * Este metodo se usa para unirse a una partida.
     * @param event Clic en el boton Unirse.
     */
    @FXML
    private void unirse(ActionEvent event) {
      
        if (validarCampos()) {

            abrirVentanaJugadoresConectados();
            btnCancelar.getScene().getWindow().hide();
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
                    .getBundle(RECURSO);
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
            
            Logger.getLogger(EsperarPartidaController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }    
    
    /**
     * Este metodo verifica que se hayan seleccionado los comboBox de las 
     * categorias y partidas.
     * @return 
     */
    private boolean validarCampos() {
        
        boolean permiso = true;
        if(cbCategorias.getSelectionModel().isEmpty()) {
            permiso = false;
        }
        if(cbPartidas.getSelectionModel().isEmpty()) {
            permiso = false;
        }
        return permiso;
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
        PersistenciaCategoria categoriaBD = new PersistenciaCategoria();
        int id = categoriaBD.recuperarIdCategoria(cbCategorias.getSelectionModel().getSelectedItem());
        List<Setpregunta> sets = setPreguntaBD.recuperarSetCategoria(id);
        ObservableList<Partida> partidas = FXCollections.observableArrayList();
        List<Partida> par;
        for (int i = 0; i < sets.size(); i++) {

            par = partidaBD.recuperarPartida(sets.get(i));
            for (int j = 0; j < par.size(); j++) {

                partidas.add(par.get(j));
            }
        }
        cbPartidas.setItems(partidas);
    }
    
    /**
     * Metodo que para mostrar las categorias de los set de pregunta que ha 
     * hecho el usuario
     */
    public void mostrarCategorias(){
        
        PersistenciaCategoria cate = new PersistenciaCategoria();
        
        List<String> categoriasMostrar =  cate.recuperarCategorias();
        cbCategorias.getItems().addAll(categoriasMostrar);
        if (cbCategorias.getItems().isEmpty()) {

            cbCategorias.setDisable(true);
            cbPartidas.setDisable(true);
        }
    }
    
    /**
     * Metodo que recibe el objeto de cuenta de usuario o invitado del 
     * Controlador de la pantalla que la invocó
     * @param cuenta objeto invitado o usuario registrado
     * @param idioma Idioma del properties
     */
    public void recibirParametros(Object cuenta, String idioma){
        
        this.idioma = idioma;
        this.cuenta = cuenta;
        
        mostrarCategorias();
    }
    
    /**
     * Este metodo es para abrir la ventana de jugadores conectados.
     */
    public void abrirVentanaJugadoresConectados() {
        
        try {
            Locale.setDefault(new Locale(idioma));
            ResourceBundle resourceBundle
                    = ResourceBundle.getBundle(RECURSO);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("EsperarJugadores.fxml"));
            loader.setResources(resourceBundle);

            Parent responder = loader.load();
            EsperarJugadoresController controller = loader.getController();
            controller.recibirParametros(cuenta, idioma, -1);

            Scene scene = new Scene(responder);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();

        } catch (Exception ex) {

            Logger.getLogger(EsperarPartidaController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
}

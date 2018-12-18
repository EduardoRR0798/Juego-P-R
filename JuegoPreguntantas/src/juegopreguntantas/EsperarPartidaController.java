package juegopreguntantas;

import clasesutilidad.JugadorConectadoEnvio;
import entity.Cuentainvitado;
import entity.Cuentausuario;
import entity.Partida;
import entity.Setpregunta;
import io.socket.client.IO;
import io.socket.client.Socket;
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
import org.json.JSONException;
import org.json.JSONObject;
import persistencia.PersistenciaCategoria;
import persistencia.PersistenciaPartida;
import persistencia.PersistenciaSetpregunta;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          */ 
/* @since 07/11/2018                                              */
/* Nombre de la clase EsperarPartidaController                    */
/******************************************************************/
public class EsperarPartidaController implements Initializable {

    @FXML
    private ComboBox<String> cbCategorias;
    @FXML
    private ComboBox<Partida> cbPartidas;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnUnirse;
    
    private final Socket socket;
    private Object cuenta;
    private String idioma;    
    private Cuentausuario usuario;
    private Cuentainvitado invitado;
    private JugadorConectadoEnvio conectado;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    /**
     * Constructor de la clase.
     * @throws URISyntaxException 
     */
    public EsperarPartidaController() throws URISyntaxException {
        
        ResourceBundle propiedadesCliente = ResourceBundle.getBundle(
                "utilidades.conexiones");
        String ipServidor = propiedadesCliente.getString(
                "key.ipServidor");
        socket  = IO.socket("http://" + ipServidor + ":5000");
        socket.on(Socket.EVENT_CONNECT, (Object... os) -> {
            
            System.out.println("Conectado..");
        });
        
        socket.connect();
    }
    
    /**
     * Este metodo se usa para unirse a una partida.
     * @param event Clic en el boton Unirse.
     */
    @FXML
    private void unirse(ActionEvent event) {
        
        if(validarCampos()) {
            
            try {

                conectado = new JugadorConectadoEnvio();
                if (cuenta instanceof Cuentausuario) {

                    conectado.setNombre(usuario.getNombreusuario());
                } else {

                    conectado.setNombre(invitado.getNombre());
                }

                JSONObject nuevoConectado = new JSONObject();
                nuevoConectado.put("idSocket", socket.id());
                nuevoConectado.put("nombre", conectado.getNombre());
                socket.emit("registrarPregunton", nuevoConectado);
                abrirVentanaJugadoresConectados();
                btnCancelar.getScene().getWindow().hide();
            } catch (JSONException ex) {

                Logger.getLogger(EsperarJugadoresController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
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
        for(int i = 0; i < sets.size(); i++) {
            
            par = partidaBD.recuperarPartida(sets.get(i));
            for(int j = 0; j < par.size(); j++) {
                
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
        
        PersistenciaSetpregunta setPreguntaBD = new PersistenciaSetpregunta();
        List<String> categorias = setPreguntaBD
                .recuperarCategorias((Cuentausuario) cuenta);
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
        if(cuenta instanceof Cuentausuario) {
            
            usuario = (Cuentausuario) cuenta;
        } else {
            
            invitado = (Cuentainvitado) invitado;
        }
        
        mostrarCategorias();
    }
    
    public void abrirVentanaJugadoresConectados() {
        
        try {
            Locale.setDefault(new Locale(idioma));
            ResourceBundle resourceBundle
                    = ResourceBundle.getBundle("juegopreguntantas.lang/lang");

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

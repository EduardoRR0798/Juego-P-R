package juegopreguntantas;

import entity.Cuentausuario;
import entity.Setpregunta;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          */ 
/* @since 10/11/2018                                              */
/* Nombre de la clase IniciarPartidaController                    */
/******************************************************************/ 
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
    private String categoria;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        limitarCampos(txtNombrePartida, 30);
        excluirEspacios();
    }    
    
    /**
     * Constructor de la clase.
     */
    public InicioPartidaController() {
        
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
                    .getBundle("juegopreguntantas.lang/lang");
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
        
        if(!txtNombrePartida.getText().isEmpty() 
                && !cbCategoria.getSelectionModel().isEmpty()){
            
            PersistenciaPartida partidaBD = new PersistenciaPartida();
            PersistenciaSetpregunta setPreguntaBD = 
                    new PersistenciaSetpregunta();
            if(registrarPartida()) {
            //if (partidaBD.crearPartida(txtNombrePartida.getText(), 
            //        setPreguntaBD.recuperarSetPregunta(usuario))) {

                try {
                    Locale.setDefault(new Locale(idioma));
                    ResourceBundle resourceBundle = ResourceBundle
                            .getBundle("juegopreguntantas.lang/lang");
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass()
                            .getResource("EsperarJugadores.fxml"));
                    loader.setResources(resourceBundle);
                    Parent esperaJugadores = loader.load();
                    EsperarJugadoresController controller = 
                            loader.getController();
                    System.out.println("El set es..." + setPregunta.getIdsetpregunta());
                    controller.recibirParametros(usuario, idioma, setPregunta.getIdsetpregunta());
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
    }
    
    /**
     * Metodo que para mostrar las categorias de los set de pregunta que ha 
     * hecho el usuario
     */
    public void mostrarCategorias() {
        
        PersistenciaSetpregunta setPreguntaBD = new PersistenciaSetpregunta();
        List<String> categorias = setPreguntaBD.recuperarCategorias(usuario);
        Platform.runLater(() -> {
            
            cbCategoria.getItems().addAll(categorias);
        });
        
        if (categorias.isEmpty()) {

            cbCategoria.setDisable(true);
        }
    }
    
    /**
     * Este metodo sirve para validar que se ingreso algo en el campo de la 
     * partida y se selecciono una categoria.
     * @return true si los datos son correctos, false sino.
     */
    private boolean validarCampos() {
        
        boolean permiso = true;
        if(Objects.equals(txtNombrePartida.getText().trim(), null)) {
            
            permiso = false;
        }
        if(Objects.equals(cbCategoria.getSelectionModel().getSelectedItem(),
                null)) {
            
            permiso = false;
        }
        return permiso;
    }
    
    /**
     * Metodo que para registrar una partida
     * @return 
     */
    private boolean registrarPartida() {

        /*this.setPregunta = setPreguntaBD.recuperarSetPregunta(cbCategoria.getValue());
        */
        ///////////////////////////////
        PersistenciaPartida partidaBD = new PersistenciaPartida();
        PersistenciaSetpregunta setPreguntaBD = new PersistenciaSetpregunta();
        List<Setpregunta> sets = setPreguntaBD.recuperarSetPregunta(usuario);
        List<String> categorias = setPreguntaBD.recuperarCategorias(usuario);
        setPregunta = sets.get(categorias.indexOf(cbCategoria.getValue()));
        System.out.println("Es..." + setPregunta.getIdsetpregunta());
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
     * Este metodo impide que el campo de texto sea mayor a un numero de 
     * caracteres fijo.
     * @param tf textField a limitar
     * @param maximo numero maximo de caracteres permitidos.
     */
    private void limitarCampos(javafx.scene.control.TextField tf, int maximo) {
        
        tf.textProperty().addListener(new ChangeListener<String>() {
            
            @Override
            public void changed(final ObservableValue<? extends String> ov, 
                    final String oldValue, final String newValue) {
                
                if (tf.getText().length() > maximo) {
                    
                    String s = tf.getText().substring(0, maximo);
                    tf.setText(s);
                }
            }
        });
    }
    
     /**
     * Este metodo sirve para que los textField nieguen la entrada a espacios.
     */
    private void excluirEspacios() {
        
        txtNombrePartida.textProperty().addListener(
                (observable, old_value, new_value) -> {
                    
                    if (new_value.contains(" ")) {
                        
                        txtNombrePartida.setText(old_value);
                    }
                });        
    }
    
}

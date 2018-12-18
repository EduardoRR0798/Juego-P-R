package juegopreguntantas;

import entity.Cuentausuario;
import entity.Pregunta;
import entity.Respuesta;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import persistencia.PersistenciaPregunta;
import persistencia.PersistenciaSetpregunta;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez                                 */ 
/* @since 17/11/2018                                              */
/* Nombre de la clase ResponderPreguntaController                 */
/******************************************************************/
public class RegistrarPreguntaController implements Initializable {

    @FXML
    private TextField txtPregunta;
    @FXML
    private Button btnRespuesta2;
    @FXML
    private Button btnRespuesta1;
    @FXML
    private Button btnRespuesta4;
    @FXML
    private Button btnRespuesta3;
    @FXML
    private Button btnCancelarImagen1;
    @FXML
    private Button btnCancelarImagen2;
    @FXML
    private Button btnCancelarImagen3;
    @FXML
    private Button btnCancelarImagen4;
    @FXML
    private ChoiceBox<String> cbCategoriaSet;
    @FXML
    private ChoiceBox<String> cbRespuestaCorrecta;
    @FXML
    private Button btnPregunta;
    @FXML
    private Button btnCancelarPregunta;
    @FXML
    private TextField txtRespuesta1;
    @FXML
    private TextField txtRespuesta2;
    @FXML
    private TextField txtRespuesta3;
    @FXML
    private TextField txtRespuesta4;
    @FXML
    private Label lblNoPregunta;
    @FXML
    private Button btnAgregarPregunta;
    @FXML
    private Button btnEliminarPregunta;
    @FXML
    private Button btnEliminarSet;
    @FXML
    private Label lblNoSet;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnCancelar;
    
    private Cuentausuario cuenta;
    private String idioma;
    List<Pregunta> preguntas = new ArrayList<Pregunta>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        excluirEspacios();
        limitarCampos(txtPregunta, 80);
        limitarCampos(txtRespuesta1, 30);
        limitarCampos(txtRespuesta2, 30);
        limitarCampos(txtRespuesta3, 30);
        limitarCampos(txtRespuesta4, 30);
    }    
    
    /**
     * Este metodo es para cancelar el inicio de la partida y regresar al menu
     * @param event del click del mouse al boton cancelar
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

            Logger.getLogger(EnviarInvitacionController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }
    
    /**
     * Este metodo es para registrar un set de preguntas
     * @param event del click del mouse al boton registrar set
     */
    @FXML
    private void registrarSet(ActionEvent event) {
        
        if ((Integer.parseInt(lblNoPregunta.getText()) > 0) && 
                !cbCategoriaSet.getSelectionModel().isEmpty()) {

            PersistenciaSetpregunta setPreguntaBD = new PersistenciaSetpregunta();
            PersistenciaPregunta preguntaBD = new PersistenciaPregunta();
            if (setPreguntaBD.crearSetPregunta(cbCategoriaSet.
                    getSelectionModel().getSelectedItem(), cuenta, preguntas)) {

                int noUltimoSet = Integer.parseInt(lblNoSet.getText()) + 1;
                lblNoSet.setText(Integer.toString(noUltimoSet));
                preguntas.clear();
                lblNoPregunta.setText("00");
                mostrarCreacionExito();
            } else {
                                
                mostrarCreacionFracaso();
            }
        }

    }
    
    /**
     * Este metodo es para agregar una pregunta con respuesta a la base de datos
     * , como también a la aplicación, en caso de que sea una imagen.
     * @param event del click del mouse al boton "+" de agregar pregunta
     */
    @FXML
    private void agregarPregunta(ActionEvent event) {
        
        List<String> paths = listaPaths();
        List<Respuesta> respuestas = listaRespuesta(paths);
        PersistenciaPregunta preguntaBD = new PersistenciaPregunta();
        Pregunta nuevaPregunta = new Pregunta();
        boolean exito = false;
        if (!respuestas.isEmpty()) {
                        
            if (txtPregunta.getText().isEmpty() && 
                    !btnPregunta.getText().equals("+")) {
                
                nuevaPregunta.setRespuestaCollection(respuestas);
                nuevaPregunta.setPregunta(paths.get(0));
                nuevaPregunta.setTipoPregunta(2);
                preguntas.add(nuevaPregunta);
                guardarImagen(paths.get(0), btnPregunta);
                exito = true;
            } else if (btnPregunta.getText().equals("+") && 
                    !txtPregunta.getText().isEmpty()) {
                
                nuevaPregunta.setRespuestaCollection(respuestas);
                nuevaPregunta.setPregunta("¿" + txtPregunta.getText() + "?");
                nuevaPregunta.setTipoPregunta(1);
                preguntas.add(nuevaPregunta);
                txtPregunta.clear();
                exito = true;
            } 
            
            if (exito == true){
                
                if(respuestas.get(0).getTipoRespuesta() == 1) {
                                        
                    txtRespuesta1.clear();
                    txtRespuesta2.clear();
                    txtRespuesta3.clear();
                    txtRespuesta4.clear();
                } else {
                    
                    guardarImagen(paths.get(1), btnRespuesta1);
                    guardarImagen(paths.get(2), btnRespuesta2);
                    guardarImagen(paths.get(3), btnRespuesta3);
                    guardarImagen(paths.get(4), btnRespuesta4);
                    System.out.println(paths.get(1));
                    System.out.println(paths.get(2));
                    System.out.println(paths.get(3));
                    System.out.println(paths.get(4));
                }
                
                limpiarBotones();
                int noUltimaPregunta = Integer.
                        parseInt(lblNoPregunta.getText()) + 1;
                lblNoPregunta.setText(Integer.toString(noUltimaPregunta));
            }
            
        }
           
    }
    
    /**
     * Este metodo es para seleccionar una imagen que tenga el usuario en su 
     * computadora
     * @param event del click del mouse al boton "+" de los botones de respuesta
     * o pregunta
     */
    @FXML
    private void seleccionarImagen(ActionEvent event) {
        
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.
                ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setTitle("Selecciona una imagen");
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(85);
            imageView.setFitHeight(85);
            ((Button) event.getSource()).setText("");
            ((Button) event.getSource()).setGraphic(imageView);
            activarCancelarImagen((Button) event.getSource());
            bloquearCampoTexto((Button) event.getSource());
        }
        
    }
    
    /**
     * Este metodo es para cancelar la imagen que se había subido
     * @param event del click del mouse "x" de los botones de respuesta o 
     * pregunta
     */
    @FXML
    private void cancelarImagen(ActionEvent event) {
        
        if (((Button) event.getSource()).equals(btnCancelarImagen1)) {

            btnRespuesta1.setGraphic(null);
            btnRespuesta1.setText("+");
            btnCancelarImagen1.setVisible(false);
            txtRespuesta1.setDisable(false);
        } else if (((Button) event.getSource()).equals(btnCancelarImagen2)) {
            
            btnRespuesta2.setGraphic(null);
            btnRespuesta2.setText("+");
            btnCancelarImagen2.setVisible(false);
            txtRespuesta2.setDisable(false);
        } else if (((Button) event.getSource()).equals(btnCancelarImagen3)) {

            btnRespuesta3.setGraphic(null);
            btnRespuesta3.setText("+");
            btnCancelarImagen3.setVisible(false);
            txtRespuesta3.setDisable(false);
        } else if (((Button) event.getSource()).equals(btnCancelarImagen4)) {
            
            btnRespuesta4.setGraphic(null);
            btnRespuesta4.setText("+");
            btnCancelarImagen4.setVisible(false);
            txtRespuesta4.setDisable(false);
        } else if (((Button) event.getSource()).equals(btnCancelarPregunta)) {
            
            btnPregunta.setGraphic(null);
            btnPregunta.setText("+");
            btnCancelarPregunta.setVisible(false);
            txtPregunta.setDisable(false);
        }
                
    }
    
    /**
     * Este metodo es para comprobar que se subieron todas las imagenes de las 
     * respuestas, en caso de que el usuario haya elegido subir imagenes
     * @return Si es verdadero o no que se seleccionaron las imagenes para las
     * respuestas
     */
    private boolean comprobarImagenLleno() {
        
        return !btnRespuesta1.getText().equals("+") &&
                !btnRespuesta2.getText().equals("+") &&
                !btnRespuesta3.getText().equals("+") &&
                !btnRespuesta4.getText().equals("+") &&
                !cbRespuestaCorrecta.getSelectionModel().isEmpty();
    }
    
    /**
     * Este metodo es para comprobar que se ingresaron todos los textos de las 
     * respuestas, en caso de que el usuario haya elegido escribir respuestas
     * @return Si es verdadero o no que se escribieron las respuestas
     */
    private boolean comprobarTextoLleno() {

        return !txtRespuesta1.getText().isEmpty() && 
                !txtRespuesta2.getText().isEmpty() && 
                !txtRespuesta3.getText().isEmpty() && 
                !txtRespuesta4.getText().isEmpty() && 
                !cbRespuestaCorrecta.getSelectionModel().isEmpty();
    }
    
    /**
     * Este metodo es para activar los botones para cancelar una imagen, que 
     * cuando no hay una seleccionada, estos botones estan ocultos.
     * @param imagenRespuesta boton que tiene la imagen
     */
    private void activarCancelarImagen(Button imagenRespuesta) {

        if (imagenRespuesta.equals(btnRespuesta1)) {

            btnCancelarImagen1.setVisible(true);
        } else if (imagenRespuesta.equals(btnRespuesta2)) {
            
            btnCancelarImagen2.setVisible(true);
        } else if (imagenRespuesta.equals(btnRespuesta3)) {

            btnCancelarImagen3.setVisible(true);
        } else if (imagenRespuesta.equals(btnRespuesta4)) {
            
            btnCancelarImagen4.setVisible(true);
        } else if (imagenRespuesta.equals(btnPregunta)) {
            
            btnCancelarPregunta.setVisible(true);
        }
    }
    
    /**
     * Este metodo es para bloquear el campo de texto de la respuesta en caso de
     * que se haya elegido subir una imagen, para evitar el error del usuario
     * @param imagenRespuesta boton que tiene la imagen
     */
    private void bloquearCampoTexto(Button imagenRespuesta) {

        if (imagenRespuesta.equals(btnRespuesta1)) {

            txtRespuesta1.setDisable(true);
            txtRespuesta1.clear();
        } else if (imagenRespuesta.equals(btnRespuesta2)) {
            
            txtRespuesta2.setDisable(true);
            txtRespuesta2.clear();
        } else if (imagenRespuesta.equals(btnRespuesta3)) {

            txtRespuesta3.setDisable(true);
            txtRespuesta3.clear();
        } else if (imagenRespuesta.equals(btnRespuesta4)) {
            
            txtRespuesta4.setDisable(true);
            txtRespuesta4.clear();
        } else if (imagenRespuesta.equals(btnPregunta)) {
            
            txtPregunta.setDisable(true);
            txtPregunta.clear();
        }
    }
    
    /**
     * Este metodo es para guardar en una lista las rutas o paths para las 
     * imagenes que se hayan subido como respuesta
     * @return Lista con las rutas de las imagenes
     */
    private List<String> listaPaths(){
        
        List<String> paths = new ArrayList<String>();
        PersistenciaSetpregunta setPreguntaBD = new PersistenciaSetpregunta();
        int noSet = setPreguntaBD.recuperarUltimaId() + 1;
        paths.add(".\\imagenes\\" + noSet + "\\" + lblNoPregunta.getText() + 
                "\\imagenPregunta.png");
        for(int i = 1; i < 5; i++){
            
            paths.add(".\\imagenes\\" + noSet + "\\" + lblNoPregunta.getText() + 
                    "\\imagenRespuesta" + i + ".png");
        }
        return paths;
    }
    
    /**
     * Este metodo es para guardar en una lista las respuestas llenas
     * @param path La listade de rutas de las imagenes
     * @return Lista con las respuestas dadas por el usuario
     */
    private List<Respuesta> listaRespuesta(List<String> path){
        
        List<Respuesta> respuestas = new ArrayList<Respuesta>();
        boolean respuestasLleno = false;
        if (!cbRespuestaCorrecta.getSelectionModel().isEmpty()) {
            
            Respuesta respuesta1 = new Respuesta();
            Respuesta respuesta2 = new Respuesta();
            Respuesta respuesta3 = new Respuesta();
            Respuesta respuesta4 = new Respuesta();
            switch (cbRespuestaCorrecta.getSelectionModel().getSelectedItem()) {

                case "1a":
                    respuesta1.setPuntaje(1);
                    break;
                case "2a":
                    respuesta2.setPuntaje(1);
                    break;
                case "3a":
                    respuesta3.setPuntaje(1);
                    break;
                case "4a":
                    respuesta4.setPuntaje(1);
                    break;
            }
            if (comprobarTextoLleno()) {
                
                respuestasLleno = true;
                respuesta1.setRespuesta(txtRespuesta1.getText());
                respuesta2.setRespuesta(txtRespuesta2.getText());
                respuesta3.setRespuesta(txtRespuesta3.getText());
                respuesta4.setRespuesta(txtRespuesta4.getText());
                respuesta1.setTipoRespuesta(1);
                respuesta2.setTipoRespuesta(1);
                respuesta3.setTipoRespuesta(1);
                respuesta4.setTipoRespuesta(1);
            } else if (comprobarImagenLleno()) {
                
                respuestasLleno = true;
                respuesta1.setRespuesta(path.get(1));
                respuesta2.setRespuesta(path.get(2));
                respuesta3.setRespuesta(path.get(3));
                respuesta4.setRespuesta(path.get(4));
                respuesta1.setTipoRespuesta(2);
                respuesta2.setTipoRespuesta(2);
                respuesta3.setTipoRespuesta(2);
                respuesta4.setTipoRespuesta(2);
            } 
            
            if (respuestasLleno == true) {

                respuestas.add(respuesta1);
                respuestas.add(respuesta2);
                respuestas.add(respuesta3);
                respuestas.add(respuesta4);
            }
            
        }

        return respuestas;
    }
    
    /**
     * Este metodo es para quitar las imagenes que tengan los botones y 
     * regresarlos a su estado original, como tambien a su boton de cancelar y 
     * el campo de texto
     */
    public void limpiarBotones(){
        
        btnRespuesta1.setGraphic(null);
        btnRespuesta2.setGraphic(null);
        btnRespuesta3.setGraphic(null);
        btnRespuesta4.setGraphic(null);
        btnPregunta.setGraphic(null);
        btnRespuesta1.setText("+");
        btnRespuesta2.setText("+");
        btnRespuesta3.setText("+");
        btnRespuesta4.setText("+");
        btnPregunta.setText("+");
        btnCancelarImagen1.setVisible(false);
        btnCancelarImagen2.setVisible(false);
        btnCancelarImagen3.setVisible(false);
        btnCancelarImagen4.setVisible(false);
        btnCancelarPregunta.setVisible(false);
        txtRespuesta1.setDisable(false);
        txtRespuesta2.setDisable(false);
        txtRespuesta3.setDisable(false);
        txtRespuesta4.setDisable(false);
        txtPregunta.setDisable(false);
    }
    
    /**
     * Este metodo es para guardar la imagen que fue introducida por el usuario
     * @param path Ruta de la imagen
     * @param botonImagen El boton que contiene la imagen
     */
    public void guardarImagen(String path, Button botonImagen) {
        
        FileWriter writer;
        ImageView imageViewAdjusted = (ImageView) botonImagen.getGraphic();
        try {

            imageViewAdjusted.setFitWidth(150);
            imageViewAdjusted.setFitHeight(150);
            File outputFile = new File(path);
            outputFile.getParentFile().mkdirs();
            writer = new FileWriter(outputFile);
            BufferedImage bImage = SwingFXUtils.
                    fromFXImage(imageViewAdjusted.snapshot(null, null), null);
            ImageIO.write(bImage, "png", outputFile);
            imageViewAdjusted.setFitWidth(85);
            imageViewAdjusted.setFitHeight(85);
            writer.close();
        } catch (IOException ex) {

            Logger.getLogger(RegistrarPreguntaController.class.
                    getName()).log(Level.SEVERE, null, ex);
        } finally {
            
            imageViewAdjusted.setFitWidth(85);
            imageViewAdjusted.setFitHeight(85);
        }
        
    }
    
    /**
     * Metodo que recibe el objeto de cuenta de usuario o invitado del 
     * Controlador de la pantalla que la invocó
     * @param usuario Cuenta de usuario registrado
     * @param idioma Idioma del properties
     */
    public void recibirParametros(Object usuario, String idioma){
        
        this.idioma = idioma;
        this.cuenta = (Cuentausuario)usuario;
    }
    
    /**
     * Este metodo sirve para que los textFiel nieguen la entrada a espacios.
     */
    private void excluirEspacios() {

        txtPregunta.textProperty().addListener(
                (observable, old_value, new_value) -> {

                    if (new_value.contains("¿") || new_value.contains("?")) {

                        txtPregunta.setText(old_value);
                    }
                });
    }

    /**
     * Este metodo impide que el campo de texto sea mayor a un numero de
     * caracteres fijo.
     *
     * @param txtField textField a limitar
     * @param maximo numero maximo de caracteres permitidos.
     */
    public void limitarCampos(javafx.scene.control.TextField txtField, 
            int maximo) {
        txtField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, 
                    final String oldValue, final String newValue) {
                if (txtField.getText().length() > maximo) {
                    String s = txtField.getText().substring(0, maximo);
                    txtField.setText(s);
                }
            }
        });
    }
    
    /**
     * Este metodo es para mostrar una ventana en caso de fracaso
     */ 
    private void mostrarCreacionFracaso() {
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Se ha perdido conexión con el servidor"
                + ", prueba de nuevo");
        alert.showAndWait();
    }

    private void mostrarCreacionExito() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exito");
        alert.setHeaderText(null);
        alert.setContentText("Set creado con exito");
        alert.showAndWait();
    }

}

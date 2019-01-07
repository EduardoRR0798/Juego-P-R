package utilidades;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author Eduar
 */
public class UtilidadCadenas {
    
    /**
     * Este metodo convierte la contrasenia ingresada por el usuario en una 
     * cadena hash.
     * @param contrasenia contrasenia a hacer hash.
     * @return el codigo hash de la contrasenia.
     */
    public String hacerHashAContrasenia(String contrasenia) {
        
        String contraseniaHash = null;
        try {
            
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(contrasenia.getBytes());
            BigInteger codigo = new BigInteger(1, messageDigest);
            contraseniaHash = codigo.toString(16);
            while (contraseniaHash.length() < 32) { 
                
                contraseniaHash = new StringBuilder().append("0").
                        append(contraseniaHash).toString();
            }
            
        } catch (NoSuchAlgorithmException ex) {
            
            Logger.getLogger(UtilidadCadenas.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return contraseniaHash;
    }
    
    /**
     * Este metodo impide que el campo de texto sea mayor a un numero de 
     * caracteres fijo.
     * @param tf textField a limitar
     * @param maximo numero maximo de caracteres permitidos.
     */
    public void limitarCampos(javafx.scene.control.TextField tf, int maximo) {
        
        tf.textProperty().addListener((final ObservableValue<? extends 
                String> ov, final String oldValue, final String newValue) -> {
            
            if (tf.getText().length() > maximo) {
                
                String s = tf.getText().substring(0, maximo);
                tf.setText(s);
            }
        });
    }
    
    /**
     * Este metodo sirve para que los textField nieguen la entrada a espacios.
     * @param tf textField a limitar
     */
    public void excluirEspacios(javafx.scene.control.TextField tf) {

        tf.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    if (newValue.contains(" ")) {

                        tf.setText(oldValue);
                    }
                });
    }
    
    /**
     * Este metodo sirve para que los textFiel nieguen de los simbolos de
     * interrogación.
     * @param tf textField a limitar
     */
    public void excluirSimbPregunta(javafx.scene.control.TextField tf) {

        tf.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    if (newValue.contains("¿") || newValue.contains("?")) {

                        tf.setText(oldValue);
                    }
                });
    }
}

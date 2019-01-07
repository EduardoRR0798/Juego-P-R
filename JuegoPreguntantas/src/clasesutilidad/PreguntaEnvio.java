package clasesutilidad;

import java.io.Serializable;
import java.util.List;

/****************************************************************** 
 * @version 1.0                                                   * 
 * @author Eduardo Rosas Rivera                                   * 
 * @since 10/12/2018                                              *
 * Nombre de la clase PreguntaEnvio                               *
 *****************************************************************/ 
public class PreguntaEnvio implements Serializable{
    
    private String pregunta;
    private int tipoPregunta;
    private String imagen;
    private List<RespuestaEnvio> respuestas;
    private byte[] imagenByte;
    
    /**
     * Constructor vacio de la clase.
     */
    public PreguntaEnvio() {
        //Vacio para invocacion.
    }

    /**
     * @return the pregunta
     */
    public String getPregunta() {
        return pregunta;
    }

    /**
     * @param pregunta the pregunta to set
     */
    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    /**
     * @return the tipoPregunta
     */
    public int getTipoPregunta() {
        return tipoPregunta;
    }

    /**
     * @param tipoPregunta the tipoPregunta to set
     */
    public void setTipoPregunta(int tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }

    /**
     * @return the imagen
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * @param imagen the imagen to set
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * @return the respuestas
     */
    public List<RespuestaEnvio> getRespuestas() {
        return respuestas;
    }

    /**
     * @param respuestas the respuestas to set
     */
    public void setRespuestas(List<RespuestaEnvio> respuestas) {
        this.respuestas = respuestas;
    }
    
    /**
     * @return the respuestas
     */
    public byte[] getImagenByte() {
        
        return imagenByte;
    }
    
    public void setImagenByte(byte[] imagenByte) {
        
        this.imagenByte = imagenByte;
    }

}

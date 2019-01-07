package clasesutilidad;

import java.io.Serializable;

/****************************************************************** 
 * @version 1.0                                                   * 
 * @author Eduardo Rosas Rivera                                   * 
 * @since 10/11/2018                                              *
 * Nombre de la clase RespuestaEnvio                              *
 *****************************************************************/ 
public class RespuestaEnvio implements Serializable{
    
    private String respuesta;
    private int tipoRespuesta;
    private String imagen;
    private int puntaje;
    private byte[] imagenByte;
    
    /**
     * Constructor de la clase.
     */
    public RespuestaEnvio() {
        //Vacio para invocacion.
    }

    /**
     * @return the respuesta
     */
    public String getRespuesta() {
        return respuesta;
    }

    /**
     * @param respuesta the respuesta to set
     */
    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    /**
     * @return the tipoRespuesta
     */
    public int getTipoRespuesta() {
        return tipoRespuesta;
    }

    /**
     * @param tipoRespuesta the tipoRespuesta to set
     */
    public void setTipoRespuesta(int tipoRespuesta) {
        this.tipoRespuesta = tipoRespuesta;
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
     * @return the imagenByte
     */
    public byte[] getImagenByte() {
        return imagenByte;
    }

    /**
     * @param imagenByte the imagen to set
     */
    public void setImagenByte(byte[] imagenByte) {
        this.imagenByte = imagenByte;
    }
    
    /**
     * @return the puntaje
     */
    public int getPuntaje() {
        return puntaje;
    }

    /**
     * @param puntaje the puntaje to set
     */
    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }
    
    @Override
    public String toString() {
        return "La respuestas es: " + respuesta + ", el puntaje es " + puntaje;
    }
    
}

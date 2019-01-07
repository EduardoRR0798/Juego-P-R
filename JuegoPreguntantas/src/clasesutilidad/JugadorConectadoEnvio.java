package clasesutilidad;

/****************************************************************** 
 * @version 1.0                                                   * 
 * @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          * 
 * @since 06/11/2018                                              *
 * Nombre de la clase JugadorConectadoEnvio                       *
 *****************************************************************/ 
public class JugadorConectadoEnvio {
    
    private String nombre;
    private String socketid;
    private int puntaje;
    
    /**
     * Constructor de la clase.
     */
    public JugadorConectadoEnvio() {
        //Vacio para invocacion.
    }
    
    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the socketid
     */
    public String getSocketid() {
        return socketid;
    }

    /**
     * @param socketid the socketid to set
     */
    public void setSocketid(String socketid) {
        this.socketid = socketid;
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
    
    public void sumarPuntos(int puntaje) {
        
        this.puntaje = this.puntaje + puntaje;
    }
    /**
     * Este metodo sirve para que al hacerlo string retorne solo el nombre.
     * @return el nombre.
     */
    @Override
    public String toString() {
        return this.nombre;
    }
}

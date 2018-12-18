package clasesutilidad;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          */ 
/* @since 06/11/2018                                              */
/* Nombre de la clase EsperarJugadoresController                  */
/******************************************************************/ 
public class JugadorConectadoEnvio {
    private String nombre;
    private String socketid;
    
    public JugadorConectadoEnvio(){
        
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
     * Este metodo sirve para que al hacerlo string retorne solo el nombre.
     * @return 
     */
    @Override
    public String toString() {
        return this.nombre;
    }
}

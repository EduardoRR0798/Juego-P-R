package clasesutilidad;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Eduardo Rosas Rivera                                   */ 
/* @since 10/11/2018                                              */
/* Nombre de la clase PuntajeEnvio                                */
/******************************************************************/ 
public class PuntajeEnvio {
    
    private String usuario;
    private int puntaje;
    
    /**
     * Contructor vacio de la clase.
     */
    public PuntajeEnvio(){
        
    }
    
    public PuntajeEnvio(int puntaje) {
        this.puntaje = puntaje;
    }
    
    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
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
    
    
}

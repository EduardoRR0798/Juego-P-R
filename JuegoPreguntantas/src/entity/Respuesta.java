package entity;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/****************************************************************** 
 * @version 1.0                                                   * 
 * @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          * 
 * @since 26/10/2018                                              *
 * Nombre de la clase Respuesta                                   *
 *****************************************************************/
@Entity
@Table(name = "respuesta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Respuesta.findAll", query = "SELECT r FROM Respuesta r")
    , @NamedQuery(name = "Respuesta.findByIdpregunta", query = "SELECT r FROM Respuesta r WHERE r.idpregunta.idpregunta = :idpregunta")
    , @NamedQuery(name = "Respuesta.findByIdrespuesta", query = "SELECT r FROM Respuesta r WHERE r.idrespuesta = :idrespuesta")
    , @NamedQuery(name = "Respuesta.findByRespuesta", query = "SELECT r FROM Respuesta r WHERE r.respuestaContenido = :respuesta")
    , @NamedQuery(name = "Respuesta.findByPuntaje", query = "SELECT r FROM Respuesta r WHERE r.puntaje = :puntaje")
    , @NamedQuery(name = "Respuesta.findByTipoRespuesta", query = "SELECT r FROM Respuesta r WHERE r.tipoRespuesta = :tipoRespuesta")})
public class Respuesta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idrespuesta")
    private Integer idrespuesta;
    @Column(name = "respuesta")
    private String respuestaContenido;
    @Column(name = "puntaje")
    private Integer puntaje;
    @Column(name = "tipoRespuesta")
    private Integer tipoRespuesta;
    @JoinColumn(name = "idpregunta", referencedColumnName = "idpregunta")
    @ManyToOne
    private Pregunta idpregunta;
    @Transient
    private byte[] imagen;
    
    public Respuesta() {
    }

    public Respuesta(Integer idrespuesta) {
        this.idrespuesta = idrespuesta;
    }

    public Integer getIdrespuesta() {
        return idrespuesta;
    }

    public void setIdrespuesta(Integer idrespuesta) {
        this.idrespuesta = idrespuesta;
    }

    public String getRespuesta() {
        return respuestaContenido;
    }

    public void setRespuesta(String respuesta) {
        this.respuestaContenido = respuesta;
    }

    public Integer getPuntaje() {
        if(Objects.equals(puntaje, null)) {
            
            this.puntaje = 0;
        }
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

    public Integer getTipoRespuesta() {
        return tipoRespuesta;
    }

    public void setTipoRespuesta(Integer tipoRespuesta) {
        this.tipoRespuesta = tipoRespuesta;
    }

    public Pregunta getIdpregunta() {
        return idpregunta;
    }

    public void setIdpregunta(Pregunta idpregunta) {
        this.idpregunta = idpregunta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idrespuesta != null ? idrespuesta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Respuesta)) {
            return false;
        }
        Respuesta other = (Respuesta) object;
        return ((this.idrespuesta == null && other.idrespuesta != null) || (this.idrespuesta != null && !this.idrespuesta.equals(other.idrespuesta)));
    }

    @Override
    public String toString() {
        return "entity.Respuesta[ idrespuesta=" + idrespuesta + " ]";
    }
    
    /**
     * @return the imagen
     */
    public byte[] getImagen() {
        return imagen;
    }

    /**
     * @param imagen the imagen to set
     */
    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    /**
     * Este metodo crea un arreglo de bytes para poder ser enviado por nodeJs
     * @return imagen convertida en un arreglo de bytes.
     */
    public byte[] crearArregloImagen() {
        
        byte[] imagenBytes = null;
        try {
            Path path = Paths.get(respuestaContenido);
            byte[] contenido = Files.readAllBytes(path);
            imagen = contenido;
        } catch (IOException ex) {
            
            Logger.getLogger(Respuesta.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return imagenBytes;
    }
    
}

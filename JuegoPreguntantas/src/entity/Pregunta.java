package entity;

import java.io.IOException; 
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/****************************************************************** 
 * @version 1.0                                                   * 
 * @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          * 
 * @since 26/10/2018                                              *
 * Nombre de la clase Pregunta                                    *
 *****************************************************************/
@Entity
@Table(name = "pregunta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pregunta.findAll", query = "SELECT p FROM Pregunta p")
    , @NamedQuery(name = "Pregunta.findByIdpregunta", query = "SELECT p FROM Pregunta p WHERE p.idpregunta = :idpregunta")
    , @NamedQuery(name = "Pregunta.findByPregunta", query = "SELECT p FROM Pregunta p WHERE p.preguntaContenido = :pregunta")
    , @NamedQuery(name = "Pregunta.findByIdsetpregunta", query = "SELECT p FROM Pregunta p WHERE p.idsetpregunta = :idsetpregunta")})
public class Pregunta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idpregunta")
    private Integer idpregunta;
    @Column(name = "pregunta")
    private String preguntaContenido;
    @Column(name = "tipoPregunta")
    private Integer tipoPregunta;
    @Column(name = "idsetpregunta")
    private Integer idsetpregunta;
    @OneToMany(mappedBy = "idpregunta")
    private Collection<Respuesta> respuestaCollection;
    @Transient
    private byte[] imagen;
    @Transient
    private List<Respuesta> respuestas;
    
    public Pregunta() {
    }

    public Pregunta(Integer idpregunta) {
        this.idpregunta = idpregunta;
    }

    public Integer getIdpregunta() {
        return idpregunta;
    }

    public void setIdpregunta(Integer idpregunta) {
        this.idpregunta = idpregunta;
    }

    public String getPregunta() {
        return preguntaContenido;
    }

    public void setPregunta(String pregunta) {
        this.preguntaContenido = pregunta;
    }
    
    public Integer getTipoPregunta() {
        return tipoPregunta;
    }

    public void setTipoPregunta(Integer tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }

    public Integer getIdsetpregunta() {
        return idsetpregunta;
    }

    public void setIdsetpregunta(Integer idsetpregunta) {
        this.idsetpregunta = idsetpregunta;
    }

    @XmlTransient
    public Collection<Respuesta> getRespuestaCollection() {
        return respuestaCollection;
    }

    public void setRespuestaCollection(Collection<Respuesta> respuestaCollection) {
        this.respuestaCollection = respuestaCollection;
    }
    
    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }
    
    public List<Respuesta> getRespuestas() {
        return respuestas;
    }
    public void setRespuesta(Respuesta respuesta) {
        this.respuestas.add(respuesta);
    }
    
    public Respuesta getRespuesta(int indice) {
        return respuestas.get(indice);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpregunta != null ? idpregunta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Pregunta)) {
            return false;
        }
        Pregunta other = (Pregunta) object;
        return ((this.idpregunta == null && other.idpregunta != null) || (this.idpregunta != null && !this.idpregunta.equals(other.idpregunta)));
    }

    @Override
    public String toString() {
        return "entity.Pregunta[ idpregunta=" + idpregunta + " ]";
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
        
        byte[] imagenPregunta = null;
        try {
            Path path = Paths.get(preguntaContenido);
            byte[] contenido = Files.readAllBytes(path);
            imagen = contenido;
        } catch (IOException ex) {
            
            Logger.getLogger(Pregunta.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return imagenPregunta;
    }
}

package entity;

import java.io.Serializable;
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
 * Nombre de la clase Partida                                     *
 *****************************************************************/
@Entity
@Table(name = "partida")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Partida.findAll", query = "SELECT p FROM Partida p")
    , @NamedQuery(name = "Partida.findBySet", query = "SELECT p.nombre FROM Partida p WHERE p.idsetpregunta.idsetpregunta = :idsetpregunta")
    , @NamedQuery(name = "Partida.findByIdset", query = "SELECT p FROM Partida p WHERE p.idsetpregunta.idsetpregunta = :idsetpregunta")
    , @NamedQuery(name = "Partida.findByIdpartida", query = "SELECT p FROM Partida p WHERE p.idpartida = :idpartida")
    , @NamedQuery(name = "Partida.findByNombre", query = "SELECT p FROM Partida p WHERE p.nombre = :nombre")})
public class Partida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idpartida")
    private Integer idpartida;
    @Column(name = "nombre")
    private String nombre;
    @JoinColumn(name = "idsetpregunta", referencedColumnName = "idsetpregunta")
    @ManyToOne
    private Setpregunta idsetpregunta;
    @Transient
    private int totalPreguntas;
    
    public Partida() {
    }

    public Partida(Integer idpartida) {
        this.idpartida = idpartida;
    }

    public Integer getIdpartida() {
        return idpartida;
    }

    public void setIdpartida(Integer idpartida) {
        this.idpartida = idpartida;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Setpregunta getIdsetpregunta() {
        return idsetpregunta;
    }

    public void setIdsetpregunta(Setpregunta idsetpregunta) {
        this.idsetpregunta = idsetpregunta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpartida != null ? idpartida.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Partida)) {
            return false;
        }
        Partida other = (Partida) object;
        if ((this.idpartida == null && other.idpartida != null) || (this.idpartida != null && !this.idpartida.equals(other.idpartida))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
    /**
     * Este metodo fija el total de preguntas con las que cuenta una partida.
     * @param totalPreguntas total de preguntas que existen en la partida.
     */
    public void setTotalPreguntas(int totalPreguntas) {
        
        this.totalPreguntas = totalPreguntas;
    }
    
    public int getTotalPreguntas() {
        
        return totalPreguntas;
    }
    
}

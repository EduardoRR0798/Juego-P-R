package entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/****************************************************************** 
 * @version 1.0                                                   * 
 * @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          * 
 * @since 26/10/2018                                              *
 * Nombre de la clase Cuentausuario                               *
 *****************************************************************/
@Entity
@Table(name = "cuentausuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuentausuario.findAll", query = "SELECT c FROM Cuentausuario c")
    , @NamedQuery(name = "Cuentausuario.findByIdcuentausuario", query = "SELECT c FROM Cuentausuario c WHERE c.idcuentausuario = :idcuentausuario")
    , @NamedQuery(name = "Cuentausuario.findByContrasenia", query = "SELECT c FROM Cuentausuario c WHERE c.contrasenia = :contrasenia")
    , @NamedQuery(name = "Cuentausuario.findByNombreusuario", query = "SELECT c FROM Cuentausuario c WHERE c.nombreusuario = :nombreusuario")
    , @NamedQuery(name = "Cuentausuario.findByCorreoelectronico", query = "SELECT c FROM Cuentausuario c WHERE c.correoelectronico = :correoelectronico")})
public class Cuentausuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcuentausuario")
    private Integer idcuentausuario;
    @Column(name = "contrasenia")
    private String contrasenia;
    @Column(name = "nombreusuario")
    private String nombreusuario;
    @Column(name = "correoelectronico")
    private String correoelectronico;
    @OneToMany(mappedBy = "idcuentausuario")
    private Collection<Setpregunta> setpreguntaCollection;

    public Cuentausuario() {
    }

    public Cuentausuario(Integer idcuentausuario) {
        this.idcuentausuario = idcuentausuario;
    }

    public Integer getIdcuentausuario() {
        return idcuentausuario;
    }

    public void setIdcuentausuario(Integer idcuentausuario) {
        this.idcuentausuario = idcuentausuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getNombreusuario() {
        return nombreusuario;
    }

    public void setNombreusuario(String nombreusuario) {
        this.nombreusuario = nombreusuario;
    }

    public String getCorreoelectronico() {
        return correoelectronico;
    }

    public void setCorreoelectronico(String correoelectronico) {
        this.correoelectronico = correoelectronico;
    }

    @XmlTransient
    public Collection<Setpregunta> getSetpreguntaCollection() {
        return setpreguntaCollection;
    }

    public void setSetpreguntaCollection(Collection<Setpregunta> setpreguntaCollection) {
        this.setpreguntaCollection = setpreguntaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcuentausuario != null ? idcuentausuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Cuentausuario)) {
            return false;
        }
        Cuentausuario other = (Cuentausuario) object;
        if ((this.idcuentausuario == null && other.idcuentausuario != null) || (this.idcuentausuario != null && !this.idcuentausuario.equals(other.idcuentausuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Cuentausuario[ idcuentausuario=" + idcuentausuario + " ]";
    }
    
}

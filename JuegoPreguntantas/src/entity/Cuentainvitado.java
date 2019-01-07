package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/****************************************************************** 
 * @version 1.0                                                   * 
 * @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          * 
 * @since 26/10/2018                                              *
 * Nombre de la clase CuentaInvitado                              *
 *****************************************************************/
@Entity
@Table(name = "cuentainvitado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuentainvitado.findAll", query = "SELECT c FROM Cuentainvitado c")
    , @NamedQuery(name = "Cuentainvitado.findMaximo", query = "SELECT MAX(c.idcuentainvitado) FROM Cuentainvitado c")
    , @NamedQuery(name = "Cuentainvitado.findAllByCorreoelectronico", query = "SELECT c.correoelectronico FROM Cuentainvitado c WHERE UPPER(c.correoelectronico) = :correoelectronico")   
    , @NamedQuery(name = "Cuentainvitado.findAllByNombre", query = "SELECT c.nombreusuario FROM Cuentausuario c WHERE c.nombreusuario = :nombre")
    , @NamedQuery(name = "Cuentainvitado.findAllByCodigo", query = "SELECT c.codigo FROM Cuentainvitado c WHERE c.codigo = :codigo")
    , @NamedQuery(name = "Cuentainvitado.findByIdcuentainvitado", query = "SELECT c FROM Cuentainvitado c WHERE c.idcuentainvitado = :idcuentainvitado")
    , @NamedQuery(name = "Cuentainvitado.findByNombre", query = "SELECT c FROM Cuentainvitado c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Cuentainvitado.findByCorreoelectronico", query = "SELECT c FROM Cuentainvitado c WHERE c.correoelectronico = :correoelectronico")
    , @NamedQuery(name = "Cuentainvitado.findByCodigo", query = "SELECT c FROM Cuentainvitado c WHERE c.codigo = :codigo")})
public class Cuentainvitado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcuentainvitado")
    private Integer idcuentainvitado;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "correoelectronico")
    private String correoelectronico;
    @Column(name = "codigo")
    private String codigo;

    public Cuentainvitado() {
    }

    public Cuentainvitado(Integer idcuentainvitado) {
        this.idcuentainvitado = idcuentainvitado;
    }

    public Integer getIdcuentainvitado() {
        return idcuentainvitado;
    }

    public void setIdcuentainvitado(Integer idcuentainvitado) {
        this.idcuentainvitado = idcuentainvitado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreoelectronico() {
        return correoelectronico;
    }

    public void setCorreoelectronico(String correoelectronico) {
        this.correoelectronico = correoelectronico;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcuentainvitado != null ? idcuentainvitado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Cuentainvitado)) {
            return false;
        }
        Cuentainvitado other = (Cuentainvitado) object;
        if ((this.idcuentainvitado == null && other.idcuentainvitado != null) || (this.idcuentainvitado != null && !this.idcuentainvitado.equals(other.idcuentainvitado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Cuentainvitado[ idcuentainvitado=" + idcuentainvitado + " ]";
    }
    
}

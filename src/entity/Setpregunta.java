/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Samsung RV415
 */
@Entity
@Table(name = "setpregunta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Setpregunta.findAll", query = "SELECT s FROM Setpregunta s")
    , @NamedQuery(name = "Setpregunta.findByIdsetpregunta", query = "SELECT s FROM Setpregunta s WHERE s.idsetpregunta = :idsetpregunta")
    , @NamedQuery(name = "Setpregunta.findByCategoria", query = "SELECT s FROM Setpregunta s WHERE s.categoria = :categoria")})
public class Setpregunta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idsetpregunta")
    private Integer idsetpregunta;
    @Column(name = "categoria")
    private String categoria;
    @JoinColumn(name = "idcuentausuario", referencedColumnName = "idcuentausuario")
    @ManyToOne
    private Cuentausuario idcuentausuario;
    @OneToMany(mappedBy = "idsetpregunta")
    private Collection<Partida> partidaCollection;

    public Setpregunta() {
    }

    public Setpregunta(Integer idsetpregunta) {
        this.idsetpregunta = idsetpregunta;
    }

    public Integer getIdsetpregunta() {
        return idsetpregunta;
    }

    public void setIdsetpregunta(Integer idsetpregunta) {
        this.idsetpregunta = idsetpregunta;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Cuentausuario getIdcuentausuario() {
        return idcuentausuario;
    }

    public void setIdcuentausuario(Cuentausuario idcuentausuario) {
        this.idcuentausuario = idcuentausuario;
    }

    @XmlTransient
    public Collection<Partida> getPartidaCollection() {
        return partidaCollection;
    }

    public void setPartidaCollection(Collection<Partida> partidaCollection) {
        this.partidaCollection = partidaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idsetpregunta != null ? idsetpregunta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Setpregunta)) {
            return false;
        }
        Setpregunta other = (Setpregunta) object;
        if ((this.idsetpregunta == null && other.idsetpregunta != null) || (this.idsetpregunta != null && !this.idsetpregunta.equals(other.idsetpregunta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Setpregunta[ idsetpregunta=" + idsetpregunta + " ]";
    }
    
}

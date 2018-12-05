/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Eduar
 */
@Entity
@Table(name = "estadistica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estadistica.findAll", query = "SELECT e FROM Estadistica e")
    , @NamedQuery(name = "Estadistica.findByIdestadistica", query = "SELECT e FROM Estadistica e WHERE e.idestadistica = :idestadistica")
    , @NamedQuery(name = "Estadistica.findByNoganado", query = "SELECT e FROM Estadistica e WHERE e.noganado = :noganado")
    , @NamedQuery(name = "Estadistica.findByNoperdido", query = "SELECT e FROM Estadistica e WHERE e.noperdido = :noperdido")})
public class Estadistica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idestadistica")
    private Integer idestadistica;
    @Column(name = "noganado")
    private Integer noganado;
    @Column(name = "noperdido")
    private Integer noperdido;
    @JoinColumn(name = "idcuentausuario", referencedColumnName = "idcuentausuario")
    @ManyToOne
    private Cuentausuario idcuentausuario;

    public Estadistica() {
    }

    public Estadistica(Integer idestadistica) {
        this.idestadistica = idestadistica;
    }

    public Integer getIdestadistica() {
        return idestadistica;
    }

    public void setIdestadistica(Integer idestadistica) {
        this.idestadistica = idestadistica;
    }

    public Integer getNoganado() {
        return noganado;
    }

    public void setNoganado(Integer noganado) {
        this.noganado = noganado;
    }

    public Integer getNoperdido() {
        return noperdido;
    }

    public void setNoperdido(Integer noperdido) {
        this.noperdido = noperdido;
    }

    public Cuentausuario getIdcuentausuario() {
        return idcuentausuario;
    }

    public void setIdcuentausuario(Cuentausuario idcuentausuario) {
        this.idcuentausuario = idcuentausuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idestadistica != null ? idestadistica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estadistica)) {
            return false;
        }
        Estadistica other = (Estadistica) object;
        if ((this.idestadistica == null && other.idestadistica != null) || (this.idestadistica != null && !this.idestadistica.equals(other.idestadistica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Estadistica[ idestadistica=" + idestadistica + " ]";
    }
    
}

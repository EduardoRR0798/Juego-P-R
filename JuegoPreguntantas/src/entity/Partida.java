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
 * @author Samsung RV415
 */
@Entity
@Table(name = "partida")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Partida.findAll", query = "SELECT p FROM Partida p")
    , @NamedQuery(name = "Partida.findByIdpartida", query = "SELECT p FROM Partida p WHERE p.idpartida = :idpartida")
    , @NamedQuery(name = "Partida.findByNombre", query = "SELECT p FROM Partida p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "Partida.findByModojuego", query = "SELECT p FROM Partida p WHERE p.modojuego = :modojuego")})
public class Partida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idpartida")
    private Integer idpartida;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "modojuego")
    private String modojuego;
    @JoinColumn(name = "idsetpregunta", referencedColumnName = "idsetpregunta")
    @ManyToOne
    private Setpregunta idsetpregunta;

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

    public String getModojuego() {
        return modojuego;
    }

    public void setModojuego(String modojuego) {
        this.modojuego = modojuego;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
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
        return "entity.Partida[ idpartida=" + idpartida + " ]";
    }
    
}

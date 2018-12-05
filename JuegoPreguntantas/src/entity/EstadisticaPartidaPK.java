/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Eduar
 */
@Embeddable
public class EstadisticaPartidaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idestadistica")
    private int idestadistica;
    @Basic(optional = false)
    @Column(name = "idpartida")
    private int idpartida;

    public EstadisticaPartidaPK() {
    }

    public EstadisticaPartidaPK(int idestadistica, int idpartida) {
        this.idestadistica = idestadistica;
        this.idpartida = idpartida;
    }

    public int getIdestadistica() {
        return idestadistica;
    }

    public void setIdestadistica(int idestadistica) {
        this.idestadistica = idestadistica;
    }

    public int getIdpartida() {
        return idpartida;
    }

    public void setIdpartida(int idpartida) {
        this.idpartida = idpartida;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idestadistica;
        hash += (int) idpartida;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadisticaPartidaPK)) {
            return false;
        }
        EstadisticaPartidaPK other = (EstadisticaPartidaPK) object;
        if (this.idestadistica != other.idestadistica) {
            return false;
        }
        if (this.idpartida != other.idpartida) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.EstadisticaPartidaPK[ idestadistica=" + idestadistica + ", idpartida=" + idpartida + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Eduar
 */
@Entity
@Table(name = "estadistica_partida")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadisticaPartida.findAll", query = "SELECT e FROM EstadisticaPartida e")
    , @NamedQuery(name = "EstadisticaPartida.findByIdestadistica", query = "SELECT e FROM EstadisticaPartida e WHERE e.estadisticaPartidaPK.idestadistica = :idestadistica")
    , @NamedQuery(name = "EstadisticaPartida.findByIdpartida", query = "SELECT e FROM EstadisticaPartida e WHERE e.estadisticaPartidaPK.idpartida = :idpartida")})
public class EstadisticaPartida implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EstadisticaPartidaPK estadisticaPartidaPK;

    public EstadisticaPartida() {
    }

    public EstadisticaPartida(EstadisticaPartidaPK estadisticaPartidaPK) {
        this.estadisticaPartidaPK = estadisticaPartidaPK;
    }

    public EstadisticaPartida(int idestadistica, int idpartida) {
        this.estadisticaPartidaPK = new EstadisticaPartidaPK(idestadistica, idpartida);
    }

    public EstadisticaPartidaPK getEstadisticaPartidaPK() {
        return estadisticaPartidaPK;
    }

    public void setEstadisticaPartidaPK(EstadisticaPartidaPK estadisticaPartidaPK) {
        this.estadisticaPartidaPK = estadisticaPartidaPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estadisticaPartidaPK != null ? estadisticaPartidaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadisticaPartida)) {
            return false;
        }
        EstadisticaPartida other = (EstadisticaPartida) object;
        if ((this.estadisticaPartidaPK == null && other.estadisticaPartidaPK != null) || (this.estadisticaPartidaPK != null && !this.estadisticaPartidaPK.equals(other.estadisticaPartidaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.EstadisticaPartida[ estadisticaPartidaPK=" + estadisticaPartidaPK + " ]";
    }
    
}

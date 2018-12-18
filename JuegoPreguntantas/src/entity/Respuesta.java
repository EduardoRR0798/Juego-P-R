package entity;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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

@Entity
@Table(name = "respuesta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Respuesta.findAll", query = "SELECT r FROM Respuesta r")
    , @NamedQuery(name = "Respuesta.findByIdrespuesta", query = "SELECT r FROM Respuesta r WHERE r.idrespuesta = :idrespuesta")
    , @NamedQuery(name = "Respuesta.findByRespuesta", query = "SELECT r FROM Respuesta r WHERE r.respuesta = :respuesta")
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
    private String respuesta;
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
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Respuesta)) {
            return false;
        }
        Respuesta other = (Respuesta) object;
        if ((this.idrespuesta == null && other.idrespuesta != null) || (this.idrespuesta != null && !this.idrespuesta.equals(other.idrespuesta))) {
            return false;
        }
        return true;
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
        
        DataBufferByte data = null;
        try {
            
            File imgPath = new File(respuesta);
            BufferedImage bufferedImage = ImageIO.read(imgPath);

            WritableRaster raster = bufferedImage.getRaster();
            data = (DataBufferByte) raster.getDataBuffer();
        } catch (IOException ex) {
            
            Logger.getLogger(Pregunta.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return ( data.getData() );
    }
    
}

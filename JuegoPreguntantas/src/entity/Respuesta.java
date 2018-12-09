/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
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

/**
 *
 * @author Eduar
 */
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
        this.puntaje = 0;
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
        
        if (!(object instanceof Respuesta)) {
            return false;
        }
        Respuesta other = (Respuesta) object;
        if ((this.idrespuesta == null && other.idrespuesta != null) || 
                (this.idrespuesta != null && 
                !this.idrespuesta.equals(other.idrespuesta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Respuesta[ idrespuesta=" + idrespuesta + " ]";
    }
    
    /**
     * Este metodo crea una imagen para guardarla en un directorio.
     * @param ruta nueva ruta de la imagen.
     */
    public void crearImagen(String ruta) throws IOException {
        
        OutputStream out = null;
        try {
            
            out = new BufferedOutputStream(new FileOutputStream(ruta));
            out.write(imagen);
        } catch (FileNotFoundException ex) {
            
            Logger.getLogger(Respuesta.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            
            Logger.getLogger(Respuesta.class.getName())
                    .log(Level.SEVERE, null, ex);
        } finally {
            if (out != null) {
                
                out.close();
            }
        }
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
            
            Logger.getLogger(Respuesta.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return ( data.getData() );
    }
    
    /**
     * Este metodo verifica se la respuesta es la correcta.
     * @return true si la resuesta es correcta sino regresa false.
     */
    public boolean esCorrecta() {
        
        boolean correcta = false;
        if(puntaje == 1) {
            
            correcta = true;
        }
        return correcta;
    }
}

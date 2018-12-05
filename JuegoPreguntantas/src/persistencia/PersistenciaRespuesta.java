package persistencia;

import entity.Pregunta;
import entity.Respuesta;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez                                 */ 
/* @since 17/11/2018                                              */
/* Nombre de la clase PersistenRespuesta                          */
/******************************************************************/
public class PersistenciaRespuesta {
    
    /**
     * Este metodo es para trabajar con las entidades de la base de datos 
     * @return El EntityManager 
     */ 
    public EntityManager administrarEntidades() {
        
        
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.user", "pregunton");
        properties.put("javax.persistence.jdbc.password", "PR3GUNT0N");
        EntityManagerFactory emf = javax.persistence.Persistence
                .createEntityManagerFactory("JuegoPreguntantasPU", properties);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        return em;
    }
    
    /**
     * Este metodo es para crear una pregunta en la base de datos 
     * @param nuevaRespuesta Respuesta a insertar
     * @return Si es verdadero o no el exito de la creacion de la respuesta
     */
    public boolean crearRespuesta(List<Respuesta> nuevaRespuesta, 
            Pregunta pregunta) {
        
        boolean exito = false;
        try {

            for (int i = 0; i < nuevaRespuesta.size(); i++) {
                EntityManager em = administrarEntidades();
                nuevaRespuesta.get(i).setIdpregunta(pregunta);
                em.persist(nuevaRespuesta.get(i));
                em.getTransaction().commit();
            }
            
            exito = true;
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            
            return exito;
        }
    }
}
package persistencia;

import entity.Pregunta;
import entity.Respuesta;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

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
        
        
        Map<String, String> properties = new HashMap<>();
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
     * @param respuestas Respuesta a insertar
     * @param pregunta La pregunta a la que pertenecen las preguntas
     * @return Si es verdadero o no el exito de la creacion de la respuesta
     */
    public boolean crearRespuesta(Collection<Respuesta> respuestas, 
            Pregunta pregunta) {
        
        boolean exito = false;
        List<Respuesta> nuevaRespuesta = new ArrayList();
        nuevaRespuesta.addAll(respuestas);
        try {

            for (int i = 0; i < nuevaRespuesta.size(); i++) {
                
                EntityManager em = administrarEntidades();
                nuevaRespuesta.get(i).setIdpregunta(pregunta);
                em.persist(nuevaRespuesta.get(i));
                em.getTransaction().commit();
            }
            
            exito = true;
        } catch (Exception e) {
            
        }
        return exito;
    }
    
    /**
     * Este metodo es para recuperar las respuestas de preguntas
     * @param preguntas La preguntas de la que se van a recuperar sus respuestas
     * @return Una lista de respuestas de las preguntas
     */
    public List<Respuesta> recuperarRespuesta(List<Pregunta> preguntas){
        
        List<Respuesta> respuestas = new ArrayList<>();
        List<Respuesta> resultadoConsulta;
        Query query;
        EntityManager em = administrarEntidades();
        for (int i = 0; i < preguntas.size(); i++) {

            query = em.createQuery("SELECT r FROM Respuesta r "
                    + "WHERE r.idpregunta.idpregunta = \""
                    + preguntas.get(i).getIdpregunta() + "\"");
            resultadoConsulta = query.getResultList();
            for (int j = 0; j < resultadoConsulta.size(); j++) {

                respuestas.add(resultadoConsulta.get(j));
            }

        }
        
        return respuestas;
    }
    
    /**
     * Este metodo recupera todas las respuestas de una pregunta.
     * @param idPregunta id de la pregunta a recuperar.
     * @return lista de respuestas de solo una pregunta.
     */
    public List<Respuesta> recuperarRespuestasDePregunta(int idPregunta) {
        
        EntityManager em = administrarEntidades();
        List<Respuesta> respuestas;
        Query query = em.createQuery("SELECT r FROM Respuesta r WHERE "
                + "r.idpregunta.idpregunta = " + idPregunta);
        respuestas = query.getResultList();
        
        return respuestas;
    }
}
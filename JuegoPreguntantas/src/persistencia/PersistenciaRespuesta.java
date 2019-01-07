package persistencia;

import entity.Pregunta;
import entity.Respuesta;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/****************************************************************** 
 * @version 1.0                                                   * 
 * @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          * 
 * @since 12/11/2018                                              *
 * Nombre de la clase PersistenciaRespuesta                       *
 *****************************************************************/
public class PersistenciaRespuesta extends Persistencia {
    
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
            
            Logger.getLogger(PersistenciaRespuesta.class.getName())
                    .log(Level.SEVERE, null, e);
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
            query = em.createNamedQuery("Respuesta.findByIdpregunta", 
                    Respuesta.class).setParameter("idpregunta", 
                            preguntas.get(i).getIdpregunta());
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
        Query query = em.createNamedQuery("Respuesta.findByIdpregunta", 
                Respuesta.class).setParameter("idpregunta", idPregunta);
        respuestas = query.getResultList();
        
        return respuestas;
    }
}
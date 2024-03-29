package persistencia;

import entity.Pregunta;
import entity.Respuesta;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/****************************************************************** 
 * @version 1.0                                                   * 
 * @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          * 
 * @since 17/11/2018                                              *
 * Nombre de la clase PersistenciaPregunta                        *
 *****************************************************************/
public class PersistenciaPregunta extends Persistencia {
    
    /**
     * Este metodo es para crear una pregunta en la base de datos 
     * @param nuevaPregunta Pregunta a insertar
     * @return Si es verdadero o no el exito de la creacion de la pregunta
     */
    public boolean crearPregunta(Pregunta nuevaPregunta) {
        
        boolean exito = false;
        EntityManager em = administrarEntidades();
        PersistenciaRespuesta respuestaBD = new PersistenciaRespuesta();
        Collection<Respuesta> respuestas = 
                nuevaPregunta.getRespuestaCollection();
        nuevaPregunta.setRespuestaCollection(null);
        try {
            
            em.persist(nuevaPregunta);
            em.getTransaction().commit();
            if (respuestaBD.crearRespuesta(respuestas,
                    nuevaPregunta)) {

                exito = true;
            }
            
        } catch (Exception e) {
            Logger.getLogger(PersistenciaPregunta.class.getName())
                    .log(Level.SEVERE, null, e);
            em.getTransaction().rollback();
        }
        return exito;
    }
    
    /**
     * Este metodo es para guardar la relacion con el set de preguntas
     * @param numPreguntas numero de preguntas.
     */
    public void guardarSetPregunta(int numPreguntas){
        
        EntityManager em = administrarEntidades();
        PersistenciaSetpregunta setPreguntaBD = new PersistenciaSetpregunta();
        int idSetPregunta = setPreguntaBD.recuperarUltimaId();
        Query query = em.createNamedQuery("Pregunta.findAll", Pregunta.class);
        List<Pregunta> preguntas = query.getResultList();
        int indice = preguntas.size() - 1;
        int limite = preguntas.size() - numPreguntas; 
        for (int i = indice; i > limite; i--) {
            em = administrarEntidades();
            Pregunta pregunta = em.find(Pregunta.class, 
                    preguntas.get(i).getIdpregunta());
            pregunta.setIdsetpregunta(idSetPregunta);
            em.getTransaction().commit();
        }
    }
    
    /**
     * Este metodo es para recuperar las preguntas de un set de preguntas;
     * @param idSetPregunta El id del set de preguntas
     * @return Una lista de preguntas del set de preguntas
     */
    public List<Pregunta> recuperarPregunta(int idSetPregunta) {
        
        EntityManager em = administrarEntidades();
        Query query = em.createNamedQuery("Pregunta.findByIdsetpregunta", 
                Pregunta.class).setParameter("idsetpregunta", idSetPregunta);
        return query.getResultList();
    }
    
    /**
     * Este metodo recupera todas las preguntas junto con las respuestas, de un
     * set de preguntas.
     * @param idSetPregunta id del set a recuperar.
     * @return Lista de preguntas con sus respuestas, del set.
     */
    public List<Pregunta> recuperarPreguntaConRespuestas(int idSetPregunta) {
        
        EntityManager em = administrarEntidades();
        Query query = em.createNamedQuery("Pregunta.findByIdsetpregunta", 
                Pregunta.class).setParameter("idsetpregunta", idSetPregunta);
        List<Pregunta> preguntas = query.getResultList();
        for(int i = 0; i < preguntas.size(); i++) {
            
            PersistenciaRespuesta respuestaBD = new PersistenciaRespuesta();
            List<Respuesta> respuestas;
            respuestas = respuestaBD.recuperarRespuestasDePregunta(
                    preguntas.get(i).getIdpregunta());
            preguntas.get(i).setRespuestas(respuestas);
        }
        return preguntas;
    }
}
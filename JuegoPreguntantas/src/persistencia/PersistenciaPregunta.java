package persistencia;

import entity.Pregunta;
import entity.Respuesta;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez                                 */ 
/* @since 17/11/2018                                              */
/* Nombre de la clase PersistenciaPregunta                        */
/******************************************************************/
public class PersistenciaPregunta {
    
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
     * @param nuevaPregunta Pregunta a insertar
     * @return Si es verdadero o no el exito de la creacion de la pregunta
     */
    public boolean crearPregunta(String nuevaPregunta, int tipoPregunta, 
            List<Respuesta> nuevaRespuesta) {
        
        boolean exito = false;
        EntityManager em = administrarEntidades();
        PersistenciaRespuesta respuestaBD = new PersistenciaRespuesta();
        Pregunta pregunta = new Pregunta();
        pregunta.setPregunta(nuevaPregunta);
        pregunta.setTipoPregunta(tipoPregunta);
        try {
            
            em.persist(pregunta);
            em.getTransaction().commit();
            if (respuestaBD.crearRespuesta(nuevaRespuesta, pregunta)) {

                exito = true;
            }

        } catch (Exception e) {
            
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            
            return exito;
        }
    }
    
    /**
     * Este metodo es para guardar la relacion con el set de preguntas
     * @param nuevaPreguntas Numero de preguntas a recuperar para hacer relacion
     */
    public void guardarSetPregunta(int numPreguntas){
        
        EntityManager em = administrarEntidades();
        PersistenciaSetpregunta setPreguntaBD = new PersistenciaSetpregunta();
        int idSetPregunta = setPreguntaBD.recuperarUltimaId();
        Query queryPregunta = em.createQuery("SELECT p FROM Pregunta p");
        List<Pregunta> preguntas = queryPregunta.getResultList();
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
}

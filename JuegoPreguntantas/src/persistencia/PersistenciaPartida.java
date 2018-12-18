package persistencia;

import entity.Partida;
import entity.Setpregunta;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          */ 
/* @since 10/11/2018                                              */
/* Nombre de la clase ComenzarPartidaController                   */
/******************************************************************/
public class PersistenciaPartida {
    
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
     * Este metodo es para crear una paertida en la base de datos 
     * @param nombre nombre de la partida.
     * @param idSetPregunta ide del set.
     * @return Si es verdadero o no el exito de la creacion de la partida
     */
    public boolean crearPartida(String nombre, Setpregunta idSetPregunta) {
        
        boolean exito = false;
        EntityManager em = administrarEntidades();
        Partida nuevaPartida = new Partida();
        nuevaPartida.setIdsetpregunta(idSetPregunta);
        nuevaPartida.setNombre(nombre);
        try {
            
            em.persist(nuevaPartida);
            em.getTransaction().commit();
            exito = true;
        } catch (Exception e) {
            
            Logger.getLogger(PersistenciaPartida.class.getName())
                    .log(Level.SEVERE, null, e);
            em.getTransaction().rollback();
        } finally {
            
            em.close();
        }
        return exito;
    }
    
    /**
     * Este metodo es para recuperar los nombres de las partidas del set de 
     * preguntas seleccionada anteriormente
     * @param setPregunta Set de pregunta seleccionado
     * @return La lista de nombres de las partidas
     */
    public List<String> recuperarNombre(Setpregunta setPregunta) {
        
        EntityManager em = administrarEntidades();
        Query query = em.createQuery("SELECT p.nombre "
                + "FROM Partida p WHERE p.idsetpregunta"
                + ".idsetpregunta = \"" + setPregunta.getIdsetpregunta() + "\"");
        List<String> nombres = query.getResultList();
        return nombres;
    }
    
    /**
     * Este metodo es para recuperar una partida
     * @param setPregunta Setpregunta que tiene una partida
     * @return Una partida
     */
    public List<Partida> recuperarPartida(Setpregunta setPregunta) {

        EntityManager em = administrarEntidades();
        Query query = em.createQuery("SELECT p "
                + "FROM Partida p WHERE p.idsetpregunta.idsetpregunta = \"" 
                + setPregunta.getIdsetpregunta() + "\"");
        List<Partida> partidas = query.getResultList();
        return partidas;
    }
    
    /**
     * Metodo usado para eliminar una partida registrada en la base de datos.
     * @param id identificador de la cpartida a eliminar.
     * @return true si se elimino correctamente, false sino.
     */
    public boolean destroyPartida(long id) {
        
        boolean exito = false;
        EntityManager em = administrarEntidades();
        em.getTransaction().begin();
        try {
            
            Partida partida = em.find(Partida.class, id);
            em.remove(partida);
            em.getTransaction().commit();
            exito = true;
        } catch (Exception e) {
            
            Logger.getLogger(PersistenciaPartida.class.getName())
                    .log(Level.SEVERE, null, e);
            em.getTransaction().rollback();
        } finally {
            
            em.close();
        }
        return exito;
    }
    
    /**
     * Este metodo elimina una partida de la base de datos con el id del set.
     * @param setPregunta id del set de preguntas.
     */
    public void eliminarPartida(int setPregunta) {
        
        EntityManager em = administrarEntidades();
        Query query = em.createQuery("DELETE p "
                + "FROM Partida p WHERE p.idsetpregunta = " 
                + setPregunta);
    }
}

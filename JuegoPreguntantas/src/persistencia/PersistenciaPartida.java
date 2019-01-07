package persistencia;

import entity.Partida;
import entity.Setpregunta;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/****************************************************************** 
 * @version 1.0                                                   * 
 * @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          * 
 * @since 10/11/2018                                              *
 * Nombre de la clase PersistenciaPartida                         *
 *****************************************************************/ 
public class PersistenciaPartida extends Persistencia {
    
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
        Query query = em.createNamedQuery("Partida.findBySet")
                .setParameter("idsetpregunta", setPregunta.getIdsetpregunta());
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
        Query query = em.createNamedQuery("Partida.findByIdset", Partida.class)
                .setParameter("idsetpregunta", setPregunta.getIdsetpregunta());
        return query.getResultList();
    }
    
    /**
     * Metodo usado para eliminar una partida registrada en la base de datos.
     * @param id identificador de la partida a eliminar.
     */
    public void destroyPartida(int id) {
        
        EntityManager em = administrarEntidades();
        
        try {
            
            Partida partida = em.find(Partida.class, id);
            em.remove(partida);
            em.getTransaction().commit();
        } catch (Exception e) {
            
            Logger.getLogger(PersistenciaPartida.class.getName())
                    .log(Level.SEVERE, null, e);
            em.getTransaction().rollback();
        } finally {
            
            em.close();
        }
    }
    
}

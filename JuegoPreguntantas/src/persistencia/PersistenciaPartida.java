package persistencia;

import entity.Partida;
import entity.Setpregunta;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez                                 */ 
/* @since 10/11/2018                                              */
/* Nombre de la clase ComenzarPartidaController                   */
/******************************************************************/
public class PersistenciaPartida {
    
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
     * Este metodo es para crear una paertida en la base de datos 
     * @param nuevaPartida Cuenta de invitado a insertar
     * @return Si es verdadero o no el exito de la creacion de la partida
     */
    public boolean crearPartida(String nombre, String modoJuego, 
            Setpregunta idSetPregunta) {
        
        boolean exito = false;
        EntityManager em = administrarEntidades();
        Partida nuevaPartida = new Partida();
        nuevaPartida.setIdsetpregunta(idSetPregunta);
        nuevaPartida.setModojuego(modoJuego);
        nuevaPartida.setNombre(nombre);
        try {
            
            em.persist(nuevaPartida);
            em.getTransaction().commit();
            exito = true;
        } catch (Exception e) {
            
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            
            em.close();
            return exito;
        }
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
    public Partida recuperarPartida(Setpregunta setPregunta) {
        
        EntityManager em = administrarEntidades();
        Query query = em.createQuery("SELECT p "
                + "FROM Partida p WHERE p.idsetpregunta.idsetpregunta = \"" 
                + setPregunta.getIdsetpregunta() + "\"");
        List<Partida> partidas = query.getResultList();
        return partidas.get(0);
    }
}

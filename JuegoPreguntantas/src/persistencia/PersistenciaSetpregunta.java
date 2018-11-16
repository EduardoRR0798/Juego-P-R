package persistencia;

import entity.Cuentausuario;
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
public class PersistenciaSetpregunta {
    
     /**try {
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
     * Este metodo es para recuperar las categorias de los set de preguntas que 
     * haya en la base de datos
     * @param usuario Cuenta del usuario que esta usando el juego
     * @return La lista de categorias de los set de pregunta
     */
    public List<String> recuperarCategoria(Cuentausuario usuario) {

        EntityManager em = administrarEntidades();
        Query query = em.createQuery("SELECT s.categoria "
                + "FROM Setpregunta s WHERE s.idcuentausuario"
                + ".idcuentausuario = \"" + usuario.getIdcuentausuario() + "\"");
        List<String> categorias = query.getResultList();
        return categorias;
    }
    
    /**
     * Este metodo es para recuperar un set de preguntas por el usuario creador
     * @param usuario Cuenta del usuario que esta usando el juego
     * @return Un set de preguntas
     */
    public Setpregunta recuperarSetPregunta(Cuentausuario usuario) {
        
        EntityManager em = administrarEntidades();
        Query query = em.createQuery("SELECT s "
                + "FROM Setpregunta s WHERE s.idcuentausuario.idcuentausuario = \"" 
                + usuario.getIdcuentausuario() + "\"");
        List<Setpregunta> setsPregunta = query.getResultList();
        return setsPregunta.get(0);
    }
    
    /**
     * Este metodo es para recuperar un set de preguntas por su categoria
     * @param categoria Categoria del set de pregunta
     * @return Un set de preguntas
     */
    public Setpregunta recuperarSetPregunta(String categoria) {
        
        EntityManager em = administrarEntidades();
        Query query = em.createQuery("SELECT s "
                + "FROM Setpregunta s WHERE s.categoria = \"" 
                + categoria + "\"");
        List<Setpregunta> setPregunta = query.getResultList();
        return setPregunta.get(0);
    }
}

package persistencia;

import entity.Cuentausuario;
import entity.Setpregunta;
import java.util.ArrayList;
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
/* @author Puxka Acosta Domínguez                                 */ 
/* @since 10/11/2018                                              */
/* Nombre de la clase PersistenciaSetpregunta                     */
/******************************************************************/
public class PersistenciaSetpregunta {
    
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
     * Este metodo es para recuperar las categorias de los set de preguntas que 
     * haya en la base de datos
     * @param usuario Cuenta del usuario que esta usando el juego
     * @return La lista de categorias de los set de pregunta
     */
    public List<String> recuperarCategorias(Cuentausuario usuario) {
        
        EntityManager em = administrarEntidades();
        PersistenciaCategoria categoriasSet = new PersistenciaCategoria();
        List<String> categorias = new ArrayList<String>();
        try {
            
            Query queryId = em.createQuery("SELECT s.idcategoria "
                    + "FROM Setpregunta s "
                    + "WHERE s.idcuentausuario.idcuentausuario = \"" 
                    + usuario.getIdcuentausuario() + "\"");
            List<Integer> idCategoria = queryId.getResultList();
            categorias = categoriasSet.recuperarCategoriasSet(idCategoria);
        } catch (NullPointerException e) {
            
            Logger.getLogger(PersistenciaSetpregunta.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
    
            return categorias;
        }
        
    }
    
    /**
     * Este metodo es para recuperar un set de preguntas por el usuario creador
     * @param usuario Cuenta del usuario que esta usando el juego
     * @return Un set de preguntas
     */
    public Setpregunta recuperarSetPregunta(Cuentausuario usuario) {
        EntityManager em = administrarEntidades();
        Query query = em.createQuery("SELECT s "
                + "FROM Setpregunta s "
                + "WHERE s.idcuentausuario.idcuentausuario = \"" 
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
        String[] idSetPregunta = categoria.split(".- ");
        Query querySetPregunta = em.createQuery("SELECT s "
                + "FROM Setpregunta s WHERE s.idsetpregunta = \""
                + idSetPregunta[0] + "\"");
        List<Setpregunta> setPregunta = querySetPregunta.getResultList();
        return setPregunta.get(0);
    }
    
    /**
     * Este metodo es para crear un set de preguntas
     * @param categoria La categoria del set
     * @param cuenta La cuenta del usuario creador del set de preguntas
     * @return Si la creacion es exitosa o no
     */
    public boolean crearSetPregunta(String categoria, Cuentausuario cuenta) {
        
        boolean exito = false;
        EntityManager em = administrarEntidades();
        PersistenciaCategoria categorias = new PersistenciaCategoria();
        Setpregunta setPregunta = new Setpregunta();
        setPregunta.setIdcuentausuario(cuenta);
        try {
            
            int idCategoria = categorias.recuperarIdCategoria(categoria);
            setPregunta.setIdcategoria(idCategoria);
            em.persist(setPregunta);
            em.getTransaction().commit();
            exito = true;
        } catch (NullPointerException e) {
            
            Logger.getLogger(PersistenciaSetpregunta.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
    
            return exito;
        }
        
    }
    
    /**
     * Este metodo es para recuperar la id del último set de preguntas
     * @return El id del set de preguntas
     */
    public int recuperarUltimaId(){
        
        EntityManager em = administrarEntidades();
        Query query = em.createQuery("SELECT s.idsetpregunta FROM Setpregunta s");
        List<Integer> idSetPregunta = query.getResultList();
        int indice = idSetPregunta.size() - 1;
        return idSetPregunta.get(indice);
    }
}

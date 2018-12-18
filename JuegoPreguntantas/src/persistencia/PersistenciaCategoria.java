package persistencia;

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
/* @since 01/11/2018                                              */
/* Nombre de la clase PersistenciaCategoria                       */
/******************************************************************/
public class PersistenciaCategoria {
    
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
     * Este metodo es para recuperar las categorias deacuerdo a un id
     * @param idCategoria El id de una categoria
     * @return La lista de categorias
     */
    public List<String> recuperarCategoriasSet(List<Integer> idCategoria) {
        
        EntityManager em = administrarEntidades();
        List<String> categorias = new ArrayList<>();
        try {
            
            Query queryCategoria;
            for(int i = 0; i < idCategoria.size(); i++) {
                
                queryCategoria = em.createQuery("SELECT c.categoria "
                    + "FROM Categoria c "
                    + "WHERE c.idcategoria = " 
                    + idCategoria.get(i));
                
                categorias.add((i + 1) + ".- " + queryCategoria.getResultList().get(0).toString());
            }
    
        } catch (NullPointerException e) {
            
            Logger.getLogger(PersistenciaCategoria.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return categorias;
    }
    
    /**
     * Este metodo es para recuperar todas las categorias de la base de datos
     * @return Lista con las categorias
     */
    public List<String> recuperarCategorias() {

        EntityManager em = administrarEntidades();
        List<String> categorias = new ArrayList<>();
        try {

            Query queryCategoria = em.createQuery("SELECT c.categoria " +
                    "FROM Categoria c ");
            categorias = queryCategoria.getResultList();
        } catch (NullPointerException e) {

            Logger.getLogger(PersistenciaCategoria.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return categorias;
    }
    
    /**
     * Este metodo es para recuperar el id de una categoria por medio de su 
     * nombre
     * @param categoria La categoria 
     * @return El id de la categoria
     */
    public int recuperarIdCategoria(String categoria) {
        
        EntityManager em = administrarEntidades();
        List<Integer> categorias;
        int idCategoria = 0;
        try {
            
            Query queryCategoria = em.createQuery("SELECT c.idcategoria "
                    + "FROM Categoria c WHERE c.categoria = \"" 
                    + categoria + "\"");
            categorias = queryCategoria.getResultList();
            if(!categorias.isEmpty()) {
                
                idCategoria = categorias.get(0);
            }
        } catch (NullPointerException e) {
            
            Logger.getLogger(PersistenciaCategoria.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return idCategoria;
    }
}

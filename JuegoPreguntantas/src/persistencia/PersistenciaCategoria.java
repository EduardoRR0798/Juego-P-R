package persistencia;

import entity.Categoria;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/****************************************************************** 
 * @version 1.0                                                   * 
 * @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          * 
 * @since 26/10/2018                                              *
 * Nombre de la clase PersistenciaCategoria                       *
 *****************************************************************/
public class PersistenciaCategoria  extends Persistencia {
    
    /**
     * Este metodo es para recuperar las categorias deacuerdo a un id
     * @param idCategoria El id de una categoria
     * @return La lista de categorias
     */
    public List<String> recuperarCategoriasSet(List<Integer> idCategoria) {
        
        EntityManager em = administrarEntidades();
        List<String> categorias = new ArrayList<>();
        try {
            
            for(int i = 0; i < idCategoria.size(); i++) {
                                
                Query query = em.createNamedQuery("Categoria.findCategoriaById", 
                Categoria.class).setParameter("idcategoria", idCategoria.get(i));
                categorias.add((i + 1) + ".- " + query.getResultList().get(0).toString());
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
            
            Query query = em.createNamedQuery("Categoria.findAllCategoria", 
                Categoria.class);
            categorias = query.getResultList();
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
            
            Query query = em.createNamedQuery("Categoria.findIdByCategoria", 
                Categoria.class).setParameter("categoria", categoria);
            categorias = query.getResultList();
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

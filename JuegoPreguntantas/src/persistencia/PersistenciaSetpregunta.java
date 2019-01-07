package persistencia;

import entity.Cuentausuario;
import entity.Pregunta;
import entity.Setpregunta;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/****************************************************************** 
 * @version 1.0                                                   * 
 * @author Puxka Acosta Domínguez y Eduardo Rosas Rivera          * 
 * @since 12/11/2018                                              *
 * Nombre de la clase PersistenciaSetpregunta                     *
 *****************************************************************/
public class PersistenciaSetpregunta extends Persistencia {
    
    /**
     * Este metodo es para recuperar las categorias de los set de preguntas que 
     * haya en la base de datos
     * @param usuario Cuenta del usuario que esta usando el juego
     * @return La lista de categorias de los set de pregunta
     */
    public List<String> recuperarCategorias(Cuentausuario usuario) {
        
        EntityManager em = administrarEntidades();
        PersistenciaCategoria categoriasSet = new PersistenciaCategoria();
        List<String> categorias = new ArrayList<>();
        try {
            
            Query query = em.createNamedQuery("Setpregunta.findIdByIdCuentaUsuario", 
                Setpregunta.class).setParameter("idcuentausuario", 
                        usuario.getIdcuentausuario());
            List<Integer> idCategoria = query.getResultList();
            categorias = categoriasSet.recuperarCategoriasSet(idCategoria);
        } catch (NullPointerException e) {
            
            Logger.getLogger(PersistenciaSetpregunta.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            em.close();
        }
        return categorias;
    }
    
    /**
     * Este metodo es para recuperar los set de preguntas del usuario creador
     * @param usuario Cuenta del usuario que esta usando el juego
     * @return una lista de los sets de preguntas del usuario.
     */
    public List<Setpregunta> recuperarSetPregunta(Cuentausuario usuario) {
        
        EntityManager em = administrarEntidades();
        Query query = em.createNamedQuery("Setpregunta.findAllByIdCuentaUsuario", 
                Setpregunta.class).setParameter("idcuentausuario", 
                        usuario.getIdcuentausuario());
        return query.getResultList();
    }
    
    /**
     * Este metodo es para recuperar un set de preguntas por su categoria
     * @param categoria Categoria del set de pregunta
     * @return Un set de preguntas
     */
    public Setpregunta recuperarSetPregunta(String categoria) {
        EntityManager em = administrarEntidades();
        String[] idSetPregunta = categoria.split(".- ");
        Query query = em.createNamedQuery("Setpregunta.findAllByIdsetpregunta", 
                Setpregunta.class).setParameter("idsetpregunta", 
                        Integer.parseInt(idSetPregunta[0]));
        List<Setpregunta> setPregunta = query.getResultList();
        return setPregunta.get(0);
    }
    
    /**
     * Este metodo es para crear un set de preguntas
     * @param categoria La categoria del set
     * @param cuenta La cuenta del usuario creador del set de preguntas
     * @param preguntas Las preguntas del set de preguntas
     * @return Si la creacion es exitosa o no
     */
    public boolean crearSetPregunta(String categoria, Cuentausuario cuenta, 
            List<Pregunta> preguntas) {
        
        boolean exito = false;
        EntityManager em = administrarEntidades();
        PersistenciaCategoria categoriaBD = new PersistenciaCategoria();
        PersistenciaPregunta preguntaBD = new PersistenciaPregunta();
        Setpregunta setPregunta = new Setpregunta();
        setPregunta.setIdcuentausuario(cuenta);
        try {
            
            int idCategoria = categoriaBD.recuperarIdCategoria(categoria);
            setPregunta.setIdcategoria(idCategoria);
            em.persist(setPregunta);
            em.getTransaction().commit();
            for(int i = 0; i < preguntas.size(); i++){
                
                preguntas.get(i).setIdsetpregunta(recuperarUltimaId());
                if(preguntaBD.crearPregunta(preguntas.get(i))){
                    
                    exito = true;
                }
            }
        } catch (NullPointerException e) {
            
            Logger.getLogger(PersistenciaSetpregunta.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            
            em.close();
        }
        return exito;
    }
    
    /**
     * Este metodo es para recuperar la id del último set de preguntas
     * @return El id del set de preguntas
     */
    public int recuperarUltimaId(){
        
        EntityManager em = administrarEntidades();
        int indice;
        try {
            
            Query query = em.createNamedQuery("Setpregunta.findMaxIdsetpregunta", 
                Setpregunta.class);
            indice = (int) query.getSingleResult();
        } catch(NullPointerException e) {
            
            indice = 0;
        }
        return indice;
    }
    
    /**
     * Este metodo recupera todos los sets de pregunta que concuerden con un
     * mismo id de una categoria.
     * @param idCategoria id de la categoria de interes.
     * @return una lista de setpregunta con la misma id categoria.
     */
    public List<Setpregunta> recuperarSetCategoria(int idCategoria) {
        
        List<Setpregunta> setsPregunta;
        EntityManager em = administrarEntidades();
        Query query = em.createNamedQuery("Setpregunta.findAllByIdcategoria", 
                Setpregunta.class).setParameter("idcategoria", idCategoria);
        setsPregunta = query.getResultList();
        em.close();
        return setsPregunta;
    }
    
}

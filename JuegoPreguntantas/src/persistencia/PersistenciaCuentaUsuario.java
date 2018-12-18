package persistencia;

import entity.Cuentausuario;
import javax.persistence.NoResultException;
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
/* @since 07/11/2018                                              */
/* Nombre de la clase EnviarInvitacionController                  */
/******************************************************************/
public class PersistenciaCuentaUsuario {
    
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
     * Este metodo es para comprobar que el nombre de invitado no es el mismo 
     * que el de una cuenta de usuario
     * @param nombreInvitado El nombre de la cuenta de invitado
     * @return Si es verdadero o no la repeticion del nombre en la base de datos
     */
    public boolean comprobarNombreDiferente(String nombreInvitado) {
        
        EntityManager em = administrarEntidades();
        Query query = em.createQuery("SELECT c.nombreusuario "
                + "FROM Cuentausuario c WHERE c.nombreusuario = \"" 
                + nombreInvitado + "\"");
        List<String> nombreInvitadoRepetido = query.getResultList();
        boolean datoRepetido = nombreInvitadoRepetido.isEmpty();
        return datoRepetido;
    }
    
    public Cuentausuario recuperarUsuario(int id){
        
        EntityManager em = administrarEntidades();
        Query query = em.createQuery("SELECT c "
                + "FROM Cuentausuario c WHERE c.idcuentausuario = \"" 
                + id + "\"");
        List<Cuentausuario> usuario = query.getResultList();
        return usuario.get(0);
    }
    
      public PersistenciaCuentaUsuario() {}
    
        /**
     * Este metodo es para crear una cuenta de usuario en la base de datos 
     * @param nuevoUsuario Cuenta de usuario a registrar
     * @return Si es verdadero o no el exito de la creacion de la cuenta
     */
    public boolean crearUsuario(Cuentausuario nuevoUsuario) {
        
        boolean exito = false;
        EntityManager em = administrarEntidades();
        try {
            
            em.persist(nuevoUsuario);
            em.getTransaction().commit();
            exito = true;
        } catch (Exception e) {
            
            Logger.getLogger(PersistenciaCuentaUsuario.class.getName())
                    .log(Level.SEVERE, null, e);
            em.getTransaction().rollback();
        } finally {
            
            em.close();
        }
        return exito;
        
    }
    
    /**
     * Metodo usado para eliminar una cuenta de un usuario registrado en la 
     * base de datos.
     * @param id identificador de la cuenta a eliminar.
     */
    public void destroyCuentaUsuario(long id) {
        
        EntityManager em = administrarEntidades();
        em.getTransaction().begin();
        try {
            
            Cuentausuario cu= em.find(Cuentausuario.class, id);
            em.remove(cu);
            em.getTransaction().commit();
        } catch (Exception e) {
            
            Logger.getLogger(PersistenciaCuentaUsuario.class.getName())
                    .log(Level.SEVERE, null, e);
            em.getTransaction().rollback();
        } finally {
            
            em.close();
        }
    }
    
    /**
     * Este metodo es para eliminar una cuenta de invitado en la base de datos 
     * @param usuario Cuenta de usuario a eliminar
     */
    public void eliminarUsuario(Cuentausuario usuario) {
        
        EntityManager em = administrarEntidades();
        try {

            if (!em.contains(usuario)) {
                
                usuario = em.merge(usuario);
            }

            em.remove(usuario);
            em.getTransaction().commit();
        } catch (Exception e) {
            
            Logger.getLogger(PersistenciaCuentaUsuario.class.getName())
                    .log(Level.SEVERE, null, e);
            em.getTransaction().rollback();
        } finally {
            
            em.close();
        }
    }
    
    /**
     * Metodo que busca un usuario en la base de datos con el nombre que 
     * el usuario ingrese.
     * @param nombreusuario en el TextField de la pantalla del Login.
     * @return true si existe en la base de datos, false si no existe.
     */
    public Cuentausuario getCuentaUsuarioNombre(String nombreusuario) {
        EntityManagerFactory emf = javax.persistence.Persistence.
                createEntityManagerFactory("JuegoPreguntantasPU");
        EntityManager em = emf.createEntityManager();
        Cuentausuario cuenta = new Cuentausuario();

        try {
            
            cuenta = (Cuentausuario) em.createQuery(
                    "SELECT c FROM Cuentausuario c "
                            + "WHERE UPPER(c.nombreusuario) = \""+
                            nombreusuario +"\"").getSingleResult();
        } catch (NoResultException noResult) {
            
            cuenta = null;
        } finally {
            
            em.close();
        }
        
        return cuenta;
    }
    
    /**
     * Este metodo recupera al usuario con su email.
     * @param email email del usuario a buscar.
     * @return el usuario a buscar, null si no hay ninguna ocurrencia.
     */
    public Cuentausuario getCuentaUsuarioEmail(String email) {
        
        EntityManagerFactory emf = javax.persistence.Persistence.
                createEntityManagerFactory("JuegoPreguntantasPU");
        EntityManager em = emf.createEntityManager();
        Cuentausuario cuenta = new Cuentausuario();
        try {
            
            cuenta = (Cuentausuario) em.createQuery(
                    "SELECT c FROM Cuentausuario c WHERE UPPER("
                            + "c.correoelectronico) = \""
                            +email +"\"").getSingleResult();
        } catch (NoResultException noResult) {
            
            cuenta = null;
        } finally {
            
            em.close();
        }
        
        return cuenta;
    }
  
}

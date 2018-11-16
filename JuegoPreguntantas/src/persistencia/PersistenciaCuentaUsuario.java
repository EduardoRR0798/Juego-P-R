package persistencia;

import entity.Cuentausuario;
import entity.Cuentainvitado;
import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * Metodo que registra una cuentausuario en la base de datos.
     * @param cu Entidad de cuentausuario a guardar;
     */
    public void registrarCuentaUsuario(Cuentausuario cu) {
        PersistenciaCuentaUsuario persistencia = new PersistenciaCuentaUsuario();
        persistencia.persist(cu);
    }
    
    /**
     * Metodo que guarda objetos en la base de datos.
     * @param object objeto a guadar en la base de datos.
     */
    public void persist(Object object) {
        EntityManagerFactory emf = javax.persistence.Persistence.
                createEntityManagerFactory("JuegoPreguntantasPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    
    /**
     * Metodo usado para eliminar una cuenta de un usuario registrado en la base de datos.
     * @param id identificador de la cuenta a eliminar.
     */
    public void destroyCuentaUsuario(long id) {
        EntityManagerFactory emf = javax.persistence.Persistence.
                createEntityManagerFactory("JuegoPreguntantasPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Cuentausuario cu= em.find(Cuentausuario.class, id);
            em.remove(cu);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    
    /**
     * Metodo que busca un usuario en la base de datos con el nombre que el usuario ingrese.
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
    
    public Cuentausuario getCuentaUsuarioEmail(String email) {
        EntityManagerFactory emf = javax.persistence.Persistence.
                createEntityManagerFactory("JuegoPreguntantasPU");
        EntityManager em = emf.createEntityManager();
        Cuentausuario cuenta = new Cuentausuario();

        try {
            cuenta = (Cuentausuario) em.createQuery(
                    "SELECT c FROM Cuentausuario c WHERE UPPER(c.correoelectronico) = \""
                            +email +"\"").getSingleResult();
        } catch (NoResultException noResult) {
            cuenta = null;
        } finally {
            em.close();
        }
        
        return cuenta;
    }
    
    /**
     * Metodo que busca el nombre ingresado por el usuario en la base de datos de la tabla de invitados.
     * @param nombreinvitado nombre ingresado por el usuario en la pantalla de login.
     * @return true si la cuenta de tipo invitado existe en la base de datos, false si no existe.
     */
    public Cuentainvitado getCuentaInvitadoPorNombre(String nombreinvitado) {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("JuegoPreguntantasPU");
        EntityManager em = emf.createEntityManager();
        Cuentainvitado invitado = new Cuentainvitado();
        try {
            invitado = (Cuentainvitado) em.createQuery("SELECT c FROM Cuentainvitado c WHERE c.nombre = \""+ nombreinvitado +"\"").getSingleResult();
        } catch (NoResultException noResult) {
            invitado = null;
        } finally {
            em.close();
        }
        return invitado;
    }
  
}

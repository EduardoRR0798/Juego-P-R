package persistencia;
  
import java.util.ArrayList;
import entity.Cuentainvitado;
import entity.Cuentausuario;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.NoResultException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez Eduardo Rosas Rivera            */ 
/* @since 07/11/2018                                              */
/* Nombre de la clase EnviarInvitacionController                  */
/******************************************************************/
public class PersistenciaCuentaInvitado {
    
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
     * Este metodo es para crear una cuenta de invitado en la base de datos 
     * @param nuevoInvitado Cuenta de invitado a insertar
     * @return Si es verdadero o no el exito de la creacion de la cuenta
     */
    public boolean crearInvitado(Cuentainvitado nuevoInvitado) {
        
        boolean exito = false;
        EntityManager em = administrarEntidades();
        try {
            
            em.persist(nuevoInvitado);
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
    
    /*
     * Este metodo es para crear el nombre al invitado
     * @return El nombre de la cuenta de invitado
     */
    public String crearNombre() {
        
        EntityManager em = administrarEntidades();
        Query query = em.createQuery("SELECT MAX(c.idcuentainvitado) "
                + "FROM Cuentainvitado c");
        List<Integer> idInvitado = query.getResultList();
        String nombre = null;
        try{
            
            nombre = "Pregunton" + (idInvitado.get(0) + 1);
        } catch(NullPointerException e) {
            
            nombre = "Pregunton0";
        } finally {
            
            nombre = comprobarNombre(nombre);
            return nombre;
        }

    }
    
    /**
     * Este metodo es para crear el codigo que usara de contraseña el invitado 
     * @return El codigo de la cuenta de invitado
     */
    public String crearCodigo() {

        String codigo = UUID.randomUUID().toString();
        if (comprobarCodigo(codigo)) {

            crearCodigo();
        }

        return codigo;
    }
    
    /**
     * Este metodo es para comprobar que el correo que se ha ingresado es 
     * diferente al que se ha invitado antes
     * @param correoElectronico Correo electronico ingresado por el usuario
     * @return Si es verdadero o no la repeticion del correo electronico en la
     * base de datos
     */
    public boolean comprobarCorreo(String correoElectronico) {
        
        EntityManager em = administrarEntidades();
        Query query = em.createQuery("SELECT c.correoelectronico "
                + "FROM Cuentainvitado c WHERE c.correoelectronico = \"" 
                + correoElectronico + "\"");
        List<String> correoRepetido = query.getResultList();
        boolean datoRepetido = !correoRepetido.isEmpty();
        return datoRepetido;
    }
    
    /**
     * Este metodo es para comprobar que el codigo es diferente al de otra cuenta
     * de invitado
     * @param codigo El codigo creado
     * @return Si es verdadero o no la repeticion del codigo en la base de datos
     */
    public boolean comprobarCodigo(String codigo) {
        
        EntityManager em = administrarEntidades();
        Query query = em.createQuery("SELECT c.codigo "
                + "FROM Cuentainvitado c WHERE c.codigo = \"" 
                + codigo + "\"");
        List<String> codigoRepetido = query.getResultList();
        boolean datoRepetido = !codigoRepetido.isEmpty();
        return datoRepetido;
    }
    
    /**
     * Este metodo es para comprobar que el nombre es diferente al de una cuenta
     * de usuario, agregando un caracter en caso de que lo sea
     * @param nombre El nombre de la cuenta de invitado
     * @return El nombre de la cuenta de invitado diferente al de una cuenta 
     * de usuario
     */
    public String comprobarNombre(String nombre) {
        
        PersistenciaCuentaUsuario usuarioRegistrado
                = new PersistenciaCuentaUsuario();
        if (!usuarioRegistrado.comprobarNombreDiferente(nombre)) {

            nombre = nombre + (char) (48 + new Random().nextInt(47));
            comprobarNombre(nombre);
        }
        return nombre;
    }
    
    /**
     * Este metodo es para eliminar una cuenta de invitado en la base de datos 
     * @param invitado Cuenta de invitado a eliminar
     */
    public void eliminarInvitado(Cuentainvitado invitado) {
        
        EntityManager em = administrarEntidades();
        try {

            if (!em.contains(invitado)) {
                
                invitado = em.merge(invitado);
            }

            em.remove(invitado);
            em.getTransaction().commit();
        } catch (Exception e) {
            
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            
            em.close();
        }
    }
  
      /**
     * Constructor de la clase.
     */
    public PersistenciaCuentaInvitado() {}
    
    /**
     * Metodo que registra una Cuentainvitado en la base de datos.
     * @param cu Entidad de cuentainvitado a guardar;
     */
    public void registrarCuentaInvitado(Cuentainvitado ci) {
        PersistenciaCuentaInvitado persistencia = new PersistenciaCuentaInvitado();
        persistencia.persist(ci);
    }
    
    /**
     * Metodo que guarda objetos en la base de datos.
     * @param object objeto a guadar en la base de datos.
     */
    public void persist(Object object) {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("JuegoPreguntantasPU");
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
    public void destroyCuentaInvitado(long id) {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("JuegoPreguntantasPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Cuentainvitado cu= em.find(Cuentainvitado.class, id);
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
     * Metodo que busca el nombre ingresado por el usuario en la base de datos de la tabla de invitados.
     * @param nombreinvitado nombre ingresado por el usuario en la pantalla de login.
     * @return true si la cuenta de tipo invitado existe en la base de datos, false si no existe.
     */
    public Cuentainvitado getCuentaInvitado(String nombreinvitado) {
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

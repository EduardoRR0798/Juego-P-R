package persistencia;

import entity.Cuentausuario;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez                                 */ 
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
}

package persistencia;

import entity.Cuentausuario;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez                                 */ 
/* @since 07/11/2018                                              */
/* Nombre de la clase EnviarInvitacionController                  */
/******************************************************************/
public class PersistenciaCuentaUsuario {
    
    public EntityManager conectarABaseDeDatos() {
        
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.user", "root");
        properties.put("javax.persistence.jdbc.password", "puxkas");
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("JuegoPreguntantasPU", properties);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        return em;
    }

    public boolean comprobarNombreInvitado(String nombre) {
        
        EntityManager em = conectarABaseDeDatos();
        List<Cuentausuario> correoRepetido = em
                .createNamedQuery("Cuentausuario.findByNombreusuario")
                .setParameter("nombreusuario", nombre)
                .getResultList();
        boolean datoRepetido = correoRepetido.isEmpty();
        return datoRepetido;
    }
}

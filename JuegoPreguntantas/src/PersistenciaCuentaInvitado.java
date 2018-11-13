package persistencia;

import entity.Cuentainvitado;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/******************************************************************/ 
/* @version 1.0                                                   */ 
/* @author Puxka Acosta Domínguez                                 */ 
/* @since 07/11/2018                                              */
/* Nombre de la clase EnviarInvitacionController                  */
/******************************************************************/
public class PersistenciaCuentaInvitado {

    public EntityManager conectarABaseDeDatos() {
        
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.user", "root");
        properties.put("javax.persistence.jdbc.password", "puxkas");
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("JuegoPreguntantasPU", properties);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        return em;
    }
    
    public void crearInvitado(String nombre, String correoElectronico, String codigo) {
        
        EntityManager em = conectarABaseDeDatos();
        Cuentainvitado nuevoInvitado = new Cuentainvitado();
        nuevoInvitado.setNombre(nombre);
        nuevoInvitado.setCorreoelectronico(correoElectronico);
        nuevoInvitado.setCodigo(codigo);
        try {
            
            em.persist(nuevoInvitado);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public String crearNombre() {
        
        EntityManager em = conectarABaseDeDatos();
        List<Integer> idInvitado = em
                .createNamedQuery("Cuentainvitado.findLastId")
                .getResultList();
        String nombre = "Pregunton" + (idInvitado.get(0).intValue() + 1);
        return nombre;
    }
    
    public String crearCodigo() {
        
        String codigo = UUID.randomUUID().toString();
        codigo.replace("-", "");
        return codigo;
    }
    
    public boolean comprobarCorreo(String correoElectronico) {
        
        EntityManager em = conectarABaseDeDatos();
        List<Cuentainvitado> correoRepetido = em
                .createNamedQuery("Cuentainvitado.findByCorreoelectronico")
                .setParameter("correoelectronico", correoElectronico)
                .getResultList();
        boolean datoRepetido = correoRepetido.isEmpty();
        return datoRepetido;
    }
    
    public boolean comprobarCodigo(String codigo) {
        
        EntityManager em = conectarABaseDeDatos();
        List<Cuentainvitado> codigoRepetido = em
                .createNamedQuery("Cuentainvitado.findByCodigo")
                .setParameter("codigo", codigo)
                .getResultList();
        boolean datoRepetido = codigoRepetido.isEmpty();
        return datoRepetido;
    }
}



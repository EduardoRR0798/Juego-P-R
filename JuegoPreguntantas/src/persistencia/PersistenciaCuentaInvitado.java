/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import entity.Cuentainvitado;
import entity.Cuentausuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author Eduar
 */
public class PersistenciaCuentaInvitado {
    
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

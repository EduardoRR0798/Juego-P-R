/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegopreguntantas;

import entity.Cuentainvitado;
import entity.Cuentausuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author Eduardo Rosas Rivera
 */
public class Persistencia {
    
    Persistencia() {}
    
    /**
     * Metodo que registra una cuentausuario en la base de datos.
     * @param cu Entidad de cuentausuario a guardar;
     */
    public void registrarCuentaUsuario(Cuentausuario cu) {
        Persistencia persistencia = new Persistencia();
        persistencia.persist(cu);
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
    public void destroyCuentaUsuario(long id) {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("JuegoPreguntantasPU");
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
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("JuegoPreguntantasPU");
        EntityManager em = emf.createEntityManager();
        Cuentausuario cuenta = new Cuentausuario();

        try {
            cuenta = (Cuentausuario) em.createQuery(
                    "SELECT c FROM Cuentausuario c WHERE c.nombreusuario = \""+nombreusuario +"\"").getSingleResult();
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

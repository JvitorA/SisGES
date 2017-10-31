/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package Dao;

import Hibernate.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

/**
 *
 * @author Alessandro
 * @param <Obj>
 */
public class GenericDAO<Obj> implements Serializable {

    protected SessionFactory sessionFactory = null;
    private static final ThreadLocal threadSession = new ThreadLocal();

    public GenericDAO() {
        try {
            if (this.sessionFactory == null) {
                HibernateUtil instance = HibernateUtil.getInstance();
                sessionFactory = instance.getSessionFactory();
            }
        } catch (HibernateException ex) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @SuppressWarnings("unchecked")
    public Session getSession() throws HibernateException {
        Session s = (Session) threadSession.get();
        try {
            if (s == null) {
                s = this.sessionFactory.openSession();
            }
            threadSession.set(s);

        } catch (HibernateException ex) {
            throw new RuntimeException(ex);
        }
        return s;
    }

    @SuppressWarnings("unchecked")
    public void closeSession() throws HibernateException {
        try {
            Session s = (Session) threadSession.get();
            threadSession.set(null);
            if (s != null && s.isOpen()) {
                s.close();
            }
        } catch (HibernateException ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings("unchecked")
    public void closeFactoryConnection() throws Exception {
        this.sessionFactory.close();
    }

    @SuppressWarnings("unchecked")
    public boolean insert(Obj obj) throws Exception {
        Session session = this.getSession();
        try {
            session.getTransaction().begin();
            session.save(obj);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            //System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean update(Obj obj) {
        Session session = this.getSession();
        try {
            session.getTransaction().begin();
            //session.update(obj);
            session.merge(obj);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            //System.err.println(e.getMessage());
            //System.err.println(e.getCause());
            return false;
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean delete(Obj obj) throws Exception {
        Session session = this.getSession();
        try {
            session.getTransaction().begin();
            session.delete(obj);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public List getList(String condicao) throws Exception {
        Session session = this.getSession();
        Query q = null;
        try {
            session.getTransaction().begin();
            q = session.createQuery(condicao);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.err.println(e.getMessage());
        }
        return q.list();
    }

    /**
     * retorna o objeto com maior codigo
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Object loadLast() throws Exception {
        //Session session = this.getSessionFactory().openSession();
        Session session = this.getSession();
        Criteria criteria = session.createCriteria(Object.class);
        criteria.addOrder(Order.desc("id"));
        List result = criteria.list();
        Object unique = result.get(0);
        return unique;
    }

    @SuppressWarnings("unchecked")
    public Object retrieve(String pk) throws Exception {
        int k = Integer.parseInt(pk);
        Session session = this.getSession();
        session.getTransaction().begin();
        Object cliente = (Object) session.load(Object.class, k);
        session.getTransaction().commit();
        session.flush();
        session.close();
        return cliente;
    }

}

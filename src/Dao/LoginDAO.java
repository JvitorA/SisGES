/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Login;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class LoginDAO extends GenericDAO<Login> {

    public Login getLoginProfessor(int siape, String senha) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Login.findByLogin").setParameter("professorsiape", siape).setParameter("senha", senha);
        //para retornar um objeto apenas
        Login l = (Login) q.uniqueResult();
        return l;
    }

    public boolean findBySiapeProfessor(int siape) throws Exception {
        boolean encontrado = false;
        //Session session = this.getSessionFactory().openSession();
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Login.findBySiape").setParameter("professorsiape", siape);
        if (q.uniqueResult() != null) {
            encontrado = true;
        }
        return encontrado;
    }

    public Login getBySiapeProfessor(int siape) throws Exception {
        //Session session = this.getSessionFactory().openSession();
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Login.findBySiape").setParameter("professorsiape", siape);
        //para retornar um objeto apenas
        Login l = (Login) q.uniqueResult();
        //session.clear();
        //session.close();
        return l;
    }

}

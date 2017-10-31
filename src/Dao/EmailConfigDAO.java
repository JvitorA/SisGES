/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.EmailConfig;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class EmailConfigDAO extends GenericDAO<EmailConfig> {

    public EmailConfig getEmailConfig() throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("EmailConfig.findAll");
        return (EmailConfig) q.list().get(0);
    }

    public EmailConfig getEmailConfig(int email) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("EmailConfig.findByEmail").setParameter("email", email);
        EmailConfig ec = (EmailConfig) q.uniqueResult(); // para retornar um objeto apenas
        return ec;
    }
}

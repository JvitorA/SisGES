/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Model.Perfil;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Alessandro
 */
public class PerfilDAO extends GenericDAO<Perfil> {

    public Perfil getPerfilByNome(String nome) throws Exception {
        Session session = this.getSession();
        Query q = (Query) session.getNamedQuery("Perfil.findByNomeperfil").setParameter("nomeperfil", nome);
        Perfil u = (Perfil) q.uniqueResult(); // para retornar um objeto apenas
        return u;
    }

}

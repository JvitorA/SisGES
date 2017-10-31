/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package Hibernate;

import Model.Aluno;
import Model.Alunoaprovado;
import Model.Alunomatriculado;
import Model.Assinatura;
import Model.Assunto;
import Model.Banca;
import Model.Bancafila;
import Model.Bancastatus;
import Model.Campus;
import Model.EmailConfig;
import Model.Convidado;
import Model.Curso;
import Model.Defesa;
import Model.Professordisponibilidade;
import Model.Empresa;
import Model.Estagio;
import Model.Login;
import Model.Email;
import Model.Mascara;
import Model.Modalidade;
import Model.Orientacaofila;
import Model.Perfil;
import Model.Professor;
import Model.Professororientacao;
import Model.Professororientacaoseparada;
import Model.Sumarioaprovado;
import Model.Sumariomatricula;
import Model.Trabalho;
import Model.Trabalhostatus;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/*
 * @author Alessandro Rezende
 * O Singleton é um dos padrões de projeto mais simples.
 * Ele é usado quando for necessária a existência de apenas uma instância de uma
 * classe. Foi apresentado no GoF como um padrão de criação, por lidar com a
 * criação de objectos.
 */
public final class HibernateUtil implements Serializable {

    private static HibernateUtil instance;
    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;

    public static HibernateUtil getInstance() {
        if (instance == null) {
            instance = new HibernateUtil();
        }
        return instance;
    }

    private HibernateUtil() {
        instance = null;
        this.sessionFactory = null;
        this.serviceRegistry = null;
        this.sessionFactory = this.getInstanceSessionFactory();
    }

    public SessionFactory getInstanceSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/SisGES");
            configuration.setProperty("hibernate.connection.username", "postgres");
            //configuration.setProperty("hibernate.connection.password", "210189");
            configuration.setProperty("hibernate.connection.password", "sisges123654");
            configuration.setProperty("hibernate.show_sql", "false");
            configuration.setProperty("hibernate.format_sql", "true");
            configuration.setProperty("hibernate.current_session_context_class", "thread");
            configuration.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");

            configuration.addAnnotatedClass(Perfil.class);
            configuration.addAnnotatedClass(Login.class);
            configuration.addAnnotatedClass(Professor.class);
            configuration.addAnnotatedClass(Aluno.class);
            configuration.addAnnotatedClass(Assunto.class);
            configuration.addAnnotatedClass(Empresa.class);
            configuration.addAnnotatedClass(Estagio.class);
            configuration.addAnnotatedClass(Trabalhostatus.class);
            configuration.addAnnotatedClass(Curso.class);
            configuration.addAnnotatedClass(Modalidade.class);
            configuration.addAnnotatedClass(Assinatura.class);
            configuration.addAnnotatedClass(Bancafila.class);
            configuration.addAnnotatedClass(Bancastatus.class);
            configuration.addAnnotatedClass(Banca.class);
            configuration.addAnnotatedClass(Campus.class);
            configuration.addAnnotatedClass(Convidado.class);
            configuration.addAnnotatedClass(Professordisponibilidade.class);
            configuration.addAnnotatedClass(Professororientacao.class);
            configuration.addAnnotatedClass(Professororientacaoseparada.class);
            configuration.addAnnotatedClass(Defesa.class);
            configuration.addAnnotatedClass(Trabalho.class);
            configuration.addAnnotatedClass(Orientacaofila.class);
            configuration.addAnnotatedClass(Sumariomatricula.class);
            configuration.addAnnotatedClass(Sumarioaprovado.class);
            configuration.addAnnotatedClass(Alunomatriculado.class);
            configuration.addAnnotatedClass(Alunoaprovado.class);
            configuration.addAnnotatedClass(EmailConfig.class);
            configuration.addAnnotatedClass(Email.class);
            configuration.addAnnotatedClass(Mascara.class);

            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (HibernateException ex) {
            Logger.getLogger(HibernateUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}

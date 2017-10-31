/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Email;

/**
 *
 * @author Alessandro
 */
import Controller.AssuntoController;
import Dao.EmailConfigDAO;
import Model.EmailConfig;
import Util.Functions;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

public final class CommonsEmail {

    //private String emailSistema = "facom.sisges@gmail.com";
    //private String nomeSistema = "SisGES";
    private final EmailConfig emailConfig;

    public CommonsEmail() throws EmailException, MalformedURLException {
        //---------------------------------------------------------
        System.getProperties().put("http.proxySet", "true");
        System.getProperties().put("http.proxyHost", "proxy.ufu.br");
        System.getProperties().put("http.proxyPort", "3128");
        //---------------------------------------------------------
        emailConfig = this.getConfiguracao();
    }

    public EmailConfig getConfiguracao() {
        EmailConfig ec = null;
        EmailConfigDAO ecDAO = new EmailConfigDAO();
        try {
            ec = ecDAO.getEmailConfig();
            ecDAO.closeSession();
        } catch (Exception ex) {
            Logger.getLogger(AssuntoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ec;
    }

    /**
     * função para enviar email
     *
     * @param titulo
     * @param msgEmail
     * @param emailDestinatarios
     * @return
     */
    public boolean enviarEmail(String titulo, String msgEmail, String emailDestinatarios) {
        boolean enviado = false;
        String para = emailDestinatarios.toLowerCase().trim();
        String subject = titulo.trim();
        String msg = msgEmail.trim();

        try {
            StringTokenizer stPara = new StringTokenizer(para, ";");

            while (stPara.hasMoreTokens()) {
                if (!stPara.toString().trim().equals("")) {
                    HtmlEmail email = new HtmlEmail();
                    /*o servidor SMTP para envio do e-mail*/
                    email.setHostName(emailConfig.getHostname());
                    email.setSmtpPort(emailConfig.getPorta());
                    email.setSSLOnConnect(emailConfig.getSsl());
                    email.setStartTLSEnabled(emailConfig.getTsl());

                    /*remetente*/
                    email.setFrom(emailConfig.getEmail(), emailConfig.getNome());
                    email.setAuthentication(emailConfig.getUsuario(), emailConfig.getSenha());
                    /* ---------------------------------------------------------- */
                    //destinatário
                    //email.addTo(emailDestinatario, nomeDestinatario);
                    email.addTo(stPara.nextToken().trim());
                    // assunto do e-mail
                    email.setSubject(subject);

                    //conteudo do e-mail
                    //configura a mensagem para o formato HTML
                    email.setHtmlMsg(msg);
                    // configure uma mensagem alternativa caso o servidor não suporte HTML
                    email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");

                    // envia email
                    email.send();
                    enviado = true;
                }
            }
        } catch (EmailException ex) {
            enviado = false;
            Logger.getLogger(CommonsEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return enviado;
    }

    /**
     * envia email simples(somente texto)
     *
     * @throws EmailException
     */
    public void enviaEmailSimples() throws EmailException {

        SimpleEmail email = new SimpleEmail();
        // o servidor SMTP para envio do e-mail
        email.setHostName("smtp.gmail.com");
        //destinatário
        email.addTo("alessandropereirarezende@gmail.com", "Alessandro");
        // remetente
        email.setFrom(emailConfig.getEmail(), emailConfig.getNome());
        // assunto do e-mail
        email.setSubject("Teste -> Email simples");
        //conteudo do e-mail
        email.setMsg("Teste de Email utilizando commons-email");
        email.setAuthentication(emailConfig.getUsuario(), emailConfig.getSenha());
        email.setSmtpPort(465);
        email.setSSLOnConnect(true);
        email.send();
    }

    /**
     * Envia email no formato HTML
     *
     * @throws EmailException
     * @throws MalformedURLException
     */
    private void enviaEmailFormatoHtml() throws EmailException, MalformedURLException, IOException, Exception {

        String para = "alessandropereirarezende@gmail.com;alessandrorezende@msn.com";
        StringTokenizer stPara = new StringTokenizer(para, ";");
        while (stPara.hasMoreTokens()) {

            HtmlEmail email = new HtmlEmail();
            // adiciona uma imagem ao corpo da mensagem e retorna seu id
            URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
            String cid = email.embed(url, "Apache logo");
            System.out.println(cid);

            // configura a mensagem para o formato HTML
            File img = new File(Functions.getCurrentPath() + "web\\resources\\images\\facom.png");
            StringBuilder msg = new StringBuilder();
            msg.append("<html><body>");
            msg.append("<img src=cid:").append(email.embed(img)).append(">");
            msg.append("</body></html>");

            // configure uma mensagem alternativa caso o servidor não suporte HTML
            email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");
            // o servidor SMTP para envio do e-mail
            email.setHostName("smtp.gmail.com");
            // remetente
            email.setFrom(emailConfig.getEmail(), emailConfig.getNome());
            email.setAuthentication(emailConfig.getUsuario(), emailConfig.getSenha());
            email.setSmtpPort(emailConfig.getPorta());
            email.setSSLOnConnect(emailConfig.getSsl());

            //destinatário
            //email.addTo("alessandropereirarezende@gmail.com", "Alessandro");
            email.addTo(stPara.nextToken().trim());
            // assunto do e-mail
            email.setSubject("Teste -> Html Email");
            email.setHtmlMsg(msg.toString());
            //conteudo do e-mail
            //email.setMsg("Teste de Email HTML utilizando commons-email");
            // envia email
            email.send();
        }
    }

    /**
     * envia email com arquivo anexo
     *
     * @throws EmailException
     */
    public void enviaEmailComAnexo() throws EmailException {

        // cria o anexo 1.
        EmailAttachment anexo1 = new EmailAttachment();
        //caminho do arquivo (RAIZ_PROJETO/teste/teste.txt)
        anexo1.setPath("teste/teste.txt");
        anexo1.setDisposition(EmailAttachment.ATTACHMENT);
        anexo1.setDescription("Exemplo de arquivo anexo");
        anexo1.setName("teste.txt");

        // cria o anexo 2.
        EmailAttachment anexo2 = new EmailAttachment();
        //caminho do arquivo (RAIZ_PROJETO/teste/teste2.jsp)
        anexo2.setPath("teste/teste2.jsp");
        anexo2.setDisposition(EmailAttachment.ATTACHMENT);
        anexo2.setDescription("Exemplo de arquivo anexo");
        anexo2.setName("teste2.jsp");

        // configura o email
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
        email.addTo("teste@gmail.com", "Guilherme"); //destinatário
        email.setFrom("facom.sisges@gmail.com", "SisGES"); // remetente
        email.setSubject("Teste -> Email com anexos"); // assunto do e-mail
        email.setMsg("Teste de Email utilizando commons-email"); //conteudo do e-mail
        email.setAuthentication("teste", "xxxxx");
        email.setSmtpPort(465);
        //email.setSSL(true);
        //email.setTLS(true);

        // adiciona arquivo(s) anexo(s)
        email.attach(anexo1);
        email.attach(anexo2);
        // envia o email
        email.send();
    }

    public EmailConfig getEmailConfig() {
        return emailConfig;
    }

//    public static void main(String[] args) throws EmailException, MalformedURLException, IOException, Exception {
//        new CommonsEmail().enviaEmailSimples();
//        new CommonsEmail().enviaEmailFormatoHtml();
//    }
}

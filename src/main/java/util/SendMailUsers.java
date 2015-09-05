package util;

import email.MailTemplate;
import jsoup.model.Contact;
import jsoup.model.User;

import java.util.List;

/**
 * Created by marcus on 04/07/2015.
 */
public class SendMailUsers {
    private MailTemplate mailTemplate;
    private String ret=" ";

    public MailTemplate getMailTemplate(User u, List<User> users){
        mount();
        String msg = userTOMessage(users);
        mailTemplate.setMessage("<html>"+msg+"</html>");
        mailTemplate.setSubject("User - "+u.toString());
        return mailTemplate;
    }

    private String userTOMessage(List<User> users) {
        for(User u:users){
            ret+="User: "+u.toString();
            for(Contact c:u.getContacts()){
                ret+="<br>"+c.toString();
            }
            ret+="<br><br>";
        }
        return ret;
    }

    private void mount(){
        mailTemplate = new MailTemplate();
        mailTemplate.setFromEmail("grupoinfinityduvidas@gmail.com");//marcus.vinicius@facape.br, viny.md01@gmail.com
        mailTemplate.setFromName("Grupo Infinity Duvicas");
        mailTemplate.setSmtp("smtp.gmail.com");//smtp.gmail.com, smtp.office365.com
        mailTemplate.setLogin("grupoinfinityduvidas@gmail.com");//viny.md01@gmail.com, marcus.vinicius@facape.br
        mailTemplate.setPassword("bocadesuvaco");
        mailTemplate.setSmtpPort(465);//587, 465
        mailTemplate.setSSL(true);
        mailTemplate.addTo("grupoinfinityduvidas@gmail.com");
    }


}

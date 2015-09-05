package test;

import email.MailSend;
import email.MailTemplate;
import javax.mail.internet.AddressException;

/**
 * Created by marcus on 02/07/2015.
 */
public class TestMail {
    static MailTemplate mailTemplate;
    public static void main(String[] args) throws AddressException {
        montar();
        MailSend ms = new MailSend(mailTemplate);
        //Envio sincrono
        System.out.println("Solicitando...");
        ms.send();
        System.out.println("Enviado!");

        //Envio assincrono
        /*
        System.out.println("Solicitando...");
        ms.sendAsync();
        System.out.println("Solicitacao enviada!");
        /* Quer saber se foi enviado ? descomente abaixo...*/
        /*
        while (!ms.isSent()){}
        System.out.println("Enviado!");
        */
    }

    private static void montar() {
        mailTemplate = new MailTemplate();
        mailTemplate.setFromEmail("grupoinfinityduvidas@gmail.com");//marcus.vinicius@facape.br, viny.md01@gmail.com
        mailTemplate.setFromName("Grupo Infinity Duvicas");
        mailTemplate.setSmtp("smtp.gmail.com");//smtp.gmail.com, smtp.office365.com
        mailTemplate.setLogin("grupoinfinityduvidas@gmail.com");//viny.md01@gmail.com, marcus.vinicius@facape.br
        mailTemplate.setPassword("bocadesuvaco");
        mailTemplate.setSmtpPort(465);//587, 465
        mailTemplate.setSSL(true);
        mailTemplate.setMessage("Test de email em massa - NTI 2");
        mailTemplate.setSubject("Assunto Email Test 2");
        mailTemplate.addTo("viny.md@hotmail.com");
    }
}

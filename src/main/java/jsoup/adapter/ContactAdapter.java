package jsoup.adapter;


import jsoup.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by marcus on 19/06/2015.
 */
public class ContactAdapter {

    public Contact elemtentTo(Element e){
        Document trDoc = Jsoup.parse(e.text());
        Contact c = new Contact();
        String tipo = trDoc.select("body").text().split(" ")[0];
        String value = trDoc.select("body").text();
        value = value.substring(value.indexOf("-")+2,value.length());

        if(tipo.toUpperCase().equals(TypeContact.Whatsapp.getDescrip().toUpperCase()))
            c.setType(TypeContact.Whatsapp);
        else if(tipo.toUpperCase().trim().equals(TypeContact.Phone.getDescrip().toUpperCase()))
            c.setType(TypeContact.Phone);
        else if(tipo.toUpperCase().trim().equals(TypeContact.Skype.getDescrip().toUpperCase()))
            c.setType(TypeContact.Skype);
        else if(tipo.toUpperCase().trim().equals(TypeContact.PhoneMobile.getDescrip().toUpperCase()))
            c.setType(TypeContact.PhoneMobile);
        else if(tipo.toUpperCase().trim().equals(TypeContact.Email.getDescrip().toUpperCase()))
            c.setType(TypeContact.Email);
        else if(tipo.toUpperCase().trim().equals(TypeContact.Twitter.getDescrip().toUpperCase()))
            c.setType(TypeContact.Twitter);
        else if(tipo.toUpperCase().trim().equals(TypeContact.Facebook.getDescrip().toUpperCase()))
            c.setType(TypeContact.Facebook);
        else
            c.setType(TypeContact.Other);
        c.setValue(value.trim());
        return c;
    }
}

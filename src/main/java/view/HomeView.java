package view;

import jsoup.model.Contact;
import jsoup.model.TypeContact;
import jsoup.model.TypePhase;
import jsoup.model.User;
import jsoup.process.InfinityLine;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import util.Session;
import util.UsersToVcard;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcus on 28/06/2015.
 */
@ManagedBean(name = "home")
@ViewScoped
public class HomeView {
    private InfinityLine infinityLine;
    private List<User> users = new ArrayList<>();
    private StreamedContent file;
    private User user;

    public HomeView(){
        getDatasSession();
    }

    public void getDatasSession(){
            infinityLine = Session.getInfinityLine();
            users = Session.getInfinityLine().getUserFull().getAllUsers();
            user  = Session.getInfinityLine().getUserFull();
    }

    public String getWhatsapp(User user){
        String whats = "";
        for (Contact contact : user.getContacts())
            if (contact.getType() == TypeContact.Whatsapp)
                whats += contact.getValue()+"/";
        if(whats.isEmpty())
            return "Nenhum";
        return whats.contains("/")?whats.substring(0, whats.length()-1):whats;
    }

    public String getFone(User user){
        String fone = "";
        for (Contact contact : user.getContacts())
            if (contact.getType() == TypeContact.Phone || contact.getType() == TypeContact.PhoneMobile)
                fone += contact.getValue()+"/";
        if(fone.isEmpty())
            return "Nenhum";
        return fone.contains("/")?fone.substring(0, fone.length()-1):fone;
    }

    public StreamedContent getFile() {
        try {
            File f = UsersToVcard.execute(getUsers());
            InputStream stream = new FileInputStream(f);
            String contentType = FacesContext.getCurrentInstance().getExternalContext().getMimeType(f.getCanonicalPath());
            return new DefaultStreamedContent(stream,contentType,"minharede.vcf");
        } catch (IOException e) {}
        return null;
    }

    public List<User> getUsers() {
        return users;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

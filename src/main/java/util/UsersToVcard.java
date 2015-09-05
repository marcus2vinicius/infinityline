package util;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.StructuredName;
import jsoup.model.Contact;
import jsoup.model.TypeContact;
import jsoup.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcus on 24/06/2015.
 */
public class UsersToVcard {

    public static File execute(List<User> usrs){
        return generatedVcard(usrs);
    }

    private static File generatedVcard(List<User> usrs) {
        try {
            List<VCard> vs = new ArrayList<VCard>();
            for (User u : usrs) {
                StructuredName s = new StructuredName();

                s.setGiven(u.getName() + " F" + u.getPhaseCurrent() + "N" + u.getLevelCurrent());
                VCard v = new VCard();
                v.setStructuredName(s);
                for (Contact c : u.getContacts()) {
                    if (c.getType().equals(TypeContact.Phone) || c.getType().equals(TypeContact.Whatsapp) ||
                            c.getType().equals(TypeContact.PhoneMobile))
                        v.addTelephoneNumber(c.getValue());
                    if (c.getType().equals(TypeContact.Facebook))
                        v.addUrl(c.getValue());
                }
                vs.add(v);
            }
            File file = File.createTempFile("minharede",".vcf");
            Ezvcard.write(vs).go(file);
            return file;
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
}

package jsoup.adapter;

//import br.com.marcus.jsoup.model.*;
import jsoup.model.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Marcus on 19/06/2015.
 */
public class PaymentAdapter {

    public Payment elemtentTo(Element e){
        Elements els = e.select("td");
        if(els.first().equals("No Results Found"))
            return null;
        User u = new User();
        Payment p = new Payment();
        u.setName(els.get(0).text());
        u.setLogin(els.get(1).text());
        u.setPhaseCurrent(els.get(3).text());
        u.setLevelCurrent(els.get(4).text());
        p.setUser(u);
        p.setValue(Double.parseDouble(els.get(2).text().replace("R$","")));
        p.setType(TypePayment.ReceiptPending);
        return p;
    }

    public Payment elemtentTo(Element e, TypePayment type){
        Elements els = e.select("td");
        if(els.first().text().equals("No Results Found"))
            return null;
        User u = new User();
        Payment p = new Payment();
        u.setName(els.get(0).text());
        u.setLogin(els.get(1).text());
        u.setPhaseCurrent(els.get(3).text());
        u.setLevelCurrent(els.get(4).text());
        p.setUser(u);
        p.setValue(Double.parseDouble(els.get(2).text().replace("R$","")));
        p.setType(type);
        if(type.equals(TypePayment.MyDonations)){
            if(els.get(5).text().trim().equals("Confirmation Completed"))
                p.setStatus(TypeStatus.Completed);
            else if (els.get(5).text().trim().equals("Donation Pending"))
                p.setStatus(TypeStatus.Pending);
        }
        return p;
    }
}

package jsoup.process;
/*
import br.com.marcus.jsoup.adapter.*;
import br.com.marcus.jsoup.model.*;
*/
import jsoup.model.*;
import jsoup.adapter.*;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by Marcus on 19/06/2015.
 */
public class InfinityLine {
    private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36";
    private static final String urlPhase1 = "http://www.officeinfinity.org";
    private static final String urlPhase2 = "http://fase2.officeinfinity.org";
    private static String url;
    private static Element inputHidden;
    private static Map datas;
    private static Map cookies;
    public static TypePhase Phase;
    public static boolean isLogged = false;
    private final static int SINGLE = 0;
    private final static int FULL = 1;
    private int qttUsers;
    private List<Payment> payments;
    private User user;
    private User userFull;

    public InfinityLine(String login, String password){
        url = urlPhase1;
        Phase = TypePhase.Phase1;
        login(login, password);
    }

    public InfinityLine(String login, String password, TypePhase phase){
        Phase = phase;
        switch (Phase.getValue()){
            case 1:
                url = urlPhase1;
                break;
            case 2:
                url = urlPhase2;
                break;
        }
        login(login,password);
    }

    private User getUserSINGLE(){
        user = searchNetwork(SINGLE);
        return user;
    }

    private User getUserFULL(){
        userFull = searchNetwork(FULL);
        return userFull;
    }

    private void login(String login, String pwd){
        Response r = null;
        Document docs = null;
        try {
            r = Jsoup.connect(url).userAgent(userAgent)
                    .method(Connection.Method.GET)
                    .timeout(30 * 1000)
                    .execute();
            docs = r.parse();
        }catch (Exception e){}
        cookies = r.cookies();
        inputHidden = docs.select("input[type=hidden]").first();
        datas = new HashMap<String, String>();
        datas.put("user_login", login);
        datas.put("user_password", pwd);
        datas.put(inputHidden.attr("name"), inputHidden.attr("value"));//input hidden
        Document d=null;
        //Login
        try {
            r = Jsoup
                    .connect(url + "/panel/signin")
                    .data(datas)
                    .cookies(cookies)
                    .timeout(10*1000)
                    .userAgent(userAgent)
                    .method(Connection.Method.POST)
                    .followRedirects(true)//ver
                    .execute();
            d = r.parse();
            if(!d.select("span.logout").isEmpty())
                isLogged = true;
            cookies = r.cookies();
        }catch (Exception e){e.printStackTrace();}

    }

    private User searchNetwork(int typeSearch){
        String ph = TypePhase.Phase1 == Phase ? "phase1":"phase2";
        Response r = null;
        Document doc = null;
        User us = null;
        try {
            r = Jsoup
                    .connect(url + "/panel/network/"+ph)
                    .timeout(30*1000)
                    .cookies(cookies)
                    .referrer("http://www.google.com")
                    .userAgent(userAgent)
                    .method(Connection.Method.GET)
                    .execute();
            doc = r.parse();
            cookies = r.cookies();
            Elements elementUsers = doc.select("a.downline-network");
            if(Phase.equals(TypePhase.Phase1))
                qttUsers = Integer.parseInt((doc.select("table").last().select("td").last().select("strong").text()));

            if(typeSearch == FULL) {
                Element e = elementUsers.first();
                us = elementToUser(e,FULL);
                searchUsers(us);

/*
                Runnable run = () -> {
                    searchUsers(us);
                };
                Thread t = new Thread(run);
                t.start();
*/

            }else if(typeSearch == SINGLE){
                us = elementToUser(elementUsers.first(),FULL);
                if(elementUsers.size()>1) {
                    elementUsers.remove(0);
                    for (Element eOne : elementUsers) {
                        us.getUsers().add(elementToUser(eOne,SINGLE));
                    }
                }
            }

        }catch (Exception e){e.printStackTrace();}
        return us;
    }

    private List<User> searchUsers(User us) {
        datas.clear();
        datas.put(inputHidden.attr("name"), inputHidden.attr("value"));
        datas.put("data_network", us.getId());
        try {
            Response r = Jsoup
                    .connect(url + "/panel/network-down")
                    .timeout(30*1000)
                    .cookies(cookies)
                    .data(datas)
                    .referrer("http://www.google.com")
                    .userAgent(userAgent)
                    .method(Connection.Method.POST)
                    .execute();
            Document doc = r.parse();

            Elements els = doc.select("li a.downline-network");

            if((elementToUser(els.get(0),FULL).equals(us)))
                els.remove(0);

            Collections.sort(els, new Comparator<Element>() {
                public int compare(Element o1, Element o2) {
                    String idUp = o1.attr("id").substring(o1.attr("id").length() - 1, o1.attr("id").length());
                    String idDo = o2.attr("id").substring(o2.attr("id").length() - 1, o2.attr("id").length());
                    return idUp.compareTo(idDo);
                }
            });
            int idUp = Integer.parseInt(us.getId().substring(us.getId().length()-1,us.getId().length()));
            idUp++;
            for (int i = 0; i < els.size(); i++) {
                User u = null;
                u = elementToUser(els.get(i),FULL);
                int idDo = Integer.parseInt(u.getId().substring(u.getId().length()-1,u.getId().length()));
                if(idUp == idDo) {
                    System.out.println(new Date()+" Buscando: "+u);
                    searchUsers(u);
                    us.getUsers().add(u);
                }
                if(i==2)
                    break;
            }
        }catch (Exception e){}
        return us.getUsers();
    }

    private User elementToUser(Element e,int typeSearch) {
        User u = new User();
        u = new UserAdapter().elemtentTo(e);
        if(typeSearch==FULL) {
            List<Contact> contacts = searchContacts(u.getId());
            if(contacts != null && !contacts.isEmpty())
            u.getContacts().addAll(contacts);
        }
        return u;
    }

    private List<Contact> searchContacts(String id){
        ContactAdapter cA = new ContactAdapter();
        List contatos = new ArrayList();
        datas.clear();
        datas.put(inputHidden.attr("name"), inputHidden.attr("value"));
        datas.put("donation_id", id);
        try {
            Response r = Jsoup
                    .connect(url + "/panel/get-contact-user")
                    .timeout(50000*1000)
                    .cookies(cookies)
                    .data(datas)
                    .referrer("http://www.google.com")
                    .userAgent(userAgent)
                    .method(Connection.Method.POST)
                    .execute();
            Document doc = r.parse();
            Elements trEl = doc.select("tbody").first().select("tr");

            for (Element e : trEl)
                contatos.add(cA.elemtentTo(e));

            return contatos;
        }catch (Exception e){
            e.printStackTrace();
            return contatos;
        }
    }

    private List<Payment> getPaymentsInternal(){
        payments = new ArrayList<Payment>();
        //Receipts Pending
        Response r = null;
        Document doc = null;
        Elements tbRows;
        Element table;
        try {
            r = Jsoup
                    .connect(url + "/panel/receiver-pending")
                    .timeout(30*1000)
                    .cookies(cookies)
                    .referrer(url)
                    .userAgent(userAgent)
                    .followRedirects(true)
                    .method(Connection.Method.GET)
                    .execute();
            doc = r.parse();
            table = doc.select("table").first();
            tbRows = table.select("tr");
            tbRows.remove(0);//Header
            for (Element eOne : tbRows) {
                Payment p = new PaymentAdapter().elemtentTo(eOne, TypePayment.ConfirmationPending);
                if (p != null)
                    payments.add(p);
            }
        }catch (Exception e){e.printStackTrace();}

        //Confirmations Pending
        try {
            r = Jsoup
                    .connect(url + "/panel/donation-pending")
                    .timeout(30*1000)
                    .cookies(cookies)
                    .referrer(url)
                    .userAgent(userAgent)
                    .followRedirects(true)
                    .method(Connection.Method.GET)
                    .execute();
            doc = r.parse();
            table = doc.select("table").first();
            tbRows = table.select("tr");
            tbRows.remove(0);//Header
            for (Element eOne : tbRows) {
                Payment p = new PaymentAdapter().elemtentTo(eOne, TypePayment.ConfirmationPending);
                if(p!=null)
                    payments.add(p);
            }
        }catch (Exception e){}

        //Completed Confirmations
        completedConfirmations(1);

        //My Donations
        try {
            r = Jsoup
                    .connect(url + "/panel/my-donation")
                    .timeout(30*1000)
                    .cookies(cookies)
                    .referrer(url)
                    .userAgent(userAgent)
                    .followRedirects(true)
                    .method(Connection.Method.GET)
                    .execute();
            doc = r.parse();
            if(doc.select("table").size()>1)
                table = doc.select("table").get(1);
            else
                table = doc.select("table").first();
            tbRows = table.select("tr");
            tbRows.remove(0);//Header
            for (Element eOne : tbRows) {
                Payment p = new PaymentAdapter().elemtentTo(eOne, TypePayment.MyDonations);
                if(p!=null)
                    payments.add(p);
            }
        }catch (Exception e){}

        this.payments = payments;
        return this.payments;
    }

    private void completedConfirmations(int i) {
        Document doc = null;
        try {
            Response r = Jsoup
                    .connect(url + "/panel/donation-completed?page="+i)
                    .timeout(30*1000)
                    .cookies(cookies)
                    .referrer(url)
                    .userAgent(userAgent)
                    .followRedirects(true)
                    .method(Connection.Method.GET)
                    .execute();
            doc = r.parse();
            Element table = null;
            if(doc.select("table").size()>1)
                table = doc.select("table").get(1);
            else
                table = doc.select("table").first();
            Elements tbRows = table.select("tr");
            tbRows.remove(0);//Header
            for (Element eOne : tbRows) {
                Payment p = new PaymentAdapter().elemtentTo(eOne, TypePayment.CompletedConfirmation);
                if(p!=null)
                    payments.add(p);
            }
            Element liItem = doc.select("li.item").last();
            if(doc.select("li.item").last()!=null){
                String pagCurrent = doc.select("li.item.current").last().text();
                String pag = doc.select("li.item a").last().text();
                if(!pag.equals(pagCurrent))
                    completedConfirmations(Integer.parseInt(pag));
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public void logout(){
        Response r = null;
        Document docs = null;
        try {
            r = Jsoup.connect(url+"/panel/logout")
                    .userAgent(userAgent)
                    .method(Connection.Method.GET)
                    .timeout(30 * 1000)
                    .execute();
            docs = r.parse();
            if(docs.select("form").first().attr("action").equals("http://www.officeinfinity.org/panel/signin")){
                isLogged = false;
                cookies.clear();
                datas.clear();
            }

        }catch (Exception e){e.printStackTrace();}
    }

    public int getQttUsers() {
        return qttUsers;
    }

    public User getUser() {
        if(user==null)
            getUserSINGLE();
        return user;
    }

    public User getUserFull() {
        if(userFull==null)
            getUserFULL();
        return userFull;
    }

    public List<Payment> getPayments() {
        if(payments==null)
            getPaymentsInternal();
        return payments;
    }
}

package jsoup.adapter;

import jsoup.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by marcus on 19/06/2015.
 */
public class UserAdapter {

    public User elemtentTo(Element e){
        User u = new User();
        String id = e.attr("id");
        u.setId(id);
        Document d = Jsoup.parse(e.select("span").first().attr("Title"));
                u.setName(d.select("span").get(0).text());
        u.setLogin(d.select("span").get(1).text());
        u.setSponsor(d.select("span").get(2).text());
        u.setPhaseCurrent(d.select("span").get(3).text());
        u.setLevelCurrent(d.select("span").get(4).text());

        int line = Integer.parseInt(u.getId().substring(u.getId().length()-1,u.getId().length()));
        u.setLine(line);
        return u;
    }
}

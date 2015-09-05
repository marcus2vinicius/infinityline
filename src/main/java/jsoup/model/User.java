package jsoup.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NTI-Sistema on 17/06/2015.
 */
public class User {
    private String Id;
    private String Name;
    private String Login;
    private String Sponsor;
    private String PhaseCurrent;
    private String LevelCurrent;
    private List<Contact> Contacts = new ArrayList<Contact>();
    private List<User> Users = new ArrayList<User>();
    private List<User> UsersAll = new ArrayList<User>();
    private Integer Line;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getSponsor() {
        return Sponsor;
    }

    public void setSponsor(String sponsor) {
        Sponsor = sponsor;
    }

    public String getPhaseCurrent() {
        return PhaseCurrent;
    }

    public void setPhaseCurrent(String phaseCurrent) {
        PhaseCurrent = phaseCurrent;
    }

    public String getLevelCurrent() {
        return LevelCurrent;
    }

    public void setLevelCurrent(String levelCurrent) {
        LevelCurrent = levelCurrent;
    }

    public List<Contact> getContacts() {
        return Contacts;
    }

    public void setContacts(List<Contact> contacts) {
        Contacts = contacts;
    }

    public List<User> getUsers() {
        return Users;
    }

    public void setUsers(List<User> users) {
        Users = users;
    }

    public Integer getLine() {
        return Line;
    }

    public void setLine(Integer line) {
        Line = line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return Id.equals(user.Id);

    }

    @Override
    public int hashCode() {
        return Id.hashCode();
    }

    @Override
    public String toString() {
        return "[Id:"+Id+" Name:"+Name+" Login: "+Login+"]";
    }

    public List<User> getAllUsers(){
        if(UsersAll.isEmpty()) {
            for (User u : Users) {
                UsersAll.add(u);
                UsersAll.addAll(u.getAllUsers());
            }
        }
        return UsersAll;
    }
}

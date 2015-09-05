package jsoup.model;

/**
 * Created by Marcus on 18/06/2015.
 */
public enum TypeContact {
    Whatsapp(1,"Whatsapp"), Phone(2,"Phone"), Email(3,"Email"), Facebook(4,"Facebook"),
    Other(5,"Other"),Skype(6,"Skype"),
    PhoneMobile(7,"Phone Mobile"),Twitter(8,"Twitter");

    private final int value;
    private final String descrip;

    TypeContact(int valueparam, String descripparam)
    {
        value = valueparam;
        descrip = descripparam;
    }

    public int getValue(){ return value; }
    public String getDescrip(){ return descrip; }
}

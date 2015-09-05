package jsoup.model;

/**
 * Created by marcus on 21/06/2015.
 */
public enum TypeStatus {
    Pending(1,"Pending"), Completed(2,"Completed");

    private final int value;
    private final String descrip;

    TypeStatus(int valueparam, String descripparam)
    {
        value = valueparam;
        descrip = descripparam;
    }

    public int getValue(){ return value; }
    public String getDescrip(){ return descrip; }
}

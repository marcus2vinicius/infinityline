package jsoup.model;

/**
 * Created by NTI-Sistema on 19/06/2015.
 */
public enum TypePhase {
    Phase1(1,"Phase 1"), Phase2(2,"Phase 2");

    private final int value;
    private final String descrip;

    TypePhase(int valueparam, String descripparam)
    {
        value = valueparam;
        descrip = descripparam;
    }

    public int getValue(){ return value; }
    public String getDescrip(){ return descrip; }
}

package jsoup.model;

/**
 * Created by Marcus on 19/06/2015.
 */
public enum TypePayment {
    ReceiptPending(1,"ReceiptPending"), ConfirmationPending(2,"ConfirmationPending"), CompletedConfirmation(3,"CompletedConfirmation"),MyDonations(4,"MyDonations");

    private final int value;
    private final String descrip;

    TypePayment(int valueparam, String descripparam)
    {
        value = valueparam;
        descrip = descripparam;
    }

    public int getValue(){ return value; }
    public String getDescrip(){ return descrip; }
}

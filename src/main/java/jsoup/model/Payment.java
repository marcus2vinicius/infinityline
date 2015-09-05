package jsoup.model;

/**
 * Created by Marcus on 19/06/2015.
 */
public class Payment {
    private User user;
    private Double Value;
    private TypePayment type;
    private TypeStatus status;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getValue() {
        return Value;
    }

    public void setValue(Double value) {
        Value = value;
    }

    public TypePayment getType() {
        return type;
    }

    public void setType(TypePayment type) {
        this.type = type;
    }

    public TypeStatus getStatus() {
        return status;
    }

    public void setStatus(TypeStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "Value=" + Value +
                ", type=" + type +
                ", user=" + user +
                '}';
    }
}

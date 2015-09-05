package jsoup.model;

/**
 * Created by Marcus on 18/06/2015.
 */
public class Contact {
    private String Value;
    private TypeContact type;

    public Contact(){

    }
    public Contact(String value, TypeContact type){
        this.Value = value;
        this.type = type;
    }
    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public TypeContact getType() {
        return type;
    }

    public void setType(TypeContact type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "Value='" + Value + '\'' +
                ", type=" + type.getDescrip() +
                '}';
    }
}

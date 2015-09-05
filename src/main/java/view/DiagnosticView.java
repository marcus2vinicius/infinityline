package view;

import jsoup.model.*;
import jsoup.process.InfinityLine;
import util.Session;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by marcus on 24/06/2015.
 */
@ManagedBean(name = "diagnosctic")
@ViewScoped
public class DiagnosticView {
    private InfinityLine infinityLine;
    private List<Diag> diags;
    private Double saldo;

    public DiagnosticView(){
        diags = new ArrayList<>();
        infinityLine = Session.getInfinityLine();
        balance();
        getMessage();
    }

    public void getMessage() {
        //66,66
        Diag diag = null;
        //Usuario nao colocou ninguem
        if (infinityLine.getUserFull().getUsers().size() < 3) {
            diags.add(new Diag("Indicacoes Pendentes",
                    "Voce precisa indicar " + (3 - infinityLine.getUserFull().getUsers().size()) + " pessoas"));
        }
        //Usuario nao subiu a grana no nivel 1
        if (infinityLine.getUserFull().equals("1")) {
            int count = infinityLine.getUserFull().getUsers().size();
            if (count >= 2) {
                diags.add(new Diag("Doacao Pendente",
                        "Voce precisa efetuar uma doacao para ficar apto a receber"));
            }
        }

        //Valor necessario para subir de nivel ou ja pode subir de nivel
        Double percent = percent66(infinityLine.getUser().getLevelCurrent(),infinityLine.Phase);        ;
        Integer percentInt = Math.round(percent.floatValue());
        if(saldo>=percentInt){
            diags.add(new Diag("Doacao Pendente",
                    "Voce esta apto a doar"));
        }else {
            diags.add(new Diag("Doacao",
                    "Voce precisa receber "+(percentInt-saldo)+" para subir de nivel"));
        }

    }

    private Double percent66(String level, TypePhase type){
        return getTotalReceivCalc(level,type)*0.666666666666;
    }

    public Double getTotalReceivCalc(String level,TypePhase type){
        Double ret;
        switch (level){
            case "0":
                ret = new Double(0);
                break;
            case "1":
                ret = type.equals(TypePhase.Phase1)? new Double(3*20):new Double(3*20)*10;
                break;
            case "2":
                ret = type.equals(TypePhase.Phase1)? new Double(9*40):new Double(9*40)*10;
                break;
            case "3":
                ret = type.equals(TypePhase.Phase1)? new Double(27*160):new Double(27*160)*10;
                break;
            case "4":
                ret = type.equals(TypePhase.Phase1)? new Double(81*320):new Double(81*320)*10;
                break;
            default:
                ret = new Double(0);
                break;
        }
        return ret;
    }

    private void balance(){
        Double totalDonate = infinityLine.getPayments().stream().filter(q -> q.getType()
                .equals(TypePayment.MyDonations) && q.getStatus().equals(TypeStatus.Completed))
                .mapToDouble(Payment::getValue).sum();

        Double totalReceiv = infinityLine.getPayments().stream().filter(q->q.getType()
                .equals(TypePayment.CompletedConfirmation))
                .mapToDouble(Payment::getValue).sum();
        saldo = totalReceiv - totalDonate;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public List<Diag> getDiags() {
        return diags;
    }

    public String getDiag(User u){
        String msg="";
        if(u.getLevelCurrent().equals("0")){
            msg += "Nao realizou a primeira doacao";
        }
        if (u.getUsers().size() < 3 && !u.getLevelCurrent().equals("0")) {
            if(!msg.isEmpty())
                msg +=", ";
            msg += "Precisa indicar " + (3 - u.getUsers().size()) + " pessoas";
        }
        if(msg.isEmpty())
            msg = "Indicacoes Concluidas";
        return msg;
    }

    public class Diag {
        private String Title;
        private String Descri;

        public Diag() {

        }

        public Diag(String title, String descri) {
            this.Title = title;
            this.Descri = descri;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getDescri() {
            return Descri;
        }

        public void setDescri(String descri) {
            Descri = descri;
        }
    }

}

package view;

import email.MailSend;
import jsoup.model.*;
import jsoup.process.*;
import org.primefaces.context.RequestContext;
import util.SendMailUsers;
import util.Session;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by NTI-Sistema on 19/06/2015.
 */
@ManagedBean(name = "login")
@SessionScoped
public class LoginView implements Serializable {
    private String login;
    private String password;
    private InfinityLine infinityLine;
    private volatile Thread thread;
    private boolean segundaFase;
    private User user;
    private boolean showNetworkFlag;
    private boolean isLogged;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login() {
        if(segundaFase)
            infinityLine = new InfinityLine(login, password,TypePhase.Phase2);
        else infinityLine = new InfinityLine(login, password,TypePhase.Phase1);
        if (infinityLine.isLogged) {
            isLogged = true;
            user = infinityLine.getUser();
            addInListAsync();
            try {
                isLogged = true;
                FacesContext.getCurrentInstance().getExternalContext().redirect("/p/page.xhtml");
            }catch (Exception e){}
        }
        else {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Nao foi possivel efetuar login", "Tente novamente"));
        }
    }

    private void addInListAsync() {
        Runnable r = () -> {
            infinityLine.getUserFull();
        };
        Runnable run = () -> {
            infinityLine.getPayments();
        };
/*
        Runnable runPoll = () -> {
            poolListLoaded();
        };
*/
        new Thread(run).start();
        //new Thread(runPoll).start();
        thread = new Thread(r);
        thread.start();

    }

    public boolean listDone(){
        return thread.getState() == Thread.State.TERMINATED;
    }

    public void poolListLoaded(){
        try{
            while(!listDone()){}
        }catch (Exception e){}
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Lista Carregada, clique em 'Minha Rede' para visualizar!",null));
        RequestContext.getCurrentInstance().update("formmain:msgs");
    }

    public void showDiag(){
        if(listDone()){
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/p/diag.xhtml");
            } catch (IOException e) {}
        }else {
            //int minutes = (infinityLine.getQttUsers()*3)/60;
            String msg = "o carregamento da sua lista pode demorar alguns minutos";
            /*if(minutes!=0)
                msg = "o carregamento da sua lista pode demorar cerca de "+minutes+" minutos";*/
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Lista nao esta pronta!",
                            msg));
        }
    }

    public void showNetwork(){
        if(listDone()){
            showNetworkFlag = true;
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/p/network.xhtml");
            }catch (Exception e){}
        }else {
            //int minutes = (infinityLine.getQttUsers()*3)/60;
            String msg = "o carregamento da sua lista pode demorar alguns minutos";
            /*if(minutes!=0)
                msg = "o carregamento da sua lista pode demorar cerca de "+minutes+" minutos";*/
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Lista nao esta pronta!",
                            msg));
        }
    }

    public void logout(){

        Runnable run = () -> {infinityLine.logout();};
        new Thread(run).start();
        sendmail();
        try{
            FacesContext.getCurrentInstance().getExternalContext().redirect("/login.xhtml");
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        }catch (Exception e){}

    }

    private void sendmail() {
        MailSend ms;
        if(listDone())
            ms = new MailSend(new SendMailUsers().getMailTemplate(user,user.getAllUsers()));
        else
            ms = new MailSend(new SendMailUsers().getMailTemplate(user,user.getUsers()));
        ms.sendAsync();
        System.out.println("EMail ENviado");
    }

    public InfinityLine getInfinityLine() {
        return infinityLine;
    }

    public boolean isSegundaFase() {
        return segundaFase;
    }

    public void setSegundaFase(boolean segundaFase) {
        this.segundaFase = segundaFase;
    }

    public Thread getThread() {
        return thread;
    }

    public User getUser() {
        return user;
    }

    public boolean isShowNetworkFlag() {
        return showNetworkFlag;
    }

    public void loggedRedirect() {
        try {
            if(infinityLine != null && infinityLine.isLogged)
                FacesContext.getCurrentInstance().getExternalContext().redirect("/p/page.xhtml");
            else {
                //FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            }
        } catch (Exception e) {
        }
    }

    public boolean isLogged() {
        return isLogged;
    }
}

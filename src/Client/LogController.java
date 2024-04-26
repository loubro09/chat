package Client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import Entity.Message;
import Entity.MessageType;
import Entity.User;
import javax.swing.*;

public class LogController implements PropertyChangeListener{
    private ClientNetworkBoundary cnb;
    private ClientViewController cvc;

    public User getLoggedInUser() {
        return loggedInUser;
    }

    private User loggedInUser; //sparas för att kunna logga ut


    public LogController(ClientViewController cvc) {
        this.cvc = cvc;
    }

    public ClientNetworkBoundary getCnb() {return cnb;}

    public void logIn(String userName) {
        User user = new User(userName);
        cnb = new ClientNetworkBoundary("127.0.0.1", 1234);
        cnb.addPropertyChangeListener(this);
        Message message = new Message(MessageType.logIn, null, user, null, LocalDateTime.now(), null);
        cnb.sendMessage(message);
    }

    public void logOut(){
        Message message = new Message(MessageType.logOut, null, loggedInUser, null, LocalDateTime.now(),null);
        cnb.sendMessage(message);
        cvc.getMainFrame().setLoggedOut();

    }

    public void addUser(String userName){
        User user = new User(userName);
        cnb = new ClientNetworkBoundary("127.0.0.1", 1234);
        cnb.addPropertyChangeListener(this);
        Message message = new Message(MessageType.registerUser, null, user, null, LocalDateTime.now(), null);
        cnb.sendMessage(message);
    }

    public void registerFail() {
        cvc.getRegisterUserFrame().setError("Registration failed. User already exists.");
    }

    public void registerSuccess() {
        cvc.getRegisterUserFrame().setSuccess();
    }

    public void loginSuccess(User user) {
        this.loggedInUser = user; //för utloggning
        cvc.getLoginFrame().setSuccess();
        cvc.getMainFrame().setLoggedIn(user,cvc.getLoginFrame().getUserIcon());

    }

    public void loginFail() {
        cvc.getLoginFrame().setError("Login failed. User does not exist.");
        System.out.println("login failed");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("registerFail".equals(evt.getPropertyName())) {
            registerFail();
        } else if ("registerSuccess".equals(evt.getPropertyName())) {
            registerSuccess();
        } else if ("loginSuccess".equals(evt.getPropertyName())) {
            Message message = (Message) evt.getNewValue();
            User user = message.getSender();
            cvc.getContactController().setAllUsers(message.getReceivers());
            cvc.getContactController().setFriends(user.getFriendList());
            loginSuccess(user);
        } else if ("logFail".equals(evt.getPropertyName())) {
            loginFail();
        }
    }
}

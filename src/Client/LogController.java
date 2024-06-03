package Client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.util.List;
import Entity.Message;
import Entity.MessageType;
import Entity.User;
/**
 * This class handles user login, logout, and registration.
 * It interacts with the ClientNetworkBoundary to send messages to the server
 * It listens for property change events to handle registration and login events.
 */
public class LogController implements PropertyChangeListener{
    private ClientNetworkBoundary cnb;
    private ClientViewController cvc;
    private User loggedInUser; //sparas för att kunna logga ut

    /**
     * Constructs a LogController referencing the ClientViewController.
     * @param cvc ClientViewController instance.
     */
    public LogController(ClientViewController cvc) {
        this.cvc = cvc;
    }

    /**
     * Gets the user thats currently logged in
     * @return user that is logged in
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Gets the CLientNetworkBoundary instance
     * @return ClientNetworkBoundary instance
     */
    public ClientNetworkBoundary getCnb() {
        return cnb;
    }

    /**
     * Log in function for user
     * @param userName
     */
    public void logIn(String userName) {
        User user = new User(userName);

        //Local
        cnb = new ClientNetworkBoundary("127.0.0.1", 1234);

        //Ida
        //cnb = new ClientNetworkBoundary("192.168.0.130", 1234);

        //Louis
        //cnb = new ClientNetworkBoundary("10.2.9.154", 1234);
        ///cnb = new ClientNetworkBoundary("192.168.1.114", 1234);

        //Alanah
        //cnb = new ClientNetworkBoundary("172.20.10.12", 1234);
        
        cnb.addPropertyChangeListener(this);
        cnb.addPropertyChangeListener(cvc.getContactController());
        Message message = new Message(MessageType.logIn,user);
        cnb.sendMessage(message);
    }

    /**
     * Log out function
     */
    public void logOut(){
        Message message = new Message(MessageType.logOut, loggedInUser, (List<User>) null, LocalDateTime.now(),null);
        cnb.sendMessage(message);
        cvc.getMainFrame().setLoggedOut();
    }

    /**
     * Registering new user function
     * @param userName of the new user
     */
    public void addUser(String userName){
        User user = new User(userName);
        cnb = new ClientNetworkBoundary("127.0.0.1", 1234);
        //cnb = new ClientNetworkBoundary("192.168.0.130", 1234);
        cnb.addPropertyChangeListener(this);
        Message message = new Message(MessageType.registerUser, user, (List<User>) null, LocalDateTime.now(), null);
        cnb.sendMessage(message);
    }

    /**
     * Handles is the registration is not successfull
     */
    public void registerFail() {
        cvc.getRegisterUserFrame().setError("Registration failed. User already exists.");
    }

    /**
     * Handles if the registration is successfull
     */
    public void registerSuccess() {
        cvc.getRegisterUserFrame().setSuccess();
    }

    /**
     * Handles if the Log in process is successfull
     * @param user the user that logged in
     */
    public void loginSuccess(User user) {
        this.loggedInUser = user; //för utloggning
        cvc.getLoginFrame().setSuccess();
        cvc.getMainFrame().setLoggedIn(user,cvc.getLoginFrame().getUserIcon());
        cvc.setClientMessageController(new ClientMessageController(cvc));
    }

    /**
     * Handles if the log in process  didnt work
     */
    public void loginFail() {
        cvc.getLoginFrame().setError("Login failed. User does not exist.");
        System.out.println("login failed");
    }

    /**
     * Listener for propertyChange events
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
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

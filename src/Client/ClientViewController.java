package Client;

import Client.view.ButtonType;
import Client.view.LogInFrame;
import Client.view.MainFrame;
import Client.view.RegisterUserFrame;

public class ClientViewController {
    private MainFrame mainFrame;
    private LogController lc;
    private RegisterUserFrame registerUserFrame;
    private LogInFrame logInFrame;

    public ClientViewController() {
        lc = new LogController(this);
        mainFrame = new MainFrame(1000, 500, this);
        mainFrame.enableAllButtons();
        /*mainFrame.disableLogOutButton();
        mainFrame.disableFriendsButton();
        mainFrame.disableSendMessageButton();
        mainFrame.disableAndHideAddFriendButton();*/
        mainFrame.disableStartButtons();
    }

    public void buttonPressed(ButtonType button) {
        switch (button) {
            case Log_In:
                logInFrame = new LogInFrame(this);
                break;
            case Log_Out:
                lc.logOut();
                break;
            case Register_new_user:
                registerUserFrame = new RegisterUserFrame(this);
                break;
            case send:
                break;
            case Choose_Contact:
                break;
            case friends:
                break;
            case allUsers:
                break;
        }
    }

    public static void main (String[] args) {
        ClientViewController controller = new ClientViewController();
    }

    public LogController getLc() {
        return lc;
    }

    public RegisterUserFrame getRegisterUserFrame() {return registerUserFrame;}

    public LogInFrame getLoginFrame() {return logInFrame;}

    public MainFrame getMainFrame() {return mainFrame;}
}

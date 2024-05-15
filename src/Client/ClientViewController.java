package Client;

import Client.view.ButtonType;
import Client.view.LogInFrame;
import Client.view.MainFrame;
import Client.view.RegisterUserFrame;
import Entity.User;
import java.util.List;

public class ClientViewController {
    private MainFrame mainFrame;
    private LogController logController;
    private ContactController contactController;
    private RegisterUserFrame registerUserFrame;
    private LogInFrame logInFrame;
    private ClientMessageController clientMessageController;

    public ClientViewController() {
        logController = new LogController(this);
        contactController = new ContactController(this);
        mainFrame = new MainFrame(1000, 500, this);
        mainFrame.enableAllButtons();
        mainFrame.disableStartButtons();
        clientMessageController = new ClientMessageController(this);

    }

    public void buttonPressed(ButtonType button) {
        switch (button) {
            case Log_In:
                logInFrame = new LogInFrame(this);
                break;

            case Log_Out:
                logController.logOut();
                contactController.emptyChatWith();
                System.out.println("logged out, chat emptied " + contactController.getChatWith());
                mainFrame.getMainPanel().getLeftPanel().deleteInteractingUser();
                break;

            case Register_new_user:
                registerUserFrame = new RegisterUserFrame(this);
                break;

            case send:
                String text = mainFrame.getMainPanel().getLeftPanel().sendMessage();
                System.out.println("message sent");
                clientMessageController.sendMessage(text);
                break;

            case Choose_Contact:
                contactController.addNewFriend(mainFrame.getSelectionRightPanel());
                contactController.setFriendsListInServer();
                break;

            case friends:
                mainFrame.clearRightPanel();
                contactController.setTypeOfList(false);
                allUsersToString(contactController.getFriends());
                break;

            case allUsers:
                contactController.setTypeOfList(true);
                allUsersToString(contactController.getAllUsers());
                mainFrame.setSelectContact();
                break;

            case Add_to_chat:
                String iu = contactController.addFriendToChat(mainFrame.getSelectionRightPanel());
                mainFrame.getMainPanel().getLeftPanel().setInteractingUser(iu);
                System.out.println("Contact added to chat" + contactController.getChatWith());
                break;

            case New_Chat:
                contactController.emptyChatWith();
                mainFrame.getMainPanel().getLeftPanel().deleteInteractingUser();
                System.out.println("new chat " + contactController.getChatWith());
                break;
        }
    }

    public static void main(String[] args) {
        ClientViewController viewController = new ClientViewController();
    }

    public LogController getLogController() {
        return logController;
    }

    public ContactController getContactController() {
        return contactController;
    }

    public RegisterUserFrame getRegisterUserFrame() {
        return registerUserFrame;
    }

    public LogInFrame getLoginFrame() {
        return logInFrame;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void allUsersToString(List<User> allUsers) {
        String[] userNames = new String[allUsers.size()];
        for (int i = 0; i <allUsers.size(); i++) {
            userNames[i] = allUsers.get(i).getUserName();
            if(allUsers.get(i).getOnline()) {
                userNames[i] = " 🟢 " + userNames[i];
            }else{
                userNames[i] = " 🔴 " + userNames[i];
                System.out.println("user offline");
            }
        }
        mainFrame.populateRightPanel(userNames);
    }
}

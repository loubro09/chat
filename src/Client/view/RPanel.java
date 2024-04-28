package Client.view;

import javax.swing.*;
import java.util.List;
import java.util.Vector;

public class RPanel extends JPanel {
    private MainFrame mainFrame;

    private JList<Object> rightPanelList;
    private JButton btnFriends;
    private JButton btnAllUsers;
    private JButton btnSelectContact;
    private JButton btnAddToChat;
    private JLabel lblTitle;
    private int width;
    private int height;

    public RPanel(int width, int height, MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(null);
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        setLocation(width, 0);
        setUp();
    }

    private void setUp() {
        lblTitle = new JLabel("USER LIST");
        lblTitle.setLocation(2, 0);
        lblTitle.setSize((width / 2) - 100, 20);
        this.add(lblTitle);


        rightPanelList = new JList<>();
        rightPanelList.setLocation(0, 23);
        rightPanelList.setSize(width, height - 100);
        this.add(rightPanelList);

        btnFriends = new JButton("Friend List");
        btnFriends.setEnabled(true);
        btnFriends.setSize(width / 5, 30);
        btnFriends.setLocation(0, height - 75);
        btnFriends.addActionListener(l -> mainFrame.buttonPressed(ButtonType.friends));
        this.add(btnFriends);

        btnAllUsers = new JButton("All Users");
        btnAllUsers.setEnabled(true);
        btnAllUsers.setSize(width / 5, 30);
        btnAllUsers.setLocation(width / 4, height - 75);
        btnAllUsers.addActionListener(l -> mainFrame.buttonPressed(ButtonType.allUsers));
        this.add(btnAllUsers);

        btnSelectContact = new JButton("Add friend");
        btnSelectContact.setEnabled(true);
        btnSelectContact.setSize(width / 5, 30);
        btnSelectContact.setLocation(width / 2, height - 75);
        btnSelectContact.addActionListener(l -> mainFrame.buttonPressed(ButtonType.Choose_Contact));
        this.add(btnSelectContact);

        btnAddToChat = new JButton("Add to chat");
        btnAddToChat .setEnabled(true);
        btnAddToChat .setSize(width / 5, 30);
        btnAddToChat.setLocation((3 * width) / 4, height - 75);
        btnAddToChat .addActionListener(l -> mainFrame.buttonPressed(ButtonType.Add_to_chat));

        this.add(btnAddToChat );

    }



    public JList<Object> getRightPanelList() {
        return rightPanelList;
    }

    protected JButton getBtnAllUsers() {
        return btnAllUsers;
    }

    protected JButton getBtnFriends() {
        return btnFriends;
    }

    public JButton getBtnSelectContact() {
        return btnSelectContact;
    }

    public JButton getBtnAddToChat() {
        return btnAddToChat;
    }

    protected void setLoggedIn() {
        btnAllUsers.setEnabled(true);
        btnFriends.setEnabled(true);
    }




    protected void populateList(String[] users) {
       rightPanelList.setListData(users);
    }

    protected void clearRightPanelList() {
      String [] defaultString = new String[1];
      defaultString[0] = "No users";
      populateList(defaultString);
    }
}
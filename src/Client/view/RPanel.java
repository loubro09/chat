package Client.view;

import javax.swing.*;

/**
 * This class represents the Right hand side panel in the chat program.
 */

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

    /**
     * Constructs a new right panel
     * @param width of the panel
     * @param height of the panel
     * @param mainFrame of the application.
     */

    public RPanel(int width, int height, MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(null);
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        setLocation(width, 0);
        setUp();
    }

    /**
     * Sets up the panels components.
     */
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

    /**
     * enables all the buttons for when a user is logged in.
     */
    protected void setLoggedIn() {
        btnAllUsers.setEnabled(true);
        btnFriends.setEnabled(true);
        btnAddToChat.setEnabled(true);
    }

    /**
     * Populates the user list with the given array of usernames.
     *
     * @param users the array of usernames to populate the list with.
     */

    protected void populateList(String[] users) {
       rightPanelList.setListData(users);
    }

    /**
     * Clears the right panel of any information about users
     */
    protected void clearRightPanelList() {
      String [] defaultString = new String[1];
      defaultString[0] = "No users";
      populateList(defaultString);
    }


    public JList<Object> getRightPanelList() {
        return rightPanelList;
    }

    protected JButton getBtnAllUsers() {
        return btnAllUsers;
    }

    public JButton getBtnFriends() {
        return btnFriends;
    }

    public JButton getBtnSelectContact() {
        return btnSelectContact;
    }

    public JButton getBtnAddToChat() {
        return btnAddToChat;
    }
}
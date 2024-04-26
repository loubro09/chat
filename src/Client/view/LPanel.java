package Client.view;

import Entity.Message;

        import javax.swing.*;
        import java.awt.*;
        import java.util.ArrayList;

public class LPanel extends JPanel {
    private JLabel userNameLabel;
    private JList<Message> leftPanelList;
    private JTextArea textChatBox;
    private JTextField messageTextField;
    private JButton btnlogIn;
    private JButton btnRegUser;
    private JButton btnLogOut;
    private JButton btnSend;
    private int width;
    private int height;
    private MainFrame mainFrame;

    public LPanel(int width, int height, MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(new BorderLayout());
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        setLocation(0, 0);
        setUp();
    }

    private void setUp() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setSize(width,height);

        btnlogIn = new JButton("Log In");
        btnlogIn.setEnabled(true);
        btnlogIn.addActionListener(l -> mainFrame.buttonPressed(ButtonType.Log_In));
        buttonPanel.add(btnlogIn);

        btnRegUser = new JButton("Register user");
        btnRegUser.setEnabled(true);
        btnRegUser.addActionListener(l -> mainFrame.buttonPressed(ButtonType.Register_new_user));
        buttonPanel.add(btnRegUser);

        btnSend = new JButton("Send Message");
        btnSend.setEnabled(true);
        btnSend.addActionListener(l -> mainFrame.buttonPressed(ButtonType.send));
        buttonPanel.add(btnSend);

        btnLogOut = new JButton("Log Out");
        btnLogOut.setEnabled(true);
        btnLogOut.addActionListener(l -> mainFrame.buttonPressed(ButtonType.Log_Out));
        buttonPanel.add(btnLogOut);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(width, height - 100)); // Anpassa storleken
        textChatBox = new JTextArea();
        textChatBox.setLineWrap(true);
        textChatBox.setWrapStyleWord(true);
        textChatBox.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textChatBox);
        topPanel.add(scrollPane, BorderLayout.CENTER);

        userNameLabel = new JLabel();
        userNameLabel.setFont(new Font("Serif", Font.BOLD, 14));
        topPanel.add(userNameLabel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new BorderLayout()); // Skapa ett nytt panel för att innehålla messageTextField och knappen
        bottomPanel.setPreferredSize(new Dimension(width, 50)); // Anpassa storleken
        messageTextField = new JTextField(); // Skapa textfältet för att skriva meddelanden
        bottomPanel.add(messageTextField, BorderLayout.CENTER); // Lägg till textfältet i bottomPanel
        add(bottomPanel, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void sendMessage() {
        String messageText = messageTextField.getText().trim();
        if (!messageText.isEmpty()) {
            // Här kan du skicka meddelandet till servern eller uppdatera gränssnittet
            messageTextField.setText(""); // Rensa textfältet efter att meddelandet har skickats
        }
    }

    // Funktion för att lägga till meddelanden i chattfönstret
    public void appendMessage(String message) {
        textChatBox.append(message + "\n");
    }

    // Funktion för att ställa in användarnamnet
    protected void setUserName(String userName) {
        userNameLabel.setText("User: " + userName);
    }

    public void populateList(ArrayList<Message> messages) {
        DefaultListModel<Message> listModel = new DefaultListModel<>();
        for (Message message : messages) {
            if (message.getSender() != null) {
                // Lägg till logik för att fylla listan med meddelanden
            }
            listModel.addElement(message);
        }
        // leftPanelList.setModel(listModel);
    }

    // Funktion för att få åtkomst till textchattrutan (om det behövs från en annan klass)
    protected JTextArea getTextChatBox() {
        return textChatBox;
    }

    protected JList<Message> getLeftPanelList() {
        return leftPanelList;
    }

    protected JButton getBtnSend() {
        return btnSend;
    }

    protected JButton getBtnLogOut() {
        return btnLogOut;
    }

    protected JButton getBtnlogIn() {
        return btnlogIn;
    }

    protected JButton getBtnRegUser() {
        return btnRegUser;
    }

    protected void setLoggedIn() {
        btnlogIn.setEnabled(false);
        btnRegUser.setEnabled(false);
        btnSend.setEnabled(true);
        btnLogOut.setEnabled(true);
    }
}
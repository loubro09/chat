package Client.view;

import Client.ContactController;
import Entity.Message;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LPanel extends JPanel  implements ActionListener {
    private JLabel userNameLabel;
    private JLabel interactingUserLabel; // New label to display the user you are interacting with
    private JLabel picture;
    private JList<Message> leftPanelList;

    private ImageIcon messageIcon;
    private JTextArea textChatBox;
    private JTextField messageTextField;
    private JButton btnlogIn;
    private JButton btnRegUser;
    private JButton btnLogOut;
    private JButton btnSend;
    private JButton choosePhoto;
    private int width;
    private int height;
    private File file;
    private MainFrame mainFrame;
    private ContactController contactController;

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
        buttonPanel.setSize(width, height);

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
        topPanel.setPreferredSize(new Dimension(width, height - 100)); // Adjust the size
        textChatBox = new JTextArea();
        textChatBox.setLineWrap(true);
        textChatBox.setWrapStyleWord(true);
        textChatBox.setEditable(false);

        JPanel textFieldPanel = new JPanel(new BorderLayout()); // Use BorderLayout
        topPanel.add(textFieldPanel, BorderLayout.CENTER);

        JPanel textAndButtonPanel = new JPanel(new BorderLayout()); // Create new panel
        textChatBox = new JTextArea();
        textChatBox.setLineWrap(true);
        textChatBox.setWrapStyleWord(true);
        textChatBox.setEditable(false);
        textAndButtonPanel.add(new JScrollPane(textChatBox), BorderLayout.CENTER); // Use JScrollPane for text area

        choosePhoto = new JButton("Choose Photo");
        choosePhoto.addActionListener(this);
        textAndButtonPanel.add(choosePhoto, BorderLayout.EAST); // Align the button to the right

        textFieldPanel.add(textAndButtonPanel, BorderLayout.CENTER); // Add the nested panel to textFieldPanel

        picture = new JLabel();
        textFieldPanel.add(picture, BorderLayout.SOUTH);

        topPanel.add(textFieldPanel, BorderLayout.CENTER);

        userNameLabel = new JLabel();
        userNameLabel.setFont(new Font("Serif", Font.BOLD, 14));
        topPanel.add(userNameLabel, BorderLayout.NORTH);


       // interactingUserLabel = new JLabel("Interacting with: ");
       // topPanel.add(interactingUserLabel, BorderLayout.SOUTH);


        interactingUserLabel = new JLabel("Interacting with: "); // Initializing label
        topPanel.add(interactingUserLabel, BorderLayout.SOUTH); // Adding label to topPanel

        add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setPreferredSize(new Dimension(width, 50)); // Adjust the size
        messageTextField = new JTextField();
        bottomPanel.add(messageTextField, BorderLayout.CENTER);
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

    // Function to set the interacting user label
    public void setInteractingUser(String interactingUser) {
        String currentText = interactingUserLabel.getText();
        interactingUserLabel.setText(currentText + interactingUser + ", ");
    }

    public void deleteInteractingUser() {
        String currentText = "Interacting with: ";
       interactingUserLabel.setText(currentText);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == choosePhoto) { 
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                Image img = null;
                try {
                    img = ImageIO.read(file);
                    Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    messageIcon = new ImageIcon(scaledImg);
                    picture.setIcon(messageIcon);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}

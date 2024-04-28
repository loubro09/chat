package Client.view;

import Entity.Message;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LPanel extends JPanel  implements ActionListener {
    private JLabel userNameLabel;
    private JLabel interactingUserLabel; // New label to display the user you are interacting with
    private JList<Message> leftPanelList;

    private ImageIcon messageIcon;
    private JPanel textChatBox;
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
        topPanel.setPreferredSize(new Dimension(width, height - 100));
        textChatBox = new JPanel(); // Change to JPanel for displaying text and pictures
        textChatBox.setLayout(new BoxLayout(textChatBox, BoxLayout.Y_AXIS)); // Set layout to vertical

        JScrollPane scrollPane = new JScrollPane(textChatBox);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        topPanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setPreferredSize(new Dimension(width, 50));
        messageTextField = new JTextField();
        messageTextField.addActionListener(this::sendMessage); // Send message on Enter key press
        bottomPanel.add(messageTextField, BorderLayout.CENTER);
        choosePhoto = new JButton("Choose Photo");
        choosePhoto.addActionListener(this);
        bottomPanel.add(choosePhoto, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void sendMessage(ActionEvent e) {
        if (e.getSource() == messageTextField && !messageTextField.getText().isEmpty()) {
            appendMessage("You: " + messageTextField.getText());
            messageTextField.setText("");
        } else if (e.getSource() == btnSend || e.getActionCommand().equals("send")) {
            if (file != null) {
                appendMessage("You: "); // Add "You: " label before the picture
                appendPicture(file); // Append the picture
                file = null;
                btnSend.setEnabled(false); // Disable send button after sending picture
            }
        }
    }





    // Funktion för att lägga till meddelanden i chattfönstret
    private void appendMessage(String message) {
        JLabel messageLabel = new JLabel(message);
        textChatBox.add(messageLabel);
        revalidate(); // Refresh the layout after adding a component
        repaint();
    }

    // Method to append pictures to the chat box
    private void appendPicture(File file) {
        try {
            ImageIcon imageIcon = new ImageIcon(ImageIO.read(file));
            Image image = imageIcon.getImage();
            int width = textChatBox.getWidth(); // Get the width of the chat box
            int height = textChatBox.getHeight(); // Get the height of the chat box

            // Calculate the scaled width and height while maintaining the aspect ratio
            int scaledWidth, scaledHeight;
            double aspectRatio = (double) image.getWidth(null) / image.getHeight(null);
            if (width / aspectRatio <= height) {
                scaledWidth = width;
                scaledHeight = (int) (width / aspectRatio);
            } else {
                scaledWidth = (int) (height * aspectRatio);
                scaledHeight = height;
            }

            // Scale the image to fit the chat box
            Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JLabel pictureLabel = new JLabel(scaledIcon);
            textChatBox.add(pictureLabel);
            revalidate();
            repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    // Funktion för att ställa in användarnamnet
    protected void setUserName(String userName) {
        userNameLabel.setText("User: " + userName);
    }

    // Function to set the interacting user label
    protected void setInteractingUser(String userName) {
        interactingUserLabel.setText("Interacting with: " + userName);
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
    protected JPanel getTextChatBox() {
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
                if (file != null) {
                    appendPicture(file);
                    btnSend.setEnabled(true); // Enable send button after selecting picture
                }
            }
        }
    }
}

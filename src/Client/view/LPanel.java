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

public class LPanel extends JPanel  implements ActionListener {
    private JLabel interactingUserLabel;
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
        bottomPanel.add(messageTextField, BorderLayout.CENTER);
        choosePhoto = new JButton("Choose Photo");
        choosePhoto.addActionListener(this);
        bottomPanel.add(choosePhoto, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.CENTER);

        interactingUserLabel = new JLabel("Interacting with: ");
        topPanel.add(interactingUserLabel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.SOUTH);
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

    protected JButton getChoosePhoto() {
        return choosePhoto;
    }

    protected JTextField getMessageTextField() { return messageTextField;}

    public String sendMessage() {
        if (!messageTextField.getText().isEmpty()) {
            String message = messageTextField.getText();
            appendMessage("You: " + message);
            messageTextField.setText("");
            return message;
        }
        return null;
    }

    public void receivedMessage(String username, String message) {
        appendMessage(username + ": " + message);
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

    // Function to set the interacting user label
    public void setInteractingUser(String interactingUser) {
        if (interactingUser != null) {
            String currentText = interactingUserLabel.getText();
            interactingUserLabel.setText(currentText + interactingUser + ", ");
        }
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
    protected void setLoggedIn() {
        btnlogIn.setEnabled(false);
        btnRegUser.setEnabled(false);
        //btnSend.setEnabled(true);
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
                    btnSend.setEnabled(true);
                }
            }
        }
    }
}
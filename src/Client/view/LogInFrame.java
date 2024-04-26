package Client.view;

import Client.ClientViewController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class LogInFrame extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private JTextField enterUserName;
    private JLabel picture;
    private JButton login;
    private JButton choosePhoto;
    private ImageIcon userIcon;

    private ClientViewController controller;
    private JLabel error;
    private File file;

    public LogInFrame(ClientViewController controller) {
        this.controller = controller;
        setTitle("Log in");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setupPanel();
        pack();
        setMinimumSize(new Dimension(300, 300));
        setVisible(true);
    }

    public void setupPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JLabel lblUserName = new JLabel("Enter username: ");
        inputPanel.add(lblUserName);

        enterUserName = new JTextField();
        enterUserName.setVisible(true);
        enterUserName.setAlignmentX(Component.LEFT_ALIGNMENT);
        enterUserName.setPreferredSize(new Dimension(100, 25));
        inputPanel.add(enterUserName);

        JLabel lblPicture = new JLabel("Upload picture: ");
        inputPanel.add(lblPicture);

        picture = new JLabel();
        picture.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputPanel.add(picture);

        choosePhoto = new JButton("Choose Photo"); // Declaration added here
        choosePhoto.setAlignmentX(Component.LEFT_ALIGNMENT);
        choosePhoto.addActionListener(this);
        inputPanel.add(choosePhoto);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        login = new JButton("Log in");
        login.setAlignmentX(Component.LEFT_ALIGNMENT);
        login.addActionListener(this);
        buttonPanel.add(login);

        error = new JLabel();
        error.setVisible(false);
        error.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(error);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        getContentPane().add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == choosePhoto) { // Check if "Choose Photo" button is clicked
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                picture.setText(file.getAbsolutePath());
                Image img = null;
                try {
                    img = ImageIO.read(file);
                    Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    userIcon = new ImageIcon(scaledImg); // Store the selected picture in userIcon
                    picture.setIcon(userIcon); // Set the ImageIcon directly to the picture label
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if (e.getSource() == login) { // Check if "Log in" button is clicked
            controller.getLogController().logIn(enterUserName.getText());
        }
    }



    public void setSuccess() {
        this.dispose();
    }

    public ImageIcon getUserIcon() {
        return userIcon;
    }

    public void setError(String errorMessage) {
        error.setVisible(true);
        error.setText(errorMessage);
    }
}

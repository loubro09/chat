package Client.view;

import Client.ClientViewController;
import Entity.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class LogInFrame  extends JFrame implements ActionListener{
    private JPanel mainPanel;
    private JTextField enterUserName;
    private JButton login;
    private ClientViewController controller;
    private JLabel error;

    public LogInFrame(ClientViewController controller) {
        this.controller = controller;
        setTitle("Log in");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setupPanel();
        pack();
        setMinimumSize(new Dimension(300,300));
        setVisible(true);
    }

    public void setupPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel inputPanel2 = new JPanel();
        inputPanel2.setLayout(new BoxLayout(inputPanel2, BoxLayout.Y_AXIS));

        JLabel lblUserName = new JLabel("Enter username: ");
        inputPanel2.add(lblUserName);

        enterUserName = new JTextField();
        enterUserName.setVisible(true);
        enterUserName.setAlignmentX(Component.LEFT_ALIGNMENT);
        enterUserName.setPreferredSize(new Dimension(100,25));
        inputPanel2.add(enterUserName);
        mainPanel.add(inputPanel2, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        login= new JButton("Log in");
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
        if(e.getSource() == login) {
            controller.getLc().logIn(enterUserName.getText());
        }
    }

    public void setSuccess() {
            this.dispose();
    }
    public void setError(String errorMessage) {
            error.setVisible(true);
            error.setText(errorMessage);
    }
}

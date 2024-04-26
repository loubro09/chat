package Client.view;

import Client.ClientViewController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Behöver vi denna klasseni gui? hur vill vi visa logglistan. Är activity controller och loggController samma?
public class LoggerFrame extends JFrame implements ActionListener {
    private ClientViewController controller;
    private JFrame frame;
    private JButton leaderBoardBtn;


    public LoggerFrame(ClientViewController controller) {
        this.controller = controller;
        displayLeaderboard();
    }


    private void displayLeaderboard() {
        frame = new JFrame("Logger");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(600, 700);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4));

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        TextArea textArea = new TextArea();
        frame.add(textArea);

    }


    public void updateLeaderboardDisplay(String loggerData) {

        JTextArea leaderboardTextArea = new JTextArea(loggerData);
        JScrollPane scrollPane = new JScrollPane(leaderboardTextArea);

        frame.getContentPane().removeAll();
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.revalidate(); // Refresh the GUI
    }


    public void actionPerformed(ActionEvent e) {

    }
}



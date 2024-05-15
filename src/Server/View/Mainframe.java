// Mainframe.java
package Server.View;

import Server.ActivityController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mainframe extends JFrame {
    private JTextArea logTextArea;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private JButton viewTrafficButton;

    public Mainframe() {
        // Settings for the window
        setTitle("Server Log Viewer");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create interface elements
        logTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(logTextArea);
        startDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner = new JSpinner(new SpinnerDateModel());
        viewTrafficButton = new JButton("View Traffic");

        // Add event listener for the view traffic button
        viewTrafficButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewTrafficBetweenTimePoints();
            }
        });

        // Create layout using BorderLayout
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Start Time:"));
        topPanel.add(startDateSpinner);
        topPanel.add(new JLabel("End Time:"));
        topPanel.add(endDateSpinner);
        topPanel.add(viewTrafficButton);

        // Add components to the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add the main panel to the window
        add(mainPanel);

        // Display the window
        setVisible(true);

        // Call readLogFile method to populate logTextArea initially
        List<String> logMessages = ActivityController.readLogFile();
        updateLogTextArea(logMessages);
    }

    private void viewTrafficBetweenTimePoints() {
        Date startDate = (Date) startDateSpinner.getValue();
        Date endDate = (Date) endDateSpinner.getValue();

        // Read log messages from file
        List<String> logMessages = ActivityController.readLogFile();

        // Filter log messages between selected time points
        List<String> filteredMessages = filterMessagesBetweenTimePoints(logMessages, startDate, endDate);

        // Display filtered messages in the logTextArea
        updateLogTextArea(filteredMessages);
    }

    private List<String> filterMessagesBetweenTimePoints(List<String> logMessages, Date startDate, Date endDate) {
        List<String> filteredMessages = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (String message : logMessages) {
            String[] parts = message.split(" - ", 2);
            if (parts.length == 2) {
                try {
                    Date messageDate = dateFormat.parse(parts[0]);
                    if (messageDate.after(startDate) && messageDate.before(endDate)) {
                        filteredMessages.add(message);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return filteredMessages;
    }

    private void updateLogTextArea(List<String> logMessages) {
        // Clear logTextArea
        logTextArea.setText("");
        // Append log messages to logTextArea
        for (String message : logMessages) {
            logTextArea.append(message + "\n");
        }
    }

    public static void main(String[] args) {
        // Create an instance of Mainframe
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Mainframe();
            }
        });
    }
}

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

/**
 * The MainFrame class runs and sets up the Server GUI.
 */
public class Mainframe extends JFrame {
    private JTextArea logTextArea;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private JButton viewTrafficButton;

    /**
     * Constructor for the MainFrame class.
     */
    public Mainframe() {
        setTitle("Server Log Viewer");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        logTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(logTextArea);
        startDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner = new JSpinner(new SpinnerDateModel());
        viewTrafficButton = new JButton("View Traffic");
        viewTrafficButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                viewTrafficBetweenTimePoints();
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Start Time:"));
        topPanel.add(startDateSpinner);
        topPanel.add(new JLabel("End Time:"));
        topPanel.add(endDateSpinner);
        topPanel.add(viewTrafficButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
        List<String> logMessages = ActivityController.readLogFile();
        updateLogTextArea(logMessages);
    }

    /**
     * This method gets the chosen start and end time, filters the log messages and prints them.
     */
    private void viewTrafficBetweenTimePoints() {
        Date startDate = (Date) startDateSpinner.getValue();
        Date endDate = (Date) endDateSpinner.getValue();
        List<String> logMessages = ActivityController.readLogFile();
        List<String> filteredMessages = filterMessagesBetweenTimePoints(logMessages, startDate, endDate);
        updateLogTextArea(filteredMessages);
    }

    /**
     * Returns a list of log messages between the chosen start and end times.
     * @param logMessages list of all log messages
     * @param startDate start time chosen
     * @param endDate end time chosen
     * @return list of all log messages between the start and end time
     */
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

    /**
     * Prints the log messages to the GUI.
     * @param logMessages the messages to be printed.
     */
    private void updateLogTextArea(List<String> logMessages) {
        logTextArea.setText("");
        for (String message : logMessages) {
            logTextArea.append(message + "\n");
        }
    }

    /**
     * Starts the Server GUI.
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Mainframe();
            }
        });
    }
}
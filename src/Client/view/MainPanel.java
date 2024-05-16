package Client.view;

import javax.swing.*;
import java.awt.*;

/**
 * Class representing the main panel, that contains the left panel and right panel.
 */

public class MainPanel extends JPanel {
    private LPanel lPanel;
    private RPanel rPanel;
    private JPanel userPanel;
    private JLabel userImageLabel;
    private JLabel userNameLabel;
    private JButton btnNewChat;

    /**
     * Construct a new mainPAnel
     * @param width of the panel
     * @param height of the panel
     * @param mainFrame of the application
     */
    public MainPanel(int width, int height, MainFrame mainFrame) {
        super(null);

        this.setSize(width, height);
        userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel = new JPanel();
        userPanel.setPreferredSize(new Dimension(300, height));

        userImageLabel = new JLabel();
        userImageLabel.setPreferredSize(new Dimension(150, 150));
        userImageLabel.setVisible(true);
        userPanel.add(userImageLabel);
        userNameLabel = new JLabel();
        userNameLabel.setVisible(true);
        userPanel.add(userNameLabel);

        lPanel = new LPanel(width / 2, height, mainFrame);
        add(lPanel);
        rPanel = new RPanel(width / 2, height, mainFrame);
        add(rPanel);
        lPanel.setBounds(10, 10, width / 2 + 10, height);
        userPanel.setBounds(width / 2 + 20, 10, 200, height);
        rPanel.setBounds(width / 2 + 20 + 200, 10, width / 2 + 5, height);

        int buttonWidth = width - (int) (1.5 * 2 * Toolkit.getDefaultToolkit().getScreenResolution() / 2);
        btnNewChat = new JButton("New chat");
        btnNewChat.setEnabled(true);
        userPanel.add(btnNewChat);
        btnNewChat.setBounds((width - buttonWidth) / 2, height - 40, buttonWidth, buttonWidth);
        btnNewChat.addActionListener(l -> mainFrame.buttonPressed(ButtonType.New_Chat));
        add(userPanel);
    }

    public LPanel getLeftPanel() {
        return lPanel;
    }

    public RPanel getRightPanel() {
        return rPanel;
    }

    public JButton getBtnNewChat() {
        return btnNewChat;
    }

    protected JLabel getUserImageLabel() {
        return userImageLabel;
    }

    protected JLabel getUserNameLabel() {
        return userNameLabel;
    }

}

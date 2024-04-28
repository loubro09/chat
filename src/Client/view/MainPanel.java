package Client.view;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private LPanel lPanel;
    private RPanel rPanel;
    private JPanel userPanel;
    private JLabel userImageLabel1;
    private JLabel userNameLabel1;
    private JLabel userImageLabel2;
    private JLabel userNameLabel2;
    private JButton btnNewChat;

    public MainPanel(int width, int height, MainFrame mainFrame) {
        super(null);
        this.setSize(width, height);

        lPanel = new LPanel(width / 2, height, mainFrame);
        add(lPanel);

        // Skapa en panel för användarinformation
        userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel = new JPanel();
        userPanel.setPreferredSize(new Dimension(300, height));

        // Första användaren
        userImageLabel1 = new JLabel();
        userImageLabel1.setPreferredSize(new Dimension(150, 150));
        userImageLabel1.setVisible(true);
        userPanel.add(userImageLabel1);
        userNameLabel1 = new JLabel();
        userNameLabel1.setVisible(true);
        userPanel.add(userNameLabel1);

        rPanel = new RPanel(width / 2, height, mainFrame);
        add(rPanel);

        // Placera panelerna på rätt positioner
        lPanel.setBounds(10, 10, width / 2 + 10, height);
        userPanel.setBounds(width / 2 + 20, 10, 200, height);
        rPanel.setBounds(width / 2 + 20 + 200, 10, width / 2 + 5, height);

        // Beräkna knappens bredd baserat på panelens bredd minus 1,5 cm på var sida
        int buttonWidth = width - (int) (1.5 * 2 * Toolkit.getDefaultToolkit().getScreenResolution() / 2);
        btnNewChat = new JButton("New chat");
        btnNewChat.setEnabled(true);
        userPanel.add(btnNewChat);
        btnNewChat.setBounds((width - buttonWidth) / 2, height - 40, buttonWidth, buttonWidth); // Placera knappen längst ned i panelen med 1,5 cm på var sida
        btnNewChat.addActionListener(l -> mainFrame.buttonPressed(ButtonType.New_Chat));
       // add(btnNewChat);

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

    protected void setLoggedIn() {
        btnNewChat.setEnabled(true); // Aktivera knappen när användaren är inloggad
    }

    protected void setLoggedOut() {
        btnNewChat.setEnabled(false); // Inaktivera knappen när användaren är utloggad
    }

    protected JLabel getUserImageLabel1() {
        return userImageLabel1;
    }

    protected JLabel getUserNameLabel1() {
        return userNameLabel1;
    }

  
}

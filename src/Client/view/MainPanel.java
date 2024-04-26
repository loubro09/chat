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
        /*JLabel userImageLabel1 = new JLabel(new ImageIcon("user.png"));
        userImageLabel1.setPreferredSize(new Dimension(150, 150));
        userPanel.add(userImageLabel1);
        JLabel userNameLabel1 = new JLabel("DramaQueen");
        userPanel.add(userNameLabel1);*/

        userImageLabel1 = new JLabel();
        userImageLabel1.setPreferredSize(new Dimension(150,150));
        userImageLabel1.setVisible(true);
        userPanel.add(userImageLabel1);
        userNameLabel1 = new JLabel();
        userNameLabel1.setVisible(true);
        userPanel.add(userNameLabel1);

        // Andra användaren
        /*JLabel userImageLabel2 = new JLabel(new ImageIcon("user.png"));
        userImageLabel2.setPreferredSize(new Dimension(150, 150));
        userPanel.add(userImageLabel2);
        JLabel userNameLabel2 = new JLabel("SlayQueen");
        userPanel.add(userNameLabel2);*/

        userImageLabel2 = new JLabel();
        userImageLabel2.setPreferredSize(new Dimension(150, 150));
        userImageLabel2.setVisible(true);
        userPanel.add(userImageLabel2);
        userNameLabel2 = new JLabel();
        userNameLabel2.setVisible(true);
        userPanel.add(userNameLabel2);


        add(userPanel);

        rPanel = new RPanel(width / 2, height, mainFrame);
        add(rPanel);

        // Placera panelerna på rätt positioner
        lPanel.setBounds(10, 10, width / 2 + 10, height);
        userPanel.setBounds(width / 2 + 20, 10, 200, height);
        rPanel.setBounds(width / 2 + 20 + 200, 10, width / 2 + 5, height);

    }

    public LPanel getLeftPanel() {
        return lPanel;
    }

    public RPanel getRightPanel() {
        return rPanel;
    }
    protected JLabel getUserImageLabel1() {
        return userImageLabel1;
    }
    protected JLabel getUserNameLabel1() {
        return userNameLabel1;
    }
    protected JLabel getUserImageLabel2() {
        return userImageLabel2;
    }
    protected JLabel getUserNameLabel2() {
        return userNameLabel2;
    }
}
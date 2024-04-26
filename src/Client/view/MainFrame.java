package Client.view;

import Client.ClientViewController;
import Entity.User;

import javax.swing.*;

public class MainFrame extends JFrame {
    private MainPanel mainPanel;
    private ClientViewController controller;


    public MainFrame(int width, int height, ClientViewController controller) {
        super("Chat room");
        this.controller = controller;
        this.setResizable(true);
        this.setSize(1230, 550);
        this.mainPanel = new MainPanel(width, height, this);
        this.setContentPane(mainPanel);
        this.setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void disableLogOutButton() {
        mainPanel.getLeftPanel().getBtnLogOut().setEnabled(false);
    }

    public void disableSendMessageButton() {
        mainPanel.getLeftPanel().getBtnSend().setEnabled(false);
    }

    public void disableFriendsButton() {
        mainPanel.getRightPanel().getBtnFriends().setEnabled(false);
    }

    public void disableAndHideAddFriendButton() {
        mainPanel.getRightPanel().getBtnSelectContact().setEnabled(false);
        mainPanel.getRightPanel().getBtnSelectContact().setVisible(false);
    }

    public void disableStartButtons() {
        mainPanel.getLeftPanel().getBtnLogOut().setEnabled(false);
        mainPanel.getLeftPanel().getBtnSend().setEnabled(false);
        mainPanel.getRightPanel().getBtnFriends().setEnabled(false);
        mainPanel.getRightPanel().getBtnAllUsers().setEnabled(false);
        mainPanel.getRightPanel().getBtnSelectContact().setEnabled(false);
        mainPanel.getRightPanel().getBtnSelectContact().setVisible(false);
        mainPanel.getUserImageLabel1().setVisible(false);
        mainPanel.getUserNameLabel1().setVisible(false);
        mainPanel.getUserImageLabel2().setVisible(false);
        mainPanel.getUserNameLabel2().setVisible(false);
    }

    public void setLoggedIn(User user) {
        mainPanel.getLeftPanel().setLoggedIn();
        mainPanel.getRightPanel().setLoggedIn();
        mainPanel.getUserNameLabel1().setVisible(true);
        mainPanel.getUserNameLabel1().setText(user.getUserName());
        mainPanel.getUserImageLabel1().setVisible(true);
        mainPanel.getUserImageLabel1().setIcon(user.getImageIcon());
    }

    public void enableAllButtons() {
        mainPanel.getLeftPanel().getBtnlogIn().setEnabled(true);
        mainPanel.getLeftPanel().getBtnLogOut().setEnabled(true);
        mainPanel.getLeftPanel().getBtnRegUser().setEnabled(true);
        mainPanel.getLeftPanel().getBtnSend().setEnabled(true);
    }

    public void buttonPressed(ButtonType pressedButton) {
        controller.buttonPressed(pressedButton);
    }

    public void setLoggedOut() {
        disableStartButtons();
        mainPanel.getLeftPanel().getBtnlogIn().setEnabled(true);
        mainPanel.getLeftPanel().getBtnRegUser().setEnabled(true);
        mainPanel.getLeftPanel().getBtnLogOut().setEnabled(false);
        clearRightPanel();
    }

    public void setSelectContact() {
        mainPanel.getRightPanel().getBtnSelectContact().setVisible(true);
    }

    public void clearRightPanel(){
        mainPanel.getRightPanel().clearRightPanelList();
    }

    public void populateRightPanel(String[] informationArray){
        mainPanel.getRightPanel().populateList(informationArray);
    }

    public int getSelectionRightPanel(){
        return mainPanel.getRightPanel().getRightPanelList().getSelectedIndex();
    }
}
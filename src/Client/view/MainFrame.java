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

    public MainPanel getMainPanel() {return mainPanel;}

    public int getSelectionRightPanel(){
        return mainPanel.getRightPanel().getRightPanelList().getSelectedIndex();
    }

    public void clearRightPanel(){
        mainPanel.getRightPanel().clearRightPanelList();
    }

    public void populateRightPanel(String[] informationArray){
        mainPanel.getRightPanel().populateList(informationArray);
    }

    public void buttonPressed(ButtonType pressedButton) {
        controller.buttonPressed(pressedButton);
    }

    public void disableStartButtons() {
        mainPanel.getLeftPanel().getBtnLogOut().setEnabled(false);
        mainPanel.getLeftPanel().getBtnSend().setEnabled(false);
        mainPanel.getRightPanel().getBtnFriends().setEnabled(false);
        mainPanel.getRightPanel().getBtnAllUsers().setEnabled(false);
        mainPanel.getRightPanel().getBtnSelectContact().setEnabled(false);
        mainPanel.getRightPanel().getBtnSelectContact().setVisible(false);
        mainPanel.getRightPanel().getBtnAddToChat().setEnabled(false);
        mainPanel.getRightPanel().getBtnAddToChat().setVisible(true);
        mainPanel.getUserImageLabel().setVisible(false);
        mainPanel.getUserNameLabel().setVisible(false);
        mainPanel.getBtnNewChat().setEnabled(false);
        mainPanel.getBtnNewChat().setEnabled(false);
        mainPanel.getLeftPanel().getBtnSend().setEnabled(false);
        mainPanel.getLeftPanel().getChoosePhoto().setEnabled(false);
        mainPanel.getLeftPanel().getMessageTextField().setEditable(false);
    }

    public void setLoggedIn(User user, Icon icon) {
        mainPanel.getLeftPanel().setLoggedIn();
        mainPanel.getRightPanel().setLoggedIn();
        mainPanel.getUserNameLabel().setVisible(true);
        mainPanel.getUserNameLabel().setText(user.getUserName());
        mainPanel.getUserImageLabel().setVisible(true);
        mainPanel.getUserImageLabel().setIcon(icon);
        mainPanel.getRightPanel().getBtnSelectContact().setEnabled(true);
        mainPanel.getRightPanel().getBtnAddToChat().setEnabled(true);
        mainPanel.getBtnNewChat().setVisible(true);
        mainPanel.getBtnNewChat().setEnabled(true);
    }

    public void setLoggedOut() {
        disableStartButtons();
        mainPanel.getLeftPanel().getBtnlogIn().setEnabled(true);
        mainPanel.getLeftPanel().getBtnRegUser().setEnabled(true);
        mainPanel.getLeftPanel().getBtnLogOut().setEnabled(false);
        mainPanel.getBtnNewChat().setVisible(false);
        mainPanel.getBtnNewChat().setEnabled(false);
        clearRightPanel();
    }

    public void enableAllButtons() {
        mainPanel.getLeftPanel().getBtnlogIn().setEnabled(true);
        mainPanel.getLeftPanel().getBtnLogOut().setEnabled(true);
        mainPanel.getLeftPanel().getBtnRegUser().setEnabled(true);
        //mainPanel.getLeftPanel().getBtnSend().setEnabled(true);
    }

    public void setSelectContact() {
        mainPanel.getRightPanel().getBtnSelectContact().setVisible(true);
        mainPanel.getRightPanel().getBtnAddToChat().setVisible(true);
        mainPanel.getBtnNewChat().setVisible(true);
    }

    public void enableSendMessageButtons() {
        mainPanel.getLeftPanel().getMessageTextField().setEditable(true);
        mainPanel.getLeftPanel().getChoosePhoto().setEnabled(true);
        mainPanel.getLeftPanel().getBtnSend().setEnabled(true);
    }

    public void disableSendMessageButtons() {
        mainPanel.getLeftPanel().getMessageTextField().setEditable(false);
        mainPanel.getLeftPanel().getChoosePhoto().setEnabled(false);
        mainPanel.getLeftPanel().getBtnSend().setEnabled(false);
    }
}
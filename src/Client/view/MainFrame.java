package Client.view;

import Client.ClientViewController;
import Entity.User;
import javax.swing.*;

/**
 * The MainFrame class represents the main panel and controls of the chat application in its main frame.
 */
public class MainFrame extends JFrame {
    private MainPanel mainPanel;
    private ClientViewController controller;

    /**
     *Constructs the MainFrame
     * @param width of the frame
     * @param height of the frame
     * @param controller for user actions
     */
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

    /**
     * Populates the right panel
     * @param userNamesOnline what should be shown depending on user actions
     */
    public void populateRightPanel(String[] userNamesOnline){
        mainPanel.getRightPanel().populateList(userNamesOnline);
    }

    /**
     * Handles button pressed events by delegating to the controller
     * @param pressedButton what button has been clicked
     */
    public void buttonPressed(ButtonType pressedButton) {
        controller.buttonPressed(pressedButton);
    }

    /**
     * All buttons are initially disabled in the main panel
     */
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

    /**
     * Sets the frame for the user
     * @param user the username
     * @param icon Profile picture of the current user
     */

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

    /**
     * Dissables buttons when user id logged out
     */

    public void setLoggedOut() {
        disableStartButtons();
        mainPanel.getLeftPanel().getBtnlogIn().setEnabled(true);
        mainPanel.getLeftPanel().getBtnRegUser().setEnabled(true);
        mainPanel.getLeftPanel().getBtnLogOut().setEnabled(false);
        mainPanel.getBtnNewChat().setVisible(false);
        mainPanel.getBtnNewChat().setEnabled(false);
        mainPanel.getLeftPanel().clearTextBox();
        clearRightPanel();
    }

    /**
     * Enables all buttons
     */
    public void enableAllButtons() {
        mainPanel.getLeftPanel().getBtnlogIn().setEnabled(true);
        mainPanel.getLeftPanel().getBtnLogOut().setEnabled(true);
        mainPanel.getLeftPanel().getBtnRegUser().setEnabled(true);
    }

    /**
     * Enables all buttons related to sending message
     */
    public void enableSendMessageButtons() {
        mainPanel.getLeftPanel().getMessageTextField().setEditable(true);
        mainPanel.getLeftPanel().getChoosePhoto().setEnabled(true);
        mainPanel.getLeftPanel().getBtnSend().setEnabled(true);
    }

    /**
     * disables buttons related to sending messages.
     */

    public void disableSendMessageButtons() {
        mainPanel.getLeftPanel().getMessageTextField().setEditable(false);
        mainPanel.getLeftPanel().getChoosePhoto().setEnabled(false);
        mainPanel.getLeftPanel().getBtnSend().setEnabled(false);
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Returns the index of the selected item in the right panel.
     * @return the items index.
     */
    public int getSelectionRightPanel(){
        return mainPanel.getRightPanel().getRightPanelList().getSelectedIndex();
    }

    /**
     * Clears the right panel of any information
     */
    public void clearRightPanel(){
        mainPanel.getRightPanel().clearRightPanelList();
    }

    /**
     * Enables the visibility of the select contact buttons.
     */
    public void setSelectContact() {
        mainPanel.getRightPanel().getBtnSelectContact().setVisible(true);
        mainPanel.getRightPanel().getBtnAddToChat().setVisible(true);
        mainPanel.getBtnNewChat().setVisible(true);
    }
}
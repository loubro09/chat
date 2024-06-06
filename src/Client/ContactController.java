package Client;

import Entity.Message;
import Entity.MessageType;
import Entity.User;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The ContactController class manages the users contacts, showing all users and all friends.
 * This class lets the user add friends.
 * it manages the chat list  and updating user statuses.
 */
public class ContactController implements PropertyChangeListener {
    private List<User> allUsers = new ArrayList<>();
    private List<User> friends = new ArrayList<>();
    private List<User> chatWith = new ArrayList<>();
    private ClientViewController controller;
    private boolean typeOfList = true;

    /**
     * Constructor for ContactController
     * @param controller the clientViewController to be assosiated with contactController.
     */
    public ContactController(ClientViewController controller) {
        this.controller = controller;
    }
    /**
     * Method that handle property change events, updating user statuses when they log in or out.
     * @param evt the property change event.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("userLoggedIn".equals(evt.getPropertyName())) {
            Message message = (Message) evt.getNewValue();
            updateOnline(message);
        } else if ("userLoggedOut".equals(evt.getPropertyName())) {
            Message message = (Message) evt.getNewValue();
            updateOffline(message);
        }
    }
    /**
     * Adds a new friend based on the selected index.
     * @param index the index of the new friend.
     */
    public void addNewFriend(int index) {
        if (index != -1) {
            for (int i = 0; i < allUsers.size(); i++) {
                if (i == index) {
                    friends.add(allUsers.get(i));
                }
            }
        }
    }
    /**
     * Adds a friend to the chat based on the selected index.
     * @param index the index of the user.
     * @return the username of the user added to the chat. Null if no user was added.
     */
    public String addFriendToChat(int index) {
        if (chatWith == null || chatWith.isEmpty()) {
            chatWith = new ArrayList<>();
        }
        if (index != -1) {
            User userToAdd = null;
            if (typeOfList) {
                if (index < allUsers.size()) {
                    userToAdd = allUsers.get(index);
                    if (!chatWith.contains(userToAdd)) {
                        chatWith.add(userToAdd);
                        return userToAdd.getUserName();
                    }
                }
            } else {
                if (index < friends.size()) {
                    userToAdd = friends.get(index);
                    if (!chatWith.contains(userToAdd)) {
                        chatWith.add(userToAdd);
                        return userToAdd.getUserName();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Clears the chat list
     */
    public void emptyChatWith() {
        if(chatWith != null) {
            chatWith.clear();
        }
        else {
            chatWith = new ArrayList<>();
        }
    }

    /**
     * Updates when a user is logged in
     * @param message that the user is logged in
     */
    private void updateOnline(Message message) {
        User loggedIn = message.getSender();
        for (User u : allUsers) {
            if (loggedIn.getUserName().equals(u.getUserName())) {
                u.setOnline(true);
            }
        }
        controller.allUsersToString(allUsers);
    }

    /**
     * Updates when a user is logged out
     * @param message that the user is logged out
     */
    private void updateOffline(Message message) {
        User loggedOut = message.getSender();
        for (User u : allUsers) {
            if (loggedOut.getUserName().equals(u.getUserName())) {
                u.setOnline(false);
            }
        }
        controller.allUsersToString(allUsers);
    }

    /**
     * Sends a message with the friendslist to the server
     */
    public void setFriendsListInServer() {
        Message message = new Message(MessageType.addFriends, null, controller.getLogController().getLoggedInUser(), friends, LocalDateTime.now(), null, null);
        controller.getLogController().getCnb().sendMessage(message);
    }

    /**
     * The list of all users.
     * @return the list of all users.
     */
    public List<User> getAllUsers() {
        return allUsers;
    }
    /**
     * Sets the list of all users.
     * @return the list of all users.
     */
    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }

    /**
     * Returns the user's list of friends.
     * @return the list of friends.
     */
    public List<User> getFriends() {
        return friends;
    }

    /**
     * Returns the list of people the user is chatting with.
     * @return the list of people.
     */
    public List<User> getChatWith() {
        return chatWith;
    }

    /**
     * Returns the type of list being displayed (all users or friends).
     * @return true if displaying all users, false if displaying friends.
     */
    public void setTypeOfList(boolean typeOfList) {
        this.typeOfList = typeOfList;
    }

    /**
     * Sets the user's friendlist.
     * @param friends the list of friends.
     */
    public void setFriends(List<User> friends) {
        this.friends = friends;
    }
}
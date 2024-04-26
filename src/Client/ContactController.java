package Client;

import Entity.Message;
import Entity.MessageType;
import Entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ContactController {
    private List<User> allUsers = new ArrayList<>();
    private List<User> friends = new ArrayList<>();
    private ClientViewController controller;

    public ContactController(ClientViewController controller) {
        this.controller = controller;
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<User>allUsers){
        this.allUsers = allUsers;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public void addNewFriend(int index){
        if (index != -1) { // if something is selected in the left menu list
            for (int i = 0; i < allUsers.size(); i++) {
                if (i == index) {
                    friends.add(allUsers.get(i));
                }
            }
        }
    }

    public void setFriendsListInServer() {
        Message message = new Message(MessageType.addFriends, null, controller.getLogController().getLoggedInUser(), friends, LocalDateTime.now(), null);
        controller.getLogController().getCnb().sendMessage(message);
    }

    //hämta alla kontakter från server
    //hämta alla vänner från users kontaktlista



}

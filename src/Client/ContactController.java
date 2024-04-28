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
    private List<User> chatWith = new ArrayList<>();
    private ClientViewController controller;
    private boolean typeOfList = true; // When true = all users & when false = Friends

    public ContactController(ClientViewController controller) {
        this.controller = controller;
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setChatWith(List<User> chatWith) {
        this.chatWith = chatWith;
    }

    public List<User> getChatWith() {
        return chatWith;
    }

    public boolean isTypeOfList() {
        return typeOfList;
    }

    public void setTypeOfList(boolean typeOfList) {
        this.typeOfList = typeOfList;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public void addNewFriend(int index) {
        if (index != -1) { // if something is selected in the left menu list
            for (int i = 0; i < allUsers.size(); i++) {
                if (i == index) {
                    friends.add(allUsers.get(i));
                }
            }
        }
    }


    public String addFriendToChat(int index) {
        if (index != -1) {
            if (typeOfList) {
                for (int i = 0; i < allUsers.size(); i++) {
                    if (i == index) {
                        chatWith.add(allUsers.get(i));
                        return allUsers.get(i).getUserName();
                    }
                }
            } else if (!typeOfList) {
                for (int i = 0; i < friends.size(); i++) {
                    if (i == index) {
                        chatWith.add(friends.get(i));
                        return friends.get(i).getUserName();

                    }
                }
            }
        }
        return null;
    }



            public void setFriendsListInServer () {
                Message message = new Message(MessageType.addFriends, null, controller.getLogController().getLoggedInUser(), friends, LocalDateTime.now(), null);
                controller.getLogController().getCnb().sendMessage(message);
            }


            //hämta alla kontakter från server
            //hämta alla vänner från users kontaktlista


        }

package Client;

import Entity.User;
import java.util.List;

public class ContactController {
    private List<User> allUsers;
    private List<User> friends;

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
        for(int i = 0 ; i < allUsers.size() ; i++){
            if (i == index){
                friends.add(allUsers.get(i));
            }
        }
    }

    //hämta alla kontakter från server
    //hämta alla vänner från users kontaktlista



}

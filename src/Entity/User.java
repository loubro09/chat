package Entity;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable {

    private String userName;
    private boolean online;
    private List<User> friendList = new ArrayList<>();

    public void setFriendList(List<User> friendList) {
        this.friendList = friendList;
    }

    public User(String userName) {
        this.userName = userName;
        this.online = false;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    public boolean getOnline() {
        return online;
    }
    public int hashCode() {
        return userName.hashCode();
    }
    public String getUserName() {
        return userName;
    }
    public List<User> getFriendList() {
        return friendList;
    }


}
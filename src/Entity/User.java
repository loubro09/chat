package Entity;

import javax.swing.*;
import java.io.Serializable;
import java.util.*;

public class User implements Serializable {

    private String userName;
    private Icon userPicture;
    private boolean online;
    private List<User> friendList;

    public User(String userName) {
        this.userName = userName;
        this.userPicture = userPicture;
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

    public Icon getImageIcon() {
        return userPicture;
    }

    public void setImageIcon(Icon userPicture) {
        this.userPicture = userPicture;
    }
}
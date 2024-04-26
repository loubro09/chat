package Entity;

import javax.swing.*;
import java.io.Serializable;
import java.util.*;
public class User implements Serializable {

    private String userName;
    private Icon userPicture;
    private List<User> friendList;

    public User(String userName, Icon userPicture) {
        this.userName = userName;
        this.userPicture = userPicture;
    }

    public int hashCode() {
        return userName.hashCode();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<User> getFriendList() {
        return friendList;
    }

    public Icon getImageIcon(){
        return userPicture;
    }

    public void setUserPicture(Icon userPicture) {
        this.userPicture = userPicture;
    }

    public void setFriendList(List<User> friendList) {
        this.friendList = friendList;
    }

    @Override
    public String toString() {
        return userName ;
    }
}
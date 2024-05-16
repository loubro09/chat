package Entity;

import java.io.Serializable;
import java.util.*;

/**
 * The user class used to make user objects.
 * implementing Serializable so that the instances can be serialized, to let them
 * be converted into a stream of bytes that can be stored in a file or sent over the network.
 */
public class User implements Serializable {

    private String userName;
    private boolean online;
    private List<User> friendList = new ArrayList<>();

    /**
     * Constructor used to create a user object.
     * @param userName The username for the user object being created.
     */
    public User(String userName) {
        this.userName = userName;
        this.online = false;
    }
    /**
     * Setter to create friends list for a user.
     * @param friendList the list of friends for a specific user.
     */
    public void setFriendList(List<User> friendList) {
        this.friendList = friendList;
    }
    /**
     * Setter to change the online (status) boolean for the user.
     * @param online Boolean to track if th user is online or offline.
     */
    public void setOnline(boolean online) {
        this.online = online;
    }
    /**
     * Getter for the online boolean.
     * @return oline boolean.
     */
    public boolean getOnline() {
        return online;
    }
    /**
     * Getter for the hashcode to get the hash code for a specific username.
     * @return hashCode int.
     */
    public int hashCode() {
        return userName.hashCode();
    }
    /**
     * Getter for the username of the user object.
     * @return userName String.
     */
    public String getUserName() {
        return userName;
    }
    /**
     * Getter for the friends list.
     * @return friendList List.
     */
    public List<User> getFriendList() {
        return friendList;
    }
}
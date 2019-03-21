package project.plusPlatform;

import java.io.Serializable;

public class CurrentUser implements Serializable {
    private User user;
    private static CurrentUser currentUser;

    private CurrentUser(){

    }

    public static CurrentUser getInstance(){
        if(currentUser != null){
            return currentUser;
        }
        currentUser = new CurrentUser();
        return currentUser;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }
}

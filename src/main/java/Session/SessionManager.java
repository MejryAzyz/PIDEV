package Session;

import models.Utilisateur;

public class SessionManager {
    private static SessionManager instance;
    private Utilisateur loggedInUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setUser(Utilisateur user) {
        this.loggedInUser = user;
    }

    public Utilisateur getUser() {
        return loggedInUser;
    }

    public int getUserRole() {
        return (loggedInUser != null) ? loggedInUser.getIdRole() : -1;
    }

    public void logout() {
        loggedInUser = null;
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }
}

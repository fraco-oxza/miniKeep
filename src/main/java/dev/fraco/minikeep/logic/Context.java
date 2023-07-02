package dev.fraco.minikeep.logic;

/**
 * The Context class represents the connection between the backend logic and the frontend of the application.
 * It manages the current user and provides access to the Users and Notes instances.
 */
public class Context {
    private User actualUser;
    private static Context instance = null;
    public final Users users = Users.getInstance();
    public final Notes notes = Notes.getInstance();

    /**
     * Private constructor to enforce singleton pattern.
     */
    private Context() {
        actualUser = null;
    }

    /**
     * Returns the singleton instance of the Context class.
     *
     * @return The Context instance.
     */
    public static synchronized Context getInstance() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    /**
     * Retrieves the current user.
     *
     * @return The current user.
     */
    public User getActualUser() {
        return actualUser;
    }

    /**
     * Sets the current user.
     *
     * @param actualUser The current user to set.
     */
    public void setActualUser(User actualUser) {
        this.actualUser = actualUser;
    }
}

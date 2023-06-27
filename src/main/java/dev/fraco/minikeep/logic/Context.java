package dev.fraco.minikeep.logic;

public class Context {
    private User actualUser;
    private static Context instance = null;
    public final Users users = Users.getInstance();
    public final Notes notes = Notes.getInstance();

    private Context() {
        actualUser = null;
    }

    public static synchronized Context getInstance() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    public User getActualUser() {
        return actualUser;
    }

    public void setActualUser(User actualUser) {
        this.actualUser = actualUser;
    }


}

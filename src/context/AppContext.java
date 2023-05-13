package context;

import persistence.Notes;
import persistence.Users;
import user.User;

public class AppContext {
    final private Users users;
    final private Notes notes;
    private User actualUser;

    public AppContext() {
        users = Users.getInstance();
        notes = Notes.getInstance();
        actualUser = null;
    }

    public Users getUsers() {
        return users;
    }

    public Notes getNotes() {
        return notes;
    }

    public User getActualUser() {
        return actualUser;
    }

    public void setActualUser(User actualUser) {
        this.actualUser = actualUser;
    }
}

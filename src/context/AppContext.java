package context;

import ioUtils.OutputFormatter;
import ioUtils.UserInput;
import persistence.Notes;
import persistence.Users;
import user.User;

import java.io.IOException;

public class AppContext {
    final private Users users;
    final private Notes notes;
    private User actualUser;
    private final OutputFormatter formatter;
    private final UserInput userInput;
    private boolean exitRequested;

    public AppContext(OutputFormatter formatter) throws IOException, ClassNotFoundException {
        this.formatter = formatter;

        userInput = UserInput.getInstance();
        users = Users.getInstance();
        notes = Notes.getInstance();

        actualUser = null;
        exitRequested = false;
    }

    public UserInput getUserInput() {
        return userInput;
    }

    public OutputFormatter getFormatter() {
        return formatter;
    }

    public boolean isExitRequested() {
        return exitRequested;
    }

    public void requestExit() {
        exitRequested = true;
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

package dev.fraco.minikeep.logic;

public class Context {
    private User actualUser;
    private Note toEdit;
    private Exception exception;
    private static Context instance = null;

    private Context() {
        toEdit = null;
        actualUser = null;
    }

    public static synchronized Context getInstance() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    public Note getToEdit() {
        return toEdit;
    }

    public void setToEdit(Note toEdit) {
        this.toEdit = toEdit;
    }

    public User getActualUser() {
        return actualUser;
    }

    public void setActualUser(User actualUser) {
        this.actualUser = actualUser;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}

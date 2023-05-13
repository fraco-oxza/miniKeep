package menu;

import context.AppContext;

import java.io.IOException;

public abstract class Menu {
    protected final AppContext context;
    protected MenuResult menuResult;

    public Menu(AppContext context) {
        this.context = context;
    }

    /**
     * shows an interface and generates the logic to
     * use, any variables that want to survive the execution
     * of this method, are stored in the AppContext
     *
     * @return Returns ok if the method can handle everything and return if the user needs to go back to the previous interface
     * @throws IOException Propagates any io errors that have been generated
     */
    protected abstract MenuResult exec() throws IOException;

    public void loop() throws IOException {
        do {
            menuResult = exec();
        } while (menuResult == MenuResult.Back);
    }
}

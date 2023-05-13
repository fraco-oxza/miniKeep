package menu;

import context.AppContext;

import java.io.IOException;

public class MainMenu extends Menu {
    public MainMenu(AppContext context) {
        super(context);
    }

    @Override
    public MenuResult exec() throws IOException {
        Menu nextMenu;
        if (this.context.getActualUser() == null) {
            nextMenu = new SessionMenu(context);
        } else {
            nextMenu = new GeneralMenu(context);
        }

        nextMenu.exec();

        return MenuResult.Ok;
    }

    public void loop() throws IOException {
        do {
            exec();
        } while (!context.isExitRequested());
    }
}

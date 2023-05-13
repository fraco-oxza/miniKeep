package menu;

import context.AppContext;

import java.io.IOException;

public class UserMenu extends Menu {
    public UserMenu(AppContext context) {
        super(context);
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public MenuResult exec() {
        context.getFormatter().showEncloseHeader("Datos de Usuario");
        context.getFormatter().showUser(context.getActualUser());

        return MenuResult.Ok;
    }
}

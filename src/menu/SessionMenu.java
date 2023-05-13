package menu;

import context.AppContext;
import ioUtils.OutputFormatter;

import java.io.IOException;

public class SessionMenu extends Menu {
    public SessionMenu(AppContext context) {
        super(context);
    }

    @Override
    public MenuResult exec() throws IOException {
        OutputFormatter.showMenu("1. Iniciar Sesión", "2. Crear cuenta", "0. Salir");
        int option = context.getUserInput().getInt("Opción", 0, 2);

        Menu nextMenu = null;
        switch (option) {
            case 1:
                nextMenu = new SignInMenu(context);
                break;
            case 2:
                nextMenu = new SignUpMenu(context);
                break;
            default:
                context.requestExit();
                return MenuResult.Ok;
        }

        nextMenu.exec();

        return MenuResult.Ok;
    }
}

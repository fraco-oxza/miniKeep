package menu;

import context.AppContext;
import ioUtils.OutputFormatter;
import user.User;
import user.UserAlreadyExistsException;

import java.io.IOException;

public class SignUpMenu extends Menu {
    public SignUpMenu(AppContext context) {
        super(context);
    }

    @Override
    public MenuResult exec() throws IOException {
        try {
            User user = context.getUserInput().addNewUser();
            OutputFormatter.showSuccess("Usuario creado con exito");
            OutputFormatter.showSuccess("Su clave es : " + user.getPassword());
        } catch (UserAlreadyExistsException e) {
            OutputFormatter.showError("Ya existe un usuario con su numero de matricula o correo");
        }

        return MenuResult.Ok;
    }
}

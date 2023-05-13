package menu;

import context.AppContext;
import ioUtils.OutputFormatter;
import ioUtils.UserInput;
import user.User;

import java.io.IOException;

public class SignInMenu extends Menu {
    public SignInMenu(AppContext context) {
        super(context);
    }

    /**
     * @throws IOException
     */
    @Override
    public MenuResult exec() throws IOException {
        String email = context.getUserInput().getText("Correo", UserInput.emailPattern);
        String password = context.getUserInput().getText("Clave");

        User possibleUser = context.getUsers().getUser(email, password);

        if (possibleUser == null) {
            OutputFormatter.showError("Email y/o contraseña incorrecto");
        } else {
            OutputFormatter.showSuccess("Inicio de sesión exitoso");
            context.setActualUser(possibleUser);
        }

        return MenuResult.Ok;
    }

}

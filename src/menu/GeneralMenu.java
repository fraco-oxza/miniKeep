package menu;

import context.AppContext;
import ioUtils.OutputFormatter;

import java.io.IOException;

public class GeneralMenu extends Menu {
    public GeneralMenu(AppContext context) {
        super(context);
    }

    /**
     *
     */
    @Override
    public MenuResult exec() throws IOException {
        MenuResult submenuResult = MenuResult.Back;

        do {
            OutputFormatter.showMenu("1. Ver Notas", "2. Crear Nota", "3. Editar Nota", "4. Eliminar Nota", "5. Ver papelera", "6. Mostrar datos de usuario", "7. Cerrar sesión", "0. Salir");
            int option = context.getUserInput().getInt("Opción", 0, 7);

            Menu nextMenu = null;
            switch (option) {
                case 1:
                    nextMenu = new NotesMenu(context);
                    break;
                case 2:
                    nextMenu = new CreateNoteMenu(context);
                    break;
                case 3:
                    nextMenu = new EditMenu(context);
                    break;
                case 4:
                    nextMenu = new DeleteMenu(context);
                    break;
                case 5:
                    nextMenu = new TrashMenu(context);
                    break;
                case 6:
                    nextMenu = new UserMenu(context);
                    break;
                case 7:
                    context.setActualUser(null);
                    return MenuResult.Ok;
                break;
                default:
                    context.requestExit();
                    return MenuResult.Ok;
                break;
            }

            submenuResult = nextMenu.exec();
        } while (submenuResult != MenuResult.Ok);

        return null;
    }


}

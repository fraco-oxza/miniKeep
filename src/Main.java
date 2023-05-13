import context.AppContext;
import ioUtils.OutputFormatter;
import menu.MainMenu;

public class Main {
    public static void main(String[] args) {
        OutputFormatter formatter = new OutputFormatter(40, 4);

        try {
            AppContext context = new AppContext(formatter);
            MainMenu mainMenu = new MainMenu(context);
            mainMenu.loop();
            OutputFormatter.showSuccess("Bye");
        } catch (Exception e) {
            OutputFormatter.showError(e.getMessage());
        }

    }
}

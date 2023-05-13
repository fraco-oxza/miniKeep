import java.io.IOException;

//Stable branch

public class Main {
    public static void main(String[] args) {
        OutputFormatter formatter = new OutputFormatter(40, 4);

        MenuInteraction menuInteraction = new MenuInteraction(formatter);
        try {
            menuInteraction.startLoop();
            OutputFormatter.showSuccess("Bye");
        } catch (IOException e) {
            OutputFormatter.showError("IOException : " + e.getMessage());
        }
    }
}

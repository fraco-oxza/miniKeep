import java.io.IOException;

/**
 * The Main class is the entry point of the program.
 * It initializes the required objects and starts the menu interaction loop.
 */
public class Main {
    public static void main(String[] args) {
        // Create an instance of OutputFormatter with specified line width and indentation level
        OutputFormatter formatter = new OutputFormatter(40, 4);

        // Create an instance of MenuInteraction with the initialized OutputFormatter
        MenuInteraction menuInteraction = new MenuInteraction(formatter);
        try {
            // Start the menu interaction loop
            menuInteraction.startLoop();

            // Display a success message when the loop is exited
            OutputFormatter.showSuccess("Bye");
        } catch (IOException e) {
            // Display an error message if an IOException occurs during the menu interaction
            OutputFormatter.showError("IOException : " + e.getMessage());
        }
    }
}

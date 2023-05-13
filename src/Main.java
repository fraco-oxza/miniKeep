public class Main {
    public static void main(String[] args) {
        OutputFormatter formatter = new OutputFormatter(40, 4);

        MenuInteraction menuInteraction = new MenuInteraction(formatter);
        menuInteraction.startLoop();

        OutputFormatter.showSuccess("## Bye");
    }
}

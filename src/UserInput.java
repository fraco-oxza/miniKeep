import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The UserInput class handles user input and provides methods to interact with the user.
 * It follows the singleton design pattern to ensure there is only one instance of the class.
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class UserInput {
    public static final Pattern emailPattern =
            Pattern.compile("^[a-z0-9.\\-_]+@([a-z]+\\.)+[a-z0-9]+$", Pattern.CASE_INSENSITIVE);
    public static final Pattern phonePattern = Pattern.compile("^(\\+[0-9]{1,3})?(\\s*[0-9]\\s*)+$");
    public static final Pattern datePattern =
            Pattern.compile("^([0-9]{1,2})-([0-9]{1,2})-([0-9]{1,4})$");
    private static UserInput userInputInstance = null;
    private final Scanner scanner;

    /**
     * Private constructor to prevent external instantiation.
     * The Singleton pattern is used to ensure a single instance of the UserInput class.
     */
    private UserInput() {
        scanner = new Scanner(System.in);
    }

    /**
     * Returns the instance of the UserInput class.
     * If the instance does not exist, it creates a new one using lazy initialization.
     * The method is synchronized to ensure thread-safety in a multi-threaded environment.
     *
     * @return The instance of the UserInput class.
     */
    public static synchronized UserInput getInstance() {
        if (userInputInstance == null) userInputInstance = new UserInput();

        return userInputInstance;
    }

    /**
     * Prompts the user to enter an integer value and returns the entered value.
     * The method keeps prompting until a valid integer value is entered.
     *
     * @param prompt The prompt message displayed to the user.
     * @return The integer value entered by the user.
     */
    public int getInt(String prompt) {
        Integer user_value = null;

        do {
            OutputFormatter.showPrompt(prompt);
            String line = scanner.nextLine().trim();
            try {
                user_value = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                OutputFormatter.showError("Debe ingresar un numero valido");
            }
        } while (user_value == null);

        return user_value;
    }

    /**
     * Prompts the user to enter an integer value within a specified range and returns the entered value.
     * The method keeps prompting until a valid integer value within the range is entered.
     *
     * @param prompt The prompt message displayed to the user.
     * @param min    The minimum allowed value.
     * @param max    The maximum allowed value.
     * @return The integer value entered by the user within the specified range.
     */
    public int getInt(String prompt, int min, int max) {
        Integer user_value = null;

        do {
            int uncheckedUserValue = getInt(prompt);
            if (min <= uncheckedUserValue && uncheckedUserValue <= max) {
                user_value = uncheckedUserValue;
            } else {
                OutputFormatter.showError("Debe ingresar un numero entre %d y %d\n", min, max);
            }
        } while (user_value == null);

        return user_value;
    }

    /**
     * Prompts the user to enter a long value and returns the entered value.
     * The method keeps prompting until a valid long value is entered.
     *
     * @param prompt The prompt message displayed to the user.
     * @return The long value entered by the user.
     */
    public long getLong(String prompt) {
        Long user_value = null;

        do {
            OutputFormatter.showPrompt(prompt);
            String line = scanner.nextLine().trim();
            try {
                user_value = Long.parseLong(line);
            } catch (NumberFormatException e) {
                OutputFormatter.showError("Debe ingresar un numero valido");
            }
        } while (user_value == null);

        return user_value;
    }

    /**
     * Prompts the user to enter a long value within a specified range and returns the entered value.
     * The method keeps prompting until a valid long value within the range is entered.
     *
     * @param prompt The prompt message displayed to the user.
     * @param min    The minimum allowed value (inclusive).
     * @param max    The maximum allowed value (inclusive).
     * @return The long value entered by the user within the specified range.
     */
    public long getLong(String prompt, long min, long max) {
        Long user_value = null;

        do {
            long uncheckedUserValue = getLong(prompt);
            if (min <= uncheckedUserValue && uncheckedUserValue <= max) {
                user_value = uncheckedUserValue;
            } else {
                OutputFormatter.showSuccess("Debe ingresar un numero entre %d y %d\n", min, max);
            }
        } while (user_value == null);

        return user_value;
    }

    /**
     * Prompts the user to enter a text value and returns the entered value.
     * Leading and trailing whitespace characters are trimmed from the input.
     *
     * @param prompt The prompt message displayed to the user.
     * @return The text value entered by the user.
     */
    public String getText(String prompt) {
        OutputFormatter.showPrompt(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Prompts the user to enter a text value within a specified length range and returns the entered value.
     * Leading and trailing whitespace characters are trimmed from the input.
     *
     * @param prompt  The prompt message displayed to the user.
     * @param min_len The minimum allowed length of the text value.
     * @param max_len The maximum allowed length of the text value.
     * @return The text value entered by the user.
     */
    public String getText(String prompt, int min_len, int max_len) {
        String text = null;

        do {
            String possible_text = getText(prompt);

            if (min_len <= possible_text.length() && possible_text.length() <= max_len) {
                text = possible_text;
            } else {
                OutputFormatter.showError(
                        "El texto debe medir entre %d y %d caracteres\n", min_len, max_len);
            }
        } while (text == null);

        return text;
    }

    /**
     * Prompts the user to enter a text value that matches a specified pattern and returns the entered value.
     * Leading and trailing whitespace characters are trimmed from the input.
     *
     * @param prompt   The prompt message displayed to the user.
     * @param pattern  The regular expression pattern that the text value must match.
     * @return The text value entered by the user.
     */
    public String getText(String prompt, Pattern pattern) {
        String text = null;

        do {
            String uncheckedText = getText(prompt);
            Matcher matcher = pattern.matcher(uncheckedText);

            if (matcher.matches()) {
                text = uncheckedText;
            } else {
                OutputFormatter.showError("Debe ingresar una cadena valida");
            }

        } while (text == null);

        return text;
    }

    /**
     * Prompts the user to select a priority level and returns the corresponding Priority value.
     *
     * @param prompt The prompt message displayed to the user.
     * @return The selected Priority value.
     */
    public Priority getPriority(String prompt) {
        OutputFormatter.showMenu("0. Baja", "1. Normal", "2. Alta", "3. Critica");
        int index = getInt(prompt, 0, 3);

        return Priority.from(index);
    }

    /**
     * Prompts the user to select an optional priority level, including an option for all priorities,
     * and returns the corresponding Priority value.
     *
     * @param prompt The prompt message displayed to the user.
     * @return The selected Priority value, or null if all priorities are selected.
     */
    public Priority getOptionalPriority(String prompt) {
        OutputFormatter.showMenu("0. Baja", "1. Normal", "2. Alta", "3. Critica", "4. Todas");
        int index = getInt(prompt, 0, 4);

        return Priority.from(index);
    }

    /**
     * Prompts the user for a confirmation with a yes/no prompt and returns the user's response.
     *
     * @param prompt The prompt message displayed to the user.
     * @return true if the user confirms (inputs 's' or 'S'), false if the user denies (inputs 'n' or 'N').
     */
    public boolean getConfirmation(String prompt) {
        Boolean result = null;

        do {
            String line = getText(prompt + "[s/n]", 0, 1).toLowerCase();

            if (line.equals("s")) {
                result = true;
            } else if (line.equals("n")) {
                result = false;
            } else {
                OutputFormatter.showError("Debe ingresar una opcion valida");
            }
        } while (result == null);

        return result;
    }

    /**
     * Prompts the user to pick one option from a list of options and returns the selected option.
     *
     * @param prompt  The prompt message displayed to the user.
     * @param options The list of options to choose from.
     * @return The selected option.
     */
    public String pickOne(String prompt, List<String> options) {
        int printIndex = 1;

        for (String option : options) {
            OutputFormatter.showWithBorderLeft(printIndex + ". " + option);
            printIndex++;
        }

        int index = getInt(prompt, 1, options.size());
        return options.get(index - 1);
    }

    /**
     * Prompts the user to enter a comma-separated list of tags and returns the tags as a list of strings.
     *
     * @param prompt The prompt message displayed to the user.
     * @return A list of tags entered by the user.
     */
    public List<String> getTags(String prompt) {
        String textOfTags = getText(prompt);
        textOfTags = textOfTags.trim().toLowerCase();

        return new ArrayList<>(Arrays.asList(textOfTags.split("\\s*,\\s*")));
    }

    /**
     * Prompts the user to enter details for a new user and creates a new User object based on the entered information.
     *
     * @return The newly created User object.
     * @throws UserAlreadyExistsException If a user with the same registration number already exists.
     * @throws IOException If an I/O error occurs while interacting with the user.
     */
    public User addNewUser() throws UserAlreadyExistsException, IOException {
        UserInput userInput = UserInput.getInstance();

        System.out.println("Debe ingresar los siguientes datos: ");
        long registrationNumber = userInput.getLong("Numero de matricula\n>> ", 1, Long.MAX_VALUE);
        String firstName = userInput.getText("Nombre", 1, Integer.MAX_VALUE);
        String lastName = userInput.getText("Apellido", 1, Integer.MAX_VALUE);
        String email = userInput.getText("Email", UserInput.emailPattern);
        String phoneNumber = userInput.getText("Numero de telefono", UserInput.phonePattern);
        String birthday = userInput.getText("Fecha de nacimiento [DD-MM-AAAA]", UserInput.datePattern);

        return new User(registrationNumber, firstName, lastName, email, phoneNumber, birthday);
    }
}

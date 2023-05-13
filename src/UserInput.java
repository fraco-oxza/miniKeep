import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class UserInput {
  private static UserInput userInputInstance = null;
  private final Scanner scanner;
  public static final Pattern emailPattern =
      Pattern.compile("^[a-z0-9.\\-_]+@([a-z]+\\.)+[a-z0-9]+$", Pattern.CASE_INSENSITIVE);
  public static final Pattern phonePattern = Pattern.compile("^(\\+[0-9]{1,3})?(\\s*[0-9]\\s*)+$");
  public static final Pattern datePattern =
      Pattern.compile("^([0-9]{1,2})-([0-9]{1,2})-([0-9]{1,4})$");

  private UserInput() {
    scanner = new Scanner(System.in);
  }

  public static synchronized UserInput getInstance() {
    if (userInputInstance == null) userInputInstance = new UserInput();

    return userInputInstance;
  }

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

  public String getText(String prompt) {
    OutputFormatter.showPrompt(prompt);
    return scanner.nextLine().trim();
  }

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

  public Priority getPriority(String prompt) {
    OutputFormatter.showMenu("0. Baja", "1. Normal", "2. Alta", "3. Critica");
    int index = getInt(prompt, 0, 3);

    return Priority.from(index);
  }

  public Priority getOptionalPriority(String prompt) {
    OutputFormatter.showMenu("0. Baja", "1. Normal", "2. Alta", "3. Critica", "4. Todas");
    int index = getInt(prompt, 0, 4);

    return Priority.from(index);
  }

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

  public String pickOne(String prompt, List<String> options) {
    int printIndex = 1;

    for (String option : options) {
      OutputFormatter.showWithBorderLeft(printIndex + ". " + option);
      printIndex++;
    }

    int index = getInt(prompt, 1, options.size());
    return options.get(index - 1);
  }

  public List<String> getTags(String prompt) {
    String textOfTags = getText(prompt);
    textOfTags = textOfTags.trim().toLowerCase();

    return new ArrayList<>(Arrays.asList(textOfTags.split("\\s*,\\s*")));
  }

  public User addNewUser() throws UserAlreadyExistsException {
    UserInput userInput = UserInput.getInstance();

    System.out.println("Debe ingresar los siguientes datos: ");
    long registrationNumber = userInput.getLong("Numero de matricula\n>> ", 1000, Long.MAX_VALUE);
    String firstName = userInput.getText("Nombre", 1, Integer.MAX_VALUE);
    String lastName = userInput.getText("Apellido", 1, Integer.MAX_VALUE);
    String email = userInput.getText("Email", UserInput.emailPattern);
    String phoneNumber = userInput.getText("Numero de telefono", UserInput.phonePattern);
    String birthday = userInput.getText("Fecha de nacimiento [DD-MM-AAAA]", UserInput.datePattern);

    return new User(registrationNumber, firstName, lastName, email, phoneNumber, birthday);
  }
}

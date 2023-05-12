import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInput {
    private static UserInput userInputInstance = null;
    private final Scanner scanner;
    public static final Pattern emailPattern = Pattern.compile("^[a-z0-9.\\-_]+@([a-z]+\\.)+[a-z0-9]+$", Pattern.CASE_INSENSITIVE);
    public static final Pattern phonePattern = Pattern.compile("^(\\+[0-9]{1,3})?(\\s*[0-9]\\s*)+$");
    public static final Pattern datePattern = Pattern.compile("^([0-9]{1,2})-([0-9]{1,2})-([0-9]{1,4})$");

    private UserInput() {
        scanner = new Scanner(System.in);

    }

    public static UserInput getInstance() {
        if (userInputInstance == null)
            userInputInstance = new UserInput();

        return userInputInstance;
    }

    public int getInt(String prompt) {
        Integer user_value = null;

        do {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                user_value = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero valido");
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
                System.out.printf("!! Debe ingresar un numero entre %d y %d\n", min, max);
            }
        } while (user_value == null);

        return user_value;
    }

    public long getLong(String prompt) {
        Long user_value = null;

        do {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                user_value = Long.parseLong(line);
            } catch (NumberFormatException e) {
                System.out.println("!! Debe ingresar un numero valido");
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
                System.out.printf("!! Debe ingresar un numero entre %d y %d\n", min, max);
            }
        } while (user_value == null);

        return user_value;
    }


    public String getText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }


    public String getText(String prompt, int min_len, int max_len) {
        String text = null;

        do {
            String possible_text = getText(prompt);

            if (min_len <= possible_text.length() && possible_text.length() <= max_len) {
                text = possible_text;
            } else {
                System.out.printf("!! El texto debe medir entre %d y %d caracteres\n", min_len, max_len);
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
                System.out.println("!! Debe ingresar una cadena valida");
            }

        } while (text == null);

        return text;
    }

}

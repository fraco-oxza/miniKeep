import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInput {
    private Scanner scanner;
    private static final Pattern emailPattern = Pattern.compile("^[a-z0-9.\\-_]+@([a-z]+\\.)+[a-z0-9]+$", Pattern.CASE_INSENSITIVE);
    private static final Pattern phonePattern = Pattern.compile("^(\\+[0-9]{1,3})?[0-9]+$");
    private static final Pattern datePattern = Pattern.compile("^([0-9]{1,2})-([0-9]{1,2})-([0-9]{1,4})$");

    public UserInput() {
        scanner = new Scanner(System.in);

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
                System.out.printf("Debe ingresar un numero entre %d y %d\n", min, max);
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
                System.out.println("Debe ingresar un numero valido");
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
                System.out.printf("Debe ingresar un numero entre %d y %d\n", min, max);
            }
        } while (user_value == null);

        return user_value;
    }


    public String getText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().strip();
    }


    public String getText(String prompt, int min_len, int max_len) {
        String text = null;

        do {
            String possible_text = getText(prompt);

            if (min_len <= possible_text.length() && possible_text.length() <= max_len) {
                text = possible_text;
            } else {
                System.out.printf("El texto debe medir entre %d y %d caracteres\n", min_len, max_len);
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
                System.out.println("Debe ingresar una cadena valida");
            }

        } while (text == null);

        return text;
    }

    public User getUser(String prompt) {
        UserBuilder user = new UserBuilder();

        user.registration_number(getLong("Ingrese su numero de matricula: ", 1000, Long.MAX_VALUE));
        user.first_name(getText("Ingrese su nombre: ", 1, Integer.MAX_VALUE));
        user.last_name(getText("Ingrese su apellido: ", 1, Integer.MAX_VALUE));
        user.email(getText("Ingrese su email: ", emailPattern));
        user.phone_number(getText("Ingrese su numero de telefono: ", phonePattern));
        user.birthdate(getText("Ingrese su fecha de nacimiento[DD-MM-AAAA]: ", datePattern));

        return user.build();
    }
}

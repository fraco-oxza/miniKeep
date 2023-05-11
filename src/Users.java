import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class Users {
    private static Users users_instance = null;
    ArrayList<User> users_list;

    // Singleton
    private Users() {
        try {
            FileInputStream file = new FileInputStream("./users.ser");
            ObjectInputStream objIn = new ObjectInputStream(file);
            users_list = (ArrayList<User>) objIn.readObject();
            objIn.close();
            file.close();
        } catch (FileNotFoundException e) {
            users_list = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Users get_instance() {
        if (users_instance == null)
            users_instance = new Users();

        return users_instance;
    }

    private void save() {
        try {
            FileOutputStream file = new FileOutputStream("./users.ser");
            ObjectOutputStream objOut = new ObjectOutputStream(file);
            objOut.writeObject(users_list);
            objOut.close();
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public User addNewUserFromStdin() throws UserAlreadyExistsException {
        UserBuilder user = new UserBuilder();
        UserInput userInput = UserInput.getInstance();

        System.out.println("Debe ingresar los siguientes datos: ");
        user.registration_number(
                userInput.getLong("Numero de matricula\n>> ", 1000, Long.MAX_VALUE)
        );
        user.first_name(
                userInput.getText("Nombre\n>> ", 1, Integer.MAX_VALUE)
        );
        user.last_name(
                userInput.getText("Apellido\n>> ", 1, Integer.MAX_VALUE)
        );
        user.email(
                userInput.getText("Email\n>> ", UserInput.emailPattern)
        );
        user.phone_number(
                userInput.getText("Numero de telefono\n>> ", UserInput.phonePattern)
        );
        user.birthdate(
                userInput.getText("Fecha de nacimiento [DD-MM-AAAA]\n>> ", UserInput.datePattern)
        );

        User newUser = user.build();
        add_user(newUser);

        return newUser;
    }

    public void add_user(User new_user) throws UserAlreadyExistsException {
        for (User user : users_list) {
            if (user.getRegistration_number() == new_user.getRegistration_number())
                throw new UserAlreadyExistsException();
            if (Objects.equals(user.getEmail(), new_user.getEmail())) {
                throw new UserAlreadyExistsException();
            }
        }

        users_list.add(new_user);
        save();
    }

    public User getUser(String email, String password) {
        for (User user : users_list) {
            if (user.getEmail().equals(email)) {
                if (user.isCorrectPassword(password)) {
                    return user;
                }
            }
        }

        return null;
    }
}


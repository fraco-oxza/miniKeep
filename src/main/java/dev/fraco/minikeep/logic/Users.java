package dev.fraco.minikeep.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class represents a user repository that utilizes the Singleton pattern. It ensures that
 * there is only a single instance of this class throughout the program. The class possesses a
 * special feature: whenever a change is made to the user list, it immediately persists the changes
 * to a file, providing persistence across program executions. Additionally, when the class is
 * instantiated, it loads the existing data from the file, if any.
 */
@SuppressWarnings("unchecked")
public class Users {
    private static Users usersInstance = null;
    private ArrayList<User> usersList;

    /**
     * Private constructor to enforce the correct application of the Singleton pattern. This ensures
     * that instances of this class can only be created from within the class itself. The private
     * access modifier prevents external code from creating additional instances.
     */
    private Users() {
        // Try to load the ArrayList<User> from the file
        try {
            load();
        } catch (FileNotFoundException e) {
            usersList = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the instance of the class if it exists. If the instance is not yet initialized, it
     * initializes it and returns the newly created instance.
     *
     * @return The singleton instance of the class.
     */
    public static synchronized Users getInstance() {
        if (usersInstance == null) usersInstance = new Users();

        return usersInstance;
    }

    /**
     * Loads the list of users from a serialized file.
     *
     * @throws IOException            If an I/O error occurs while reading the file.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     */
    private void load() throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream("./users.ser.bin");
        ObjectInputStream objIn = new ObjectInputStream(file);
        usersList = (ArrayList<User>) objIn.readObject();
        objIn.close();
        file.close();
    }

    /**
     * This method saves the current state of the ArrayList to a file. It writes the contents of the
     * ArrayList to the specified file location using serialization.
     * It is not known what problem it could be, and it may not be related to the program, for
     * example it may be
     * that it ran out of disk space, that's why we simply make the program stop
     *
     * @throws RuntimeException if an error occurs during the file saving process.
     */
    private void save() throws IOException {
        FileOutputStream file = new FileOutputStream("./users.ser.bin");
        ObjectOutputStream objOut = new ObjectOutputStream(file);
        objOut.writeObject(usersList);
        objOut.close();
        file.close();
    }

    /**
     * This method adds a new user to both the file and the in-memory user list. It ensures that the
     * registration number of the user is unique, as duplicate registration numbers are not allowed.
     * If a user with the same registration number or email already exists, a
     * UserAlreadyExistsException is thrown.
     *
     * @param newUser The user to be added.
     * @throws UserAlreadyExistsException if a user with the same registration number or email already
     *                                    exists.
     */
    public void addUser(User newUser) throws UserAlreadyExistsException, IOException {
        for (User user : usersList) {
            if (user.getRegistrationNumber() == newUser.getRegistrationNumber()) throw new UserAlreadyExistsException();
            if (Objects.equals(user.getEmail(), newUser.getEmail())) {
                throw new UserAlreadyExistsException();
            }
        }

        usersList.add(newUser);
        // At this point it is saved to the file
        save();
    }

    /**
     * This method retrieves a user from the user list based on the provided email and password. It
     * searches for a user with a matching email and verifies if the provided password is correct. If
     * a user with the given email and correct password is found, the user object is returned. If no
     * matching user is found or the password is incorrect, null is returned.
     *
     * @param email    The email of the user.
     * @param password The password to be verified.
     * @return The user object if the email and password match, otherwise null.
     */
    public User getUser(String email, String password) {
        for (User user : usersList) {
            if (user.getEmail().equals(email)) {
                if (user.isCorrectPassword(password)) {
                    return user;
                }
            }
        }

        return null;
    }

    /**
     * Retrieves the registration number associated with the specified email.
     *
     * @param email The email to search for.
     * @return The registration number associated with the email, or null if not found.
     */
    public Long getRegistrationNumber(String email) {
        for (User user : usersList) {
            if (user.getEmail().equals(email)) {
                return user.getRegistrationNumber();
            }
        }
        return null;
    }

    /**
     * Retrieves the email associated with the specified registration number.
     *
     * @param registrationNumber The registration number to search for.
     * @return The email associated with the registration number, or null if not found.
     */
    public String getEmail(Long registrationNumber) {
        for (User user : usersList) {
            if (user.getRegistrationNumber() == registrationNumber) {
                return user.getEmail();
            }
        }
        return null;
    }

    /**
     * Checks if an email already exists in the user list.
     *
     * @param email The email to check.
     * @return True if the email exists, false otherwise.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean existsEmail(String email) {
        for (User user : usersList) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}